package com.nsu.ece.ferdouszislam.cse486l.sec01.tabsapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.nsu.ece.ferdouszislam.cse486l.sec01.tabsapp.FragmentScreens.HomeTabFragment;
import com.nsu.ece.ferdouszislam.cse486l.sec01.tabsapp.FragmentScreens.ProfileTabFragment;
import com.nsu.ece.ferdouszislam.cse486l.sec01.tabsapp.FragmentScreens.SearchTabFragment;
import com.nsu.ece.ferdouszislam.cse486l.sec01.tabsapp.FragmentScreens.SettingsTabFragment;

/**
 * Adapter to fill Activity with appropriate fragment view
 */
public class NavigationTabPagerAdapter extends FragmentStatePagerAdapter {

    public static final int HOME_TAB_ID = 0;
    public static final int PROFILE_TAB_ID = 1;
    public static final int SEARCH_TAB_ID = 2;
    public static final int SETTINGS_TAB_ID = 3;

    private int mNumOfTabs;

    public NavigationTabPagerAdapter(@NonNull FragmentManager fm, int mNumOfTabs) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mNumOfTabs = mNumOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){

            case HOME_TAB_ID: return new HomeTabFragment();

            case PROFILE_TAB_ID: return new ProfileTabFragment();

            case SEARCH_TAB_ID: return new SearchTabFragment();

            case SETTINGS_TAB_ID: return new SettingsTabFragment();

            default: return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
