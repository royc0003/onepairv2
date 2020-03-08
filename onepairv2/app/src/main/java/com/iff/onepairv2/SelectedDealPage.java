package com.iff.onepairv2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SelectedDealPage extends AppCompatActivity {
    Dialog myDialog;
    AlertDialog alertDialog;
    private TextView dealsDetail;
    private ImageView dealsImage;
    private TextView termsCondition;
    private TextView startEnd;
    private ArrayList<Location> locations;

    private Deal deal;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_deal_page);

        //System.out.println("NEW Dialog created");
        dealsDetail = findViewById(R.id.dealsDetails);
        dealsImage = findViewById(R.id.dealsImage);
        termsCondition = findViewById(R.id.termsConditions);
        startEnd = findViewById(R.id.startend);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://128.199.167.80:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BackEndController backEndController = retrofit.create(BackEndController.class);
        Call<ArrayList<Location>> call = backEndController.getAllLocation();
        call.enqueue(new Callback<ArrayList<Location>>() {
            @Override
            public void onResponse(Call<ArrayList<Location>> call, Response<ArrayList<Location>> response) {
                if(!response.isSuccessful()){
                    System.out.println("Oops something went wrong!");
                    return;
                }
                locations = response.body();
            }

            @Override
            public void onFailure(Call<ArrayList<Location>> call, Throwable t) {
                System.out.println("Oops something went wrong!");
            }
        });


        //get data from previous activity when item of listview is clicked using intent
        Intent intent = getIntent();
        deal = (Deal) intent.getSerializableExtra("Deal");

        //details of webscraping goes in here
        dealsDetail.setText(deal.getName());
        termsCondition.setText(deal.getTerms());
        startEnd.setText(deal.getStart());
        Picasso.get().load(Uri.parse(deal.getImage())).into(dealsImage);

        //used for match pop up box
        myDialog = new Dialog(this);
        System.out.println("Dialog created");

        //Match button once its clicked
        Button matchBtn = findViewById(R.id.matchButton);
        matchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("TESTTTTT");
                locationAlertBox();
            }
        });
    }

    //used for locationAlertBox
    private void locationAlertBox() {
        //instantiate alertdialog builder
        AlertDialog.Builder myBuilder = new AlertDialog.Builder(this);
        //data source
        final ArrayList selectedLocations = new ArrayList();
        final String locationNames[] = new String[locations.size()];
        final ArrayList<Integer> selectedKey = new ArrayList<Integer>();
        for(int i = 0; i < locations.size(); i++){
            locationNames[i] = locations.get(i).getName();
        }
        //set properties using method chanining
        myBuilder.setTitle("Choose location(s)").setMultiChoiceItems(locationNames, null, new DialogInterface.OnMultiChoiceClickListener() {

            //selected locations are placed into an selectedLocations array list
            @Override
            public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                if(isChecked) {
                    //if the user checked the item, add it to the selected items
                    selectedLocations.add(locationNames[position]);
                    selectedKey.add(locations.get(position).getId());
                }
                else if (selectedLocations.contains(position)) {
                    //else if the item is already in the array list, remove it
                    selectedLocations.remove(Integer.valueOf(position));
                    selectedKey.remove(Integer.valueOf(locations.get(position).getId()));
                }

            }
        });

        /** for (Object x : selectedLocations) {
         System.out.println("checked");
         } **/

        myBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                StringBuilder sb = new StringBuilder();
                if (selectedLocations.size() == 0)
                {
                    Toast toast = Toast.makeText(SelectedDealPage.this, "Please select at least one location.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    /*
                    for (Object nebular : selectedLocations) {
                        sb.append(nebular.toString() + "\n");
                    }
                    Toast.makeText(SelectedDealPage.this, sb.toString(), Toast.LENGTH_SHORT).show();
                    System.out.println("LOcation button clicksed");
                    ShowPopUp();*/
                    String c = "";
                    for(int j = 0; j < selectedKey.size(); j++){
                        if(j == selectedKey.size()-1){
                            c += Integer.toString(selectedKey.get(j));
                        }else{
                            String x = Integer.toString(selectedKey.get(j)) + ",";
                            c += x;
                        }
                    }
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://128.199.167.80:8080/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    BackEndController backEndController = retrofit.create(BackEndController.class);
                    Call<Void> call = backEndController.addRequest(FirebaseAuth.getInstance().getCurrentUser().getUid(), deal.getId(), c);
                    System.out.println("This is the token ID"+FirebaseAuth.getInstance().getCurrentUser().getUid());

                    Toast toast = Toast.makeText(SelectedDealPage.this, "Successfully added to wait list", Toast.LENGTH_SHORT);
                    toast.show();

                    //Work on the algo here
                    ShowPopUp(); // currently working
                    //I dont quite understand why is there a need to use retrofit here - Royce

                    /*call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if(!response.isSuccessful()){
                                Toast toast = Toast.makeText(SelectedDealPage.this, "An error occurred. Please try again", Toast.LENGTH_SHORT);
                                toast.show();
                                return;
                            }
                            Toast toast = Toast.makeText(SelectedDealPage.this, "Successfully added to wait list", Toast.LENGTH_SHORT);
                            toast.show();
                            // Add matching algo here
                            //ShowPopUp();
                        }
                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast toast = Toast.makeText(SelectedDealPage.this, "An error occurred. Please try again", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });*/


                }
            }
        });

        //create Dialog
        alertDialog = myBuilder.create();
        //show dialog
        alertDialog.show();
    }

    //Once a match is made by system, this pop up box will appear
    public void ShowPopUp() {
        //accepts 2 tokens from getUserID
        //with the 2 userIDs; check if one of them is theirs if it is
        //use the other token to display the image and details and everything
        System.out.println("INSIDE SHOW POP UP");
        final TextView txtclose, matchUserName;
        final ImageView matchProfileImage;
        Button chatBtn;
        myDialog.setContentView(R.layout.matchpopup);
        txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
        chatBtn = (Button) myDialog.findViewById(R.id.chatBt); //link this button to Nick's
        matchProfileImage = (ImageView) myDialog.findViewById(R.id.match_profile_name);
        matchUserName = (TextView) myDialog.findViewById(R.id.match_Username);
        DatabaseReference trialData = getDatabaseReference();
        trialData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                matchUserName.setText(name);
                Picasso.get().load(image).into(matchProfileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Not able to access the database");

            }
        });


        System.out.println("Chat Button clicked");
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void scheduleJob(View v){
        ComponentName componentName = new ComponentName(this, MyJobService.class);
        JobInfo info = new JobInfo.Builder(123, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED) //any network
                .setPersisted(true) //Keep job alive even if reboot
        .build();

        JobScheduler scheduler = (JobScheduler) getSystemService((JOB_SCHEDULER_SERVICE));
       int resultCode = scheduler.schedule(info);
       if(resultCode == JobScheduler.RESULT_SUCCESS){
           Log.d(TAG, "Job scheduled");
       } else{
           Log.d(TAG, "Job scheduling failed");
       }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void cancelJob(View v){
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(123);
        Log.d(TAG, "Job Cancelled");
    }




}