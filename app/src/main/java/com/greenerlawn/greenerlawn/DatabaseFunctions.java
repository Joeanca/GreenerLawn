package com.greenerlawn.greenerlawn;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by joeanca on 2017-11-08.
 */

public class DatabaseFunctions {
    public static final String ANONYMOUS = "anonymous";
    private String mUsername, mEmail,uID, email, username;
    private DatabaseReference mUserRef;
    private DatabaseReference mDatabaseReference,mZonesDatabaseReference;
    private FirebaseUser firebaseUser;
    private static final int RC_SIGN_IN = 123;


    // ENTRY POINT FOR THE APP TO ACCESS THE DATABASE
    private FirebaseDatabase mFirebaseDatabase;

    // SPECIFIC PART REFERENCE TO THE DATABASE IT ONLY GOES TO THE USER
    private DatabaseReference mUserDatabaseReference;

    // A CHILD LISTENER FOR THE
    ChildEventListener mChildEventListener;
    ValueEventListener listener;
    // LOCAL USER
    User currentUser;


    public void StartDB(FirebaseUser firebaseUser){
        this.firebaseUser = firebaseUser;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUserDatabaseReference = mFirebaseDatabase.getReference().child("users");

        uID = firebaseUser.getUid();
        email = firebaseUser.getEmail();
        Log.e("EMAIL", "StartDB: " + email );
        username = firebaseUser.getDisplayName();
        retrieveUserFromDatabase(mUserDatabaseReference);
    }

    private void retrieveUserFromDatabase(final DatabaseReference mUserDatabaseReference) {

        Log.d("mUserReference", "retrieveUserFromDatabase: " +mUserDatabaseReference);
        ValueEventListener listener  = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(uID)){
                        currentUser = dataSnapshot.child(uID).getValue(User.class);
                        System.out.println("OVA HERE" + dataSnapshot.child(uID).getValue(User.class).getZoneList());
                        System.out.println("OVA HERE" + currentUser.getUsername());

                }
                else{
                    mUserDatabaseReference.child(uID).setValue(currentUser);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TODO "something went wrong" please retry the last thing you were trying to do. If the problem persist call 1-800-OHH-WELL
            }
        };
         mUserDatabaseReference.addValueEventListener(listener);
    }

    public String getEmail(){return email;}
    public String getUsername (){return username;}
    public void removeListener(){
        mUserDatabaseReference.removeEventListener(listener);
    }
    public void attachListener(){
        mUserDatabaseReference.addValueEventListener(listener);
    }

    private void StartZones(DatabaseReference userReference){
        List<Zone> zoneList;
        DatabaseReference mZonesDBReference = mUserDatabaseReference.child("zones");
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

        mZonesDBReference.addChildEventListener(mChildEventListener);
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
}
