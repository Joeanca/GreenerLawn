package com.greenerlawn.greenerlawn;

/**
 * Created by Austin Arndt on 10/30/2017.
 */

public class UserSettings {
    private final int CELSIUS = 0;
    private final int FAHRENHEIT = 1;
    private int heatUnit;
    private String city;

    public UserSettings(int heatUnit, String city) {
        this.heatUnit = heatUnit;
        this.city = city;
    }

    public UserSettings() {
        this.heatUnit = CELSIUS;
        this.city = "Calgary";

    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getHeatUnit() {
        return heatUnit;
    }

    public void setHeatUnit(int heatUnit) {
        this.heatUnit = heatUnit;
    }

}
