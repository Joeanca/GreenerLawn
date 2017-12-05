package com.greenerlawn.greenerlawn;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.util.Log;
import android.view.MenuItem;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import me.iwf.photopicker.PhotoPicker;

public class ZoneSettings extends AppCompatActivity {
    private static final int GALLERY_INTENT = 300;
    private DatabaseReference dataRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.zone_settings_activity);

        // TO GET THE BACK ARROW ON THE ACTION BAR
        int transparent = ContextCompat.getColor(this, R.color.transparent);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(transparent));
        getSupportActionBar().setTitle("Zone settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final RecyclerView recyclerZones = (RecyclerView) findViewById(R.id.zone_recycler);
        LinearLayoutManager zoneLayoutManager = new LinearLayoutManager(this);
        recyclerZones.setLayoutManager(zoneLayoutManager);
        doRecyclerStuff(User.getInstance().zoneListGet(), recyclerZones);

        dataRef = DatabaseFunctions.getInstance().getReference(DatabaseFunctions.ZONE_REF);
        dataRef.addChildEventListener (new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Zone zone = dataSnapshot.getValue(Zone.class);
                if (zone.getPicRef()!=null) {
                    Bitmap tempBit = DatabaseFunctions.getInstance().getZonePic(Integer.parseInt(zone.getZoneNumber()));
                    User.getInstance().zoneListGet().get(Integer.parseInt(zone.getZoneNumber())-1).setzImage(tempBit);
                    zone.setzImage(User.getInstance().zoneListGet().get(Integer.parseInt(zone.getZoneNumber())-1).getzImage());
//                    Log.e("zonesettings 55", "onChildChanged: " + User.getInstance().zoneListGet().get(Integer.parseInt(zone.getZoneNumber())-1).getzImage() + " user: " + zone.getzImage());
                }
                User.getInstance().zoneListGet().get(Integer.parseInt(zone.getZoneNumber())-1).setzOnOff(zone.getzOnOff());
                User.getInstance().zoneListGet().get(Integer.parseInt(zone.getZoneNumber())-1).setPicRef(zone.getPicRef());
                User.getInstance().zoneListGet().get(Integer.parseInt(zone.getZoneNumber())-1).setzName(zone.getzName());
                doRecyclerStuff(User.getInstance().zoneListGet(), recyclerZones);
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
        // set the exit transition
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
        if (resultCode == RESULT_OK && requestCode <= 316 && requestCode >= 300) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                try{
                    Bitmap bitmap =getBitmap(photos.get(0));
                    User.getInstance().zoneListGet().get((requestCode - 300)).setzImage(bitmap);
                    DatabaseFunctions.getInstance().uploadZoneBitmap((requestCode - 300), bitmap);

                }catch(Exception e){
                    Log.e("LINE 112 ZONESETTINGS", "onActivityResult: OOps something went wrong " + e  );
                }
            }
        }
    }
    public Bitmap getBitmap(String path) {
        try {
            Bitmap bitmap=null;
            File f= new File(path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();    //Call the back button's method
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition( R.anim.animation_enter, R.anim.animation_leave);
    }
}
