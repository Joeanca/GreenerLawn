package com.greenerlawn.greenerlawn;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by joeanca on 2017-11-08.
 */

public class DatabaseFunctions {
    public static final String ANONYMOUS = "anonymous";
    private String uID;
    private DatabaseReference deviceDBRef,mZonesDatabaseReference, mUserRef;
    private FirebaseUser firebaseUser;
    private static final int RC_SIGN_IN = 123;
    private ChildEventListener mZoneChildEventListener;
    private StorageReference storageRef;
    private static  DatabaseFunctions instance;


    public static DatabaseFunctions getInstance(){
        if (instance == null){
            instance = new DatabaseFunctions();
        }
        return instance;
    }


    // ENTRY POINT FOR THE APP TO ACCESS THE DATABASE
    private FirebaseDatabase mFirebaseDatabase;

    // SPECIFIC PART REFERENCE TO THE DATABASE IT ONLY GOES TO THE USER
    private DatabaseReference mUserDatabaseReference;

    // A CHILD LISTENER FOR THE
    ChildEventListener mChildEventListener;
    ValueEventListener listener;
    // LOCAL USER


    public void StartDB(FirebaseUser firebaseUser){
        this.firebaseUser = firebaseUser;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUserDatabaseReference = mFirebaseDatabase.getReference().child("users");
        User.getInstance().uIDSet(firebaseUser.getUid());
        retrieveUserFromDatabase(mUserDatabaseReference);
        // TODO CREATE METHOD TO CHOOSE THE DEVICE IF UNKNOWN
        StartZones();
        getZonePics();
    }

    private void getZonePics() {
//        storageRef = FirebaseStorage.getInstance().getReference().child("greennerHub/" + User.getInstance().getUserSettings().getDeviceSerial());
    }

    private void retrieveUserFromDatabase(final DatabaseReference mUserDatabaseReference) {
        Log.d("mUserReference", "retrieveUserFromDatabase: " +mUserDatabaseReference);
        ValueEventListener listener  = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(User.getInstance().uIDGet())){
                    // TODO SETUP USER FIELDS.
                    mUserRef = mUserDatabaseReference.child(User.getInstance().uIDGet());
                    mUserRef.setValue(User.getInstance());
                }
                else{
                    // REMOVE ME ONCE THE SETUP OF THE DEVICE ON INITIAL IS SETUP
                    mUserDatabaseReference.child(User.getInstance().uIDGet()).setValue(User.getInstance());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TODO "something went wrong" please retry the last thing you were trying to do. If the problem persist call 1-800-OHH-WELL
            }
        };
         mUserDatabaseReference.addValueEventListener(listener);
    }



    public void removeListener(){
        mUserDatabaseReference.removeEventListener(listener);
    }
    public void attachListener(){
        mUserDatabaseReference.addValueEventListener(listener);
    }

    private void StartZones(){
        deviceDBRef = FirebaseDatabase.getInstance().getReference("greennerHubs");
        deviceDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //iterate
                if (dataSnapshot.child(User.getInstance().getUserSettings().getDeviceSerial()).exists()){
                    mZonesDatabaseReference = deviceDBRef.child(User.getInstance().getUserSettings().getDeviceSerial());
                    Iterable<DataSnapshot> zones = dataSnapshot.child(User.getInstance().getUserSettings().getDeviceSerial()).child("zones").getChildren();
                    List<Zone> actualZones = new ArrayList<>();
                    for (DataSnapshot zone: zones){
                        Zone tempZone = zone.getValue(Zone.class);
                        tempZone.dbRefSet(zone.getKey());
                        actualZones.add(tempZone);
                    }
                    User.getInstance().zoneListSet(actualZones);
                    mZoneChildEventListener = new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {}
                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            Zone zone = dataSnapshot.getValue(Zone.class);
                            for (Zone z : User.getInstance().zoneListGet()){
                                if (z.getZoneNumber() == zone.getZoneNumber()){
                                    z.setzOnOff(zone.getzOnOff());
                                }
                            }

                            Log.e("CHANGE", "onChildChanged: "+ zone.getZoneNumber() );
                        }
                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {}

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    };
                    mZonesDatabaseReference.child("zones").addChildEventListener(mZoneChildEventListener);
                }else{
                    // SETUP THE DEVICE FOR THE FIRST TIME
                    Log.e("SOMETHING", "onDataChange: DEVICE DOESN'T EXIST");

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void StartSchedule(){
        DatabaseReference mScheduleDBReference = mUserDatabaseReference.child("zones");

        // TO ADD A USER. OR PUSH INFORMATION TO THE DATABASE SETTING UP LISTENERS
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // THIS IS WHEN SOMETHING IS INSERTED INTO THE CHILDREN OR AT START TO INSERT ALL THE ZONES/ SCHEDULES

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // THIS GETS CALLED WHEN THE CONTENTS OF THE CHILD GETS CHANGED
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // THIS GETS CALLED WHEN THE CHILD IS DELETED
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                // IF ONE OF THE MESSAGES CHANGES POSITION
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // SOME ERROR OCCURRED WHEN TRYING TO MAKE CHANGES
            }
        };
        mScheduleDBReference.addChildEventListener(mChildEventListener);
    }
    public void SwitchToggleZone(int zoneNumber, Boolean status){
        // TODO SET THE OTHER ZONES OFF
        User.getInstance().zoneListGet().get(zoneNumber-1).setzOnOff(status);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("greennerHubs/" + User.getInstance().getUserSettings().getDeviceSerial() + "/zones/" + User.getInstance().zoneListGet().get(zoneNumber-1).dbRefGet());
        ref.child("zOnOff").setValue(status);
    }
    public void getImage(int imageButtonId){
        Log.e("IMAGE BUTTON ID", "getImage"+ imageButtonId);
    }
    public void updateSerialNumber(String newSerial){
        // TODO UPDATE FIREBASE
        //mUserRef.child("userSettings").child("deviceSerial").setValue(newSerial);
        User.getInstance().getUserSettings().setDeviceSerial(newSerial);

    }
}
