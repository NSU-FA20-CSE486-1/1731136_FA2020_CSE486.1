package com.ferdouszislam.nsu.cse486.sec01.networkconnectivitydetector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkConnectionBroadcastReceiver extends BroadcastReceiver {

    private DetectInternetConnectivity mDetectInternetConnectivity;

    public NetworkConnectionBroadcastReceiver(DetectInternetConnectivity mDetectInternetConnectivity) {
        this.mDetectInternetConnectivity = mDetectInternetConnectivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(isInternetAvailable(context)){

            mDetectInternetConnectivity.onInternetAvailable();
        }

        else mDetectInternetConnectivity.onInternetNotAvailable();
    }

    private boolean isInternetAvailable(Context context){

        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo!=null && networkInfo.isConnected();
    }

    public interface DetectInternetConnectivity{

        void onInternetAvailable();
        void onInternetNotAvailable();
    }

}
