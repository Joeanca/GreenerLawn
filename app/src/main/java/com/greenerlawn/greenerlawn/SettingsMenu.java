package com.greenerlawn.greenerlawn;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Austin Arndt on 11/17/2017.
 */

public class SettingsMenu extends Activity{
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private String userId = FirebaseAuth.getInstance().getUid();
    private DatabaseReference settingsRef = database.getReference("/users/"+userId+"/userSettings");
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);



        settingsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView tv = findViewById(R.id.textView6);
                UserSettings us = dataSnapshot.getValue(UserSettings.class);
                tv.setText(us.getCity());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
