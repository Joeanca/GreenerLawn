package com.greenerlawn.greenerlawn;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by joeanca on 2017-11-08.
 */

public class User {
    private String name;
    private String email;
    private List<String> scheduleList = new ArrayList<>();
    private List<String> zoneList = new ArrayList<>();


    private String deviceSerial;

    public User(){
        String name;
        String email;
        List<Zone> zoneList = new ArrayList<>();
        String deviceSerial;
    }

    public User (String name, String email, List<String> scheduleList, List<String> zoneList){
        this.email = email;
        this.name = name;
        this.zoneList = zoneList;
        this.scheduleList = scheduleList;

    }

    public User( String email, String name) {
        this.email = email;
        this.name = name;
        this.zoneList = zoneList;
        this.scheduleList = scheduleList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getZoneList() {return zoneList;}

    public void setZoneList(List<String> zoneList) {this.zoneList = zoneList;}

    public String getDeviceSerial() {return deviceSerial;}

    public void setDeviceSerial(String deviceSerial) {this.deviceSerial = deviceSerial;}


}

