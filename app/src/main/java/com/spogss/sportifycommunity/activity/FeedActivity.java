package com.spogss.sportifycommunity.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.spogss.sportifycommunity.R;
import com.spogss.sportifycommunity.tempData.User;
import com.spogss.sportifycommunity.adapter.FeedListAdapter;
import com.spogss.sportifycommunity.tempData.Post;
import com.spogss.sportifycommunity.adapter.SearchListAdapter;
import com.spogss.sportifycommunity.adapter.SectionsPageAdapter;
import com.spogss.sportifycommunity.fragment.TabFragmentSearch;

import java.util.ArrayList;

public class FeedActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    //UI Controls
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FloatingActionButton fab;
    SwipeRefreshLayout swipeRefreshLayout;
    SearchView searchView;
    ListView listViewFeed;
    View footerView;

    //Adapter for tabs
    private SectionsPageAdapter sectionsPageAdapter;
    //Adapter for posts
    FeedListAdapter feedListAdapter;
    //List of users, only for testing purposes
    private ArrayList<User> users;
    //for loading
    final int numberOfPosts = 5;
    boolean isLoading = false;
    Handler loadingHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

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


        //Custom code
        users = new ArrayList<User>();

        User johnny = new User("johnnybravo", R.drawable.ic_action_navigation_close);
        User pauli = new User("paulim", R.drawable.ic_action_voice_search);
        User simon = new User("simon", R.drawable.ic_action_navigation_close);
        User webi = new User("webi", R.drawable.ic_action_navigation_arrow_back);

        users.add(johnny);
        users.add(pauli);
        users.add(simon);
        users.add(webi);

        //tabs
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupSearchViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setVisibility(View.GONE);
        tabLayout.addOnTabSelectedListener(new TabLayoutListener());

        //content feed
        feedListAdapter = new FeedListAdapter(getApplicationContext());
        feedListAdapter.addPosts(getNewData());

        listViewFeed = (ListView)findViewById(R.id.listView_feed);
        listViewFeed.setAdapter(feedListAdapter);
        listViewFeed.setOnScrollListener(new ScrollListener());

        //swipeRefresh
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefresh_feed);
        swipeRefreshLayout.setOnRefreshListener(this);

        //loading
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        footerView = layoutInflater.inflate(R.layout.list_footer_feed, null);
        loadingHandler = new LoadingHandler();
    }

    /**
     * adds the fragments (tabs) to the ViewPager
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
        searchView  = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchViewListener());

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // TODO: implement onMenuItemClick
        if(id == R.id.nav_home) {
            Snackbar.make(getWindow().getDecorView().getRootView(), "The posts will reload soon", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        else if (id == R.id.nav_plans) {
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

        if(id == R.id.fab) {
            // TODO: open new Activity (PostActivity)
            Snackbar.make(view, "The PostActivity will be implemented soon", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    //swipeRefresh event
    @Override
    public void onRefresh() {
        // TODO: implement real refresh method
        Snackbar.make(getWindow().getDecorView().getRootView(), "Feed will be refreshed soon", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * creates a new SearchListAdapter and fills it with users whose usernames contain the newText
     * @param newText the text that the user typed in the SearchView
     */
    private void search(String newText) {
        // TODO: implement switch over selected tab
        // TODO: replace with connection to webservice and load data from database
        TabFragmentSearch fragment = ((TabFragmentSearch) sectionsPageAdapter.getItem(viewPager.getCurrentItem()));
        SearchListAdapter adapter = new SearchListAdapter(FeedActivity.this);
        if (!newText.equals("")) {
            for (User u : users) {
                if (u.getUsername().toLowerCase().contains(newText.toLowerCase()))
                    adapter.addUser(u);
            }
        }
        fragment.fillList(adapter);
    }


    /**
     * get the new Posts
     * @return an ArrayList of Posts
     */
    private ArrayList<Post> getNewData() {
        // TODO: load posts from database
        ArrayList<Post> posts = new ArrayList<Post>();
        posts.add(new Post("5 hours ago", "My name is Johnny Bravo and I am so fucking swole guys.",
                false, R.drawable.sp_test_image, 10, users.get(0)));
        posts.add(new Post("7 hours ago", "This is my first post lol.",
                true, R.drawable.sp_test_image, 5, users.get(1)));
        posts.add(new Post("2 days ago", "I am a hamster and I like to run in my laufrad!! Being a hamster is very nice after all. You can chill all day long, eat as much hamsterfutter as you want and run in your laufrag from time to time.",
                true, R.drawable.sp_test_image, 7, users.get(2)));
        posts.add(new Post("2 days ago", "What the fuck is going on??",
                true, -1, 4, users.get(1)));
        posts.add(new Post("1 week ago", "Latrell Sprewell for MVP.",
                false, R.drawable.sp_test_image, 0, users.get(3)));
        return posts;
    }



    //innerclass
    /**
     * Listener that handles the searchViewTextChange event
     */
    public class SearchViewListener implements SearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            search(newText);
            return false;
        }
    }

    /**
     * Listener that handles the searchItemExpand event
     */
    public class MenuItemListener implements MenuItem.OnActionExpandListener {

        @Override
        public boolean onMenuItemActionExpand(MenuItem menuItem) {
            fab.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.GONE);
            tabLayout.setVisibility(View.VISIBLE);
            return true;
        }

        @Override
        public boolean onMenuItemActionCollapse(MenuItem menuItem) {
            fab.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.GONE);
            return true;
        }
    }

    /**
     * Listener that handles the tabChange event
     */
    public class TabLayoutListener implements TabLayout.OnTabSelectedListener {

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            search(searchView.getQuery().toString());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    }

    /**
     * Listener that handles the ListViewScroll event
     */
    public class ScrollListener implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView absListView, int i) {

        }

        @Override
        public void onScroll(AbsListView absListView, int i, int i1, int i2) {
            if(absListView.getLastVisiblePosition() == i2 -1 && listViewFeed.getCount() >= numberOfPosts && !isLoading) {
                isLoading = true;
                Thread thread = new ThreadLoadMorePosts();
                thread.start();
            }
        }
    }

    /**
     * Handler that handles the loading Threads
     */
    public class LoadingHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //add footer when loading started
                    listViewFeed.addFooterView(footerView);
                    break;
                case 1:
                    //add new posts and remove footer
                    feedListAdapter.addPosts(getNewData());
                    listViewFeed.removeFooterView(footerView);
                    isLoading = false;
                    break;
            }
        }
    }

    /**
     * Thread that handles the loading event
     */
    public class ThreadLoadMorePosts extends Thread {
        @Override
        public void run() {
            //message for footer view
            loadingHandler.sendEmptyMessage(0);

            //simulate database call
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //send new Data
            loadingHandler.sendMessage(loadingHandler.obtainMessage(1, getNewData()));
        }
    }
}
