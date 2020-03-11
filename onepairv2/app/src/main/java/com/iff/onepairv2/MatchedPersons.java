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


public class MatchedPersons extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static MatchedPersons single_instance = null;
    DatabaseReference reff, reff2;

    private Toolbar mToolbar;
    ListView listView;
    ListViewAdapter2 adapter;

    ArrayList<String> matchUIDs = new ArrayList<String>();
    ArrayList<Users> allUsers = new ArrayList<Users>();
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> uid = new ArrayList<String>();
    ArrayList<String> image = new ArrayList<String>();
    ArrayList<ChatUser> arrayList = new ArrayList<ChatUser>();

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
        reff2 = FirebaseDatabase.getInstance().getReference("Chat").child(currentUID);
        reff2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot chatSnapshot: dataSnapshot.getChildren()) {
                    Boolean check = (Boolean) chatSnapshot.child("chat").getValue();
                    if(check){
                        matchUIDs.add(chatSnapshot.getKey());
                    }
                }

                //retrieve all data from database
                reff = FirebaseDatabase.getInstance().getReference().child("Users");
                reff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        allUsers.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            //append to allUID array list
                            Users users = ds.getValue(Users.class);
                            allUsers.add(users);
                        }

                        for (int i = matchUIDs.size() - 1; i > 0; i--) {
                            for (int j = i - 1; j >= 0; j--) {
                                if (matchUIDs.get(i).equals(matchUIDs.get(j))) {
                                    matchUIDs.remove(i);
                                    break;
                                }
                            }
                        }

                        for (String muid : matchUIDs) {
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
                            arrayList.add(model);
                        }

                        //pass results to listViewAdapter class
                        adapter = new ListViewAdapter2(MatchedPersons.this, arrayList);

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

    public static MatchedPersons getInstance() {
        if (single_instance == null) {
            single_instance = new MatchedPersons();
        }
        System.out.println("GOT INSTANCE");
        return single_instance;
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
            Intent startIntent = new Intent(MatchedPersons.this, ProfileActivity.class);
            startActivity(startIntent);
        }
        else if(item.getItemId() == R.id.main_homepage){
            Intent startIntent = new Intent(MatchedPersons.this, MainActivity.class);
            startActivity(startIntent);
        }
        else if(item.getItemId() == R.id.main_chat){
            Toast.makeText(MatchedPersons.this, "You're already in the Matches Page", Toast.LENGTH_SHORT).show();
        }
        else if(item.getItemId() == R.id.main_all_users){
            Intent startIntent = new Intent(MatchedPersons.this, AllUsers.class);
            startActivity(startIntent);
        }
        //return super.onOptionsItemSelected(item);
        return false;
    }

    private void sendToStart() {
        Intent startIntent = new Intent(MatchedPersons.this, StartActivity.class);
        startActivity(startIntent);
        finish();
    }

}



