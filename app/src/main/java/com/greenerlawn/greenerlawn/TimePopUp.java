package com.greenerlawn.greenerlawn;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.NumberPicker;

/**
 * Created by Austin on 2017-10-28.
 */

public class TimePopUp extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_time_popup_activity);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        double hieghtPercentage = .5;
        double widthPercentage = .8;

        getWindow().setLayout((int)(width*widthPercentage), (int)(height*hieghtPercentage));

        //Define the our picker
        NumberPicker nph = findViewById(R.id.numberPickerHour);
        nph.setMinValue(0);
        nph.setMaxValue(24);
        nph.setWrapSelectorWheel(true);

        //Define minute picker
        NumberPicker npm = findViewById(R.id.numberPickerMinute);
        npm.setMinValue(0);
        npm.setMaxValue(59);
        npm.setWrapSelectorWheel(true);

        //Define second picker
        NumberPicker nps = findViewById(R.id.numberPickerSecond);
        nps.setMinValue(0);
        nps.setMaxValue(59);
        nps.setWrapSelectorWheel(true);


    }
}
