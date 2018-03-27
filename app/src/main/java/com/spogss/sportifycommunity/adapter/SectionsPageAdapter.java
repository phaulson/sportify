package com.spogss.sportifycommunity.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Pauli on 25.03.2018.
 */

public class SectionsPageAdapter extends FragmentPagerAdapter {

    private final ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private final ArrayList<String> fragmentTitles = new ArrayList<String>();

    public SectionsPageAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * adds a new Fragment with title to the adapter
     * @param fragment the new fragment
     * @param title the title of the fragment
     */
    public void addFragment(Fragment fragment, String title) {
        fragments.add(fragment);
        fragmentTitles.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitles.get(position);
    }
}
