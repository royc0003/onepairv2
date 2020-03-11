package com.iff.onepairv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class AllUsers extends AppCompatActivity {

    private Toolbar mToolbar;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<Users, UsersViewHolder> adapter;
    private FirebaseRecyclerOptions<Users> options;
    private RecyclerView mUsersList;
    private ProgressDialog Loading;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        mToolbar = (Toolbar) findViewById(R.id.all_users_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Users");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mUsersList = (RecyclerView) findViewById(R.id.users_list);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(this));
        Loading = new ProgressDialog(this);
        Loading.setTitle("Loading");
        Loading.setMessage("This may take a while");
        Loading.setCanceledOnTouchOutside(false);
        Loading.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        options = new FirebaseRecyclerOptions.Builder<Users>().setQuery(mDatabase, Users.class).build();
        adapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull final Users model) {

                holder.displayName.setText(""+model.getName());
                Picasso.get().load(model.getImage()).into(holder.profilePic);

                Loading.dismiss();

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(AllUsers.this, ""+model.getName(), Toast.LENGTH_SHORT).show();
                        CharSequence initiate_options[] = new CharSequence[]{"Send Message"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(AllUsers.this);
                        builder.setItems(initiate_options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which == 0){
                                    DatabaseReference mChatDB = FirebaseDatabase.getInstance().getReference().child("Chat").child(mAuth.getCurrentUser().getUid()).child(model.getUid()).child("chat");
                                    mChatDB.setValue(true);
                                    DatabaseReference mChatDB2 = FirebaseDatabase.getInstance().getReference().child("Chat").child(model.getUid()).child(mAuth.getCurrentUser().getUid()).child("chat");
                                    mChatDB2.setValue(true);
                                    Intent chatIntent = new Intent(AllUsers.this, ChatActivity.class);
                                    chatIntent.putExtra("user_id", model.getUid());
                                    chatIntent.putExtra("user_name", model.getName());
                                    chatIntent.putExtra("user_image", model.getImage());
                                    startActivity(chatIntent);
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_single_layout, parent, false);
                return new UsersViewHolder(v);
            }
        };

        adapter.startListening();
        mUsersList.setAdapter(adapter);

    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder{

        View mView;
        TextView displayName;
        ImageView profilePic;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            displayName = itemView.findViewById(R.id.user_single_name);
            profilePic = itemView.findViewById(R.id.user_single_image);
        }
    }

}
