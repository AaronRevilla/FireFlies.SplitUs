package com.project.aaronkoti.splitus.menuViews;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import com.project.aaronkoti.splitus.adapters.AddFriendsAdapter;
import com.project.aaronkoti.splitus.beans.Friend;
import com.project.aaronkoti.splitus.beans.User;

import java.util.ArrayList;
import java.util.List;


public class AddFriends extends Fragment {

    public User user;
    private final String TAG = this.getClass().getName();
    List<User> users = new ArrayList<>();
    private RecyclerView usersList;
    public AddFriendsAdapter adapter;


    public AddFriends() {
        // Required empty public constructor
    }

    public static AddFriends newInstance(User userInfo) {
        AddFriends fragment = new AddFriends();
        Bundle args = new Bundle();
        args.putSerializable("UserInfo", userInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        this.user = (User) args.getSerializable("UserInfo");

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference root =  db.getReference("SplitUs");
        DatabaseReference userRef = root.child("Users");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users.clear();
                for(DataSnapshot child: dataSnapshot.getChildren()){
                    //Log.d("DEBUG", child.getKey() + " " + user.getUid());
                    if(!(child.getKey().equals(user.getUid()))){
                        users.add( child.getValue(User.class));
                    }
                }
                if(adapter != null){
                    adapter.setNewList(users);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "On Cancelled DB");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_friends, container, false);

        usersList = ((RecyclerView) view.findViewById(R.id.fragmentAddFriendsRecyclerV));
        adapter = new AddFriendsAdapter(getContext(), users, user);
        usersList.setAdapter(adapter);
        usersList.setLayoutManager( new LinearLayoutManager(getContext()));
        return view;
    }


}
