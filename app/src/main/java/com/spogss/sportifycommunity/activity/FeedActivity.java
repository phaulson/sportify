package com.spogss.sportifycommunity.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.spogss.sportifycommunity.R;
import com.spogss.sportifycommunity.adapter.FeedListAdapter;
import com.spogss.sportifycommunity.adapter.SearchListAdapter;
import com.spogss.sportifycommunity.adapter.SectionsPageAdapter;
import com.spogss.sportifycommunity.data.Plan;
import com.spogss.sportifycommunity.data.Post;
import com.spogss.sportifycommunity.data.SportifyClient;
import com.spogss.sportifycommunity.data.User;
import com.spogss.sportifycommunity.fragment.TabFragmentSearch;
import com.spogss.sportifycommunity.model.PostModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;

public class FeedActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    //UI Controls
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FloatingActionButton fab;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SearchView searchView;
    private ListView listViewFeed;
    private View footerView;
    private View navHeader;

    private SportifyClient client;
    //Adapter for tabs
    private SectionsPageAdapter sectionsPageAdapter;
    //Adapter for posts
    private FeedListAdapter feedListAdapter;
    //for loading
    private boolean isLoading = false;
    private Handler loadingHandler;

    private int lastPostID = -1;

    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        client = SportifyClient.newInstance();
        client.setNumberOfPosts(6);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navHeader = navigationView.getHeaderView(0);
        navHeader.setOnClickListener(this);
        TextView navUsername = (TextView)navHeader.findViewById(R.id.textView_nav_username);
        navUsername.setText(client.getCurrentUser().getUsername());

        //tabs
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupSearchViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setVisibility(View.GONE);
        viewPager.addOnPageChangeListener(new PageChangeListener(tabLayout));

        //content feed
        feedListAdapter = new FeedListAdapter(getApplicationContext());
        listViewFeed = (ListView) findViewById(R.id.listView_feed);
        listViewFeed.setAdapter(feedListAdapter);
        listViewFeed.setOnScrollListener(new ScrollListener());

        //swipeRefresh
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh_feed);
        swipeRefreshLayout.setOnRefreshListener(this);

        //loading
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        footerView = layoutInflater.inflate(R.layout.list_footer_feed, null);
        loadingHandler = new LoadingHandler();

        new LoadPostsTask().execute((Void) null);
        listViewFeed.addHeaderView(footerView);

    }

    /**
     * adds the fragments (tabs) to the ViewPager
     *
     * @param pager the ViewPager to which the fragments are added
     */
    private void setupSearchViewPager(ViewPager pager) {
        sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        sectionsPageAdapter.addFragment(new TabFragmentSearch(), "Users");
        sectionsPageAdapter.addFragment(new TabFragmentSearch(), "Pages");
        sectionsPageAdapter.addFragment(new TabFragmentSearch(), "Plans");
        pager.setAdapter(sectionsPageAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.feed, menu);

        //Custom code
        MenuItem item = menu.findItem(R.id.action_search);
        item.setOnActionExpandListener(new MenuItemListener());
        searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchViewListener());

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // TODO: implement onMenuItemClick
        if (id == R.id.nav_home) {
            Snackbar.make(getWindow().getDecorView().getRootView(), "The posts will reload soon", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else if (id == R.id.nav_plans) {
            Snackbar.make(getWindow().getDecorView().getRootView(), "The PlansActivity will be implemented soon", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        } else if (id == R.id.nav_map) {
            Snackbar.make(getWindow().getDecorView().getRootView(), "The MapActivity will be implemented soon", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        } else if (id == R.id.nav_settings) {
            Snackbar.make(getWindow().getDecorView().getRootView(), "The SettingsActivity will be implemented soon", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        } else if (id == R.id.nav_logout) {
            logout();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * sets the locally saved login credentials to null
     */
    private void deleteLoginCredentials() {
        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username", null);
        editor.putString("password", null);
        editor.commit();
    }

    /**
     * launches the LoginActivity and closes the FeedActivity
     */
    private void logout() {
        deleteLoginCredentials();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.fab) {
            Intent intent = new Intent(this, PostActivity.class);
            startActivity(intent);
        }
        else if(id == navHeader.getId()) {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("profile", client.getCurrentUserID());
            startActivity(intent);
        }
    }

    //swipeRefresh event
    @Override
    public void onRefresh() {
        lastPostID = -1;
        new LoadPostsTask().execute((Void) null);
    }

    /**
     * creates a new SearchListAdapter and fills it with users whose usernames contain the newText
     *
     * @param newText the text that the user typed in the SearchView
     */
    private void search(String newText) {
        TabFragmentSearch fragment = ((TabFragmentSearch) sectionsPageAdapter.getItem(viewPager.getCurrentItem()));
        switch (tabLayout.getSelectedTabPosition()) {
            case 0:
                new SearchUsersTask(newText.toLowerCase(), fragment, false).execute((Void) null);
                break;
            case 1:
                new SearchUsersTask(newText.toLowerCase(), fragment, true).execute((Void) null);
                break;
            case 2:
                new SearchPlansTask(newText.toLowerCase(), fragment).execute((Void) null);
                break;
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
        ArrayList<Post> posts = (ArrayList<Post>) client.getPosts(client.getCurrentUserID(), lastPostID);
        for (Post p : posts) {
            User u = client.getProfile(p.getCreatorId());
            int numberOfLikes = client.getNumberOfLikes(p.getId());
            boolean liked = client.isLiked(client.getCurrentUserID(), p.getId());
            postModels.add(new PostModel(p, u, numberOfLikes, liked));
        }
        if (postModels.size() > 0)
            lastPostID = (postModels).get(postModels.size() - 1).getPost().getId();

        return postModels;
    }


    //innerclass

    /**
     * Listener that handles the searchViewTextChange event
     */
    private class SearchViewListener implements SearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextSubmit(String query) {
            return true;
        }

        @Override
        public boolean onQueryTextChange(String query) {
            search(query);
            return false;
        }
    }

    /**
     * Listener that handles the searchItemExpand event
     */
    private class MenuItemListener implements MenuItem.OnActionExpandListener {

        @Override
        public boolean onMenuItemActionExpand(MenuItem menuItem) {
            fab.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.GONE);
            viewPager.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);
            return true;
        }

        @Override
        public boolean onMenuItemActionCollapse(MenuItem menuItem) {
            fab.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);
            return true;
        }
    }

    /**
     * Listener that handles the TabChangeEvent
     */
    private class PageChangeListener extends TabLayout.TabLayoutOnPageChangeListener {

        public PageChangeListener(TabLayout tabLayout) {
            super(tabLayout);
        }

        @Override
        public void onPageSelected(int index) {
            search(searchView.getQuery().toString());
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
            if ((listViewFeed.getCount() % client.getNumberOfPosts() == 0) && absListView.getLastVisiblePosition() == i2 - 1 && listViewFeed.getCount() >= client.getNumberOfPosts() && !isLoading) {
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
                    listViewFeed.addFooterView(footerView);
                    break;
                case 1:
                    //add new posts and remove footer
                    ArrayList<PostModel> models = (ArrayList<PostModel>) msg.obj;
                    if (models.size() > 0) {
                        feedListAdapter.addPosts(models);
                        isLoading = false;
                    }
                    listViewFeed.removeFooterView(footerView);
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
            loadingHandler.sendMessage(loadingHandler.obtainMessage(1
                    , loadPosts()));
        }
    }


    //AsyncTasks
    private class LoadPostsTask extends AsyncTask<Void, Void, Collection<PostModel>> {
        @Override
        protected Collection<PostModel> doInBackground(Void... params) {
            return loadPosts();
        }

        @Override
        protected void onPostExecute(final Collection<PostModel> posts) {
            feedListAdapter.clear();
            feedListAdapter.addPosts(posts);
            swipeRefreshLayout.setRefreshing(false);
            listViewFeed.removeHeaderView(footerView);
        }
    }

    private class SearchUsersTask extends AsyncTask<Void, Void, Collection<User>> {
        private String name;
        private TabFragmentSearch fragment;
        private boolean isPro;

        public SearchUsersTask(String name, TabFragmentSearch fragment, boolean isPro) {
            this.name = name;
            this.fragment = fragment;
            this.isPro = isPro;
        }

        @Override
        protected Collection<User> doInBackground(Void... params) {
            // TODO: call webservice for search users
            if(name.equals(""))
                return new ArrayList<User>();
            return client.searchUsers(name, isPro);
        }

        @Override
        protected void onPostExecute(final Collection<User> users) {
            SearchListAdapter<User> adapter = new SearchListAdapter<User>(FeedActivity.this);
            for(User u : users)
                adapter.addGeneric(u, u.getId());
            fragment.fillList(adapter);
        }
    }

    private class SearchPlansTask extends AsyncTask<Void, Void, Collection<Plan>> {
        private String name;
        private TabFragmentSearch fragment;

        public SearchPlansTask(String name, TabFragmentSearch fragment) {
            this.name = name;
            this.fragment = fragment;
        }

        @Override
        protected Collection<Plan> doInBackground(Void... params) {
            // TODO: call webservice for search users
            if(name.equals(""))
                return new ArrayList<Plan>();
            return client.searchPlans(name);
        }

        @Override
        protected void onPostExecute(final Collection<Plan> plans) {
            SearchListAdapter<Plan> adapter = new SearchListAdapter<Plan>(FeedActivity.this);
            for(Plan p : plans)
                adapter.addGeneric(p, p.getId());
            fragment.fillList(adapter);
        }
    }
}
