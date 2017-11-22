package com.greenerlawn.greenerlawn;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by jason on 10/28/2017.
 */

    public class Zone {
    //TODO multiple sprinklers
    //todo multiple devices

    private boolean zOnOff;
    private String zGUID, zName;
    private File zImage;

    //default constructor for empty zone
    public Zone(){}
    public Zone(String zGUID) {
        this.zGUID = zGUID;
    }

    //constructor allows for full zone config
    public Zone(String zGUID, String zName, boolean zOnOff) {
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

    public File getzImage() {
        return zImage;
    }

    public void setzImage(File zImage) {
        this.zImage = zImage;
    }

}