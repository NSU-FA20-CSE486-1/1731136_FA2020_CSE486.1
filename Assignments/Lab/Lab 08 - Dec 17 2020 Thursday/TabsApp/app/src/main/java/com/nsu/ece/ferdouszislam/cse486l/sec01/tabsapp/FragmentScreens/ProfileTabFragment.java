package com.nsu.ece.ferdouszislam.cse486l.sec01.tabsapp.FragmentScreens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nsu.ece.ferdouszislam.cse486l.sec01.tabsapp.R;

/**
 * Fragment for 'Profile' screen
 */
public class ProfileTabFragment extends Fragment {


    public ProfileTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_tab, container, false);
    }
}