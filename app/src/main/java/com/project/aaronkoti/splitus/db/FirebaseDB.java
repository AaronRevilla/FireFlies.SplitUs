package com.project.aaronkoti.splitus.db;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.aaronkoti.splitus.beans.User;

/**
 * Created by User on 10/20/2016.
 */
public class FirebaseDB {

    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private String rootRef = "SplitUs" ;
    private String childRefUser = "Users";
    private String childRefImageBills = "ImageBills";
    private String childRefBills = "Bills";
    private String childRefEventUserBills = "Event_User_Bills";
    private String childRefEvent = "Event";

    public FirebaseDB(){}

    public DatabaseReference getRootReference(){
        return database.getReference(rootRef);
    }

    public DatabaseReference getUsers(){
        return database.getReference(rootRef).child(childRefUser);
    }

    public DatabaseReference getImageBills(){
        return database.getReference(rootRef).child(childRefImageBills);
    }

    public DatabaseReference getBills(){
        return database.getReference(rootRef).child(childRefBills);
    }

    public DatabaseReference getEventUserBills(){
        return database.getReference(rootRef).child(childRefEventUserBills);
    }

    public DatabaseReference getEvent(){
        return database.getReference(rootRef).child(childRefEvent);
    }

}
