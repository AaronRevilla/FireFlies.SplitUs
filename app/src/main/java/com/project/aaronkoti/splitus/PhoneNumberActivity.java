package com.project.aaronkoti.splitus;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.project.aaronkoti.splitus.beans.User;

import static android.Manifest.permission.READ_PHONE_STATE;

public class PhoneNumberActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getName();
    private EditText userName;
    private EditText phoneNumber;
    private ImageView userImg;
    private User user;
    private User userDB;
    private boolean hasName = false;
    private boolean hasPhone = false;
    private final int PERMISION_PHONE_STATE_GRANTED = 10;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);

        user = (User) getIntent().getSerializableExtra("User");

        //Verify if the user has already in DB
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference root =  db.getReference("SplitUs");
        DatabaseReference userRef = root.child("Users").child(user.getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userDB = dataSnapshot.getValue(User.class);
                ifUserAlreadyExist(user, userDB);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        userName = ((EditText) findViewById(R.id.userName));
        phoneNumber = ((EditText) findViewById(R.id.userPhone));
        userImg = ((ImageView) findViewById(R.id.userImage));
        userName.setEnabled(true);
        phoneNumber.setEnabled(true);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void ifUserAlreadyExist(User userFromAuth, User userFromDB){

        User auxUser = null;

        if(userFromDB != null){
            auxUser = userFromDB;
        }
        else{
            auxUser = userFromAuth;
        }

        if(auxUser.getImageUrl() != null){
            Glide
                    .with(this)
                    .load(auxUser.getImageUrl())
                    .centerCrop()
                    //.placeholder(R.drawable.loading_spinner)
                    .crossFade()
                    .into(userImg);
        }
        if(auxUser.getName() != null){
            userName.setText(auxUser.getName());
            userName.setEnabled(false);
            hasName = true;
        }
        if(auxUser.getPhoneNumber() != null){
            phoneNumber.setText(auxUser.getPhoneNumber());
            phoneNumber.setEnabled(false);
            hasPhone = true;
        }
        //get phone from user
        if(hasPhonePermission()){
            if(checkSelfPermission(READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED){
                TelephonyManager tMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                String mPhoneNumber = tMgr.getLine1Number();
                if(mPhoneNumber != null){
                    phoneNumber.setText(mPhoneNumber);
                    phoneNumber.setEnabled(false);
                    hasPhone = true;
                }
            }
        }
        else{
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_PHONE_STATE)) {

//                new AlertDialog.Builder(getApplicationContext())
//                        .setTitle("REQUEST FOR PERMISSION")
//                        .setMessage("Hi, we need this permission to get your phone number automatically, also if you dont want you can do it manually")
//                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                ActivityCompat.requestPermissions(PhoneNumberActivity.this , new String[]{android.Manifest.permission.READ_PHONE_STATE}, PERMISION_PHONE_STATE_GRANTED);
//                            }
//                        })
//                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // do nothing
//                            }
//                        })
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .show();
            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this , new String[]{android.Manifest.permission.READ_PHONE_STATE}, PERMISION_PHONE_STATE_GRANTED);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        verifyUser();//verify if user has all the fields to change activity automaticaly
    }

    public void verifyUser(){
        if(hasPhone && hasName){
            changeActivity();
        }
    }

    public boolean hasPhonePermission(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        else{
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISION_PHONE_STATE_GRANTED: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    TelephonyManager tMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    String mPhoneNumber = tMgr.getLine1Number();
                    if(mPhoneNumber != null){
                        phoneNumber.setText(mPhoneNumber);
                        phoneNumber.setEnabled(false);
                        hasPhone = true;
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        finish();
    }

    public void enterApp(View view) {
        changeActivity();
    }

    public void changeActivity(){
        if(user != null){
            user.setName(userName.getText().toString());
            user.setPhoneNumber(phoneNumber.getText().toString());
            user.setNotificationToken(FirebaseInstanceId.getInstance().getToken());

            //save user into the database
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference root =  db.getReference("SplitUs");
            DatabaseReference users = root.child("Users");

            //Log.d(TAG, "key: " + user.toString());
            DatabaseReference userDB = users.child(user.getUid());
            userDB.setValue(user);

            //Log.d(TAG, "DBkey: " + userDB);

            //Log.d(TAG, user.getUid());

            Intent splitUsMenu = new Intent(PhoneNumberActivity.this, SplitUsMenu.class);
            splitUsMenu.putExtra("userKey", user.getUid());
            splitUsMenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(splitUsMenu);
            finish();
        }
        else{
            Toast.makeText(this, "The user doesnt exist", Toast.LENGTH_SHORT).show();
        }
    }
}
