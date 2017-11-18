package com.greenerlawn.greenerlawn;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
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
                UserSettings us = dataSnapshot.getValue(UserSettings.class);
                setTextViews(us);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void setTextViews(UserSettings userSettings) {
        EditText locationSetting = findViewById(R.id.locationSettingEnter);
        RadioButton celsiusButt = findViewById(R.id.celsiusButton);
        RadioButton fahrenheitButt = findViewById(R.id.fahrenheitButton);

        if(userSettings.getHeatUnit() == 0){
            celsiusButt.setChecked(true);
        } else {
            fahrenheitButt.setChecked(true);
        }

        locationSetting.setText(userSettings.getCity());

    }

    public void saveSettings(View view){
        UserSettings newSettings = new UserSettings();

        EditText locationSetting = findViewById(R.id.locationSettingEnter);
        RadioButton celsiusButt = findViewById(R.id.celsiusButton);

        if(celsiusButt.isChecked()){
            newSettings.setCelsius();
        } else {
            newSettings.setFahrenheit();
        }

        newSettings.setCity(locationSetting.getText().toString());
        settingsRef.setValue(newSettings);

        finish();
    }
}
