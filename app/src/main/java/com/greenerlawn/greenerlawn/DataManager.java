package com.greenerlawn.greenerlawn;


import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by jason on 11/5/2017.
 */

public class DataManager {
    private ArrayList<Zone> zoneArrayList = new ArrayList<>();
    private StorageReference mStorageRef;


    public DataManager() {
        pullZoneData();
        mStorageRef = FirebaseStorage.getInstance().getReference();



    }

    //TODO set method to pull from firebase
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
