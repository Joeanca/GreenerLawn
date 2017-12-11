package com.greenerlawn.greenerlawn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

/**
 * Created by Austin on 2017-10-28.
 */

public class DurationPopUp extends Activity{

    private int durHour =-1, durMin = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.duration_popup_activity);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        double heightPercentage = .5;
        double widthPercentage = .8;

        getWindow().setLayout((int)(width*widthPercentage), (int)(height*heightPercentage));

        //Define the our picker
        NumberPicker nphDur = findViewById(R.id.numberPickerHourDuration);
        nphDur.setMinValue(0);
        nphDur.setMaxValue(24);
        nphDur.setWrapSelectorWheel(true);

        //Define minute picker
        NumberPicker npmDur = findViewById(R.id.numberPickerMinuteDuration);
        npmDur.setMinValue(0);
        npmDur.setMaxValue(59);
        npmDur.setWrapSelectorWheel(true);

    }

    public void submitResult(View view) {
        NumberPicker nphDur = findViewById(R.id.numberPickerHourDuration);
        NumberPicker npmDur =  findViewById(R.id.numberPickerMinuteDuration);
            if(nphDur.getValue() != 0 || npmDur.getValue() !=0){
        durHour = nphDur.getValue();
        durMin = npmDur.getValue();
            }

    finish();
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        if( durHour != -1 && durMin != -1){
            data.putExtra("durHour", durHour);
            data.putExtra("durMinute", durMin);
            setResult(RESULT_OK, data);
        }else{setResult(RESULT_CANCELED, data);}
        super.finish();

    }
}
