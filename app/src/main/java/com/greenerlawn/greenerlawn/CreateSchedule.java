package com.greenerlawn.greenerlawn;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import java.util.ArrayList;

public class CreateSchedule extends AppCompatActivity {

    private ArrayList<ScheduleZoneSelect.SchedZoneItem> zoneSelectList = new ArrayList<ScheduleZoneSelect.SchedZoneItem>();
    private ArrayList<String> zoneNameList = new ArrayList<String>();
    private boolean[] dayArray = new boolean[8];
    private ArrayList<Integer> submitDayAL = new ArrayList<Integer>(1);
    private String[] dayTextArr = new String[]{"All", "Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat"};
    static final int START_TIME_PICK = 1;
    static final int  DURATION_TIME_PICK = 2;
    static final int ZONE_SELECT = 3;
    private long second = 1000;
    private long minute = 60*second;
    private long hour = 60*minute;
    private int startHour = -1, startMinute = -1, durHour = -1, durMinute = -1;
    private long longSH, longsM, longDH, longDM, longST, longDur;
    private boolean durFlag = false, startTimeFlag = false, zonesFlag = false, nameFlag = false;


    //todo pull images properly from DB functions
    //todo submit FAB
    //todo submit buttons close activity
    //todo start time pop up
    //todo day button stay pressed
    // todo test create schedule
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
            longSH = startHour * hour;
            longsM = startMinute * minute;
            longST = longSH + longsM;
            startTimeFlag = true;
        }
        if(requestCode == DURATION_TIME_PICK && resultCode == RESULT_OK){
            durHour = data.getIntExtra("durHour", -1);
            durMinute = data.getIntExtra("durMinute", -1);
            longDH = durHour * hour;
            longDM = durMinute * minute;
            longDur = longDH +longDM;
            durFlag = true;
        }
        if (requestCode == ZONE_SELECT && resultCode == RESULT_OK){
            String key = ScheduleZoneSelect.ZONE_CHOICE;
            zoneNameList = (ArrayList<String>) data.getSerializableExtra(key);
            zonesFlag = true;
        }
    }

    public void submitSchedule(View view) {

        EditText nameV = (EditText) view.findViewById(R.id.AschName_ET);
        Switch repeatSW = (Switch) view.findViewById(R.id.schRepeat_SW);
        boolean repeat = repeatSW.isChecked();

        if (!nameV.getText().equals("Schedule Name")){
            nameFlag = true;
        }
        if (durFlag && startTimeFlag && zonesFlag && nameFlag){
            for (int i =0; i <dayArray.length; i++){
                if (dayArray[i]){
                    submitDayAL.add(i);
                }
            }
            int timeFlag = 0;
            ScheduleManager sm = new ScheduleManager();
            sm.configureSchedule(zoneNameList,longST,longDur,timeFlag,submitDayAL,repeat);
        }
    }
}



