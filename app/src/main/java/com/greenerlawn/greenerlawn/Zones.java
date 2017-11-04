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

    public Zones(String zGUID){
        this.zGUID = zGUID;
    }
    public Zones(String zGUID, String zName, boolean zOnOff) {
        this.zGUID = zGUID;
        this.zName = zName;
        this.zOnOff = zOnOff;
        //day of week returns an int, 1 is sunday 7 is saturday
        //need to get values that meaningfully correspond to ranges of times

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
