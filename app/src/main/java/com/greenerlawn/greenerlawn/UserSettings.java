package com.greenerlawn.greenerlawn;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Austin Arndt on 10/30/2017.
 */

public class UserSettings {
    private final int CELSIUS = 0;
    private final int FAHRENHEIT = 1;
    private int heatUnit;
    private String city;
    private String cityId;
    private String deviceSerial;

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public UserSettings(int heatUnit, String city) {
        this.heatUnit = heatUnit;
        this.city = city;
    }

    public UserSettings() {
        this.heatUnit = CELSIUS;
        this.city = "Calgary";
        this.cityId = "5913490";
        this.deviceSerial = "pi2";
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getHeatUnit() { return heatUnit; }

    public void setHeatUnit(int heatUnit) {
        this.heatUnit = heatUnit;
    }

    public void setFahrenheit(){this.heatUnit = FAHRENHEIT;}

    public void setCelsius(){this.heatUnit = CELSIUS;}

    public void setDeviceSerial(String deviceSerial){this.deviceSerial=deviceSerial;}

    public String getDeviceSerial(){ return deviceSerial; }

}
