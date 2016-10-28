package com.project.aaronkoti.splitus.menuViews;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import com.project.aaronkoti.splitus.adapters.EventAdapter;
import com.project.aaronkoti.splitus.beans.Bill;
import com.project.aaronkoti.splitus.beans.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;


public class Events extends Fragment {

    public User user;
    public List<Events> listEvents = new ArrayList<>();
    public List<Bill> listBills;
    public RecyclerView eventList;
    public FloatingActionButton addEvent;
    public EventAdapter adapter;


    public Events() {
        // Required empty public constructor
    }


    public static Events newInstance(User userInfo) {
        Events fragment = new Events();
        Bundle args = new Bundle();
        args.putSerializable("UserInfo", userInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        listBills = new ArrayList<>();
        Bundle args = getArguments();
        this.user = (User) args.getSerializable("UserInfo");

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference root =  db.getReference("SplitUs");
        DatabaseReference userRef = root.child("Bills");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listBills.clear();

                if(dataSnapshot.hasChildren()){
                    for(DataSnapshot childUsr: dataSnapshot.getChildren()){//iterates over usrs ids

                        if(childUsr.hasChildren()){

                            for(DataSnapshot childBills: childUsr.getChildren()){// iterates over users bills

                                Bill auxBillUser  = childBills.getValue(Bill.class);
                                List<User> auxUsersBills = auxBillUser.getUsrList();
                                //Log.d("USER BILLS", auxUsersBills.size() + "");

                                for (User usr: auxUsersBills){
                                    if(usr.getUid().equals(user.getUid())){
                                        listBills.add(auxBillUser);
                                        Collections.sort( listBills, new EventComparator());
                                        adapter.setNewList(listBills);
                                    }
                                }

                            }

                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final View view = inflater.inflate(R.layout.fragment_events, container, false);


        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                EventAdapter.ViewHolder auxVH = (EventAdapter.ViewHolder) viewHolder;
                if(swipeDir == ItemTouchHelper.RIGHT){
                    auxVH.swipeRight();
                }
                if(swipeDir == ItemTouchHelper.LEFT){
                    auxVH.swipeLeft();
                }

            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);

        eventList = ((RecyclerView) view.findViewById(R.id.fragmentEventRecyclerV));
        eventList.setItemAnimator( new DefaultItemAnimator());
        eventList.setLayoutManager( new LinearLayoutManager(getContext()));
        adapter = new EventAdapter(getContext(), listBills, user, getFragmentManager());
        eventList.setAdapter(adapter);
        itemTouchHelper.attachToRecyclerView(eventList);

        addEvent = ((FloatingActionButton) view.findViewById(R.id.fragmentAddEvent));

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddEvent addEventFrag= new AddEvent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("UserInfo", user);
                bundle.putInt("EventNumber", listEvents.size());
                Bill newBill = new Bill();
                newBill.setDate( new Date());
                newBill.setAmount( (float) 0);
                bundle.putSerializable("Bill", newBill);
                addEventFrag.setArguments(bundle);
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragmentWrapper, addEventFrag)
                        .addToBackStack(null)
                        .commit();
            }
        });

        eventList.setLayoutManager( new LinearLayoutManager(getContext()));

        return view;
    }

    public class EventComparator implements Comparator<Bill> {
        @Override
        public int compare(Bill o1, Bill o2) {
            return o2.getDate().compareTo(o1.getDate());
        }
    }

}
