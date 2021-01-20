package com.ferdouszislam.nsu.cse486.sec01.homemealapp.commonActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.R;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.broadcastReceivers.NetworkConnectionBroadcastReceiver;
import com.google.android.material.snackbar.Snackbar;

public class BaseActivity extends AppCompatActivity implements NetworkConnectionBroadcastReceiver.InternetStatusCallback {

    private NetworkConnectionBroadcastReceiver mNetworkConnBroadcastReceiver = null;

    private Snackbar mSnackbar;

    private boolean mGiveInternetStatusFeedback = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void enableInternetStatusFeedback(View view){

        mGiveInternetStatusFeedback = true;

        mSnackbar = Snackbar.make(view, R.string.no_internet, Snackbar.LENGTH_INDEFINITE);
        mSnackbar.setAction("dismiss", v -> mSnackbar.dismiss());

        mNetworkConnBroadcastReceiver =
                new NetworkConnectionBroadcastReceiver(this);
        // register the broadcast receiver
        registerReceiver(mNetworkConnBroadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mNetworkConnBroadcastReceiver!=null && mGiveInternetStatusFeedback) {
            // register the broadcast receiver
            registerReceiver(mNetworkConnBroadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }

    }

    @Override
    protected void onStop() {

        if(mNetworkConnBroadcastReceiver!=null && mGiveInternetStatusFeedback) unregisterReceiver(mNetworkConnBroadcastReceiver);

        super.onStop();
    }

    @Override
    public void onInternetAvailable() {

        mSnackbar.dismiss();
    }

    @Override
    public void onInternetNotAvailable() {

        mSnackbar.show();
    }
}