package com.iff.onepairv2;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d("myfirebaseid", "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("TAG", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("TAG", "Message data payload: " + remoteMessage.getData());
            try {
                JSONObject obj = new JSONObject(remoteMessage.getData());
                JSONObject obj2 = new JSONObject(obj.getString("body"));
                JSONObject user1 = new JSONObject(obj2.getString("user1"));
                JSONObject user2 = new JSONObject(obj2.getString("user2"));
                JSONObject deal = new JSONObject(obj2.getString("deal"));
                final String location = obj2.getString("locations");
                String uid1 = user1.getString("uid");
                String uid2 = user2.getString("uid");
                final String dealName = deal.getString("name");

                DatabaseReference mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
                if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(uid1)){
                    mUserDatabase = mUserDatabase.child(uid2);
                }else{
                    mUserDatabase = mUserDatabase.child(uid1);
                }

                mUserDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String message = "";
                        String username = dataSnapshot.child("name").getValue().toString();
                        String image = dataSnapshot.child("image").getValue().toString();
                        String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();
                        message += "Found a match with ";
                        message += username;
                        message += " on ";
                        message += dealName;
                        message += " at ";
                        message += location;
                        

                        // Replace with popup soon
                        Toast toast = Toast.makeText(MyFirebaseMessagingService.this, message, Toast.LENGTH_LONG);
                        toast.show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }catch (Exception err){
                Log.d("Error", err.toString());
            }


            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                //scheduleJob();
            } else {
                // Handle message within 10 seconds
                //s handleNow();

            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            Log.d("TAG", "Message Notification Body: " + remoteMessage.getNotification().getBody());
            NotificationHelper.displayNotification(getApplicationContext(), title, body);
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }


}
