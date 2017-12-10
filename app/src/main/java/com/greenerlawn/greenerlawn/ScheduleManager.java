package com.greenerlawn.greenerlawn;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by jason on 12/9/2017.
 */

public class ScheduleManager {

    //SCHEDULE FIELDS
    private final boolean VALID_AT_CREATE = true;
    private final boolean SUSPEND_AT_CREATE = false;
    private final boolean IDC_FLAG = false;
    long minute = 1000* 60;

    //should update FB as there is a listener
    private List<Schedules> schedulesList;
    private ArrayList<Zone> zoneArrayList;

    public ScheduleManager() {
        zoneArrayList= (ArrayList<Zone>) User.getInstance().zoneListGet();
        schedulesList = User.getInstance().getScheduleList();
    }

//Schedule Manager

//todo push updates toDatabaseFunctions.getInstance().UpdateSchedule();

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
            if (newSched.getDay() == tempCheck.getDay() && !tempCheck.isSuspended()) {
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
    public void configureSchedule(ArrayList<String> zoneIDList, Long startTime, long duration, int timeFlag, ArrayList<Integer> dayAL, boolean repeat){
        // holds cascading start times
        int endTime = 0;
        Long[] startTimeArr = new Long[zoneIDList.size()];
        // holds redundant day entries for each sched item
        int[] expandedDays = new int[zoneIDList.size()*dayAL.size()];
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
            startTimeArr[i] +=(duration*ii) + minute;
        }

        ArrayList<Long> expandStartTimeArr = new ArrayList<>();
        //must use ArrayList as Array's require you to know the position to insert
        // creates 8 8 8 820 820 820 840 840 840
        for(int i=0; i < startTimeArr.length; i++){
            for(int ii= 0; i < dayAL.size(); ii++){
                expandStartTimeArr.add(startTimeArr[i]);
            }
        }

        //fills array with repeating day entries
        // ex MWF turns into Mwfmwf
        for(int i =0; i < expandedDays.length; i++){
            expandedDays[i] = dayAL.get(i %dayAL.size());
        }

        //fills arraylist of redundant zone entries corresponding to each day in the sched
        // ex z1,z2 turns into z1,z1,z1,z2,z2,z2
        ArrayList<String> expandedZoneList = new ArrayList();
        for (int i = 0; i < zoneIDList.size(); i++ ){
            // addreasses each zone
            for(int ii = 0; ii < dayAL.size(); ii++){
                // addreasses each day
                expandedZoneList.add(zoneIDList.get(i));
                // adds each zone to new list according to the # of days it will run
            }
        }

        createScheduleItems(duration, dayAL, repeat, endTime, expandStartTimeArr, expandedDays, expandedZoneList);
    }

    public void createScheduleItems(long duration, ArrayList<Integer> dayArr, boolean repeat, long endTime, ArrayList<Long> expandStartTimeArr, int[] expandedDays, ArrayList<String> expandedZoneList) {
        //creates and validates, sends to add method for error handling

        ArrayList<Schedules> tempSchedList = new ArrayList<>();
        for (int i = 0; i< expandedDays.length; i++){
           int sDay = expandedDays[i];
           Long sStart = expandStartTimeArr.get(i);
           String zGuid = expandedZoneList.get(i);
           tempSchedList.add(new Schedules(null, sDay,sStart, duration, endTime, zGuid, repeat,SUSPEND_AT_CREATE, VALID_AT_CREATE, IDC_FLAG));
        }

        for (Schedules temp: tempSchedList) {
            verifyValid(temp);
            addSchedule(temp);
        }
    }

    public void addSchedule(Schedules newSched) {
        if(newSched.isValid()){
            schedulesList.add(newSched);
        }else{
            //todo error handling
        }
    }

    //RUN ALL METHOD
    public void runAllNow(int rANduration){
        pauseAll();
        Calendar today = Calendar.getInstance();
        Integer day = today.DAY_OF_WEEK;
        ArrayList<Integer> oneDay = new ArrayList<>(1);
        oneDay.add(day);
        long rANStart = System.currentTimeMillis();
        //start all in 3 minutes
        rANStart += (minute *3);
        ArrayList zoneID = new ArrayList();
        for (Zone zone: zoneArrayList) {
            zoneID.add(zone.getzGUID());
        }

        configureSchedule(zoneID,rANStart,rANduration,0, oneDay, false);

    }

    public void pauseAll(){
        for (Schedules current: schedulesList) {
            if (!current.isSuspended()){
                current.setPausedByIDC(true);
                current.setSuspended(true);
            }
        }

    }
}
