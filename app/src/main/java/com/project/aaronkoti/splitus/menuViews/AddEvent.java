package com.project.aaronkoti.splitus.menuViews;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.aaronkoti.splitus.R;
import com.project.aaronkoti.splitus.adapters.AddEventAdapter;
import com.project.aaronkoti.splitus.beans.Bill;
import com.project.aaronkoti.splitus.beans.Event;
import com.project.aaronkoti.splitus.beans.ImageBill;
import com.project.aaronkoti.splitus.beans.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AddEvent extends Fragment {
    public User user;
    private final String TAG = this.getClass().getName();
    public Event event;
    public Bill bill;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public Uri imageUri;
    public ImageView imgContainer;
    public FloatingActionButton cameraButton;
    public RecyclerView imgRecyclerView;
    public AddEventAdapter adapter;
    public List<ImageBill> imgList;
    public FloatingActionButton saveButton;
    public EditText totalAmount;
    public int eventNumber;
    public int numImages = 0;
    public String imgName;
    public List<User> usrList = new ArrayList<>();


    public AddEvent() {
        // Required empty public constructor
    }

    public static AddEvent newInstance(User userInfo, Event evInfo, int evNumber) {
        AddEvent fragment = new AddEvent();
        Bundle args = new Bundle();
        args.putSerializable("UserInfo", userInfo);
        args.putSerializable("Eventinfo", evInfo);
        args.putInt("EventNumber", evNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        this.user = (User) args.getSerializable("UserInfo");
        this.event = (Event) args.getSerializable("EventInfo");
        this.eventNumber = args.getInt("EventNumber");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_add_event, container, false);

        bill = new Bill();
        bill.setDate( new Date());

        totalAmount = (EditText) view.findViewById(R.id.totalAmountBill);

        imgRecyclerView = (RecyclerView) view.findViewById(R.id.picturesRecyclerView);
        imgList = new ArrayList<>();
        adapter = new AddEventAdapter(getContext(), imgList);
        imgRecyclerView.setAdapter(adapter);
        imgRecyclerView.setLayoutManager( new GridLayoutManager(getContext(), 3));

        cameraButton = (FloatingActionButton) view.findViewById(R.id.fragmentAddPhoto);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(view.getContext().getPackageManager()) != null) {
                    imgName = "img_" + numImages + ".jpg";
                    File photo = new File(Environment.getExternalStorageDirectory(), imgName);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
                    imageUri = Uri.fromFile(photo);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });


        saveButton = (FloatingActionButton) view.findViewById(R.id.saveBillButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                bill.setImgList(imgList);
//                bill.setAmount(Float.parseFloat( totalAmount.getText().toString()));
//                Log.d("DEBUG", "click save");
//                //verify if friend rel exist
//                FirebaseDatabase db = FirebaseDatabase.getInstance();
//                DatabaseReference root =  db.getReference("SplitUs");
//                DatabaseReference ref = root.child("Bills").child(user.getUid()).push();
//                Log.d("DEBUG", ref.getKey());
//                ref.setValue(bill);
                Bundle bundle = new Bundle();
                bundle.putSerializable("UserInfo", user);
                AddFriendsToBill dialogFrag = new AddFriendsToBill();
                dialogFrag.setArguments(bundle);
                dialogFrag.show( getFragmentManager(), "AddFriendsToBill");
            }
        });

        usrList.add(user);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == Activity.RESULT_OK) {
                    final ImageBill img = new ImageBill();
                    Uri selectedImage = imageUri;
                    getActivity().getContentResolver().notifyChange(selectedImage, null);
                    ContentResolver cr = getActivity().getContentResolver();
                    Bitmap bitmap;
                    try {
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReferenceFromUrl("gs://splitus-4e68e.appspot.com/");
                        StorageReference spaceRef = storageRef.child("images/" + user.getUid() + "/" + eventNumber).child(imgName);
                        bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, selectedImage);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] imgData = baos.toByteArray();

                        UploadTask uploadTask = spaceRef.putBytes(imgData);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                img.setImgUrl(downloadUrl.toString());
                                //img.setBitmap(bitmap);
                                img.setName(imgName);
                                imgList.add(img);
                                adapter.addNewList(imgList);
                                numImages++;
                            }
                        });
                        //imgContainer.setImageBitmap(bitmap);
                        //Toast.makeText(getActivity(), selectedImage.toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Failed to load", Toast.LENGTH_SHORT)
                                .show();
                        Log.d("Camera", e.toString());
                    }
                }
        }

    }



}
