package com.greenerlawn.greenerlawn;


import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 11/5/2017.
 */

public class DataManager {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dataRef = null;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference userRef = database.getReference().child("users").child(user.getUid());

    public final static String ZONE_REF = "zone";
    public final static String USER_SETTING_REF = "userSettings";

    private ArrayList<Zone> zoneArrayList = new ArrayList<>();

    //ZCHEDULE FIELDS
    private final boolean VALID_AT_CREATE = true;
    private final boolean SUSPEND_AT_CREATE = true;
    private List<Schedules> schedulesList = new ArrayList<Schedules>();

    public DataManager() {
    }

    public <T> void uploadNewData(String reference,  T upData) {
        dataRef = userRef.child(reference);
        String key = dataRef.push().getKey();
        dataRef.child(key).setValue(upData);
    }

    public DatabaseReference getReference(String reference){
        dataRef = userRef.child(reference);
        return dataRef;
    }

    public void addSchedule(int day, Long startTime, Long duration, String zGUID, boolean repeat) {
        // create new schedule item
        Schedules newSched  = new Schedules("",day, startTime, duration, null, zGUID, repeat, SUSPEND_AT_CREATE, VALID_AT_CREATE);
        newSched.setEndTime(newSched.calcEndTime(newSched.getStartTime(), newSched.getDuration()));

        //check to see that new schedule can be made
        verifyValid(newSched);

        if(newSched.isValid()){
            schedulesList.add(newSched);
        }else{
            //todo error handling
        }

    }

    private void verifyValid(Schedules newSched) {
        //iterate over list
        for (int i = 0; i < schedulesList.size(); i++) {
            Schedules tempCheck = schedulesList.get(i);
            // check running schedules for conflicts
            // should check against all schedules
            // case
            // pause sched A
            //create sched B which conflicts with sched A
            //resume sched A and problems
            if (newSched.getDay() == tempCheck.getDay()) {
                enforceTime(newSched, tempCheck);
            }
        }
    }


    private void enforceTime(Schedules newSched, Schedules tempCheck) {
        if ((tempCheck.getStartTime()< newSched.getStartTime() && newSched.getStartTime() < tempCheck.getEndTime())
                || (tempCheck.getStartTime()> newSched.getStartTime() && newSched.getStartTime() > tempCheck.getEndTime() )){
            newSched.setValid(false);
        }
    }

    public void removeSchedule(String schGUID){
        for(int i = 0; i< schedulesList.size(); i++){
            if (schedulesList.get(i).getSchGUID().equals(schGUID)){
                Schedules temp = schedulesList.get(i);
                schedulesList.remove(i);
                temp = null;
            }
        }
    }

    public List<Schedules> getSchedulesList() {
        return schedulesList;
    }

    public void setSchedulesList(List<Schedules> schedulesList) {
        this.schedulesList = schedulesList;
    }

}
