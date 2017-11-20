package com.greenerlawn.greenerlawn;


import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by jason on 11/5/2017.
 */

public class DataManager {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dataRef = null;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference userRef = database.getReference().child("users").child(user.getUid());

    //@TODO make this better it is janky (Get the pi id from the user instead of just everyone using the same pi)
    private DatabaseReference greenerHubRef = database.getReference().child("greennerHubs").child("pi1");

    public final static String ZONE_REF = "zones";
    public final static String USER_SETTING_REF = "userSettings";


    public DataManager() {
    }

    public <T> void uploadNewData(String reference,  T upData) {
        dataRef = userRef.child(reference);
        String key = dataRef.push().getKey();
        dataRef.child(key).setValue(upData);
    }

    public DatabaseReference getReference(String reference){
        if (reference.equals(ZONE_REF)){
            dataRef = greenerHubRef.child(reference);
        } else {
            dataRef = userRef.child(reference);
        }

        return dataRef;
    }

}
