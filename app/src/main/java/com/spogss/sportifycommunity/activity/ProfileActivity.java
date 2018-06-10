package com.spogss.sportifycommunity.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.spogss.sportifycommunity.R;
import com.spogss.sportifycommunity.adapter.FeedListAdapter;
import com.spogss.sportifycommunity.adapter.ListMenuModelAdapter;
import com.spogss.sportifycommunity.data.Post;
import com.spogss.sportifycommunity.data.ProUser;
import com.spogss.sportifycommunity.data.connection.QueryType;
import com.spogss.sportifycommunity.data.connection.SportifyClient;
import com.spogss.sportifycommunity.data.User;
import com.spogss.sportifycommunity.data.connection.asynctasks.ClientQueryListener;
import com.spogss.sportifycommunity.model.ListMenuModel;
import com.spogss.sportifycommunity.model.PostModel;
import com.spogss.sportifycommunity.model.UserModel;

import java.util.ArrayList;
import java.util.Collection;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, ClientQueryListener {
    private CollapsingToolbarLayout collapsingToolbar;
    private TextView textView_description;

    private ListView listView_menu;
    private final int V_ID_MENUITEM_SHOW_LOCATIONS = 2508;
    private final int V_ID_MENUITEM_SHOW_PLANS = 2509;

    private final int REQ_CODE_EDIT_PROFILE = 30;

    private FloatingActionButton fab_editOrFollow;

    private AppBarLayout appBarLayout_profile;
    private LinearLayout linearLayout_profileContent;
    private ProgressBar progressBar_profile;

    private ListView listViewPosts;
    private View footerView;

    private SportifyClient client;
    private UserModel displayedUser;
    private FeedListAdapter feedListAdapter;

    //for loading
    private boolean isLoading = false;
    private int lastPostID = -1;


    private enum UserCorrelation {
        NOT_FOLLOWING,
        FOLLOWING,
        OWN
    }

    private UserCorrelation correlationState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initialize();

        client = SportifyClient.newInstance();

        Integer userId = (Integer) getIntent().getSerializableExtra("profile");

        feedListAdapter = new FeedListAdapter(getApplicationContext());
        listViewPosts.setAdapter(feedListAdapter);
        listViewPosts.setOnScrollListener(new ScrollListener());
        ViewCompat.setNestedScrollingEnabled(listViewPosts, true);

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        footerView = layoutInflater.inflate(R.layout.list_footer_feed, null);

        client.getProfileAsync(userId, this);
        client.getPostsAsync(userId, lastPostID, true, this);
        listViewPosts.addHeaderView(footerView);
    }

    private void initialize() {
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        textView_description = (TextView) findViewById(R.id.textView_description);
        listView_menu = (ListView) findViewById(R.id.listView_profileMenu);
        fab_editOrFollow = (FloatingActionButton) findViewById(R.id.fab_editProfile);
        fab_editOrFollow.setOnClickListener(this);

        appBarLayout_profile = (AppBarLayout) findViewById(R.id.appBar);
        linearLayout_profileContent = (LinearLayout) findViewById(R.id.linearLayout_profileContent);
        progressBar_profile = (ProgressBar) findViewById(R.id.progressBar_profile);

        listViewPosts = (ListView) findViewById(R.id.listView_profile);
    }

    private void setUser(UserModel u) {
        displayedUser = u;
        setTitle(u.getUser().getUsername());
        refreshProfile();
        refreshCorellationState(u.isFollowing());
        refresh_fab_editOrFollow();
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

    private void refreshProfile() {
        collapsingToolbar.setTitle(displayedUser.getUser().getUsername());
        String bio = displayedUser.getUser().getDescription();
        bio = (bio == null || bio.isEmpty()) ? getString(R.string.not_specified) : bio;
        Log.i("bio:", bio);
        textView_description.setText(bio);
        if (isUserPro(displayedUser.getUser())) {
            listView_menu.setVisibility(View.VISIBLE);
            setListMenu();
        } else {
            listView_menu.setVisibility(View.GONE);
        }
    }

    private void setListMenu() {

        // if extending Activity
        //setContentView(R.layout.activity_main);

        // 1. pass context and data to the custom adapter
        ListMenuModelAdapter adapter = new ListMenuModelAdapter(getApplicationContext(), generateMenuEntries());

        // if extending Activity 2. Get ListView from activity_main.xml
        //ListView listView = (ListView) findViewById(R.id.listview);

        // 3. setListAdapter
        listView_menu.setAdapter(adapter); // if extending Activity
        //ListMenuModelAdapter(adapter); // if extending ListActivity
        //setListViewHeightBasedOnChildren(listView_menu); // try

        //listView_menu.setOnItemSelectedListener(this);
    }

    private ArrayList<ListMenuModel> generateMenuEntries() {
        ArrayList<ListMenuModel> models = new ArrayList<ListMenuModel>();

        ListMenuModel m = new ListMenuModel("Group Title");
        models.add(m);

        m = new ListMenuModel(V_ID_MENUITEM_SHOW_LOCATIONS,
                android.R.drawable.ic_menu_mylocation, "Locations", ">>");
        m.setOnClickListener(this);
        models.add(m);

        m = new ListMenuModel(V_ID_MENUITEM_SHOW_PLANS,
                android.R.drawable.ic_menu_my_calendar, "Plans", ">>");
        m.setOnClickListener(this);
        models.add(m);

        return models;
    }

    private boolean isUserPro(User u) {
        return u instanceof ProUser;
    }

    private void refreshCorellationState(boolean isUserFollowed) {
        if (displayedUser.getUser().getId() == client.getCurrentUserID()) {
            correlationState = UserCorrelation.OWN;
            correlationState = UserCorrelation.OWN;
        } else {
            displayedUser.setFollowing(isUserFollowed);
            if (isUserFollowed) {
                correlationState = UserCorrelation.FOLLOWING;
            } else {
                correlationState = UserCorrelation.NOT_FOLLOWING;
            }
        }
    }

    private void refresh_fab_editOrFollow() {
        switch (correlationState) {
            case NOT_FOLLOWING:
                fab_editOrFollow.setImageResource(R.drawable.sp_follow);
                break;
            case FOLLOWING:
                fab_editOrFollow.setImageResource(R.drawable.sp_unfollow);
                break;
            case OWN:
                fab_editOrFollow.setImageResource(android.R.drawable.ic_menu_edit);
                break;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_editProfile:
                switch (correlationState) {
                    case NOT_FOLLOWING:
                        client.setUserFollowAsync(displayedUser.getUser().getId(), true, this);
                        refreshCorellationState(true);
                        refresh_fab_editOrFollow();
                        break;
                    case FOLLOWING:
                        client.setUserFollowAsync(displayedUser.getUser().getId(), false, this);
                        refreshCorellationState(false);
                        refresh_fab_editOrFollow();
                        break;
                    case OWN:
                        editProfile();
                        break;
                }
                break;
            case V_ID_MENUITEM_SHOW_LOCATIONS:
                Toast.makeText(getApplicationContext(), "show locations", Toast.LENGTH_SHORT).show();
                break;
            case V_ID_MENUITEM_SHOW_PLANS:
                Intent intent = new Intent(this, ShowPlansActivity.class);
                intent.putExtra("userid", displayedUser.getUser().getId());
                startActivity(intent);
                break;
        }
    }

    private void editProfile() {
        Intent intent_editProfile = new Intent(getApplicationContext(), EditProfileActivity.class);
        intent_editProfile.putExtra("profile", displayedUser.getUser());
        startActivityForResult(intent_editProfile, REQ_CODE_EDIT_PROFILE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_EDIT_PROFILE) {
            client.getProfileAsync(displayedUser.getUser().getId(), this);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);


            linearLayout_profileContent.setVisibility(show ? View.GONE : View.VISIBLE);
            linearLayout_profileContent.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    linearLayout_profileContent.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            fab_editOrFollow.setVisibility(show ? View.GONE : View.VISIBLE);
            fab_editOrFollow.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    fab_editOrFollow.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            appBarLayout_profile.setVisibility(show ? View.GONE : View.VISIBLE);
            appBarLayout_profile.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    appBarLayout_profile.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });


            progressBar_profile.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar_profile.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar_profile.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBar_profile.setVisibility(show ? View.VISIBLE : View.GONE);
            appBarLayout_profile.setVisibility(show ? View.GONE : View.VISIBLE);
            linearLayout_profileContent.setVisibility(show ? View.GONE : View.VISIBLE);
            fab_editOrFollow.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onSuccess(Object... results) {
        QueryType type = (QueryType)results[0];
        switch (type) {
            case GET_PROFILE:
                UserModel u = (UserModel) results[1];
                setUser(u);
                showProgress(false);
                break;

            case LOAD_POSTS:
                Collection<PostModel> posts = (Collection<PostModel>)results[1];

                if(lastPostID == -1)
                    feedListAdapter.clear();
                if(posts.size() > 0)
                    isLoading = false;

                lastPostID = (int)results[2];
                feedListAdapter.addPosts(posts);
                listViewPosts.removeHeaderView(footerView);
                listViewPosts.removeFooterView(footerView);
                ViewCompat.setNestedScrollingEnabled(listViewPosts, true);
                break;
        }
    }

    @Override
    public void onFail(Object... errors) {
        QueryType type = (QueryType)errors[0];
        switch (type) {
            case GET_PROFILE:
                Toast.makeText(this, "Error while loading profile", Toast.LENGTH_SHORT).show();
                break;
            case LOAD_POSTS:
                Toast.makeText(this, "Error while loading posts", Toast.LENGTH_SHORT).show();
                break;
            case FOLLOW:
                Toast.makeText(this, "Error while following user", Toast.LENGTH_SHORT).show();
                break;
        }
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
            if ((listViewPosts.getCount() % client.getNumberOfPosts() == 0) && absListView.getLastVisiblePosition() == i2 - 1 && listViewPosts.getCount() >= client.getNumberOfPosts() && !isLoading) {
                isLoading = true;
                client.getPostsAsync(displayedUser.getUser().getId(), lastPostID, true, ProfileActivity.this);
                listViewPosts.addFooterView(footerView);
            }
        }
    }
}
