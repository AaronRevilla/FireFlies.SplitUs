package com.project.aaronkoti.splitus.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.aaronkoti.splitus.R;
import com.project.aaronkoti.splitus.beans.ImageBill;
import com.project.aaronkoti.splitus.menuViews.AddFriends;

import java.util.List;

/**
 * Created by User on 10/24/2016.
 */

public class AddEventAdapter extends RecyclerView.Adapter<AddEventAdapter.ViewHolder> {

    List<ImageBill> billsImages;
    Context context;

    public AddEventAdapter(Context contex, List<ImageBill> billsImages){
        this.context = contex;
        this.billsImages  =  billsImages;
    }

    public Context getContex(){return context;}

    @Override
    public AddEventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.bill_image_element, parent, false);

        // Return a new holder instance
        AddEventAdapter.ViewHolder viewHolder = new AddEventAdapter.ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AddEventAdapter.ViewHolder holder, int position) {
        ImageBill auxImg = billsImages.get(position);

        //holder.billImage.setImageBitmap( auxImg.getBitmap());

        if(auxImg.getName().equals("loadinigImg")){
            Glide
                    .with(getContex())
                    .load(R.drawable.loading2)
                    .asGif()
                    .placeholder(R.drawable.loading2)
                    .centerCrop()
                    //.override(500, 500)
                    .crossFade()
                    .into(holder.billImage);
        }else if(auxImg.getImgUrl() != null){
            Glide
                    .with(getContex())
                    .load(auxImg.getImgUrl())
                    .centerCrop()
                    //.override(500, 500)
                    .placeholder(R.drawable.loading2)
                    .crossFade()
                    .into(holder.billImage);
            holder.imgName.setText(auxImg.getName());
        }
        else if(auxImg.getImg() != null){
            byte[] decodedString = Base64.decode(auxImg.getImg(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.billImage.setImageBitmap(decodedByte);
            holder.imgName.setText(auxImg.getName());
        }

    }

    @Override
    public int getItemCount() {
        return billsImages.size();
    }

    public void addNewList(List<ImageBill> newList){
        billsImages = null;
        billsImages = newList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView billImage;
        public TextView imgName;

        public ViewHolder(View itemView) {
            super(itemView);

            billImage = (ImageView) itemView.findViewById(R.id.billImage);
            imgName = (TextView) itemView.findViewById(R.id.billImgName);
        }
    }
}
