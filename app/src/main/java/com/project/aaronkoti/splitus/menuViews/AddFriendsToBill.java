package com.project.aaronkoti.splitus.menuViews;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.aaronkoti.splitus.R;
import com.project.aaronkoti.splitus.adapters.AddFriendDialogAdapter;
import com.project.aaronkoti.splitus.beans.Friend;
import com.project.aaronkoti.splitus.beans.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 10/24/2016.
 */

public class AddFriendsToBill extends DialogFragment {

    public List<User> listOfUsers;
    public User user;
    public AddFriendDialogAdapter adapter;
    public RecyclerView dialogtAddFriendsRecyclerV;

    public AddFriendsToBill(){

    }

    public static AddFriendsToBill newInstance(User userInfo) {
        AddFriendsToBill fragment = new AddFriendsToBill();
        Bundle args = new Bundle();
        args.putSerializable("UserInfo", userInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        this.user = (User) args.getSerializable("UserInfo");
        adapter = new AddFriendDialogAdapter(getContext(), listOfUsers);
        loadFriends();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Title
        builder.setTitle("Add Friends to the bill");

        //Body
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_add_users_bill, null);

        dialogtAddFriendsRecyclerV = ((RecyclerView) v.findViewById(R.id.dialogtAddFriendsRecyclerV));
        dialogtAddFriendsRecyclerV.setAdapter(adapter);
        dialogtAddFriendsRecyclerV.setLayoutManager( new LinearLayoutManager(getContext()));

        builder.setView(v);


        //Buttons
        builder.setPositiveButton("All Set", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });


        return builder.create();
    }

  /*  @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_friends, container, false);


        return view;
    }*/

    public void loadFriends(){
        listOfUsers = new ArrayList<>();

        //MakeConnection to DB
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference root =  db.getReference("SplitUs");
        DatabaseReference friendReq = root.child("Friends").child(user.getUid());

        friendReq.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child: dataSnapshot.getChildren()){
                    Friend friend = child.getValue(Friend.class);
                    DatabaseReference friendRef = root.child("Users").child(friend.getFriendId());
                    friendRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User auxUs = dataSnapshot.getValue(User.class);
                            //Log.d("DEBUG", auxUs.toString());
                            listOfUsers.add( dataSnapshot.getValue(User.class));
                            adapter.setNewList(listOfUsers);
                            //adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
