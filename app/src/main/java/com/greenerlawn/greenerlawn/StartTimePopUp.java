package com.greenerlawn.greenerlawn;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.Calendar;

public class StartTimePopUp extends Activity {
    int h = -1, m = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_time_pop_up);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        double heightPercentage = .65;
        double widthPercentage = .8;

        getWindow().setLayout((int)(width*widthPercentage), (int)(height*heightPercentage));


        configTimePicker();
    }

    private void configTimePicker() {
        TimePicker stp = findViewById(R.id.schStartTime_PCKR);
        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int currentMinute = Calendar.getInstance().get(Calendar.MINUTE);
        if (Build.VERSION.SDK_INT >=23) {
            stp.setHour(currentHour);
            stp.setMinute(currentMinute);
        } else{
            stp.setCurrentHour(currentHour);
            stp.setCurrentMinute(currentMinute);
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void submitResult(View v){
        TimePicker stp = findViewById(R.id.schStartTime_PCKR);

        if(Build.VERSION.SDK_INT >= 23){
            h = stp.getHour();
            m = stp.getMinute();
        }else{
            h = stp.getCurrentHour();
            m = stp.getCurrentMinute();
        }
        finish();
    }

    public void cancelPick(View v){finish();}

    @Override
    public void finish() {
        Intent data = new Intent();
        if(h != -1 && m != -1){
            data.putExtra("hour", h);
            data.putExtra("minute", m);
            setResult(RESULT_OK, data);
        }else{setResult(RESULT_CANCELED, data);}
        super.finish();
    }

}

