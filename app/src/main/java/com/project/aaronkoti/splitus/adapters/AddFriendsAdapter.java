package com.project.aaronkoti.splitus.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.project.aaronkoti.splitus.R;
import com.project.aaronkoti.splitus.beans.Friend;
import com.project.aaronkoti.splitus.beans.User;

import java.util.List;

/**
 * Created by User on 10/21/2016.
 */

public class AddFriendsAdapter extends RecyclerView.Adapter<AddFriendsAdapter.ViewHolder> {

    public List<User> userList;
    public Context mContext;
    public User currentUser;

    public AddFriendsAdapter(Context context, List<User> users, User currentUser){
        userList = users;
        mContext = context;
        this.currentUser = currentUser;
    }

    @Override
    public AddFriendsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.add_friends_element, parent, false);

        // Return a new holder instance
        AddFriendsAdapter.ViewHolder viewHolder = new AddFriendsAdapter.ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AddFriendsAdapter.ViewHolder holder, int position) {
        User auxUser = userList.get(position);

        if(auxUser.getImageUrl() != null){
            Glide.with(getContext())
                        .load(auxUser.getImageUrl())
                        .override(65, 65)
                        .centerCrop()
                        .into(holder.userImg);
        }

        if(auxUser.getName() != null){
            holder.usrName.setText(auxUser.getName());
        }
        else{
            holder.usrName.setText("No name");
        }

        if(auxUser.getPhoneNumber() != null){
            holder.usrPhone.setText(auxUser.getPhoneNumber());
        }
        else{
            holder.usrPhone.setText("No phone");
        }

        if(auxUser.getEmail() != null){
            holder.usrEmail.setText(auxUser.getEmail());
        }
        else{
            holder.usrEmail.setText("No Email");
        }

        //verify if friend rel exist
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference root =  db.getReference("SplitUs");
        DatabaseReference friendReq = root.child("Friends").child(currentUser.getUid()).child(auxUser.getUid());

        friendReq.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){
                    holder.selection.setChecked(false);
                }
                else{
                    holder.selection.setChecked(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void setNewList(List<User> newUsers){
        userList = null;
        userList = newUsers;
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void selectFriend(int listPosition, boolean addFriend){
        User auxUser = userList.get(listPosition);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference root =  db.getReference("SplitUs");
        DatabaseReference friendReq = root.child("Friends").child(currentUser.getUid());

        if(addFriend){
            Friend friendRel = new Friend();
            friendRel.setFriendId(auxUser.getUid());
            friendRel.setMyId(currentUser.getUid());

            friendReq.child(friendRel.getFriendId()).setValue(friendRel);
            Log.d("DEBUG", friendRel.toString());
        }
        else{
            friendReq.child(auxUser.getUid()).setValue(null);
        }

        //in case when frinReq exist, delete

    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircularImageView userImg;
        public TextView usrName;
        public TextView usrPhone;
        public TextView usrEmail;
        public RadioButton selection;

        public ViewHolder(View itemView) {
            super(itemView);

            //userImg = ((ImageView) itemView.findViewById(R.id.vh_addFriend_Img));
            usrName = ((TextView) itemView.findViewById(R.id.vh_addFriend_name));
            usrPhone = ((TextView) itemView.findViewById(R.id.vh_addFriend_phone));
            usrEmail = ((TextView) itemView.findViewById(R.id.vh_addFriend_email));
            selection = ((RadioButton) itemView.findViewById(R.id.vh_addFriend_rb));

            userImg = (CircularImageView) itemView.findViewById(R.id.vh_addFriend_Img);
// Set Border
            userImg.setBorderColor(itemView.getResources().getColor(R.color.tw__light_gray));
            userImg.setBorderWidth(2);
// Add Shadow with default param
            userImg.addShadow();
// or with custom param
            userImg.setShadowRadius(5);
            userImg.setShadowColor(Color.LTGRAY);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(selection.isChecked()){
                        Log.d("DEBUG", "delete");
                        selectFriend(getAdapterPosition(), false);
                        selection.setChecked(false);
                    }
                    else{
                        Log.d("DEBUG", "add");
                        selectFriend(getAdapterPosition(), true);
                        selection.setChecked(true);
                    }
                    //selection.toggle();
                }
            });

            selection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean checked = ((RadioButton) v).isChecked();

                    if(checked){
                        selectFriend(getAdapterPosition(), true);
                    }
                    else{
                        selectFriend(getAdapterPosition(), false);
                    }
                }
            });

        }

    }

}
