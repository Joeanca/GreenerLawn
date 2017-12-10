package com.greenerlawn.greenerlawn;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by joeanca on 2017-11-08.
 */

public class DatabaseFunctions {
    private static DatabaseReference deviceDBRef;
    private static DatabaseReference mZonesDatabaseReference, mScheduleDatabaseReference;
    private static DatabaseReference mUserRef;
    private static DatabaseReference mZoneUpdater, mScheduleUpdater;
    private static FirebaseUser firebaseUser;
    private static  DatabaseFunctions instance;
    public final static String ZONE_REF = "zones";
    public final static String USER_SETTING_REF = "userSettings";
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dataRef = null;
    private static DatabaseReference greenerHubRef;
    private static FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private static DatabaseReference userRef;

    // Create a storage reference from our app
    public static DatabaseFunctions getInstance(){
        if (instance == null){
            instance = new DatabaseFunctions();
        }
        return instance;
    }
    public DatabaseFunctions(){
        StartDB();
    }


    // ENTRY POINT FOR THE APP TO ACCESS THE DATABASE
    private static FirebaseDatabase mFirebaseDatabase;

    // SPECIFIC PART REFERENCE TO THE DATABASE IT ONLY GOES TO THE USER
    private static DatabaseReference mUserDatabaseReference;

    // A CHILD LISTENER FOR THE
    ChildEventListener mChildEventListener;
    ValueEventListener listener;
    // LOCAL USER


    public static void StartDB(){
        firebaseUser = MainScreen.USER;
        userRef = database.getReference().child("users").child(firebaseUser.getUid());
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUserDatabaseReference = mFirebaseDatabase.getReference().child("users");
        User.getInstance().uIDSet(firebaseUser.getUid());
        User.getInstance().setEmail(firebaseUser.getEmail());
        User.getInstance().setUsername(firebaseUser.getDisplayName());
        // SETS THE SETTINGS AND
        retrieveUserFromDatabase(mUserDatabaseReference);

    }
    public DatabaseReference getReference(String reference){
        if (reference.equals(ZONE_REF)){
            dataRef = greenerHubRef.child(reference);
        } else {
            dataRef = userRef.child(reference);
        }

        return dataRef;
    }

    public <T> void uploadNewData(String reference,  T upData) {
        dataRef = userRef.child(reference);
        String key = dataRef.push().getKey();
        dataRef.child(key).setValue(upData);
    }
    private static void retrieveUserFromDatabase(final DatabaseReference mUserDatabaseReference) {
        Log.d("mUserReference", "retrieveUserFromDatabase: " +mUserDatabaseReference);
        ValueEventListener listener  = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(User.getInstance().uIDGet())){
                    // TODO SETUP USER FIELDS.
                    mUserRef = mUserDatabaseReference.child(User.getInstance().uIDGet());
                    User.getInstance().setUserSettings(dataSnapshot.child(User.getInstance().uIDGet()).getValue(UserSettings.class));
                    User.getInstance().getUserSettings().setDeviceSerial(dataSnapshot.child(User.getInstance().uIDGet()).child("userSettings").child("deviceSerial").getValue().toString());
                }
                else{
                    // REMOVE ME ONCE THE SETUP OF THE DEVICE ON INITIAL IS SETUP
                    mUserDatabaseReference.child(User.getInstance().uIDGet()).setValue(User.getInstance());
                }
                greenerHubRef = database.getReference().child("greennerHubs").child(User.getInstance().getUserSettings().getDeviceSerial());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TODO "something went wrong" please retry the last thing you were trying to do. If the problem persist call 1-800-OHH-WELL
            }
        };
         mUserDatabaseReference.addListenerForSingleValueEvent(listener);
         //
        StartZones();
        StartSchedules();
    }
    public void removeListener(){
        mUserDatabaseReference.removeEventListener(listener);
    }
    public void attachListener(){
        mUserDatabaseReference.addValueEventListener(listener);
    }
    private static void StartZones(){
        deviceDBRef = FirebaseDatabase.getInstance().getReference("greennerHubs");
        deviceDBRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //iterate
                if (dataSnapshot.child(User.getInstance().getUserSettings().getDeviceSerial()).exists()){
                    mZonesDatabaseReference = deviceDBRef.child(User.getInstance().getUserSettings().getDeviceSerial()).child("zones");
                    mZoneUpdater = mZonesDatabaseReference;
                    Iterable<DataSnapshot> zones = dataSnapshot.child(User.getInstance().getUserSettings().getDeviceSerial()).child("zones").getChildren();
                    List<Zone> actualZones = new ArrayList<>();
                    for (DataSnapshot zone: zones){
                        final Zone tempZone = zone.getValue(Zone.class);
                        tempZone.dbRefSet(zone.getKey());
                        tempZone.setzGUID(zone.getKey());
                        actualZones.add(tempZone);
                    }
                    User.getInstance().zoneListSet(actualZones);
                    getZoneBitmapInitialize();
//                    mZoneChildEventListener = new ChildEventListener() {
//                        @Override
//                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {}
//                        @Override
//                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                            Zone zone = dataSnapshot.getValue(Zone.class);
//                            Log.e("dbfn 178", "onChildChanged: startzones "+ zone.getPicRef() );
//                            if (zone.getPicRef()!=null){
//                                zone.setzImage(DatabaseFunctions.getInstance().getZonePic(Integer.parseInt(zone.getZoneNumber())));
//                            }
//                            for (Zone z : User.getInstance().zoneListGet()){
//                                if (z.getZoneNumber() == zone.getZoneNumber()){
//                                    z.setzOnOff(zone.getzOnOff());
//                                }
//                            }
//                        }
//                        @Override
//                        public void onChildRemoved(DataSnapshot dataSnapshot) {}
//
//                        @Override
//                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {}
//                    };
//                    mZonesDatabaseReference.addChildEventListener(mZoneChildEventListener);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void StartSchedules(){
        deviceDBRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(User.getInstance().getUserSettings().getDeviceSerial()).exists()){
                    mScheduleDatabaseReference = deviceDBRef.child(User.getInstance().getUserSettings().getDeviceSerial()).child("schedules");
                    mScheduleUpdater = mZonesDatabaseReference;
                    Iterable<DataSnapshot> schedules = dataSnapshot.child(User.getInstance().getUserSettings().getDeviceSerial()).child("schedules").getChildren();
                    List<Schedules> actualSchedules = new ArrayList<>();
                    for (DataSnapshot schedule: schedules){
                        final Schedules tempSchedule = schedule.getValue(Schedules.class);
                        tempSchedule.setSchGUID(schedule.getKey());
                        actualSchedules.add(tempSchedule);
                        Log.e("DATABASE 213", "onDataChange: " + tempSchedule.toString());
                    }
                    User.getInstance().scheduleListSet(actualSchedules);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private static void getZoneBitmapInitialize(){
        for (final Zone zone: User.getInstance().zoneListGet()) {
            if (zone.getPicRef() != null) {
                StorageReference imagesRef = storage.getReferenceFromUrl(zone.getPicRef());
                try {
                    final File localFile = File.createTempFile("Image", "bmp");
                    imagesRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            User.getInstance().zoneListGet().get(Integer.parseInt(zone.getZoneNumber()) - 1).setzImage(BitmapFactory.decodeFile(localFile.getAbsolutePath()));
//                            Log.e("line 222 dbfn ", " PICTURE AT START" + User.getInstance().zoneListGet().get(Integer.parseInt(zone.getZoneNumber()) - 1).getzImage());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            Log.e("line 234 dbfn ", "onFailure: COULDN'T RETRIEVE" + e);
                        }
                    });
                } catch (IOException e) {
//                    Log.e("line 238 dbfn ", "onFailure: COULDN'T RETRIEVE" + e);
                }
            }
        }
    }
    public void SwitchToggleZone(int zoneNumber, Boolean status){
        // TODO SET THE OTHER ZONES OFF;
         database.getReference("greennerHubs/" + User.getInstance().getUserSettings().getDeviceSerial() + "/zones/" + User.getInstance().zoneListGet().get(zoneNumber).dbRefGet()).child("zOnOff").setValue(status);
    }
    public void updateSerialNumber(String newSerial){
        // TODO UPDATE FIREBASE
        mUserRef.child("userSettings").child("deviceSerial").setValue(newSerial);
        User.getInstance().getUserSettings().setDeviceSerial(newSerial);
        StartZones();
    }
    public void uploadZoneBitmap(final int zoneNum, Bitmap bitmapUp){
        StorageReference imagesRef = storageRef.child(User.getInstance().getUserSettings().getDeviceSerial()).child("pictures");
        final StorageReference zoneRefTemp = imagesRef.child(User.getInstance().zoneListGet().get(zoneNum).getZoneNumber());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapUp.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = zoneRefTemp.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                User.getInstance().zoneListGet().get(zoneNum).setPicRef(downloadUrl.toString());
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                database.getReference("greennerHubs/" + User.getInstance().getUserSettings().getDeviceSerial() + "/zones/" + User.getInstance().zoneListGet().get(zoneNum).dbRefGet() + "/picRef").setValue(downloadUrl.toString());
            }
        });
    }

    public void updateZoneName(String newName, int zoneNumber){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("greennerHubs/" + User.getInstance().getUserSettings().getDeviceSerial() + "/zones/" + User.getInstance().zoneListGet().get(zoneNumber).dbRefGet());
        ref.child("zName").setValue(newName);
    }
    public Bitmap getZonePic(final int zonePortNumber){
        final Bitmap[] zonePic = new Bitmap[1];
        if (User.getInstance().zoneListGet().get(zonePortNumber-1).getzImage()==null){
            if (User.getInstance().zoneListGet().get(zonePortNumber-1).getPicRef()!=null) {
                //
                StorageReference imagesRef = storage.getReferenceFromUrl(User.getInstance().zoneListGet().get((zonePortNumber - 1)).getPicRef());
                try {
                    final File localFile = File.createTempFile("Image", "bmp");
                    imagesRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            zonePic[0] = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            User.getInstance().zoneListGet().get((zonePortNumber-1)).setzImage(zonePic[0]);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else zonePic[0] = User.getInstance().zoneListGet().get(zonePortNumber-1).getzImage();
        if (zonePic[0]!=null)
        Log.e("324 dbfn", "getZonePic: " + zonePic[0].toString());
        return zonePic[0];
    }

    public void updateSchedule (List<Schedules> newScheduleList){
        User.getInstance().scheduleListSet(newScheduleList);
        mScheduleUpdater.setValue(newScheduleList);
    }
}
