package com.iff.onepairv2;
//hellyeah
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EntertainmentDealsPage extends AppCompatActivity {

    ListView listView;
    ListViewAdapter adapter;
    String[] title;
    String[] description;
    int[] icon;
    ArrayList<Deal> arrayList = new ArrayList<Deal>();
    private Toolbar mToolbar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deals_main);

        listView = findViewById(R.id.listView);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mToolbar = (Toolbar) findViewById(R.id.deal_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Entertainment");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://128.199.167.80:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BackEndController backEndController = retrofit.create(BackEndController.class);
        Call<ArrayList<Deal>> call = backEndController.getEntertainmentDeals();
        call.enqueue(new Callback<ArrayList<Deal>>() {
            @Override
            public void onResponse(Call<ArrayList<Deal>> call, Response<ArrayList<Deal>> response) {
                if(!response.isSuccessful()){
                    System.out.println("Oops something went wrong!");
                    return;
                }
                ArrayList<Deal> deals = response.body();
                for(Deal deal: deals){
                    //deal.printDeal();
                    //Model model = new Model(deal.getName(), "", deal.getImage());
                    arrayList.add(deal);
                }
                adapter = new ListViewAdapter(getApplicationContext(), arrayList);
                listView.setAdapter(adapter);

                //if array list is empty
                if(arrayList.size() == 0)
                {
                    Toast toast = Toast.makeText(EntertainmentDealsPage.this, "Sorry there are no deals currently.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Deal>> call, Throwable t) {
                System.out.println("Oops something went wrong!");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dealspagemenu, menu);
        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(TextUtils.isEmpty(s)) {
                    adapter.filter("");
                    listView.clearTextFilter();
                }
                else {
                    adapter.filter(s);
                }
                return true;
            }
        });

        return true;
    }

    //for side bar menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(item.getItemId() == R.id.main_logout_btn){
            FirebaseAuth.getInstance().signOut();
            sendToStart();
        }
        else if(item.getItemId() == R.id.main_profile_btn){
            Intent startIntent = new Intent(EntertainmentDealsPage.this, ProfileActivity.class);
            startActivity(startIntent);
        }
        //return super.onOptionsItemSelected(item);
        return false;
    }
    private void sendToStart() {
        Intent startIntent = new Intent(EntertainmentDealsPage.this, StartActivity.class);
        startActivity(startIntent);
        finish();
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
}
