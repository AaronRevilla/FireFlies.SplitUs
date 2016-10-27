package com.project.aaronkoti.splitus.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
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
import com.project.aaronkoti.splitus.beans.User;
import com.project.aaronkoti.splitus.menuViews.AddEvent;
import com.project.aaronkoti.splitus.menuViews.Events;

import java.util.List;

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
                Toast.makeText(getContext(), "Event Removed", Toast.LENGTH_SHORT);
            }
        });

        listBills.remove(position);
        notifyItemRemoved(position);
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
