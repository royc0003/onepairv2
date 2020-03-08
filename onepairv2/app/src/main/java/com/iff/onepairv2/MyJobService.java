package com.iff.onepairv2;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
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
    int isSuccessful = 0;
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Job started");
        doBackgroundWork(params);

        return true;
    }

    private void doBackgroundWork(final JobParameters params) {
        //call retrofit here

        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i <100; i++){
                    Log.d(TAG,"run: "+i);
                    //Calls matchTrigger 100 counts
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://128.199.167.80:8080/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    BackEndController backEndController = retrofit.create(BackEndController.class);
                    Call<Results> call = backEndController.matchTrigger2();
                    call.enqueue(new Callback<Results>() {
                        @Override
                        public void onResponse(Call<Results> call, Response<Results> response) {
                            if(!response.isSuccessful()){
                                System.out.println("Unable to find match");
                                return;
                            }
                            Results results = response.body();
                            String uID1 = results.getUid1();
                            String uID2 = results.getUid2();

                            Intent intent = new Intent(MyJobService.this, SelectedDealPage.class);
                            isSuccessful = 1;
                            intent.putExtra("String_uID1", uID1);
                            intent.putExtra("String_uID2",uID2);
                            intent.putExtra("isSuccessful", isSuccessful); // for checking
                            //since found can cancel job now
                            jobCancelled = true; // cancel job
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<Results> call, Throwable t) {
                            Context context = null;
                            Toast.makeText(context,t.toString(),Toast.LENGTH_SHORT).show();
                            System.out.println("Re-run matchTrigger2 again...");
                        }
                    });
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
                // failed to match
             /*   Intent intent = new Intent(MyJobService.this, SelectedDealPage.class);
                intent.putExtra("isSuccessful", isSuccessful);*/

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
