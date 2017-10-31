package com.greenerlawn.greenerlawn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kwabenaberko.openweathermaplib.Units;
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper;
import com.kwabenaberko.openweathermaplib.models.currentweather.CurrentWeather;

import org.w3c.dom.Text;


public class MainScreen extends AppCompatActivity {
    public static final String ANONYMOUS = "anonymous";
    private String mUsername;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen_activity);

        setWeather();
    }

    private void setWeather() {
        UserSettings settings = new UserSettings();
        OpenWeatherMapHelper helper = new OpenWeatherMapHelper();

        configWeather(helper, settings);

        helper.getCurrentWeatherByCityName(settings.getCity(), new OpenWeatherMapHelper.CurrentWeatherCallback() {
            @Override
            public void onSuccess(CurrentWeather currentWeather) {
                TextView weatherBox = findViewById(R.id.currentTemp);
                weatherBox.setText("" + currentWeather.getMain().getTemp()+ "°");
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.v("ERROR", throwable.getMessage());
            }
        });

    }
  // TODO: create resource file which contains all the keys and logins for apis (good practice)
    private void configWeather(OpenWeatherMapHelper helper, UserSettings settings) {
        helper.setApiKey(getString(R.string.OPEN_WEATHER_MAP_API_KEY));

        //@TODO Might change the settings heat unit to use same values as library for cleaner code.
        if(settings.getHeatUnit() == settings.CELSIUS) {
            helper.setUnits(Units.METRIC);
        } else {
            helper.setUnits(Units.IMPERIAL);
        }
    }

    public void openTimer(View view) {
        startActivity(new Intent(MainScreen.this, TimePopUp.class));

    }
    private void attachDatabaseReadListener(){
        if (mChildEventListener == null){
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                    FriendlyMessage friendlyMessage = dataSnapshot.getValue(FriendlyMessage.class);
//                    mMessageAdapter.add(friendlyMessage);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {  }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

                @Override
                public void onCancelled(DatabaseError databaseError) {   }
            };
            mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
        }

    }
    private void detachDatabaseReadListener(){
        if (mChildEventListener != null){
            mMessagesDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }
}
