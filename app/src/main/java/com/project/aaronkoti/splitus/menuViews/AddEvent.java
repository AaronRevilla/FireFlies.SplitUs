package com.project.aaronkoti.splitus.menuViews;


import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.lang.UScript;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.aaronkoti.splitus.R;
import com.project.aaronkoti.splitus.adapters.AddEventAdapter;
import com.project.aaronkoti.splitus.adapters.AddFriendDialogAdapter;
import com.project.aaronkoti.splitus.beans.Bill;
import com.project.aaronkoti.splitus.beans.Event;
import com.project.aaronkoti.splitus.beans.Friend;
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


    public List<User> listOfUsers;
    public AddFriendDialogAdapter adapterDialog;
    public RecyclerView dialogtAddFriendsRecyclerV;
    public Button dialogSaveButton;


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
        this.bill = (Bill)args.getSerializable("Bill");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_add_event, container, false);
        Log.d("DEBUG", bill.toString());
        //set amount
        totalAmount = (EditText) view.findViewById(R.id.totalAmountBill);
        totalAmount.setText(String.valueOf( bill.getAmount()));

        //if their have some pictures, add
        if(bill.getImgList() != null && !bill.getImgList().isEmpty() ){
            imgList = bill.getImgList();
            numImages = imgList.size();
        }
        else{
            imgList = new ArrayList<>();
            numImages = imgList.size();
        }

        imgRecyclerView = (RecyclerView) view.findViewById(R.id.picturesRecyclerView);
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
                bill.setImgList(imgList);
                bill.setAmount(Float.parseFloat( totalAmount.getText().toString()));
                createModal();

//                Bundle bundle = new Bundle();
//                bundle.putSerializable("UserInfo", user);
//                AddFriendsToBill dialogFrag = new AddFriendsToBill();
//                dialogFrag.setArguments(bundle);
//                dialogFrag.show( getFragmentManager(), "AddFriendsToBill");
            }
        });
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

    public void createModal(){
        // custom dialog
        listOfUsers = new ArrayList<>();
        List<User> friendToSplitBill;
        if(bill.getUsrList() != null && !bill.getUsrList().isEmpty()){
            friendToSplitBill = bill.getUsrList();
        }
        else{
            friendToSplitBill = new ArrayList<>();
        }

        adapterDialog = new AddFriendDialogAdapter(getContext(), listOfUsers, friendToSplitBill);
        loadFriends();
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_add_users_bill);

        // set the custom dialog components - text, image and button
        dialogtAddFriendsRecyclerV = ((RecyclerView) dialog.findViewById(R.id.dialogtAddFriendsRecyclerV));
        dialogtAddFriendsRecyclerV.setAdapter(adapterDialog);
        dialogtAddFriendsRecyclerV.setLayoutManager( new LinearLayoutManager(getContext()));
        dialogSaveButton = (Button) dialog.findViewById(R.id.dialogSaveButton);

        dialogSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<User> auxFriends = adapterDialog.getSelectedUsers();
                if(!auxFriends.contains(user)){
                    auxFriends.add(user);
                }
                bill.setUsrList(auxFriends);

                //verify if friend rel exist
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference root =  db.getReference("SplitUs");
                DatabaseReference ref;
                if(bill.getId() != null){
                    ref = root.child("Bills").child(user.getUid()).child(bill.getId());
                }
                else{
                    ref = root.child("Bills").child(user.getUid()).push();
                    bill.setId(ref.getKey());
                }

                Log.d("DEBUG", ref.getKey());
                ref.setValue(bill);
                dialog.hide();
                returnToEvents();
            }
        });

        dialog.show();
    }

    public void returnToEvents(){
        Events eventFrag= new Events();
        Bundle bundle = new Bundle();
        bundle.putSerializable("UserInfo", user);
        eventFrag.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.fragmentWrapper, eventFrag)
                .addToBackStack(null)
                .commit();
    }

    public void loadFriends(){
        //MakeConnection to DB
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference root =  db.getReference("SplitUs");
        DatabaseReference friendReq = root.child("Friends").child(user.getUid());

        friendReq.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listOfUsers.clear();
                for(DataSnapshot child: dataSnapshot.getChildren()){
                    Friend friend = child.getValue(Friend.class);
                    DatabaseReference friendRef = root.child("Users").child(friend.getFriendId());
                    friendRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User auxUs = dataSnapshot.getValue(User.class);
                            Log.d("DEBUG", auxUs.toString());
                            listOfUsers.add( dataSnapshot.getValue(User.class));
                            adapterDialog.setNewList(listOfUsers);
                            //dialogtAddFriendsRecyclerV.setAdapter(adapter);
                            //adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}
