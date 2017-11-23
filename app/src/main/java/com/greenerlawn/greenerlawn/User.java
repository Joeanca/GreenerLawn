package com.greenerlawn.greenerlawn;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by joeanca on 2017-11-08.
 */

public  class User {
    private String username;
    private String email;
    private List<Schedules> scheduleList = new ArrayList<>();
    private List<Zone> zoneList = new ArrayList<>();


    private String deviceSerial;

    private static  User instance;

    public static User getInstance(){
        if (instance == null){
            instance = new User();
        }
        return instance;
    }

    public User(){
        String username;
        String email;
        List<Zone> zoneList = new ArrayList<>();
        String deviceSerial;
    }

    public User (String name, String email, List<Schedules> scheduleList, List<Zone> zoneList){
        this.email = email;
        this.username = name;
        this.zoneList = zoneList;
        this.scheduleList = scheduleList;

    }

    public User( String email, String name) {
        this.email = email;
        this.username = name;
        this.zoneList = zoneList;
        this.scheduleList = scheduleList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Zone> zoneListGet() {return zoneList;}

    public void zoneListSet(List<Zone> zoneList) {this.zoneList = zoneList;}

    public String getDeviceSerial() {return deviceSerial;}

    public void setDeviceSerial(String deviceSerial) {this.deviceSerial = deviceSerial;}


}

