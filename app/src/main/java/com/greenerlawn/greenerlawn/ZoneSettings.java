package com.greenerlawn.greenerlawn;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.view.Window;

import com.firebase.ui.auth.AuthUI;

import java.io.File;
import java.util.ArrayList;

public class ZoneSettings extends AppCompatActivity {
    DataManager dM = new DataManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zone_settings_activity);
        // TO GET THE BACK ARROW ON THE ACTION BAR
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final RecyclerView recyclerZones = (RecyclerView) findViewById(R.id.zone_recycler);
        final LinearLayoutManager zoneLayoutManager = new LinearLayoutManager(this);
        recyclerZones.setLayoutManager(zoneLayoutManager);
        ArrayList<Zone> zones = dM.getZoneArrayList();
        final ZoneSettingsRecyclerAdapter zoneSettingsRecyclerAdapter = new ZoneSettingsRecyclerAdapter(this,zones);
        recyclerZones.setAdapter(zoneSettingsRecyclerAdapter);


    // set an exit transition
        getWindow().setExitTransition(new Explode());
    }
    @Override
    public boolean onSupportNavigateUp(){

        finish();
        return true;
    }
}
