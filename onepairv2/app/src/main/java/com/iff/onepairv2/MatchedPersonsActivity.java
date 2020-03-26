package com.iff.onepairv2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Activity to show list of all past matches and allow users to chat with past matches when clicked
 * @author ifandonlyif
 */
public class MatchedPersonsActivity extends AppCompatActivity {

    /** Firebase authentication object */
    private FirebaseAuth mAuth;
    /** Database reference */
    private DatabaseReference mDatabaseReference, mDatabaseReference2;

    /** Toolbar at the top of the activity */
    private Toolbar mToolbar;
    /** Used to display past matches in a list format */
    private ListView listView;
    /** Adapter used to display matches */
    private MatchesListViewAdapter adapter;

    /** Used to store UIDS of all past matches */
    private ArrayList<String> matchUids = new ArrayList<String>();
    /** Used to store all past matches Users objects */
    private ArrayList<Users> allUsers = new ArrayList<Users>();
    /** Used to store names of all past matches */
    private ArrayList<String> name = new ArrayList<String>();
    /** Used to store uid of all past matches */
    private ArrayList<String> uid = new ArrayList<String>();
    /** Used to store image url of all past matches */
    private ArrayList<String> image = new ArrayList<String>();
    /** Used to store past matches ChatUser objects */
    private ArrayList<ChatUser> chatList = new ArrayList<ChatUser>();

    /**
     * Called when the activity is first launched
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deals_main);

        mToolbar = (Toolbar) findViewById(R.id.deal_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Matches");

        listView = findViewById(R.id.listView);

        mAuth = FirebaseAuth.getInstance();
        String currentUID = mAuth.getCurrentUser().getUid();
        System.out.println("GET CURRENT USER UID " + currentUID);

        //retrieve data from matched users for each push key
        mDatabaseReference2 = FirebaseDatabase.getInstance().getReference("Chat").child(currentUID);
        mDatabaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot chatSnapshot: dataSnapshot.getChildren()) {
                    Boolean check = (Boolean) chatSnapshot.child("chat").getValue();
                    if(check){
                        matchUids.add(chatSnapshot.getKey());
                    }
                }

                //retrieve all data from database
                mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
                mDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        allUsers.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            //append to allUID array list
                            Users users = ds.getValue(Users.class);
                            allUsers.add(users);
                        }

                        for (int i = matchUids.size() - 1; i > 0; i--) {
                            for (int j = i - 1; j >= 0; j--) {
                                if (matchUids.get(i).equals(matchUids.get(j))) {
                                    matchUids.remove(i);
                                    break;
                                }
                            }
                        }

                        for (String muid : matchUids) {
                            for (Users u : allUsers) {
                                if (muid.equals(u.getUid())) {
                                    name.add(u.getName());
                                    uid.add(u.getUid());
                                    image.add(u.getImage());
                                    break;
                                }
                            }
                        }

                        for (int i = 0; i < name.size(); i++) {
                            ChatUser model = new ChatUser(name.get(i), uid.get(i), image.get(i));
                            //bind all strings in an array
                            chatList.add(model);
                        }

                        //pass results to listViewAdapter class
                        adapter = new MatchesListViewAdapter(MatchedPersonsActivity.this, chatList);

                        //bind the adapter to the listview
                        listView.setAdapter(adapter);
        }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }});

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    /**
     * Creates a drop down menu at the right side of the toolbar
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * For selection of items in the toolbar menu
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(item.getItemId() == R.id.main_logout_btn){
            FirebaseAuth.getInstance().signOut();
            sendToStart();
        }
        else if(item.getItemId() == R.id.main_profile_btn){
            Intent startIntent = new Intent(MatchedPersonsActivity.this, ProfileActivity.class);
            startActivity(startIntent);
        }
        else if(item.getItemId() == R.id.main_homepage){
            Intent startIntent = new Intent(MatchedPersonsActivity.this, MainActivity.class);
            startActivity(startIntent);
        }
        else if(item.getItemId() == R.id.main_chat){
            Toast.makeText(MatchedPersonsActivity.this, "You're already in the Matches Page", Toast.LENGTH_SHORT).show();
        }/*
        else if(item.getItemId() == R.id.main_all_users){
            Intent startIntent = new Intent(MatchedPersonsActivity.this, AllUsers.class);
            startActivity(startIntent);
        }*/
        //return super.onOptionsItemSelected(item);
        return false;
    }

    /**
     * Sends user to start activity if user is not logged in
     */
    private void sendToStart() {
        Intent startIntent = new Intent(MatchedPersonsActivity.this, StartActivity.class);
        startActivity(startIntent);
        finish();
    }

}



