package com.greenerlawn.greenerlawn;

import java.util.ArrayList;
import java.util.List;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Created by joeanca on 2017-11-08.
 */

public class User {
    private String username;
    private String email;
    private List<Schedules> scheduleList;
    private List<Zone> zoneList;
    private String uID;
    private UserSettings userSettings;
    private static  User instance;

    public static User getInstance(){
        if (instance == null){
            instance = new User();
        }
        return instance;
    }

    private User(){
        this.userSettings = new UserSettings();
        this.zoneList = new ArrayList<>();
        this.scheduleList = new ArrayList<>();
    }

    private User (String name, String email, List<Schedules> scheduleList, List<Zone> zoneList, UserSettings userSettings){
        this.email = email;
        this.username = name;
        this.zoneList = zoneList;
        this.scheduleList = scheduleList;
        this.userSettings = userSettings;

    }

    private User( String email, String name) {
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

    public List<Schedules> getScheduleList() {
        return scheduleList;
    }

    public void setScheduleList(List<Schedules> scheduleList) {
        this.scheduleList = scheduleList;
    }

    public String uIDGet(){ return uID; }

    public void uIDSet(String uID){ this.uID = uID; }

    public UserSettings getUserSettings() {return userSettings;}

    public void setUserSettings(UserSettings userSettings) {this.userSettings = userSettings; }
    public List<Schedules> scheduleListGet() {return scheduleList;}
    public void scheduleListSet(List<Schedules> newScheduleList) {this.scheduleList = newScheduleList;}

}

