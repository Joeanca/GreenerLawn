package com.greenerlawn.greenerlawn;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.view.Window;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        DatabaseReference dataRef = dM.getReference(dM.ZONE_REF);
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> childern = dataSnapshot.getChildren();

                ArrayList<Zone> zones = new ArrayList<>();
                for (DataSnapshot child : childern) {
                    Zone zone = child.getValue(Zone.class);
                    zones.add(zone);
                }

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

    private void doRecyclerStuff(ArrayList<Zone> zones, RecyclerView recyclerZones){
        final ZoneSettingsRecyclerAdapter zoneSettingsRecyclerAdapter = new ZoneSettingsRecyclerAdapter(this,zones);
        recyclerZones.setAdapter(zoneSettingsRecyclerAdapter);
    }
}
