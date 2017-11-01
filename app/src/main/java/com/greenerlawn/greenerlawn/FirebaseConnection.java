package com.greenerlawn.greenerlawn;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Abstract class to implement a database connection to hangfire.
 *
 * Created by Jorge on 2017-10-31.
 */

public abstract class FirebaseConnection {
    // Main entry point for the app to access the database
    private FirebaseDatabase mFirebaseDatabase;
    // DB reference object: references a specific part of the db
    private DatabaseReference mMessagesDatabaseReference;

    /**
     * This method initializes the database and gets the elements under "root".
     * i.e users = root -> get all users
     *
     * Created by Jorge on 2017-10-31.
     */
    public DatabaseReference FirebaseObject(String root){
        //main access point for the database
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        // reference to a specific part of the database
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child(root);
        return mMessagesDatabaseReference;
    }



}

