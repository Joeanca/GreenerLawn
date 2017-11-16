package com.greenerlawn.greenerlawn;

import java.util.List;

/**
 * Created by jason on 11/16/2017.
 */

public class GreenHub {

    private String ghGUID;
    private int ports;
    private List<Zone> zoneList;
    private List<Schedules> schedulesList;

    public GreenHub() {
    }

    public GreenHub(String ghGUID, int ports, List<Zone> zoneList, List<Schedules> schedulesList) {
        this.ghGUID = ghGUID;
        this.ports = ports;
        this.zoneList = zoneList;
        this.schedulesList = schedulesList;
    }

    public String getGhGUID() {
        return ghGUID;
    }

    public void setGhGUID(String ghGUID) {
        this.ghGUID = ghGUID;
    }

    public int getPorts() {
        return ports;
    }

    public void setPorts(int ports) {
        this.ports = ports;
    }

    public List<Zone> getZoneList() {
        return zoneList;
    }

    public void setZoneList(List<Zone> zoneList) {
        this.zoneList = zoneList;
    }

    public List<Schedules> getSchedulesList() {
        return schedulesList;
    }

    public void setSchedulesList(List<Schedules> schedulesList) {
        this.schedulesList = schedulesList;
    }
}
