package com.ferdouszislam.nsu.cse486.sec01.asynctaskalternate.asyncTaskAlternate;

import android.app.Activity;

import java.util.ArrayList;

public class SampleInstantBackgroundTask extends InstantBackgroundTask<Integer, Integer, String> {


    public SampleInstantBackgroundTask(Activity activity,
                                       InstantBackgroundTaskCallback<Integer, String> instantBackgroundTaskCallback) {
        super(activity, instantBackgroundTaskCallback);
    }

    @Override
    protected void doInBackground(Integer... initialData) {

        int delayInSeconds = initialData[0], timeSteps = 0;

        try {

            for(timeSteps=1;timeSteps<=5;timeSteps++){

                Thread.sleep(delayInSeconds*1000);
                progressUpdate(timeSteps*24);
            }

        } catch (InterruptedException e) {

            notifyError(e.getMessage());
        }

        taskComplete("Took " + timeSteps*delayInSeconds + " seconds to complete");
    }
}
