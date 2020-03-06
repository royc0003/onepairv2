package com.iff.onepairv2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
                    for (Object nebular : selectedLocations) {
                        sb.append(nebular.toString() + "\n");
                    }
                    Toast.makeText(SelectedDealPage.this, sb.toString(), Toast.LENGTH_SHORT).show();
                    System.out.println("LOcation button clicksed");
                    ShowPopUp();

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
        System.out.println("INSIDE SHOW POP UP");
        TextView txtclose;
        Button chatBtn;
        myDialog.setContentView(R.layout.matchpopup);
        txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
        chatBtn = (Button) myDialog.findViewById(R.id.chatBtn);
        System.out.println("Chat Button clicked");
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }




}