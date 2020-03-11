package com.iff.onepairv2;

import android.os.Bundle;
import android.widget.ListView;

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
    private static int numberOfMatches = 0;
    DatabaseReference reff, reff2, reff3;

    static ArrayList<Users> matchedPersons = new ArrayList<Users>();
    private Toolbar mToolbar;
    ListView listView;
    ListViewAdapter2 adapter;

    ArrayList<String> matchUIDs = new ArrayList<String>();
    ArrayList<Users> allUsers = new ArrayList<Users>();
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> uid = new ArrayList<String>();
    ArrayList<String> image = new ArrayList<String>();
    ArrayList<ChatUser> arrayList = new ArrayList<ChatUser>();
    ArrayList<String> pushKeys = new ArrayList<String>();

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
                    System.out.println("TEET " + chatSnapshot.getKey());
                    matchUIDs.add(chatSnapshot.getKey());
                    //System.out.println("TEEET " + matchUIDs.get(0));
                }
                for(String m: matchUIDs)
                {
                    System.out.println("MATCHHH UID " + m);
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
                            System.out.println("USERNAME " + users.getName());
                        }

                        System.out.println("muid " + matchUIDs.get(0));
                        System.out.println("muid2 " + allUsers.get(0).getName());

                        for (int i = matchUIDs.size() - 1; i > 0; i--) {
                            for (int j = i - 1; j >= 0; j--) {
                                if (matchUIDs.get(i).equals(matchUIDs.get(j))) {
                                    matchUIDs.remove(i);
                                    break;
                                }
                            }
                        }

                        //System.out.println("SIZE " + matchUIDs.size());

                        for (String muid : matchUIDs) {
                            System.out.println("MUIDDD " + muid);

                            for (Users u : allUsers) {
                                System.out.println("USERRR: " + u.getUid());

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
                            System.out.println("MODEL ADDED");
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



}



