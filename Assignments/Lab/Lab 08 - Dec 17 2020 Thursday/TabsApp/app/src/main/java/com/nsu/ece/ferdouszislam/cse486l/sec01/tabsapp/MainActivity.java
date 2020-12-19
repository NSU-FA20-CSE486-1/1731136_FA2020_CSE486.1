package com.nsu.ece.ferdouszislam.cse486l.sec01.tabsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MA-debug";
    // previous selected tab position
    // required to switch to last selected tab
    // when user presses physical back button
    private int mPreviousSelectedTabPosition = -1;

    // ui
    private TabLayout mNavigationTabLayout;
    private ViewPager mNavigationViewPager;
    private NavigationTabPagerAdapter mNavigationTabPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    /*
    Physical back button press callback
    show last picked tab,
    if no last picked tab then close activity
     */
    @Override
    public void onBackPressed() {

        if(mPreviousSelectedTabPosition!=-1 && mNavigationViewPager!=null){

            mNavigationViewPager.setCurrentItem(mPreviousSelectedTabPosition);
        }

        else finish();
    }


    private void init() {

        setupNavigationFragment();

    }

    private void setupNavigationFragment() {

        mNavigationTabLayout = findViewById(R.id.navigation_tab);

        mNavigationViewPager = findViewById(R.id.navigation_viewPager);

        mNavigationTabPagerAdapter =
                new NavigationTabPagerAdapter(getSupportFragmentManager(), mNavigationTabLayout.getTabCount());

        mNavigationViewPager.setAdapter(mNavigationTabPagerAdapter);

        // navigation tab onClick listener
        mNavigationViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mNavigationTabLayout));
        mNavigationTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                changeTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                mPreviousSelectedTabPosition = tab.getPosition();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public void homeTabItemClick(View view) {

        changeTab(NavigationTabPagerAdapter.HOME_TAB_ID);
    }

    public void profileTabItemClick(View view) {

        changeTab(NavigationTabPagerAdapter.PROFILE_TAB_ID);
    }

    public void searchTabItemClick(View view) {

        changeTab(NavigationTabPagerAdapter.SEARCH_TAB_ID);
    }

    public void settingsTabItemClick(View view) {

        changeTab(NavigationTabPagerAdapter.SETTINGS_TAB_ID);
    }

    /*
    change screen based on navigation tab selection
     */
    private void changeTab(int tabPosition){

        if(mNavigationViewPager!=null){
            mNavigationViewPager.setCurrentItem(tabPosition);
        }
    }
}