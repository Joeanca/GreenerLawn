package com.greenerlawn.greenerlawn;

import android.graphics.Bitmap;

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

    private String zGUID;
    private String zName;
    private String dbRef;

    public String getPicRef() {return picRef; }

    public void setPicRef(String picRef) {this.picRef = picRef;}

    private String picRef;

    public Bitmap getzImage() {
        return zImage;
    }

    public void setzImage(Bitmap zImage) {
        this.zImage = zImage;
    }

    private Bitmap zImage;

    //default constructor for empty zone
    public Zone(){
    }
    public Zone(String zGUID){this.zGUID = zGUID;}

    //constructor allows for full zone config
    public Zone(String zGUID, String zName, String zoneNumber, boolean zOnOff) {
        this.zoneNumber = zoneNumber;
        this.zOnOff = zOnOff;
        this.zName = zName;
        this.zGUID = zGUID;
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

    public void dbRefSet(String ref){ this.dbRef= ref;}
    public String dbRefGet(){return dbRef;}


}