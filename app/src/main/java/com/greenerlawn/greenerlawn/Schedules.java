package com.greenerlawn.greenerlawn;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JC on 2017-11-04.
 */

// todo allow for IDFC setting
public class Schedules {

    private String schGUID;
    String tempGuid = "";

    //0 - 7, 0 = Sunday, 1 = Monday... 7 = saturday
    private int day;
    private Long startTime, duration, endTime;
    private String zGUID;

    //todo remove is from names
    private boolean isValid, isRepeat, isSuspended;
    private final boolean VALID_AT_CREATE = false;
    private final boolean SUSPEND_AT_CREATE = true;

    // needs to be extracted to a manager class
    private List<Schedules> schedulesList = new ArrayList<Schedules>();


    //todo firebase updates
    //todo add cascade enforcement


    public Schedules() {
    }

    public Schedules(String schGUID, int day, Long startTime, Long duration, Long endTime, String zGUID, boolean isRepeat, boolean isSuspended, boolean isValid) {
        this.schGUID = schGUID;
        this.day = day;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = endTime;
        this.zGUID = zGUID;
        this.isRepeat = isRepeat;
        this.isSuspended = isSuspended;
        this.isValid = isValid;
    }

    public void addSchedule(int day, Long startTime, Long duration, String zGUID, boolean repeat) {
        Schedules newSched  = new Schedules("",day, startTime, duration, null, zGUID, repeat, SUSPEND_AT_CREATE, VALID_AT_CREATE);
        newSched.endTime = newSched.calcEndTime(newSched.startTime, newSched.duration);
        //check if valid
            // check for conflicts
            // enforce cascade
        verifyValid(newSched);
    }

    private void verifyValid(Schedules newSched) {
        for (int i = 0; i < schedulesList.size(); i++){
            Schedules tempCheck = schedulesList.get(i);
            //checkOverlap
            checkOverlap(newSched, tempCheck);
        }
    }

    private void checkOverlap(Schedules newSched, Schedules tempCheck) {
        if (newSched.day == tempCheck.getDay() && newSched.zGUID.equals(tempCheck.getzGUID())){
            // (start < nstart < end) (start > nstart > end)
            enforceTime(newSched, tempCheck);
        }
    }

    private void enforceTime(Schedules newSched, Schedules tempCheck) {
        if ((tempCheck.getStartTime()< newSched.getStartTime() && newSched.getStartTime() < tempCheck.getEndTime())
                || (tempCheck.getStartTime()> newSched.getStartTime() && newSched.startTime > tempCheck.getEndTime() )){
            newSched.setValid(false);
        }
    }

    public void removeSchedule(String schGUID){
        for(int i = 0; i< schedulesList.size(); i++){
            if (schedulesList.get(i).schGUID.equals(schGUID)){
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

    public String getSchGUID() {
        return schGUID;
    }

    public void setSchGUID(String schGUID) {
        this.schGUID = schGUID;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getEndTime() {
        return endTime;
    }

    public Long calcEndTime(Long startTime, Long duration){
        Long cEndTime = startTime + duration;

        return cEndTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getzGUID() {
        return zGUID;
    }

    public void setzGUID(String zGUID) {
        this.zGUID = zGUID;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public boolean isRepeat() {
        return isRepeat;
    }

    public void setRepeat(boolean repeat) {
        isRepeat = repeat;
    }
}