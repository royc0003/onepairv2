package com.iff.onepairv2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SelectedDealPage extends AppCompatActivity {
    Dialog myDialog;
    AlertDialog alertDialog;
    private TextView dealsDetail;
    private ImageView dealsImage;
    private TextView termsCondition;
    private TextView startEnd;

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
        /*
        ActionBar actionBar = getSupportActionBar();
        TextView mDetailTv = findViewById(R.id.textView);*/

        //get data from previous activity when item of listview is clicked using intent
        Intent intent = getIntent();
        deal = (Deal) intent.getSerializableExtra("Deal");
        //System.out.println(deal.getId());

        //set actionbar title
        //actionBar.setTitle(deal.getName());
        //set text in textview

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
        final CharSequence[] locations = Location.LOCATIONS;
        final ArrayList selectedLocations = new ArrayList();
        //set properties using method chanining
        myBuilder.setTitle("Choose location(s)").setMultiChoiceItems(locations, null, new DialogInterface.OnMultiChoiceClickListener() {

            //selected locations are placed into an selectedLocations array list
            @Override
            public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                if(isChecked) {
                    //if the user checked the item, add it to the selected items
                    selectedLocations.add(locations[position]);
                }
                else if (selectedLocations.contains(position)) {
                    //else if the item is already in the array list, remove it
                    selectedLocations.remove(Integer.valueOf(position));
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
                for (Object nebular : selectedLocations) {
                    sb.append(nebular.toString() + "\n");
                }
                Toast.makeText(SelectedDealPage.this, sb.toString(), Toast.LENGTH_SHORT).show();
                System.out.println("LOcation button clicksed");
                ShowPopUp();

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