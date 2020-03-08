package com.iff.onepairv2;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import javax.xml.transform.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
        //call retrofit here
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://128.199.167.80:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BackEndController backEndController = retrofit.create(BackEndController.class);
        Call<Result> call = backEndController.matchTrigger2();
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if(response.isSuccessful()){
                    Results results = (Results) response.body();
                    String uID1 = results.getUid1();
                    String uID2 = results.getUid2();

                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Context context = null;
                Toast.makeText(context,t.toString(),Toast.LENGTH_SHORT).show();
            }
        });

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
