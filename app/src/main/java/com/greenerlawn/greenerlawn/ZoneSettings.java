package com.greenerlawn.greenerlawn;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.view.Window;

import com.firebase.ui.auth.AuthUI;

public class ZoneSettings extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zone_settings_activity);
        // TO GET THE BACK ARROW ON THE ACTION BAR
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


// set an exit transition
        getWindow().setExitTransition(new Explode());
    }
    @Override
    public boolean onSupportNavigateUp(){

        finish();
        return true;
    }
}
