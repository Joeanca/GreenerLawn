package com.greenerlawn.greenerlawn;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by jason on 10/28/2017.
 */

public class Zones {

    private String zGUID,zName;
    private boolean zOnOff;
    private ArrayList<zSchedule> zoneSchedule = new ArrayList<zSchedule>();

    //default constructor for empty zone
    public Zones(String zGUID){
        this.zGUID = zGUID;
    }

    //constructor allows for full zone config
    public Zones(String zGUID, String zName, boolean zOnOff) {
        this.zGUID = zGUID;
        this.zName = zName;
        this.zOnOff = zOnOff;
    }

    public String getzGUID() {
        return zGUID;
    }

    public void setzGUID(String zGUID) {
        this.zGUID = zGUID;
    }

    public String getzName() {
        return zName;
    }

    public void setzName(String zName) {
        this.zName = zName;
    }

    public boolean iszOnOff() {
        return zOnOff;
    }

    public void setzOnOff(boolean zOnOff) {
        this.zOnOff = zOnOff;
    }

    public ArrayList<zSchedule> getZoneSchedule() {
        return zoneSchedule;
    }

    public void setZoneSchedule(ArrayList<zSchedule> zoneSchedule) {
        this.zoneSchedule = zoneSchedule;
    }

    public void addToSchedule(int aDay, Long aStartTime, Long aDuration){
        zoneSchedule.add(new zSchedule(aDay,aStartTime,aDuration));
    }

    private class zSchedule{
        private int day;
        private Long startTime;
        private Long duration;

        public zSchedule(int day, Long startTime, Long duration) {
            this.day = day;
            this.startTime = startTime;
            this.duration = duration;
        }

    }
}
