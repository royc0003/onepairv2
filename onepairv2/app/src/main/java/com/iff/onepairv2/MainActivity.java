package com.iff.onepairv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.datatransport.runtime.backends.BackendFactory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private CardView foodCard, entertainmentCard, retailCard, othersCard;
    private ViewFlipper vFlipper;
    private ArrayList<String> imgArray = new ArrayList<String>(); //arrayList to be used for url
    private ArrayList<Deal> dealList = new ArrayList<Deal>(); // added arrayList of deals

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getIntent().hasExtra("messageFrom")) {
            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
            intent.putExtra("user_id", getIntent().getStringExtra("messageFrom"));
            intent.putExtra("user_name", getIntent().getStringExtra("name"));
            intent.putExtra("user_image", getIntent().getStringExtra("image"));
            System.out.println("OWN IMAGE ISSS" + getIntent().getStringExtra("image"));
            startActivity(intent);
        }

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("1Pair");

        //add click listener to cards

        foodCard = (CardView) findViewById(R.id.food);
        entertainmentCard = (CardView) findViewById(R.id.entertainment);
        retailCard = (CardView) findViewById(R.id.retail);
        othersCard = (CardView) findViewById(R.id.others);
        //Include view flipper here
        vFlipper = (ViewFlipper)findViewById(R.id.v_flipper);
        //obtain the url for foodDealsPage/ RetailDealsPage/OtherDealPage/Entertaininment Deal page


        //add click listener to cards
        foodCard.setOnClickListener(this);
        entertainmentCard.setOnClickListener(this);
        retailCard.setOnClickListener(this);
        othersCard.setOnClickListener(this);

        //call this anywhere to get token id of current device
        //id can be used to send notifications to the device
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("123", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        saveToken(token);

                        // Log and toast
                        //String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("token ID", token);
                        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });


        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(BackEndController.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BackEndController backEndController2 =retrofit2.create(BackEndController.class);
        Call<ArrayList<Deal>> call = backEndController2.getAllDeals();
        call.enqueue(new Callback<ArrayList<Deal>>() {
            @Override
            public void onResponse(Call<ArrayList<Deal>> call, Response<ArrayList<Deal>> response) {
                if(!response.isSuccessful()){
                    System.out.print("Unable to retrieve data properly");
                    return;
                }
                ArrayList<Deal> deals = response.body();
                for(Deal deal: deals){
                    dealList.add(deal);
                }
                System.out.println("the total amount of deals in MainActivity" + dealList.size());
                imgArray = useFlipperImages(dealList); //
                if(imgArray.isEmpty()){
                    System.out.println("imgArray size is currently empty");
                }
                else{
                    System.out.println("Imagearray size is currently NOT empty: " + imgArray.size());
                    System.out.println("hello: "+ imgArray.get(3));
                    for(String imgURL: imgArray){
                        flipperImages(imgURL);
                        System.out.println("current url"+imgURL);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Deal>> call, Throwable t) {
                System.out.println("***Unable to access retrofit properly... *** ");
            }
        });
    }

    public ArrayList<String> useFlipperImages(ArrayList<Deal> deal){
        ArrayList<Deal> tempDeal = deal;
        ArrayList<String> tempString = new ArrayList<>();
        if(!tempDeal.isEmpty()){
            for(Deal deals: tempDeal){
                tempString.add(deals.getImage());
            }
        }
        System.out.println(tempString.get(0));
        return tempString;
    }

    public void flipperImages(String imageURL){
        ImageView imageView = new ImageView(this);
        Picasso.get().load(imageURL).into(imageView);

        vFlipper.addView(imageView);

        vFlipper.setFlipInterval(4000);//4sec
        vFlipper.setAutoStart(true);

        //animation
        vFlipper.setInAnimation(this, android.R.anim.slide_in_left);
        vFlipper.setOutAnimation(this, android.R.anim.slide_out_right);
        vFlipper.startFlipping();

    }

    private void saveToken(String token) {
        String email;
        String uid;
        if(mAuth.getCurrentUser() != null){
            email = mAuth.getCurrentUser().getEmail();
            uid = mAuth.getCurrentUser().getUid();

            final UserToken userToken = new UserToken(email, token);

            // Update django db too
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BackEndController.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            BackEndController backEndController = retrofit.create(BackEndController.class);
            Call<Void> call = backEndController.updateToken(uid, token);
            //System.out.print(token);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(!response.isSuccessful()){
                        System.out.println("Oops something went wrong!");
                        return;
                    }
                    DatabaseReference dbUsers = FirebaseDatabase.getInstance().getReference("UserToken");
                    dbUsers.child(mAuth.getCurrentUser().getUid()).setValue(userToken).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Token saved", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    System.out.println("Oops something went wrong!");
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){

            sendToStart();

        }
    }

    private void sendToStart() {
        Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(startIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(item.getItemId() == R.id.main_logout_btn){
            FirebaseAuth.getInstance().signOut();
            sendToStart();
        }
        else if(item.getItemId() == R.id.main_profile_btn){
            Intent startIntent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(startIntent);
        }
        else if(item.getItemId() == R.id.main_homepage){
            Toast.makeText(MainActivity.this, "You're already in the Homepage", Toast.LENGTH_SHORT).show();
        }
        else if(item.getItemId() == R.id.main_chat){
            Intent startIntent = new Intent(MainActivity.this, MatchedPersonsActivity.class);
            startActivity(startIntent);
        }/*
        else if(item.getItemId() == R.id.main_all_users){
            Intent startIntent = new Intent(MainActivity.this, AllUsers.class);
            startActivity(startIntent);
        }*/
        //return super.onOptionsItemSelected(item);
        return false;
    }

    public void onClick(View v) {
        Intent i;

        switch (v.getId()) {
            case R.id.food:
                i = new Intent(this, FoodDealsPage.class);
                startActivity(i);
                break;
            case R.id.entertainment:
                i = new Intent(this, EntertainmentDealsPage.class);
                startActivity(i);
                break;
            case R.id.retail:
                i = new Intent(this, RetailDealsPage.class);
                startActivity(i);
                break;
            case R.id.others:
                i = new Intent(this, OthersDealsPage.class);
                startActivity(i);
                break;
        }
    }

}