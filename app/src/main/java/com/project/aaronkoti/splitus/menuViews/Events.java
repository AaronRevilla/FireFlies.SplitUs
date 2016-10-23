package com.project.aaronkoti.splitus.menuViews;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.aaronkoti.splitus.R;
import com.project.aaronkoti.splitus.beans.User;

import java.util.List;


public class Events extends Fragment {

    public User user;
    public List<Events> listEvents;
    public RecyclerView eventList;


    public Events() {
        // Required empty public constructor
    }


   /* public static Events newInstance(String param1, String param2) {
        Events fragment = new Events();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Bundle args = getArguments();
        this.user = (User) args.getSerializable("UserInfo");

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference root =  db.getReference("SplitUs");
        DatabaseReference userRef = root.child("Events").child(user.getUid());

        View view = inflater.inflate(R.layout.fragment_events, container, false);

        eventList = ((RecyclerView) view.findViewById(R.id.fragmentFriendsRecyclerV));


        eventList.setLayoutManager( new LinearLayoutManager(getContext()));


        return view;
    }

}
