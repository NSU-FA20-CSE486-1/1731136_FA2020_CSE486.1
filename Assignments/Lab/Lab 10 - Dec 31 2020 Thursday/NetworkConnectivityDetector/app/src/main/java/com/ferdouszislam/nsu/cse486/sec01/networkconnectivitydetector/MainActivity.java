package com.ferdouszislam.nsu.cse486.sec01.networkconnectivitydetector;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity
        implements NetworkConnectionBroadcastReceiver.DetectInternetConnectivity {

    // ui
    private Snackbar mSnackbar;

    // broadcast receiver
    NetworkConnectionBroadcastReceiver mNetworkConnBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {

        View view = findViewById(R.id.main_layout);
        mSnackbar = Snackbar.make(view, R.string.no_internet, Snackbar.LENGTH_INDEFINITE);
        mSnackbar.setAction("dismiss", v -> mSnackbar.dismiss());

        mNetworkConnBroadcastReceiver =
                new NetworkConnectionBroadcastReceiver(this);
        // register the broadcast receiver
        registerReceiver(mNetworkConnBroadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {

        // unregister receiver
        unregisterReceiver(mNetworkConnBroadcastReceiver);

        super.onDestroy();
    }

    /**
     * Method called when internet has become available
     */
    @Override
    public void onInternetAvailable() {

        mSnackbar.dismiss();
    }

    /**
     * Method called when internet has become unavailable
     */
    @Override
    public void onInternetNotAvailable() {

        mSnackbar.show();
    }


}