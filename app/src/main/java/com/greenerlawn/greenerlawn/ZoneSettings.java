package com.greenerlawn.greenerlawn;


import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.util.Log;
import android.view.Window;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ZoneSettings extends AppCompatActivity {
    DataManager dM = new DataManager();
    private static final int GALLERY_INTENT = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zone_settings_activity);

        // TO GET THE BACK ARROW ON THE ACTION BAR
        int transparent = ContextCompat.getColor(this, R.color.transparent);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(transparent));
        getSupportActionBar().setTitle("Zone settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final RecyclerView recyclerZones = (RecyclerView) findViewById(R.id.zone_recycler);
        final LinearLayoutManager zoneLayoutManager = new LinearLayoutManager(this);
        recyclerZones.setLayoutManager(zoneLayoutManager);
        DatabaseReference dataRef = dM.getReference(dM.ZONE_REF);
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Zone> zones = new ArrayList<>();
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    Zone zone = child.getValue(Zone.class);
                    zone.setzGUID(child.getKey());
                    zone.dbRefSet(child.getKey());
                    zones.add(zone);
                }
                User.getInstance().zoneListSet(zones);
                doRecyclerStuff(zones, recyclerZones);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    // set an exit transition
        getWindow().setExitTransition(new Explode());
    }
    @Override
    public boolean onSupportNavigateUp(){

        finish();
        return true;
    }

    private void doRecyclerStuff(List<Zone> zones, RecyclerView recyclerZones){
        final ZoneSettingsRecyclerAdapter zoneSettingsRecyclerAdapter = new ZoneSettingsRecyclerAdapter(this,zones);
        recyclerZones.setAdapter(zoneSettingsRecyclerAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            Log.e("PICTURE TIME", "onActivityResult: " + data.toString() + data.getStringExtra("ZONEID") + requestCode);
        }

    }
}
