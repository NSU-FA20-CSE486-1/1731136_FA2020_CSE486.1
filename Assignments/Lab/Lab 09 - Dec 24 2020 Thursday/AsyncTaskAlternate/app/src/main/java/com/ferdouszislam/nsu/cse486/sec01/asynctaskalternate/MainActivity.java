package com.ferdouszislam.nsu.cse486.sec01.asynctaskalternate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ferdouszislam.nsu.cse486.sec01.asynctaskalternate.asyncTaskAlternate.InstantBackgroundTask;
import com.ferdouszislam.nsu.cse486.sec01.asynctaskalternate.asyncTaskAlternate.SampleInstantBackgroundTask;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MA-debug";

    // ui
    private TextView mBackgroundTaskResultTV, mBackgroundTaskProgressTV;

    // background task executor
    private SampleInstantBackgroundTask mSampleInstantBackgroundTask;
    private InstantBackgroundTask.InstantBackgroundTaskCallback<Integer, String> mInstantBackgroundTaskCallback =
            new InstantBackgroundTask.InstantBackgroundTaskCallback<Integer, String>() {

                @Override
                public void onTaskStarted() {

                    if(mBackgroundTaskResultTV!=null) mBackgroundTaskResultTV.setText("Task started!");
                    if(mBackgroundTaskProgressTV!=null) mBackgroundTaskProgressTV.setVisibility(View.VISIBLE);
                }

                @Override
                public void onProgressUpdate(Integer progressData) {

                    if(mBackgroundTaskProgressTV!=null) mBackgroundTaskProgressTV.setText(progressData+"% task complete...");
                }

                @Override
                public void onTaskFinished(String... resultData) {

                    if(mBackgroundTaskProgressTV!=null) mBackgroundTaskProgressTV.setVisibility(View.GONE);

                    if(mBackgroundTaskResultTV!=null) mBackgroundTaskResultTV.setText(resultData[0]);
                }

                @Override
                public void onError(String message) {

                    showToast("some unknown error occurred!");

                    Log.d(TAG, "onError: error -> " + message);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {

        mBackgroundTaskResultTV = findViewById(R.id.backgroundTaskResult_TextView);
        mBackgroundTaskProgressTV = findViewById(R.id.backgroundTaskProgress_TextView);

        startBackgroundTask();
    }

    /*
    start the demo background task
     */
    private void startBackgroundTask() {

        mSampleInstantBackgroundTask =
                new SampleInstantBackgroundTask(this, mInstantBackgroundTaskCallback);

        mSampleInstantBackgroundTask.execute(2);
    }

    private void showToast(String message){

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}