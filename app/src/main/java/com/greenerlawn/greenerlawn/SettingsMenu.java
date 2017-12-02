package com.greenerlawn.greenerlawn;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

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


// TODO REMOVE BUTTON THAT SUBMITS THE VALUES OF THE BOXES


public class SettingsMenu extends Activity{
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
//    private String userId = FirebaseAuth.getInstance().getUid();
    private DatabaseReference settingsRef = database.getReference("/users/"+User.getInstance().uIDGet()+"/userSettings");
    static final int GET_CITY_ID = 69;
    private static String deviceText;
    final Context context = this;

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
        TextView cityId = findViewById(R.id.secretView);
        TextView locationSetting = findViewById(R.id.locationSettingEnter);
        final TextView tv_deviceIDtext = findViewById(R.id.tv_deviceID);
        tv_deviceIDtext.setText(User.getInstance().getUserSettings().getDeviceSerial());
        tv_deviceIDtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.text_input, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        Editable tempText = userInput.getText();
                                        tv_deviceIDtext.setText(tempText);
                                        DatabaseFunctions.getInstance().updateSerialNumber(tempText.toString());
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
        });
        RadioButton celsiusButt = findViewById(R.id.celsiusButton);
        RadioButton fahrenheitButt = findViewById(R.id.fahrenheitButton);

        if(userSettings.getHeatUnit() == 0){
            celsiusButt.setChecked(true);
        } else {
            fahrenheitButt.setChecked(true);
        }

        cityId.setText(userSettings.getCityId());
        locationSetting.setText(userSettings.getCity());

    }

    public void saveSettings(View view){
        UserSettings newSettings = new UserSettings();

        TextView cityId = findViewById(R.id.secretView);
        TextView locationSetting = findViewById(R.id.locationSettingEnter);
        RadioButton celsiusButt = findViewById(R.id.celsiusButton);

        if(celsiusButt.isChecked()){
            newSettings.setCelsius();
        } else {
            newSettings.setFahrenheit();
        }

        newSettings.setCityId(cityId.getText().toString());
        newSettings.setCity(locationSetting.getText().toString());
        settingsRef.setValue(newSettings);

        finish();
    }

    public void modifyCity(View view) {
        TextView text = (TextView) view;
        Intent searchCity = new Intent(SettingsMenu.this, SearchCity.class);
        searchCity.putExtra("Country", text.getText().toString());
        startActivityForResult(searchCity, GET_CITY_ID);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_CITY_ID) {
            if (resultCode == Activity.RESULT_OK){
                TextView id = findViewById(R.id.secretView);
                TextView country = findViewById(R.id.locationSettingEnter);

                id.setText(data.getStringExtra("resultId"));

                country.setText(data.getStringExtra("resultCountry"));

                Toast.makeText(getApplicationContext(), "Location Changed", Toast.LENGTH_SHORT).show();
            }

            if(requestCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "What", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
