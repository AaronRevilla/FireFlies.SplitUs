package com.project.aaronkoti.splitus.adapters;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.aaronkoti.splitus.R;
import com.project.aaronkoti.splitus.beans.Bill;
import com.project.aaronkoti.splitus.beans.Notification;
import com.project.aaronkoti.splitus.beans.ResponseFCM;
import com.project.aaronkoti.splitus.beans.SplitUsNotification;
import com.project.aaronkoti.splitus.beans.User;
import com.project.aaronkoti.splitus.menuViews.AddEvent;
import com.project.aaronkoti.splitus.menuViews.Events;
import com.project.aaronkoti.splitus.net.FirebaseMessageInterface;
import com.project.aaronkoti.splitus.net.RetrofitServiceGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User on 10/23/2016.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder>{


    public List<Bill> listBills;
    public Context context;
    public User currentUser;
    public FragmentManager fm;

    public EventAdapter(Context context, List<Bill> listBills, User currentUser, FragmentManager fm){
        this.listBills = listBills;
        this.context = context;
        this.currentUser = currentUser;
        this.fm = fm;
    }

    public void setNewList(List<Bill> newBills){
        listBills = null;
        listBills = newBills;
        notifyDataSetChanged();
    }

    public Context getContext(){return context;}

    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.bill_element, parent, false);

        // Return a new holder instance
        EventAdapter.ViewHolder viewHolder = new EventAdapter.ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EventAdapter.ViewHolder holder, int position) {
        Bill bill = listBills.get(position);
        int billSize = bill.getUsrList().size();
        holder.totalAmount.setText( String.valueOf(bill.getAmount()) + " / " + billSize + " = " + String.valueOf(bill.getAmountEachOne()));
        holder.numUsers.setText(billSize+"");
        holder.date.setText(bill.getDate().toString());
    }

    @Override
    public int getItemCount() {
        return listBills.size();
    }

    public void editSelectedBill(int billPosition){
        Bill selectedBill  = listBills.get(billPosition);

        AddEvent addEventFrag= new AddEvent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("UserInfo", currentUser);
        bundle.putInt("EventNumber", billPosition);
        bundle.putSerializable("Bill", selectedBill);
        addEventFrag.setArguments(bundle);
        fm.beginTransaction()
                .replace(R.id.fragmentWrapper, addEventFrag)
                .addToBackStack(null)
                .commit();
    }

    public void deleteSelectedBill(int position) {
        Bill billToRemove = listBills.get(position);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference root =  db.getReference("SplitUs");
        Task<Void> friendReq = root.child("Bills").child(billToRemove.getOwnerUid()).child(billToRemove.getId()).removeValue();

        friendReq.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Toast.makeText(getContext(), "Event Removed", Toast.LENGTH_SHORT).show();
            }
        });

        FirebaseMessageInterface mInterface = RetrofitServiceGenerator.createService(FirebaseMessageInterface.class);

        //sendNotifications
        for (User notifUsr: billToRemove.getUsrList()){
            if(notifUsr.getNotificationToken() != null){
                if(isConnectedToInternet()){
                    SplitUsNotification splitNotif = new SplitUsNotification();
                    splitNotif.setTo( notifUsr.getNotificationToken());
                    Notification not = new Notification();
                    not.setTitle("Split Us Notification");
                    not.setText("Hi, " + currentUser.getName() + " delete one event in where you were subscribe total amount $" +String.valueOf(billToRemove.getAmount()) + " for each one $" + String.valueOf(billToRemove.getAmountEachOne()));
                    not.setSound("default");
                    //not.setIcon("@drawable/ic_person_pin_black_24dp");
                    splitNotif.setNotification(not);

                    Map<String, String> header = new HashMap<String, String>();
                    header.put("Content-Type", "application/json");
                    header.put("Authorization", "key=AIzaSyBB7qzEKFTGpoDJirMcfHC9bGhFgXTigLw");


                    Call<ResponseFCM> response =  mInterface.sendNotification(splitNotif, header);
                    response.enqueue(new Callback<ResponseFCM>() {
                        @Override
                        public void onResponse(Call<ResponseFCM> call, Response<ResponseFCM> response) {
                            Log.d("DEBUG", "NOTIFICATION SENDED");
                        }

                        @Override
                        public void onFailure(Call<ResponseFCM> call, Throwable t) {
                            Log.d("DEBUG", "NOTIFICATION NOT SENDED");
                        }
                    });
                }
            }
        }

        listBills.remove(position);
        notifyItemRemoved(position);
    }

    public boolean isConnectedToInternet(){
        ConnectivityManager cm  = ((ConnectivityManager) getContext().getSystemService(getContext().CONNECTIVITY_SERVICE));
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()){
            return true;
        }
        else{
            return false;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView totalAmount;
        public TextView numUsers;
        public TextView date;

        public ViewHolder(View itemView) {
            super(itemView);

            totalAmount = (TextView) itemView.findViewById(R.id.vh_bill_totalAmount);
            numUsers = (TextView) itemView.findViewById(R.id.vh_bill_numUsers);
            date = (TextView) itemView.findViewById(R.id.vh_bill_date);
//            SwipeLayout swipeLayout =  (SwipeLayout) itemView.findViewById(R.id.swipe);
//            //set show mode.
//            swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
//            swipeLayout.addDrag(SwipeLayout.DragEdge.Left, itemView.findViewById(R.id.bottom_wrapper));
//
//            swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
//                @Override
//                public void onClose(SwipeLayout layout) {
//                    //when the SurfaceView totally cover the BottomView.
//                }
//
//                @Override
//                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
//                    //you are swiping.
//                    if(leftOffset < -320){
//                        swipeLeft();
//                    }
//
//                }
//
//                @Override
//                public void onStartOpen(SwipeLayout layout) {
//
//                }
//
//                @Override
//                public void onOpen(SwipeLayout layout) {
//                    //when the BottomView totally show.
//                }
//
//                @Override
//                public void onStartClose(SwipeLayout layout) {
//
//                }
//
//                @Override
//                public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
//                    //when user's hand released.
//                }
//            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editSelectedBill(getAdapterPosition());
                }
            });

        }

        public void swipeLeft(){
            deleteSelectedBill(getAdapterPosition());
        }

        public void swipeRight(){
            editSelectedBill(getAdapterPosition());
        }


    }
}
