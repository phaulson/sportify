package com.spogss.sportifycommunity.activity;

import android.graphics.Color;
import android.os.Bundle;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.spogss.sportifycommunity.R;
import com.spogss.sportifycommunity.feed.FeedListAdapter;
import com.spogss.sportifycommunity.feed.Post;
import com.spogss.sportifycommunity.search.SectionsPageAdapter;
import com.spogss.sportifycommunity.search.TabFragmentSearch;

import java.util.ArrayList;

public class FeedActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    //UI Controls
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FloatingActionButton fab;
    ListView listViewFeed;
    SwipeRefreshLayout swipeRefreshLayout;

    //Adapter for tabs
    private SectionsPageAdapter sectionsPageAdapter;

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
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        //tabs
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setVisibility(View.GONE);

        //content feed
        listViewFeed = (ListView)findViewById(R.id.listView_feed);
        FeedListAdapter feedListAdapter = new FeedListAdapter(getApplicationContext());
        feedListAdapter.addPost(new Post("johnnybravo", "5 hours ago", "My name is Johnny Bravo and I am so fucking swole guys.",
                false, R.drawable.sp_heart_filled, R.drawable.sp_test_image, 10));
        feedListAdapter.addPost(new Post("paulim", "7 hours ago", "This is my first post lol.",
                true, R.drawable.ic_action_voice_search, R.drawable.sp_test_image, 5));
        feedListAdapter.addPost(new Post("simon", "2 days ago", "I am a hamster and I like to run in my Laufrad!!",
                true, R.drawable.ic_menu_camera, R.drawable.sp_test_image, 7));
        feedListAdapter.addPost(new Post("paulim", "2 days ago", "What the fuck is going on??",
                true, R.drawable.ic_action_voice_search, -1, 4));
        feedListAdapter.addPost(new Post("webi", "1 week ago", "Latrell Sprewell for MVP.",
                false, R.drawable.sp_home, R.drawable.sp_test_image, 0));
        listViewFeed.setAdapter(feedListAdapter);

        //swipeRefresh
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefresh_feed);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    //adds the fragments (tabs) to the viewPager
    private void setupViewPager(ViewPager pager) {
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
        item.setOnActionExpandListener(this);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);

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
            Snackbar.make(getWindow().getDecorView().getRootView(), "The LogoutActivity will be implemented soon", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    //SearchView events
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
    @Override
    public boolean onQueryTextChange(String newText) {
        // TODO: implement switch over selected tab
        // TODO: replace with connection to webservice and load data from database
        String[] search = {
                "nico", "pauli", "nina", "simon", "samuel"
        };
        ArrayList<String> list = new ArrayList<String>();
        if(!newText.isEmpty()) {
            for (String s : search) {
                if (s.toLowerCase().contains(newText.toLowerCase()))
                    list.add(s);
            }
        }

        ((TabFragmentSearch)sectionsPageAdapter.getItem(viewPager.getCurrentItem())).fillListSearch(this, list);
        return false;
    }
    @Override
    public boolean onMenuItemActionExpand(MenuItem menuItem) {
        fab.setVisibility(View.GONE);
        listViewFeed.setVisibility(View.GONE);
        tabLayout.setVisibility(View.VISIBLE);
        return true;
    }
    @Override
    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        fab.setVisibility(View.VISIBLE);
        listViewFeed.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.GONE);
        return true;
    }

    //swipeRefresh event
    @Override
    public void onRefresh() {
        // TODO: implement real refresh method
        Snackbar.make(getWindow().getDecorView().getRootView(), "Feed will be refreshed soon", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        swipeRefreshLayout.setRefreshing(false);
    }
}
