package com.greenerlawn.greenerlawn;


import java.util.ArrayList;

/**
 * Created by jason on 11/5/2017.
 */

public class DataManager {
    private ArrayList<Zone> zoneArrayList = new ArrayList<>();

    public DataManager() {
        pullZoneData();
    }

    //TODO set method to pull from firebse
    private void pullZoneData() {
        String[] guidArray = {"aa","ab","ac","ad","ae","af","ag","ah","ai"};
        String[] nameArray = {"name1","name2","name3","name4","name5","name6","name7","name8"};
        boolean status = false;
        for (int i = 0; i < 8; i++){
            zoneArrayList.add(new Zone(guidArray[i], nameArray[i], status));
        }
    }

    public ArrayList<Zone> getZoneArrayList() {
        return zoneArrayList;
    }
}
