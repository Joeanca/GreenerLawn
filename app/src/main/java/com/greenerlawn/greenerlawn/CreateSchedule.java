package com.greenerlawn.greenerlawn;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class CreateSchedule extends AppCompatActivity {

    private ArrayList<ScheduleZoneSelect.SchedZoneItem> zoneSelectList = new ArrayList<ScheduleZoneSelect.SchedZoneItem>();
    private boolean[] dayArray = new boolean[8];
    private String[] dayTextArr = new String[]{"All", "Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat"};
    static final int START_TIME_PICK = 1;
    static final int  DURATION_TIME_PICK = 2;
    static final int ZONE_SELECT = 3;
    private int startHour = -1, startMinute = -1, durHour = -1, durMinute = -1;
    private boolean durFlag = false, startTimeFlag = false, zonesFlag = false, nameFlag = false;


    //todo pull images properly from DB functions
    //todo submit FAB
    //no need to create a boolean flag for repeat button, can just test if is checked
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_schedule);

    }

    public void selectDay(View v) {
        Button dayBTN = (Button) v;
        dayBTN.getText();
        int dayValue = 0;
        for (int i = 0; i < dayTextArr.length; i++) {
            if (dayBTN.getText().equals(dayTextArr[i])) {
                dayValue = i;
            }
        }
        if (!dayBTN.isPressed()) {
            dayBTN.setPressed(true);
        } else {
            dayBTN.setPressed(false);
        }
        dayArray[dayValue] = dayBTN.isPressed();
    }

    public void durationSet(View durBTN){
        Intent durationIntent = new Intent(this, DurationPopUp.class);
        startActivityForResult(durationIntent, DURATION_TIME_PICK);
    }


    public void startTimePick(View v){
        Intent startTimeInent = new Intent(this,StartTimePopUp.class);
        startActivityForResult(startTimeInent,START_TIME_PICK);

    }

    public void chooseZones(View v){
        Intent i = new Intent(this, ScheduleZoneSelect.class);
        startActivityForResult(i, ZONE_SELECT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == START_TIME_PICK && resultCode == RESULT_OK) {
            startHour = data.getIntExtra("hour", -1);
            startMinute = data.getIntExtra("minute", -1);
        }
        if(requestCode == DURATION_TIME_PICK && resultCode == RESULT_OK){
            durHour = data.getIntExtra("durHour", -1);
            durMinute = data.getIntExtra("durMinute", -1);
        }
        if (requestCode == ZONE_SELECT && resultCode == RESULT_OK){
            String key = ScheduleZoneSelect.ZONE_CHOICE;
            zoneSelectList = (ArrayList<ScheduleZoneSelect.SchedZoneItem>) data.getSerializableExtra(key);
        }
    }

    public void createSchedule(View view) {
        EditText nameV =
        if (durFlag && startTimeFlag && zonesFlag && nameFlag){

        }
    }
}



