package com.greenerlawn.greenerlawn;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
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
    private String[] dayTextArr = new String[]{"", "Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat"};
    static final int START_TIME_PICK = 1;
    static final int  DURATION_TIME_PICK = 2;
    static final int ZONE_SELECT = 3;
    private long second = 1000;
    private long minute = 60*second;
    private long hour = 60*minute;
    private int startHour = -1, startMinute = -1, durHour = -1, durMinute = -1;
    private long longSH, longsM, longDH, longDM, longST, longDur;
    private boolean durFlag = false, startTimeFlag = false, zonesFlag = false, nameFlag = false;
    private boolean repeat = false;
    EditText et;


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
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        //something in API 23 doesn't like this....
        setContentView(R.layout.activity_create_schedule);

        // TO GET THE BACK ARROW ON THE ACTION BAR
        int transparent = ContextCompat.getColor(this, R.color.transparent);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(transparent));
        getSupportActionBar().setTitle("Create Schedule");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setRepeat(View v){
        if (repeat)repeat = false;
        else repeat = true;

        Log.e("61", "setRepeat: "+ repeat );
    }

    public void selectDay(View v) {
        // i know it's a button, i don't know which button
        Button dayBTN = (Button) v;

        int dayValue = 0;
        for (int i = 0; i < dayTextArr.length; i++) {
            if (dayBTN.getText().equals(dayTextArr[i])) {
                dayValue = i;
                Log.e("64", "selectDay: "+dayTextArr[i] );
            }
        }
        if (dayArray[dayValue]) dayArray[dayValue] = false;
        else{dayArray[dayValue] = true;}

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
            zoneNameList = (ArrayList<String>) data.getSerializableExtra("Zone Choice");
            zonesFlag = true;
        }
    }

    public void submitSchedule(View view) {

        et =  findViewById(R.id.AschName_ET);

        if (!et.getText().equals("")){
            nameFlag = true;

        }
        Log.e("CreateSchedule", "submitSchedule: dur " + durFlag + "name "+nameFlag+ "zone" +zonesFlag +"start" + startTimeFlag );
        if (durFlag && startTimeFlag && zonesFlag && nameFlag){
            for (int i =0; i <dayArray.length; i++){
                if (dayArray[i]){
                    submitDayAL.add(i);
                }
            }
            String name = et.getText().toString();
            int timeFlag = 0;
            ScheduleManager sm = new ScheduleManager();
            sm.configureSchedule(name,zoneNameList,longST,longDur,timeFlag,submitDayAL,repeat);
        }
        //finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();    //Call the back button's method
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition( R.anim.animation_enter, R.anim.animation_leave);
    }

}



