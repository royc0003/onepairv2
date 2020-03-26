package com.iff.onepairv2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Activity for a selected deal
 */
public class SelectedDealPage extends AppCompatActivity {
    /**
     * Shows a pop-up of the matched user
     */
    private Dialog myDialog;
    /**
     * Dialog to be appeared if user does not select a single location before hitting the matching button
     */
    private AlertDialog alertDialog;
    /**
     * Details of the selected deal
     */
    private TextView dealsDetail;
    /**
     * Reveals selected deal image
     */
    private ImageView dealsImage;
    /**
     * Terms and Condition for the deal
     */
    private TextView termsCondition;
    /**
     * Start time and end time of the deal
     */
    private TextView startEnd;
    /**
     * List of location to select
     */
    private ArrayList<Location> locations;
    /**
     * Firebase user authentication
     */
    private FirebaseAuth mAuth;
    /**
     * Buffering animation for finding a match
     */
    public static ProgressDialog mQueueProgress;
    /**
     * Deal object
     */
    private Deal deal;

    /**
     * Called when activity is first launched
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_deal_page);

        //System.out.println("NEW Dialog created");
        dealsDetail = findViewById(R.id.dealsDetails);
        dealsImage = findViewById(R.id.dealsImage);
        termsCondition = findViewById(R.id.termsConditions);
        startEnd = findViewById(R.id.startend);
        mAuth = FirebaseAuth.getInstance();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BackEndController.URL)
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

    /**
     * Show an alert box for user to choose their lcoation
     */
    //used for locationAlertBox
    private void locationAlertBox() {
        //instantiate alertdialog builder
        AlertDialog.Builder myBuilder = new AlertDialog.Builder(this);
        //data source
        final ArrayList<String> selectedLocations = new ArrayList();
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
                else if (!isChecked) {
                    for(int k=0; k<selectedLocations.size(); k++){
                        if(selectedLocations.get(k).compareTo(locationNames[position]) == 0){
                            selectedLocations.remove(k);
                            selectedKey.remove(k);
                        }
                    }
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

                    //Add Dialog to halt user
                    mQueueProgress = new ProgressDialog(SelectedDealPage.this);
                    mQueueProgress.setTitle("Finding you a match!");
                    mQueueProgress.setMessage("This may take a while");
                    mQueueProgress.setCanceledOnTouchOutside(false);

                    //Dismiss listener
                    mQueueProgress.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            //if dismissed when match is not found, remove request from server
                            if(MyFirebaseMessagingService.matched == 0){
                                //JOZUA ADD YOUR CODE HERE TO REMOVE THIS PERSON'S REQUEST
                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(BackEndController.URL)
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();
                                BackEndController backEndController = retrofit.create(BackEndController.class);
                                Call<Void> call = backEndController.deleteRequest(FirebaseAuth.getInstance().getCurrentUser().getUid(), deal.getId());
                                call.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        if(!response.isSuccessful()){
                                            Toast toast = Toast.makeText(SelectedDealPage.this, "An error occurred. Please try againz", Toast.LENGTH_SHORT);
                                            toast.show();
                                            return;
                                        }
                                        //if successfully remove request
                                        Toast toast = Toast.makeText(SelectedDealPage.this, "Your request has been removed from wait list.", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Toast toast = Toast.makeText(SelectedDealPage.this, "An error occurred. Please try again", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                });
                            }
                            //if dismissed when match is found, change variable back to 0
                            if(MyFirebaseMessagingService.matched == 1){
                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SelectedDealPage.this);
                                String username = prefs.getString("username_matched","");
                                String image = prefs.getString("image_matched","");
                                String thumb_image = prefs.getString("thumb_image_matched","");
                                String dealName = prefs.getString("dealName_matched","");
                                String location= prefs.getString("location_matched","");
                                String uid = prefs.getString("uid matched", "");
                                showPopUp(username, image, thumb_image, dealName, location, uid);


                                MyFirebaseMessagingService.matched = 0;
                            }
                        }
                    });

                    //Button in dialog to cancel matching
                    mQueueProgress.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mQueueProgress.dismiss();//dismiss dialog
                        }
                    });
                    mQueueProgress.show();

                    //add Request to server on click
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BackEndController.URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    BackEndController backEndController = retrofit.create(BackEndController.class);
                    final Call<Void> call = backEndController.addRequest(FirebaseAuth.getInstance().getCurrentUser().getUid(), deal.getId(), c);
                    System.out.println(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    Handler handler = new Handler();
                    int counter = (int) getRandomIntegerBetweenRange(0,3000);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if(!response.isSuccessful()){
                                        mQueueProgress.dismiss();
                                        Toast toast = Toast.makeText(SelectedDealPage.this, "An error occurred. Please try againz", Toast.LENGTH_SHORT);
                                        toast.show();
                                        return;
                                    }
                                    Toast toast = Toast.makeText(SelectedDealPage.this, "Successfully added to wait list", Toast.LENGTH_SHORT);

                                }
                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    mQueueProgress.dismiss();
                                    Toast toast = Toast.makeText(SelectedDealPage.this, "An error occurred. Please try again", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            });

                        }
                    }, counter);







                }
            }
        });

        //create Dialog
        alertDialog = myBuilder.create();
        //show dialog
        alertDialog.show();
    }

    /**
     * Show a successfully matched pop-up
     * @param name Name of matched user
     * @param image Image of matched user
     * @param thumbImage ThumbImage of matched user
     * @param dealName Name of deal
     * @param location Name of location
     * @param uid Uid of matched user
     */
    public void showPopUp(final String name, final String image, String thumbImage, String dealName, String location, final String uid) {
        System.out.println("INSIDE SHOW POP UP");
        TextView matchName, matchDeal, matchLocation;
        ImageView matchProfileImage;
        Button chatBtn;

        myDialog = new Dialog(SelectedDealPage.this);
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.setContentView(R.layout.matchpopup);

        matchName = (TextView) myDialog.findViewById(R.id.matchname);
        //noMatches = (TextView) myDialog.findViewById(R.id.numberofmatches);
        //noMatches.setText(numberOfMatches);
        matchProfileImage = (ImageView) myDialog.findViewById(R.id.popupimage);
        matchDeal = (TextView) myDialog.findViewById(R.id.deal_name);
        matchLocation = (TextView) myDialog.findViewById(R.id.location);
        chatBtn = (Button) myDialog.findViewById(R.id.chatBtn);
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //start newActivity with title for actionbar and text for textview
                Intent intent = new Intent(SelectedDealPage.this, ChatActivity.class);
               // add in matched user id
                intent.putExtra("user_name", name);
                System.out.println("MATCHED USER ISS: " + name);
                intent.putExtra("user_image", image);
                intent.putExtra("user_id", uid);
                System.out.println("MATCHED USER UID: " + uid);

                //set chat to true
                DatabaseReference mChatDB = FirebaseDatabase.getInstance().getReference().child("Chat").child(mAuth.getCurrentUser().getUid()).child(uid).child("chat");
                mChatDB.setValue(true);
                DatabaseReference mChatDB2 = FirebaseDatabase.getInstance().getReference().child("Chat").child(uid).child(mAuth.getCurrentUser().getUid()).child("chat");
                mChatDB2.setValue(true);

                if((Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)){
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                SelectedDealPage.this.startActivity(intent);
                finish();
            }
        });

        matchName.setText(name);
        Picasso.get().load(image).into(matchProfileImage);
        matchDeal.setText(dealName);
        matchLocation.setText(location);

        myDialog.show();
    }

    /**
     * Generate random double from min to max
     * @param min Lowest value to start from
     * @param max Highest value to end with
     * @return Double type is returned
     */
    public static double getRandomIntegerBetweenRange(double min, double max){
        double x = (int)(Math.random()*((max-min)+1))+min;
        return x;
    }



}