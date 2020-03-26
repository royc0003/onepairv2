package com.iff.onepairv2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

/**
 * Activity to show current user's account information
 * @author ifandonlyif
 */
public class ProfileActivity extends AppCompatActivity {

    /** Firebase authentication object */
    private FirebaseAuth mAuth;
    /** Toolbar at the top of the activity */
    private Toolbar mToolbar;
    /** Database reference */
    private DatabaseReference mUserDatabase;
    /** Firebase database reference of the current user */
    private FirebaseUser mCurrentUser;

    /** Used to display the profile picture of the current user */
    private ImageView mProfilePic;
    /** Used to display the name of the current user */
    private TextView mDisplayName;
    /** Button to allow user to change the user's name */
    private Button profileChangeNameBtn;
    /** Button to allow user to change the usser's profile picture */
    private Button profileChangePicBtn;

    /** Progress dialog to show that the profile picture is currently being changed */
    private ProgressDialog mProfileImageDialog;
    /** integer for gallery pick */
    private static final int GALLERY_PICK = 1;
    /** StorageReference object */
    private StorageReference mImageStorage;

    /**
     * Called when activity is first launched
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mImageStorage = FirebaseStorage.getInstance().getReference();

        mProfilePic = (ImageView) findViewById(R.id.profilepic);
        mDisplayName = (TextView) findViewById(R.id.profilename);
        profileChangeNameBtn = (Button) findViewById(R.id.changename);
        profileChangePicBtn = (Button) findViewById(R.id.changeimage);

        mToolbar = (Toolbar) findViewById(R.id.profile_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Profile");

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                mDisplayName.setText(name);
                Picasso.get().load(image).into(mProfilePic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        profileChangeNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_value = mDisplayName.getText().toString();
                Intent change_name_int = new Intent(ProfileActivity.this, ChangeNameActivity.class);
                change_name_int.putExtra("OldName", name_value);
                startActivity(change_name_int);
                finish();

            }
        });

        profileChangePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), 1);

            }
        });

    }

    /** Used for uploading of profile picture and changing of profile picture */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK){
            Uri imageUri = data.getData();

            // start cropping activity for pre-acquired image saved on the device
            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mProfileImageDialog = new ProgressDialog(ProfileActivity.this);
                mProfileImageDialog.setTitle("Uploading Image...");
                mProfileImageDialog.setMessage("This may take a while");
                mProfileImageDialog.setCanceledOnTouchOutside(false);
                mProfileImageDialog.show();
                Uri resultUri = result.getUri();
                final String current_user_id = mCurrentUser.getUid();
                final StorageReference filepath = mImageStorage.child("profile_images").child(current_user_id + ".jpg");
                filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Uri downloadUrl = uri;
                                mUserDatabase.child("image").setValue(downloadUrl.toString());
                                mProfileImageDialog.dismiss();
                                Toast.makeText(ProfileActivity.this, "Profile Image Updated", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        }
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
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.main_logout_btn){
            FirebaseAuth.getInstance().signOut();
            sendToStart();
        }
        else if(item.getItemId() == R.id.main_profile_btn){
            Toast.makeText(ProfileActivity.this, "You are already in your Profile", Toast.LENGTH_LONG).show();
        }

        else if(item.getItemId() == R.id.main_homepage){
            Intent startIntent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(startIntent);
        }
        else if(item.getItemId() == R.id.main_chat){
            Intent startIntent = new Intent(ProfileActivity.this, MatchedPersonsActivity.class);
            startActivity(startIntent);
        }/*
        else if(item.getItemId() == R.id.main_all_users){
            Intent startIntent = new Intent(ProfileActivity.this, AllUsers.class);
            startActivity(startIntent);
        }*/
        //return super.onOptionsItemSelected(item);
        return false;
    }

    /**
     *  Sends user to start activity if user is not logged in
     */
    private void sendToStart() {
        Intent startIntent = new Intent(ProfileActivity.this, StartActivity.class);
        startActivity(startIntent);
        finish();
    }

    /**
     * Called when activity is subsequently launched
     */
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){

            sendToStart();

        }
    }
}
