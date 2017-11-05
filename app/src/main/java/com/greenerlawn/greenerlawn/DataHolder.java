package com.greenerlawn.greenerlawn;


import java.util.ArrayList;

/**
 * Created by jason on 11/5/2017.
 */

public class DataHolder {
    private ArrayList<Zone> zoneArrayList = new ArrayList<Zone>();

    public DataHolder() {
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
