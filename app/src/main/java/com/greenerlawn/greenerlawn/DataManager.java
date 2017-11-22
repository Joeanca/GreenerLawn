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

    //SCHEDULE FIELDS
    private final boolean VALID_AT_CREATE = true;
    private final boolean SUSPEND_AT_CREATE = false;
    private final boolean IDC_FLAG = false;
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


    //Schedule Manager

    //todo IDC sched functions
        //method suspends all schedules, for those not suspended sets flad
        // adds new sched item
        // removes sched item and reverses changes afterweard.
    public void addSchedule(Schedules newSched) {
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


    // todo condense expand calls
    public void  configureSchedule(ArrayList<String> zoneIDList, Long startTime, Long duration, int timeFlag, int[]dayArr, boolean repeat){
        // holds cascading start times
        Long endTime = Long.valueOf(0);
        Long[] startTimeArr = new Long[zoneIDList.size()];
        // holds redundant day entries for each sched item
        int[] expandedDays = new int[zoneIDList.size()*dayArr.length];
        // timeFlag is a 0 or 1
        // where 0 sets the duration param as the duration for each zone
        // 1 sets the duration as total for duration for all zones in the schedule item
            //IE pass 2 zones, start at 8, run for 20 min, flag = 1
                //results in each zone running for 10 minutes
            // if flag is set to 0
                // each zone runs for 20 minutes

        //determines the duration for each scheduleitem
        if (timeFlag == 1){duration = duration/zoneIDList.size();}

        //creates array with single cascading start times based on durations
        // ex 8, 8:20
        for(int i = 0,ii =1; i <startTimeArr.length; i++, ii++ ){
            startTimeArr[i] = startTime;
            startTimeArr[i] +=duration*ii;
        }

        ArrayList<Long> expandStartTimeArr = new ArrayList<>();
        //must use ArrayList as Array's require you to know the position to insert
        for(int i=0; i < startTimeArr.length; i++){
            for(int ii= 0; i < dayArr.length; ii++){
                expandStartTimeArr.add(startTimeArr[i]);
            }
        }

        //fills array with repeating day entries
        // ex MWF turns into MMWWFF
        for(int i =0; i < expandedDays.length; i++){
            expandedDays[i] = dayArr[i%dayArr.length];
        }

        //fills arraylist of redundant zone entries corresponding to each day in the sched
        // ex z1,z2 turns into z1,z1,z1,z2,z2,z2
        ArrayList<String> expandedZoneList = new ArrayList();
        for (int i = 0; i < zoneIDList.size(); i++ ){
            // addreasses each zone
            for(int ii = 0; ii < dayArr.length; ii++){
                // addreasses each day
                expandedZoneList.add(zoneIDList.get(i));
                // adds each zone to new list according to the # of days it will run
            }
        }

        createScheduleItems(duration, dayArr, repeat, endTime, startTimeArr, expandedDays, expandedZoneList);
    }

    public void createScheduleItems(Long duration, int[] dayArr, boolean repeat, Long endTime, Long[] startTimeArr, int[] expandedDays, ArrayList<String> expandedZoneList) {
        //creates and validates, sends to add method for error handling

        ArrayList<Schedules> tempSchedList = new ArrayList<>();
        for (int i = 0; i< expandedDays.length; i++){
           int sDay = dayArr[i];
           Long sStart = startTimeArr[i];
           String zGuid = expandedZoneList.get(i);
           tempSchedList.add(new Schedules(null, sDay,sStart, duration, endTime, zGuid, repeat,SUSPEND_AT_CREATE, VALID_AT_CREATE, IDC_FLAG));
        }

        for (Schedules temp: tempSchedList) {
            verifyValid(temp);
            addSchedule(temp);
        }
    }



}
