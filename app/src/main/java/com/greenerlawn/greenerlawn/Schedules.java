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
    private boolean isValid, isRepeat;
    private List<Schedules> schedulesList = new ArrayList<Schedules>();


    //todo firebase updates
    //todo add cascade enforcement


    public Schedules() {
    }

    public Schedules(String schGUID, int day, Long startTime, Long duration, Long endTime, String zGUID, boolean isRepeat) {
        this.schGUID = schGUID;
        this.day = day;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = endTime;
        this.zGUID = zGUID;
        this.isRepeat = isRepeat;
    }

    public void addSchedule(int day, Long startTime, Long duration, String zone) {
        Schedules newItem = new Schedules(null, day, startTime, duration, endTime, zone, isRepeat);

        for (int i = 0; i < schedulesList.size(); i++) {
            if (schedulesList.get(i).zGUID.equals(zone) && schedulesList.get(i).startTime == startTime) {
                schedulesList.remove(i);
            }
        }
        schedulesList.add(newItem);
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