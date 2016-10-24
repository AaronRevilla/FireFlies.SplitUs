package com.project.aaronkoti.splitus.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.aaronkoti.splitus.R;
import com.project.aaronkoti.splitus.beans.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 10/24/2016.
 */

public class AddFriendDialogAdapter extends RecyclerView.Adapter<AddFriendDialogAdapter.ViewHolder> {

    List<User> users;
    Context context;
    List<User> selectedUsers;

    public AddFriendDialogAdapter(Context context, List<User> users){
        this.users = users;
        this.context = context;
        selectedUsers = new ArrayList<>();
    }

    public Context getContext(){return context;}

    @Override
    public AddFriendDialogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.add_friends_element, parent, false);

        // Return a new holder instance
        AddFriendDialogAdapter.ViewHolder viewHolder = new AddFriendDialogAdapter.ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AddFriendDialogAdapter.ViewHolder holder, int position) {
        User auxUser = users.get(position);
        Log.d("DEBUG FROM ADAPTER", auxUser.toString());
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
    }

    public void selectFriend(int position, boolean addFriend){
        User user = users.get(position);

        if(addFriend){
            selectedUsers.add(user);
        }
        else{
            selectedUsers.remove(user);
        }
    }

    public List<User> getSelectedUsers(){return selectedUsers;}

    public void setNewList(List<User> newUsers){
        this.users = null;
        this.users = newUsers;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView userImg;
        public TextView usrName;
        public TextView usrPhone;
        public TextView usrEmail;
        public RadioButton selection;

        public ViewHolder(View itemView) {
            super(itemView);

            userImg = ((ImageView) itemView.findViewById(R.id.vh_addFriend_Img));
            usrName = ((TextView) itemView.findViewById(R.id.vh_addFriend_name));
            usrPhone = ((TextView) itemView.findViewById(R.id.vh_addFriend_phone));
            usrEmail = ((TextView) itemView.findViewById(R.id.vh_addFriend_email));
            selection = ((RadioButton) itemView.findViewById(R.id.vh_addFriend_rb));


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
