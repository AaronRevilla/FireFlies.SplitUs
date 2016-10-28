package com.project.aaronkoti.splitus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.aaronkoti.splitus.beans.User;
import com.project.aaronkoti.splitus.menuViews.AddFriends;
import com.project.aaronkoti.splitus.menuViews.Events;
import com.project.aaronkoti.splitus.menuViews.Friends;
import com.project.aaronkoti.splitus.menuViews.UserInfo;

public class SplitUsMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private User user;
    private final String TAG = this.getClass().getName();
    public ImageView navUsrImg;
    public TextView navUsrName;
    public TextView navUsrMail;
    public TextView netStatus;
    public LinearLayout fragmentWrapper;
    public FragmentManager fm ;
    public FragmentTransaction ft;
    public BroadcastReceiver deviceConnectedToWifi;
    public IntentFilter intentFilterWifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split_us_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navUsrImg = ((ImageView) navigationView.getHeaderView(0).findViewById(R.id.nav_userimage));
        navUsrName = ((TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_user_name));
        navUsrMail = ((TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_user_mail));
        netStatus = ((TextView) navigationView.getHeaderView(0).findViewById(R.id.net_status));
        if(isConnectedToInternet()){
            netStatus.setText("online");
        }
        fragmentWrapper = ((LinearLayout) findViewById(R.id.fragmentWrapper));


        //get User from DB
        String usrKey = getIntent().getStringExtra("userKey");
        Log.d(TAG, usrKey);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference root =  db.getReference("SplitUs");
        DatabaseReference userRef = root.child("Users").child(usrKey);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if(user.getImageUrl() != null){
                    Glide
                            .with(SplitUsMenu.this)
                            .load(user.getImageUrl())
                            .centerCrop()
                            .override(140, 150)
                            //.placeholder(R.drawable.loading_spinner)
                            .crossFade()
                            .into(navUsrImg);
                }
                if(user.getName() != null){
                    navUsrName.setText(user.getName());
                }
                if(user.getEmail() != null){
                    navUsrMail.setText(user.getEmail());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "loadPost:onCancelled", databaseError.toException());
                Toast.makeText(SplitUsMenu.this, "loadPost:onCancelled " + databaseError.getDetails(), Toast.LENGTH_LONG);
            }
        });

        //fragments
        fm = getSupportFragmentManager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(deviceConnectedToWifi == null){
            deviceConnectedToWifi = new BroadcastReceiver() {
                @Override
                    public void onReceive(Context context, Intent intent) {
                    Log.d("DEBUG", intent.getAction().toString());

                    if(intent.getAction().equals( "android.net.wifi.STATE_CHANGE")){
                        //Toast.makeText(getApplicationContext(), "CONNECTED TO WI-FI", Toast.LENGTH_SHORT).show();
                        netStatus.setText("online");
                    }
                    else {
                        //Toast.makeText(getApplicationContext(), "DISCONNECTED FROM WI-FI", Toast.LENGTH_SHORT).show();
                        netStatus.setText("offline");
                    }
                }
            };
        }
        if(intentFilterWifi == null){
            intentFilterWifi = new IntentFilter();
            intentFilterWifi.addAction("android.net.wifi.WIFI_STATE_CHANGED");
            intentFilterWifi.addAction("android.net.wifi.STATE_CHANGE");
        }

        registerReceiver(deviceConnectedToWifi, intentFilterWifi);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(deviceConnectedToWifi != null){
            unregisterReceiver(deviceConnectedToWifi);
        }
    }

    public boolean isConnectedToInternet(){
        ConnectivityManager cm  = ((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE));
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.split_us_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Bundle bundle = new Bundle();
        ft = fm.beginTransaction();

        if (id == R.id.nav_events) {
            // Handle the event for creating events
            Log.d(TAG, "nav_event");
            bundle.putSerializable("UserInfo", user );
            Events eventsFrag = new Events();
            eventsFrag.setArguments(bundle);
            ft.replace(fragmentWrapper.getId(), eventsFrag);
            ft.commit();

        } else if (id == R.id.nav_add_friends) {
            //add friends
            bundle.putSerializable("UserInfo", user );
            AddFriends fragFriends = new AddFriends();
            fragFriends.setArguments(bundle);
            ft.replace(fragmentWrapper.getId(), fragFriends);
            ft.commit();

        } else if (id == R.id.nav_user_info) {
            //user info
            bundle.putSerializable("UserInfo", user );
            UserInfo fragUserInfo = new UserInfo();
            fragUserInfo.setArguments(bundle);
            ft.replace(fragmentWrapper.getId(), fragUserInfo);
           /* if(ft.isEmpty()){
                ft.add(fragmentWrapper.getId(), fragUserInfo);
            }
            else{
                ft.addToBackStack(null);
                ft.replace(fragmentWrapper.getId(), fragUserInfo);
            }*/
            ft.commit();

        } else if (id == R.id.nav_share) {
            //share
        } else if (id == R.id.nav_log_out) {
            FirebaseAuth.getInstance().signOut();
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
