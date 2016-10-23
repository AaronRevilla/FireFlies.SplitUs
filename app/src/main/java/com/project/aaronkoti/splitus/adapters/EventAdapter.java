package com.project.aaronkoti.splitus.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.project.aaronkoti.splitus.beans.User;
import com.project.aaronkoti.splitus.menuViews.Events;

import java.util.List;

/**
 * Created by User on 10/23/2016.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder>{


    public List<Events> listEvents;
    public Context context;
    public User currentUser;

    public EventAdapter(Context context, List<Events> listEvents, User currentUser){
        this.listEvents = listEvents;
        this.context = context;
        this.currentUser = currentUser;
    }

    public void setNewList(List<Events> newEvents){
        listEvents = null;
        listEvents = newEvents;
        notifyDataSetChanged();
    }


    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(EventAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return listEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);

            

        }
    }
}
