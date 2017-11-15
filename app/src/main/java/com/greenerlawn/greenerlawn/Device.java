package com.greenerlawn.greenerlawn;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 11/14/2017.
 */

public abstract class Device {
    private String devGUID;
    private String zGUID;
    private boolean isOn;
    private Long lastUsed;
    private List<Device> deviceList = new ArrayList<>();

    public Device() {
    }

    public Device(String devGUID, String zGUID, boolean isOn, Long lastUsed) {
        this.devGUID = devGUID;
        this.zGUID = zGUID;
        this.isOn = isOn;
        this.lastUsed = lastUsed;
    }


    public String getDevGUID() {
        return devGUID;
    }

    public void setDevGUID(String devGUID) {
        this.devGUID = devGUID;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public Long getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(Long lastUsed) {
        this.lastUsed = lastUsed;
    }

    public String getzGUID() {
        return zGUID;
    }

    public void setzGUID(String zGUID) {
        this.zGUID = zGUID;
    }
}
