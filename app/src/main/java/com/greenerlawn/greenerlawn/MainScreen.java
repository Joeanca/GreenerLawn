package com.greenerlawn.greenerlawn;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;


import com.firebase.ui.database.FirebaseIndexArray;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.survivingwithandroid.weather.lib.WeatherClient;
import com.survivingwithandroid.weather.lib.WeatherConfig;
import com.survivingwithandroid.weather.lib.exception.WeatherLibException;
import com.survivingwithandroid.weather.lib.exception.WeatherProviderInstantiationException;
import com.survivingwithandroid.weather.lib.model.CurrentWeather;
import com.survivingwithandroid.weather.lib.model.Weather;
import com.survivingwithandroid.weather.lib.provider.openweathermap.OpenweathermapProviderType;
import com.survivingwithandroid.weather.lib.request.WeatherRequest;

//import com.google.firebase.database.ValueEventListener;


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
    private ImageView mImage;
    private DatabaseReference mUserRef;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference, mZonesDatabaseReference;
    private FirebaseUser firebaseUser;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle abdt;
//    private ListView mDrawerList;
    private static final int RC_SIGN_IN = 123;
    private static final String OPEN_API_KEY = "bea4b929ff482f02d7ab334b6e015467";
    List<AuthUI.IdpConfig> providers;
    private DatabaseFunctions dbFn;
    private  DataManager dm;
    User user = User.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO SETUP THE ACTIVITY TO SETUP THE DEVICE ON USERS FIRST INTERACTION WITH DEVICE
        user.setDeviceSerial("pi2");
        // TODO IF PI ZONES HAVE NEVER BEEN INITIALIZED THEN SETUP THROUGH USER APP


        // TRANSLUCENT STATUS AND ACTION BAR
        transparentBars();




        //set content view AFTER ABOVE sequence (to avoid crash)
        this.setContentView(R.layout.main_screen_activity);
        mImage = findViewById(R.id.iv_drawer_user);


        // DRAWER FUNCTIONS
        InitializeDrawer();

        // SETUP USER
        getFirebaseUser();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
                Log.d("onActivityResult", "onActivityResult: " + data.getAction());
            } else if (resultCode == RESULT_CANCELED) {
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


    // USE THIS FOR THE DRAWER OPTIONS TO GIVE THEM CONTEXT OR MAKE THE ACTIONABLE.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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

    private void getFirebaseUser() {
        mUsername = ANONYMOUS;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = mFirebaseAuth.getCurrentUser();
        providers = new ArrayList<>();

        //get Firebase user
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser fbuser = firebaseAuth.getInstance().getCurrentUser();
                if (fbuser != null) {
                    //user is signed in
                    user.setEmail(fbuser.getEmail());
                    user.setUsername(fbuser.getDisplayName());
                    onSignedInInitialize(fbuser);
                } else {
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
                                    .setLogo(R.drawable.droplet)      // Set logo drawable
                                    .setTheme(R.style.Theme_AppCompat_Light_main)      // Set theme
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }

    private void getWeather() {
        //test add to database use to add some zones for testing
//        Zone zone = new Zone("1231", "zone2", true);
        dm = new DataManager();
//        dm.uploadNewData(dm.ZONE_REF, zone);

        mUserRef = dm.getReference(dm.USER_SETTING_REF);

        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    dm.uploadNewData(dm.USER_SETTING_REF, new UserSettings());
                }
                UserSettings userSettings = null;
                userSettings = dataSnapshot.getValue(UserSettings.class);


                // Weather setup
                setupWeather(userSettings);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void transparentBars() {
        int transparent = ContextCompat.getColor(this, R.color.transparent);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(transparent);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(transparent));
    }

    @SuppressLint("ResourceAsColor")
    private void InitializeDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_screen_layout);
        abdt = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.Open, R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(abdt);
        abdt.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
//            @Override
//            public void onDrawerSlide(View drawerView, float slideOffset) {
//                //Called when a drawer's position changes.
//            }
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                //Called when a drawer has settled in a completely open state.
//                //The drawer is interactive at this point.
//                // If you have 2 drawers (left and right) you can distinguish
//                // them by using id of the drawerView. int id = drawerView.getId();
//                // id will be your layout's id: for example R.id.left_drawer
//            }
//            @Override
//            public void onDrawerClosed(View drawerView) {
//                // Called when a drawer has settled in a completely closed state.
//            }
//
//            @Override
//            public void onDrawerStateChanged(int newState) {
//                // Called when the drawer motion state changes. The new state will be one of STATE_IDLE, STATE_DRAGGING or STATE_SETTLING.
//            }
//        });

        // HERE YOU CAN CHANGE THE ACTIONS FOR THE DRAWER
        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_gallery) {
                    Toast.makeText(MainScreen.this, "smtg", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.sign_out_menu) {
                    AuthUI.getInstance().signOut(MainScreen.this);
                }
                return true;
            }
        });
    }



    private void userFunctions(FirebaseUser fbuser) {
        //TO PULL EVERYTHING FROM THE DB
        dbFn = new DatabaseFunctions();
        dbFn.StartDB(fbuser);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerLayout = navigationView.getHeaderView(0);
        TextView headerEmail = headerLayout.findViewById(R.id.tv_drawer_email);
        TextView headerName = headerLayout.findViewById(R.id.tv_drawer_user);
        headerEmail.setText(user.getEmail());
        headerName.setText(user.getUsername());
    }

    private void onSignedInInitialize(FirebaseUser user) {
        attachDatabaseReadListener();
        userFunctions(user);
        getWeather();
    }

    private void onSignedOutCleanup() {
        mUsername = ANONYMOUS;
        detachDatabaseReadListener();

    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            //mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
        }

    }

    public void openTimer(View view) {
        startActivity(new Intent(MainScreen.this, TimePopUp.class));

    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            //mMessagesDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

//    private void collectZones(Map<String, Object> users) {
//
//        ArrayList<Zone> zones = new ArrayList<>();
//
//        //iterate through each user, ignoring their UID
//        for (Map.Entry<String, Object> entry : users.entrySet()) {
//
//            //Get user map
//            Map singleUser = (Map) entry.getValue();
//            //Get zone field and append to list
//            zones.add((Zone) singleUser.get("zones"));
//            Log.d("zonemssg", "collectZones: " + singleUser.toString());
//        }
//
//        System.out.println(zones.toString());
//    }

    public void modifyZones(View view) {
        startActivity(new Intent(MainScreen.this, ZoneSettings.class));

    }

    private void setupWeather( final UserSettings userSettings) {
        WeatherClient.ClientBuilder builder = new WeatherClient.ClientBuilder();
        WeatherConfig config = new WeatherConfig();

        config.ApiKey = OPEN_API_KEY;
        if(userSettings.getHeatUnit() == 0){
            config.unitSystem = WeatherConfig.UNIT_SYSTEM.M;
        } else {
            config.unitSystem = WeatherConfig.UNIT_SYSTEM.I;
        }
        try {
            WeatherClient client = builder.attach(MainScreen.this)
                    .provider(new OpenweathermapProviderType())
                    .httpClient(com.survivingwithandroid.weather.lib.client.okhttp.WeatherDefaultClient.class)
                    .config(config)
                    .build();
            client.getCurrentCondition(new WeatherRequest(userSettings.getCityId()), new WeatherClient.WeatherEventListener() {
                @Override
                public void onWeatherRetrieved(CurrentWeather weather) {

                    Weather currWeather = weather.weather;

                    Log.d("Weatherhahasdf", "onWeatherRetrieved: "+ currWeather.rain[1].getAmmount());
                    TextView tempTV = findViewById(R.id.currentTemp);
                    TextView humidityTV = findViewById(R.id.currentHumidity);
                    TextView conditionTV = findViewById(R.id.currentCondition);
                    TextView rainTV = findViewById(R.id.currentRainfall);
                    ImageView conditionIV = findViewById(R.id.conditionImage);

                    tempTV.setText(((int) currWeather.temperature.getTemp())+ "°");
                    humidityTV.setText(currWeather.currentCondition.getHumidity() + "%");
                    conditionTV.setText(currWeather.currentCondition.getCondition());
                    rainTV.setText(currWeather.rain[0].getAmmount()+"");
                    Glide.with(getApplicationContext())
                            .load("http://openweathermap.org/img/w/"+currWeather.currentCondition.getIcon()+".png")
                            .into(conditionIV)
                    ;
                }

                @Override
                public void onWeatherError(WeatherLibException wle) {

                }

                @Override
                public void onConnectionError(Throwable t) {

                }
            });
        } catch (WeatherProviderInstantiationException e) {
            e.printStackTrace();
        }


    }


    public void modifySettings(View view) {
        startActivity(new Intent(MainScreen.this, SettingsMenu.class));

    }
}