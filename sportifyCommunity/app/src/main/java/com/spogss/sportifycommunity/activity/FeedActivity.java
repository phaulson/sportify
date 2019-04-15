package com.spogss.sportifycommunity.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.spogss.sportifycommunity.R;
import com.spogss.sportifycommunity.adapter.FeedListAdapter;
import com.spogss.sportifycommunity.adapter.SearchListAdapter;
import com.spogss.sportifycommunity.adapter.SectionsPageAdapter;
import com.spogss.sportifycommunity.data.Plan;
import com.spogss.sportifycommunity.data.Post;
import com.spogss.sportifycommunity.data.connection.QueryType;
import com.spogss.sportifycommunity.data.connection.SportifyClient;
import com.spogss.sportifycommunity.data.User;
import com.spogss.sportifycommunity.data.connection.asynctasks.ClientQueryListener;
import com.spogss.sportifycommunity.fragment.TabFragmentSearch;
import com.spogss.sportifycommunity.model.PlanModel;
import com.spogss.sportifycommunity.model.PostModel;

import java.util.ArrayList;
import java.util.Collection;

public class FeedActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, ClientQueryListener {

    //UI Controls
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FloatingActionButton fab;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SearchView searchView;
    private ListView listViewFeed;
    private View footerView;
    private View navHeader;

    //Google Maps API
    private static final int ERROR_DIALOG_REQUEST = 9001;

    private SportifyClient client;
    //Adapter for tabs
    private SectionsPageAdapter sectionsPageAdapter;
    //Adapter for posts
    private FeedListAdapter feedListAdapter;
    //for loading
    private boolean isLoading = false;

    private int lastPostID = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        client = SportifyClient.newInstance();
        client.setNumberOfPosts(6);
        client.setNumberOfUsers(14);
        client.setNumberOfPlans(14);
        client.setNumberOfComments(14);

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

        client.getPostsAsync(client.getCurrentUserID(), lastPostID, false, this);
        listViewFeed.addHeaderView(footerView);

        //Map
        isServicesOK();

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
            onRefresh();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_plans) {
            Intent intent = new Intent(this, ShowPlansActivity.class);
            intent.putExtra("userid", -1);
            startActivity(intent);
        } else if (id == R.id.nav_map) {
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_settings) {
            Snackbar.make(getWindow().getDecorView().getRootView(), "The SettingsActivity will be implemented soon", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        } else if (id == R.id.nav_logout) {
            logout();
        }
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
        client.getPostsAsync(client.getCurrentUserID(), lastPostID, false, this);
    }

    /**
     * creates a new PlansListAdapter and fills it with users whose usernames contain the newText
     *
     * @param newText the text that the user typed in the SearchView
     */
    private void search(String newText) {
        TabFragmentSearch fragment = ((TabFragmentSearch) sectionsPageAdapter.getItem(viewPager.getCurrentItem()));
        switch (tabLayout.getSelectedTabPosition()) {
            case 0:
                client.searchUsersAsync(newText.toLowerCase(), fragment, false, this);
                break;
            case 1:
                client.searchUsersAsync(newText.toLowerCase(), fragment, true, this);
                break;
            case 2:
                client.searchPlansAsync(newText.toLowerCase(), fragment, this);
                break;
        }
    }

    @Override
    public void onSuccess(Object... results) {
        QueryType type = (QueryType)results[0];
        switch (type) {
            case LOAD_POSTS:
                Collection<PostModel> postModels = (Collection<PostModel>) results[1];

                if(lastPostID == -1)
                    feedListAdapter.clear();
                if(postModels.size() > 0)
                    isLoading = false;

                feedListAdapter.addPosts(postModels);
                swipeRefreshLayout.setRefreshing(false);
                listViewFeed.removeHeaderView(footerView);
                listViewFeed.removeFooterView(footerView);

                lastPostID = (int)results[2];
                break;

            case SEARCH_USERS:
                SearchListAdapter<User> adapter = new SearchListAdapter<User>(this);
                for(User u : (Collection<User>)results[1])
                    adapter.addGeneric(u, u.getId());
                ((TabFragmentSearch)(results[2])).fillList(adapter);
                break;

            case SEARCH_PLANS:
                SearchListAdapter<PlanModel> a = new SearchListAdapter<PlanModel>(this);
                for(PlanModel p : (Collection<PlanModel>)results[1])
                    a.addGeneric(p, p.getPlan().getId());
                ((TabFragmentSearch)(results[2])).fillList(a);
                break;
        }
    }

    @Override
    public void onFail(Object... errors) {
        QueryType type = (QueryType)errors[0];
        switch (type){
            case LOAD_POSTS:
                Toast.makeText(this, "Error while loading posts", Toast.LENGTH_SHORT).show();
                break;
            case SEARCH_USERS:
                Toast.makeText(this, "Error while searching for users", Toast.LENGTH_SHORT).show();
                break;
            case SEARCH_PLANS:
                Toast.makeText(this, "Error while searching plans", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private boolean isServicesOK(){
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(FeedActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //error occured but we can resolve it
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(FeedActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this,"You can't make map requests",Toast.LENGTH_SHORT).show();
        }
        return false;

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
                listViewFeed.addFooterView(footerView);
                client.getPostsAsync(client.getCurrentUserID(), lastPostID, false, FeedActivity.this);
            }
        }
    }
}
