package com.spogss.sportifycommunity.activity;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.spogss.sportifycommunity.R;
import com.spogss.sportifycommunity.adapter.CommentsListAdapter;
import com.spogss.sportifycommunity.data.Comment;
import com.spogss.sportifycommunity.data.connection.QueryType;
import com.spogss.sportifycommunity.data.connection.SportifyClient;
import com.spogss.sportifycommunity.data.connection.asynctasks.ClientQueryListener;
import com.spogss.sportifycommunity.model.CommentModel;
import com.spogss.sportifycommunity.model.PostModel;

import java.util.Collection;

public class CommentActivity extends AppCompatActivity implements ClientQueryListener, SwipeRefreshLayout.OnRefreshListener {
    private PostModel postModel;
    private EditText mContent;
    private ListView listViewComments;

    private View footerView;
    private CommentsListAdapter commentsListAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private SportifyClient client;

    //for loading
    private boolean isLoading = false;
    private int lastCommentId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        postModel = (PostModel) getIntent().getSerializableExtra("post");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Comments");

        initEditText();

        client = SportifyClient.newInstance();
        commentsListAdapter = new CommentsListAdapter(this);

        listViewComments = (ListView) findViewById(R.id.listView_comments);
        listViewComments.setAdapter(commentsListAdapter);
        listViewComments.setOnScrollListener(new ScrollListener());

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh_comments);
        swipeRefreshLayout.setOnRefreshListener(this);

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        footerView = layoutInflater.inflate(R.layout.list_footer_feed, null);

        client.getCommentsAsync(postModel.getPost().getId(), lastCommentId, this);
        listViewComments.addHeaderView(footerView);
    }

    private void initEditText() {
        mContent = (EditText) findViewById(R.id.editText_comment);
        mContent.setRawInputType(InputType.TYPE_CLASS_TEXT);
        mContent.setImeActionLabel(getResources().getString(R.string.send), EditorInfo.IME_ACTION_SEND);
        mContent.setImeOptions(EditorInfo.IME_ACTION_SEND);

        mContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event == null) {
                    if (actionId == EditorInfo.IME_ACTION_SEND) {
                        // Capture soft enters in a singleLine EditText that is the last EditText
                        // This one is useful for the new list case, when there are no existing ListItems
                        mContent.clearFocus();
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

                        if (mContent.getText().toString().trim() != "")
                            postComment();
                    } else {
                        // Let the system handle all other null KeyEvents
                        return false;
                    }
                } else {
                    // We let the system handle it when the listener is triggered by something that
                    // wasn't an enter.
                    return false;
                }
                return true;
            }
        });
    }

    private void postComment() {
        client.addCommentAsync(postModel.getPost().getId(), mContent.getText().toString(), this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSuccess(Object... results) {
        QueryType type = (QueryType) results[0];
        switch (type) {
            case LOAD_COMMENTS:
                Collection<CommentModel> commentModels = (Collection<CommentModel>) results[1];

                if (lastCommentId == -1)
                    commentsListAdapter.clear();
                if (commentModels.size() > 0)
                    isLoading = false;

                commentsListAdapter.addComments(commentModels);
                swipeRefreshLayout.setRefreshing(false);
                listViewComments.removeHeaderView(footerView);
                listViewComments.removeFooterView(footerView);

                lastCommentId = (int) results[2];
                break;

            case ADD_COMMENT:
                mContent.setText("");
                lastCommentId = -1;
                client.getCommentsAsync(postModel.getPost().getId(), lastCommentId, this);
                listViewComments.addHeaderView(footerView);
                break;
        }
    }

    @Override
    public void onFail(Object... errors) {
        QueryType type = (QueryType) errors[0];
        switch (type) {
            case LOAD_COMMENTS:
                Toast.makeText(this, "Error while loading comments", Toast.LENGTH_SHORT).show();
                break;
            case ADD_COMMENT:
                Toast.makeText(this, "Error while posting comment", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onRefresh() {
        lastCommentId = -1;
        client.getCommentsAsync(postModel.getPost().getId(), lastCommentId, this);
    }


    /**
     * Listener that handles the ListViewScroll event
     */
    private class ScrollListener implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView absListView, int i) {

        }

        @Override
        public void onScroll(AbsListView absListView, int i, int i1, int i2) {
            Log.i("position", isLoading + "");
            if ((listViewComments.getCount() % client.getNumberOfComments() == 0) && absListView.getLastVisiblePosition() == i2 - 1 && listViewComments.getCount() >= (client.getNumberOfComments() - 1) && !isLoading) {
                isLoading = true;
                listViewComments.addFooterView(footerView);
                client.getCommentsAsync(postModel.getPost().getId(), lastCommentId, CommentActivity.this);
            }
        }
    }
}
