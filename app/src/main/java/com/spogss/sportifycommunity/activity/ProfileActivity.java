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
import com.spogss.sportifycommunity.data.SportifyClient;
import com.spogss.sportifycommunity.data.User;
import com.spogss.sportifycommunity.model.ListMenuModel;
import com.spogss.sportifycommunity.model.PostModel;

import java.util.ArrayList;
import java.util.Collection;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
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
    private User displayedUser;
    private FeedListAdapter feedListAdapter;

    //for loading
    private boolean isLoading = false;
    private Handler loadingHandler;
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
        loadingHandler = new LoadingHandler();

        new SetUserTask().execute(userId);
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

    private void setUser(User u, boolean isUserFollowed) {
        displayedUser = u;
        setTitle(u.getUsername());
        refreshProfile();
        refreshCorellationState(isUserFollowed);
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
        collapsingToolbar.setTitle(displayedUser.getUsername());
        String bio = displayedUser.getDescription();
        bio = (bio == null || bio.isEmpty()) ? getString(R.string.not_specified) : bio;
        Log.i("bio:", bio);
        textView_description.setText(bio);
        if (isUserPro(displayedUser)) {
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
        if (displayedUser.getId() == client.getCurrentUserID()) {
            correlationState = UserCorrelation.OWN;
            correlationState = UserCorrelation.OWN;
        } else {
            if (isUserFollowed) {
                correlationState = UserCorrelation.FOLLOWING;
            } else {
                correlationState = UserCorrelation.NOT_FOLLOWING;
            }
        }
    }

    private boolean isUserFollowed(User u) {
        return client.isFollowing(client.getCurrentUserID(), u.getId());
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
                        new FollowUserTask(displayedUser.getId(), true).execute((Void) null);
                        refreshCorellationState(true);
                        refresh_fab_editOrFollow();
                        break;
                    case FOLLOWING:
                        new FollowUserTask(displayedUser.getId(), false).execute((Void) null);
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
                Toast.makeText(getApplicationContext(), "show plans", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void editProfile() {
        Intent intent_editProfile = new Intent(getApplicationContext(), EditProfileActivity.class);
        intent_editProfile.putExtra("profile", displayedUser);
        startActivityForResult(intent_editProfile, REQ_CODE_EDIT_PROFILE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_EDIT_PROFILE) {
            new SetUserTask().execute(displayedUser.getId());
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

    /**
     * loads Post, creator, numberOfLikes and isLiked from database and returns the PostModels
     *
     * @return a collection of PostModels
     */
    private Collection<PostModel> loadPosts() {
        // TODO: find way to implement with better performance
        ArrayList<PostModel> postModels = new ArrayList<PostModel>();
        ArrayList<Post> posts = (ArrayList<Post>) client.getPostsByCreator(displayedUser.getId(), lastPostID);
        for (Post p : posts) {
            int numberOfLikes = client.getNumberOfLikes(p.getId());
            boolean liked = client.isLiked(client.getCurrentUserID(), p.getId());
            postModels.add(new PostModel(p, displayedUser, numberOfLikes, liked));
        }
        if (postModels.size() > 0)
            lastPostID = (postModels).get(postModels.size() - 1).getPost().getId();

        return postModels;
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
                Thread thread = new ThreadLoadMorePosts();
                thread.start();
            }
        }
    }

    /**
     * Handler that handles the loading Threads
     */
    private class LoadingHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //add footer when loading started
                    listViewPosts.addFooterView(footerView);
                    break;
                case 1:
                    //add new posts and remove footer
                    ArrayList<PostModel> models = (ArrayList<PostModel>) msg.obj;
                    if (models.size() > 0) {
                        feedListAdapter.addPosts(models);
                        isLoading = false;
                    }
                    listViewPosts.removeFooterView(footerView);
                    break;
            }
        }
    }

    /**
     * Thread that handles the loading event
     */
    private class ThreadLoadMorePosts extends Thread {
        @Override
        public void run() {
            //message for footer view
            loadingHandler.sendEmptyMessage(0);

            //send new Data
            loadingHandler.sendMessage(loadingHandler.obtainMessage(1, loadPosts()));
        }
    }

    private class SetUserTask extends AsyncTask<Integer, Void, User> {
        private boolean isUserFollowed;

        @Override
        protected void onPreExecute() {
            showProgress(true);
        }

        @Override
        protected User doInBackground(Integer... integers) {
            User u = client.getProfile(integers[0]);
            isUserFollowed = isUserFollowed(u);
            return u;
        }

        @Override
        protected void onPostExecute(User u) {
            setUser(u, isUserFollowed);
            showProgress(false);

            lastPostID = -1;
            new LoadPostsTask().execute((Void) null);
            listViewPosts.addHeaderView(footerView);
        }
    }

    private class FollowUserTask extends AsyncTask<Void, Void, Void> {
        private boolean follow;
        private int userId;

        public FollowUserTask(int userId, boolean follow) {
            this.userId = userId;
            this.follow = follow;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            client.setUserFollow(client.getCurrentUserID(), userId, follow);
            return null;
        }
    }

    private class LoadPostsTask extends AsyncTask<Void, Void, Collection<PostModel>> {
        @Override
        protected Collection<PostModel> doInBackground(Void... params) {
            return loadPosts();
        }

        @Override
        protected void onPostExecute(final Collection<PostModel> posts) {
            feedListAdapter.clear();
            feedListAdapter.addPosts(posts);
            listViewPosts.removeHeaderView(footerView);
            ViewCompat.setNestedScrollingEnabled(listViewPosts, true);
        }
    }
}
