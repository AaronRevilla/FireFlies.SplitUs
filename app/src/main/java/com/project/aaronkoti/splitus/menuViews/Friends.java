package com.project.aaronkoti.splitus.menuViews;


import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.aaronkoti.splitus.R;
import com.project.aaronkoti.splitus.beans.Friend;
import com.project.aaronkoti.splitus.beans.User;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Friends extends Fragment {

    private User user;
    private RecyclerView friendsList;
    private final String TAG = this.getClass().getName();
    List<Friend> friends = new ArrayList<>();
    FloatingActionButton addFriend;

    public Friends() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_friends, container, false);

        friendsList = ((RecyclerView) view.findViewById(R.id.fragmentFriendsRecyclerV));
        addFriend = ((FloatingActionButton) view.findViewById(R.id.fragmentAddFriend));

        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriend();
            }
        });


        return view;
    }

    // TODO: Rename and change types and number of parameters
    public static Friends newInstance(User userInfo) {
        Friends fragment = new Friends();
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
        DatabaseReference userRef = root.child("Friends").child(user.getUid());


        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d(TAG, dataSnapshot.exists() + "");
//                Log.d(TAG, dataSnapshot.hasChildren() + "");
//                Friend auxF = dataSnapshot.getValue(Friend.class);
//                Log.d(TAG, auxF.toString());
//                friends.add( dataSnapshot.getValue(Friend.class));
                //Log.d("DEBUG", dataSnapshot.getValue(Friend.class));
                /*for(DataSnapshot child: dataSnapshot.getChildren()){
                    Friend auxF = dataSnapshot.getValue(Friend.class);
                    Log.d(TAG, auxF.toString());
                    friends.add( child.getValue(Friend.class));
                }*/

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "On Cancelled DB");
            }
        });


    }

    public void addFriend(){
        AddFriends addFriendsFrag= new AddFriends();
        Bundle bundle = new Bundle();
        bundle.putSerializable("UserInfo", this.user);
        addFriendsFrag.setArguments(bundle);
        this.getFragmentManager().beginTransaction()
                .replace(R.id.fragmentWrapper, addFriendsFrag)
                .addToBackStack(null)
                .commit();
    }

}
