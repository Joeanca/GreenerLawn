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
    private boolean valid, repeat, suspended;
    private final boolean VALID_AT_CREATE = false;
    private final boolean SUSPEND_AT_CREATE = true;

    //so what am i using my flags for
    // valid is a running schedule
    //suspended is a paused schedule
    //repeat is the irepeat flag


    // needs to be extracted to a manager class
    private List<Schedules> schedulesList = new ArrayList<Schedules>();


    //todo firebase updates
    //todo add cascade enforcement


    public Schedules() {
    }

    public Schedules(String schGUID, int day, Long startTime, Long duration, Long endTime, String zGUID, boolean isRepeat, boolean suspended, boolean valid) {
        this.schGUID = schGUID;
        this.day = day;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = endTime;
        this.zGUID = zGUID;
        this.repeat = isRepeat;
        this.suspended = suspended;
        this.valid = valid;
    }

    public void addSchedule(int day, Long startTime, Long duration, String zGUID, boolean repeat) {
        Schedules newSched  = new Schedules("",day, startTime, duration, null, zGUID, repeat, SUSPEND_AT_CREATE, VALID_AT_CREATE);
        newSched.endTime = newSched.calcEndTime(newSched.startTime, newSched.duration);
        verifyValid(newSched);

        //if invalid go into error case
    }

    private void verifyValid(Schedules newSched) {
        for (int i = 0; i < schedulesList.size(); i++){
            Schedules tempCheck = schedulesList.get(i);
            //checkOverlap
            checkOverlap(newSched, tempCheck);
        }
    }

    // don't need to check overlap only for conflict times
    private void checkOverlap(Schedules newSched, Schedules tempCheck) {
        if (!tempCheck.isSuspended()) {
            if (newSched.day == tempCheck.getDay()) {
                enforceTime(newSched, tempCheck);
            }
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
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        repeat = repeat;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }
}