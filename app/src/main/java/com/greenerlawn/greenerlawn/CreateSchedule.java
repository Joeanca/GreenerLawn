package com.greenerlawn.greenerlawn;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CreateSchedule extends AppCompatActivity {
    private List<Zone> zoneList;
    private ArrayList<SchedZoneSelect> zoneSelectList = new ArrayList<SchedZoneSelect>();
    private final boolean NOT_SELECTED = false;
    private boolean[] dayArray = new boolean[8];
    private String[] dayTextArr = new String[]{"All", "Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat"};

    //todo pull images properly from DB functions
    //todo pop up for timepicker
    //todo submit FAB
    //todo
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        zoneList = User.getInstance().zoneListGet();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_schedule);
        fillZoneSelectList();
        final RecyclerView selectRecycler = (RecyclerView) findViewById(R.id.zoneSelect_Recycler);
        final LinearLayoutManager zoneSelLayMan = new LinearLayoutManager(this);
        selectRecycler.setLayoutManager(zoneSelLayMan);

        CreateSchZoneRecyclerAdapter c = new CreateSchZoneRecyclerAdapter(zoneSelectList,this);
        selectRecycler.setAdapter(c);
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

    private void fillZoneSelectList() {
        for (Zone z : zoneList) {
            SchedZoneSelect newSZS = new SchedZoneSelect(z.getzImage(), z.getzName(), NOT_SELECTED);
            zoneSelectList.add(newSZS);
        }
    }

    public class SchedZoneSelect {
        private File zImage;
        private String name;
        private boolean selected;

        public SchedZoneSelect(File zImage, String name, boolean selected) {
            this.zImage = zImage;
            this.name = name;
            this.selected = selected;
        }

        public File getzImage() {
            return zImage;
        }

        public void setzImage(File zImage) {
            this.zImage = zImage;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }

}



