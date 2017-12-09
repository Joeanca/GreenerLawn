package com.greenerlawn.greenerlawn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScheduleZoneSelect extends AppCompatActivity {
    private List<Zone> zoneList;
    private ArrayList<SchedZoneSelect> zoneSelectList = new ArrayList<SchedZoneSelect>();
    private final boolean NOT_SELECTED = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        zoneList = User.getInstance().zoneListGet();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_zone_select_activity);
    }

    @Override
    public void finish() {
        super.finish();
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
