package com.ferdouszislam.nsu.cse486.sec01.asynctaskalternate.asyncTaskAlternate;

import android.app.Activity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Abstract class created as an alternative to AsyncTask
 * this class should be extended by user defined classes
 * for doing single task outside of UI thread
 * @param <T1> initial data for task to begin
 * @param <T2> task progress update data
 * @param <T3> task complete result data
 */
public abstract class InstantBackgroundTask<T1, T2, T3> {

    // ALLOW ONLY 1 BACKGROUND THREAD
    private static ExecutorService backgoundExecutor = Executors.newFixedThreadPool(1);

    // the activity from where this instant task is to be performed
    private Activity activity;
    // callbacks to let the UI know of task progression
    protected InstantBackgroundTaskCallback<T2, T3> instantBackgroundTaskCallback;

    public InstantBackgroundTask(Activity activity, InstantBackgroundTaskCallback<T2, T3> instantBackgroundTaskCallback) {
        this.activity = activity;
        this.instantBackgroundTaskCallback = instantBackgroundTaskCallback;
    }

    /**
     * This method will be implemented in the class that extends this class
     * inside this method the background task will be performed
     */
    protected abstract void doInBackground(T1... initialData);

    public void execute(T1... initialData){

        try{

            backgoundExecutor.execute(() -> {

                activity.runOnUiThread(() -> instantBackgroundTaskCallback.onTaskStarted());

                doInBackground(initialData);
            });

        }catch (Exception e){

            instantBackgroundTaskCallback.onError(e.getMessage());
        }
    }

    // update progress to UI
    protected void progressUpdate(T2 progressData){

        try{

            activity.runOnUiThread(() -> instantBackgroundTaskCallback.onProgressUpdate(progressData));

        }catch (Exception e){

            instantBackgroundTaskCallback.onError(e.getMessage());
        }
    }

    // update error to UI
    protected void notifyError(String message){

        activity.runOnUiThread(() -> instantBackgroundTaskCallback.onError(message));
    }

    // update result data after task completion to UI
    protected void taskComplete(T3... resultData){

        try {

            activity.runOnUiThread(() -> instantBackgroundTaskCallback.onTaskFinished(resultData));

        }catch (Exception e){

            instantBackgroundTaskCallback.onError(e.getMessage());
        }
    }

    /**
     * Interface for
     * @param <CT1> update data type
     * @param <CT2> result data type (after task completion)
     */
    public interface InstantBackgroundTaskCallback<CT1, CT2>{

        void onTaskStarted();

        void onProgressUpdate(CT1 progressData);

        void onTaskFinished(CT2... resultData);

        void onError(String message);

    }

}
