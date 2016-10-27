package com.project.aaronkoti.splitus.menuViews;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.project.aaronkoti.splitus.R;
import com.project.aaronkoti.splitus.beans.User;


public class UserInfo extends Fragment {

    private User user;

    //private OnFragmentInteractionListener mListener;

    public UserInfo() {
    }

    // TODO: Rename and change types and number of parameters
    public static UserInfo newInstance(User userInfo) {
        UserInfo fragment = new UserInfo();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);


        //ImageView img = ((ImageView) view.findViewById(R.id.fragmentUserImage));
        TextView name = ((TextView) view.findViewById(R.id.fragmentUserName));
        TextView phone = ((TextView) view.findViewById(R.id.fragmentUserPhone));
        TextView mail = ((TextView) view.findViewById(R.id.fragmentUserEmail));


        CircularImageView circularImageView = (CircularImageView) view.findViewById(R.id.fragmentUserImage);
// Set Border
        circularImageView.setBorderColor(getResources().getColor(R.color.tw__light_gray));
        circularImageView.setBorderWidth(10);
// Add Shadow with default param
        circularImageView.addShadow();
// or with custom param
        circularImageView.setShadowRadius(15);
        circularImageView.setShadowColor(Color.LTGRAY);

        if(user.getImageUrl() != null){
            Glide
                    .with(this)
                    .load(user.getImageUrl())
                    .fitCenter()
                    //.override(1000, 1000)
                    //.placeholder(R.drawable.loading_spinner)
                    .crossFade()
                    .into(circularImageView);
        }
        if(user.getName() != null){
            name.setText(user.getName());
        }
        if(user.getPhoneNumber() != null){
            phone.setText(user.getPhoneNumber());
        }
        if(user.getEmail() != null){
            mail.setText(user.getEmail());
        }
        return view;
    }

    /*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
   /* public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
