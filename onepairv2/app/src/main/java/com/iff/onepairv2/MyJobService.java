package com.iff.onepairv2;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MyJobService extends JobService {
    private static final String TAG = "MyJobService";
    private boolean jobCancelled = false;
    private int counter = 10000;
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Job started");
        doBackgroundWork(params);

        return true;
    }

    private void doBackgroundWork(final JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i <100; i++){
                    Log.d(TAG,"run: "+i);
                    if(jobCancelled){
                        return; //breaks from the current running thread
                    }
                    try {
                        Thread.sleep(counter); // start off w 10s
                        if(i%10 == 0){
                            counter -= 1000; //decrement of 1s for every 10 counts
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Log.d(TAG, "Job Finished");
                jobFinished(params, false); // end work
            }
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters params) { //if wifi connection
        Log.d(TAG, "Job cancelled before completion");
        jobCancelled = true;
        return true;
    }
}
