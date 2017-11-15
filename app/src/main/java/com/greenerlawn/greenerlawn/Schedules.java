package com.greenerlawn.greenerlawn;

/**
 * Created by JC on 2017-11-04.
 */

class Schedules {

    private String schGUID;
    private int day;
    private Long startTime;
    private Long duration;
    private String zone;


    public Schedules() {
    }

    public Schedules(String schGUID, int day, Long startTime, Long duration, String zone) {
        this.schGUID = schGUID;
        this.day = day;
        this.startTime = startTime;
        this.duration = duration;
        this.zone = zone;
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