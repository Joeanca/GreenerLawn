package com.greenerlawn.greenerlawn;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kwabenaberko.openweathermaplib.Units;
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper;
import com.kwabenaberko.openweathermaplib.models.common.Weather;
import com.kwabenaberko.openweathermaplib.models.currentweather.CurrentWeather;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

// TODO revise weather api which includes rain? precipitation
// https://www.apixu.com/doc/current.aspx apikey: 708a2ed675de4b6a9fb171931170111
public class MainScreen extends AppCompatActivity {
    public static final String ANONYMOUS = "anonymous";
    private String mUsername, mEmail;
    private ImageView mImage;
    public String uid;
    private DatabaseReference mUserRef;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference,mZonesDatabaseReference;
    private FirebaseUser firebaseUser;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle abdt;
    private ListView mDrawerList;
    private static final int RC_SIGN_IN = 123;

    List<AuthUI.IdpConfig> providers;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        int transparent = ContextCompat.getColor(this, R.color.transparent);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(transparent);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(transparent));





        //set content view AFTER ABOVE sequence (to avoid crash)
        this.setContentView(R.layout.main_screen_activity);
        mImage = findViewById(R.id.iv_drawer_user);


        // Drawer functions
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_screen_layout);
        abdt = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.Open, R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(abdt);
        abdt.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView nav_view = (NavigationView)findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_gallery){
                    Toast.makeText(MainScreen.this, "smtg", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.sign_out_menu){
                    AuthUI.getInstance().signOut(MainScreen.this);
                }
                return true;
            }
        });

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
                    onSignedInInitialize(user);
                    createUser(user);

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
        // TODO place the username and password variables to the textviews
        View drawerView = (DrawerLayout)findViewById(R.id.drawer_layout);
        NavigationView mNavView = (NavigationView) drawerView.findViewById(R.id.nav_view);
        LinearLayout headerDrawer = (LinearLayout) mNavView.findViewById( R.id.nav_header_drawer);
        TextView tvUserName,tvEmail;
        tvUserName = (TextView) headerDrawer.findViewById(R.id.tv_drawer_user);
        tvUserName.setText("someName");
         //Weather setup
        setWeather();
    }

    private void createUser(FirebaseUser user) {
        String profileEmail = user.getEmail();
        writeNewUser(user.getUid(),user.getDisplayName(),profileEmail);
    }
    private void writeNewUser(String userId, String name, String email) {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
        com.greenerlawn.greenerlawn.User user = new com.greenerlawn.greenerlawn.User(name, email);
        mDatabaseReference.child(userId).setValue(user);
//        mUserRef = mDatabaseReference.child(userId).push().setValue(user);
    }


    private void onSignedInInitialize(FirebaseUser users){
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

        ArrayList<Zone> zones = new ArrayList<>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get zone field and append to list
            zones.add((Zone) singleUser.get("zones"));
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
                Log.d("onActivityResult", "onActivityResult: " + data.getAction());
            }else if (resultCode == RESULT_CANCELED){
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main_menu, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sign_out_menu:
                // sign out
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);

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
