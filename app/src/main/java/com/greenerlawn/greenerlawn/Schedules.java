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
    private Long startTime;
    private Long duration;
    private String zone;
    private List<Schedules> schedulesList = new ArrayList<Schedules>();

    public Schedules() {
    }

    public Schedules(String schGUID, int day, Long startTime, Long duration, String zone) {
        this.schGUID = schGUID;
        this.day = day;
        this.startTime = startTime;
        this.duration = duration;
        this.zone = zone;
    }

    public void addSchedule(int day, Long startTime, Long duration, String zone) {
        Schedules newItem = new Schedules(null, day, startTime, duration, zone);

        for (int i = 0; i < schedulesList.size(); i++) {
            if (schedulesList.get(i).zone.equals(zone) && schedulesList.get(i).startTime == startTime) {
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

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

}