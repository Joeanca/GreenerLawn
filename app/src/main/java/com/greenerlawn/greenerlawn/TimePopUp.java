package com.greenerlawn.greenerlawn;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

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
        double heightPercentage = .5;
        double widthPercentage = .8;

        getWindow().setLayout((int)(width*widthPercentage), (int)(height*heightPercentage));

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

    }

    public void submitResult(View view) {
        NumberPicker nph = findViewById(R.id.numberPickerHour);
        NumberPicker npm =  findViewById(R.id.numberPickerMinute);
        int minutes;
        minutes = 60 * nph.getValue();
        minutes = minutes + npm.getValue();
        TextView tv = findViewById(R.id.textView);
        tv.setText("The time is: " + minutes);
        //setResult(RESULT_OK);
    }

}
