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
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.spogss.sportifycommunity.R;
import com.spogss.sportifycommunity.tab.SectionsPageAdapter;
import com.spogss.sportifycommunity.tab.TabFragmentSearch;

import java.util.ArrayList;

public class FeedActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    //UI Controls
    private SearchView searchView;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FloatingActionButton fab;

    //Adapter for tabs
    private SectionsPageAdapter sectionsPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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
    }

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
        searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //   return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //SearchView
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
        tabLayout.setVisibility(View.VISIBLE);
        return true;
    }
    @Override
    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        fab.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.GONE);
        return true;
    }
}
