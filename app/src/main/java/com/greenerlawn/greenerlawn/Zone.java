package com.greenerlawn.greenerlawn;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by jason on 10/28/2017.
 */

    public class Zone {
    //TODO multiple sprinklers
    //todo multiple devices

    private boolean zOnOff;
    private String  zoneNumber;


    //default constructor for empty zone
    public Zone(){
    }

    //constructor allows for full zone config
    public Zone(String zoneNumber, boolean zOnOff) {
        this.zoneNumber = zoneNumber;
        this.zOnOff = zOnOff;
    }

    public void setzOnOff(boolean zOnOff) {
        this.zOnOff = zOnOff;
    }

    public String getZoneNumber() {
        return zoneNumber;
    }

    public void setZoneNumber(String zoneNumber) {
        this.zoneNumber = zoneNumber;
    }

    public boolean getzOnOff() {
        return zOnOff;
    }


}