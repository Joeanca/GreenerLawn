package com.greenerlawn.greenerlawn;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.firebase.ui.auth.AuthUI;
import com.greenerlawn.greenerlawn.R;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kwabenaberko.openweathermaplib.Units;
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper;
import com.kwabenaberko.openweathermaplib.models.currentweather.CurrentWeather;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

// TODO revise weather api which includes rain? precipitation
// https://www.apixu.com/doc/current.aspx apikey: 708a2ed675de4b6a9fb171931170111
public class MainScreen extends AppCompatActivity {
    public static final String ANONYMOUS = "anonymous";
    private String mUsername;
    public String uid;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUsersDatabaseReference,mZonesDatabaseReference;
    private FirebaseUser firebaseUser;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private static final int RC_SIGN_IN = 123;
    List<AuthUI.IdpConfig> providers;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        //        android.support.v7.widget.Toolbar myToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        //        setSupportActionBar(myToolbar);




        // Set colored status bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primary_dark));

        //set content view AFTER ABOVE sequence (to avoid crash)
        this.setContentView(R.layout.main_screen_activity);
        Log.d("getUser", "onCreate: Getting user" );

        // for add back arrow in action bar
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        mUsername = ANONYMOUS;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = mFirebaseAuth.getCurrentUser();
        providers = new ArrayList<>();


        //get Firebase user
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    //user is signed in
                    Log.d("insideListener", "onAuthStateChanged: inside mAuthStateListener" + user.getDisplayName());
                    onSignedInInitialize(user.getDisplayName());
                }else {
                    //user is signed out
                    onSignedOutCleanup();
                    providers.add(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build());
                    providers.add(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(
                                            Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                    .build(),
                            RC_SIGN_IN);

                }
            }
        };
        // TODO transparent action_bar and get it to record on firebase.
        //getUser();

        // Weather setup
        setWeather();
    }
    private void onSignedInInitialize(String username){
        mUsername = username;
    // remove it after validation it's just to test the id
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("username", "onSignedInInitialize: "  + user.getUid());
        attachDatabaseReadListener();
    }
    private void onSignedOutCleanup(){
        mUsername = ANONYMOUS;
        detachDatabaseReadListener();

    }
    private void attachDatabaseReadListener(){
        if (mChildEventListener == null){
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    //FriendlyMessage friendlyMessage = dataSnapshot.getValue(FriendlyMessage.class);
                    //mMessageAdapter.add(friendlyMessage);
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
            //mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
        }

    }
    private void getUser(){
        // Getting the firebase user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            uid = user.getUid();
            Log.d("userid", "getUser: " + uid.toString());
            mZonesDatabaseReference = FirebaseDatabase.getInstance(uid).getReference().child("zones");
            mZonesDatabaseReference.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //Get map of users in datasnapshot
                            collectZones((Map<String,Object>) dataSnapshot.getValue());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //handle databaseError
                        }
                    });
        } else{
            Log.d("noUser", "getUser: No user found");
        }
    }

    private void setWeather() {
        UserSettings settings = new UserSettings();
        OpenWeatherMapHelper helper = new OpenWeatherMapHelper();

        configWeather(helper, settings);

        helper.getCurrentWeatherByCityName(settings.getCity(), new OpenWeatherMapHelper.CurrentWeatherCallback() {
            @Override
            public void onSuccess(CurrentWeather currentWeather) {
                TextView weatherBox = findViewById(R.id.currentTemp);
                weatherBox.setText(new StringBuilder().append(getBaseContext().getString(R.string.current_temp)).append(currentWeather.getMain().getTemp()).append("°").toString());
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
    public void modifyZones(View view){
        startActivity(new Intent(MainScreen.this, ZoneSettings.class));
    }

    private void detachDatabaseReadListener(){
        if (mChildEventListener != null){
            //mMessagesDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }
    private void collectZones(Map<String,Object> users) {

        ArrayList<Zones> zones = new ArrayList<>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get zone field and append to list
            zones.add((Zones) singleUser.get("zones"));
            Log.d("zonemssg", "collectZones: "+ singleUser.toString());
        }

        System.out.println(zones.toString());
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN){
            if (resultCode == RESULT_OK){
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
            }else if (resultCode == RESULT_CANCELED){
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sign_out_menu:
                // sign out
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
