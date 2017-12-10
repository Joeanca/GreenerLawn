package com.greenerlawn.greenerlawn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScheduleZoneSelect extends AppCompatActivity {
    public static final String ZONE_CHOICE = "Zone Choice";
    private List<Zone> zoneList;
    private ArrayList<String> zoneNameList = new ArrayList<>();
    private ArrayList<SchedZoneItem> zoneSelectList = new ArrayList<SchedZoneItem>();
    private final boolean NOT_SELECTED = false;
    private final RecyclerView selectRecycler = (RecyclerView) findViewById(R.id.zoneSelect_Recycler);
    private boolean submit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        zoneList = User.getInstance().zoneListGet();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_zone_select_activity);

        final LinearLayoutManager zoneSelLayMan = new LinearLayoutManager(this);
        selectRecycler.setLayoutManager(zoneSelLayMan);
        fillZoneSelectList();
        final CreateSchZoneRecyclerAdapter createSchZoneRecyclerAdapter = new CreateSchZoneRecyclerAdapter(zoneSelectList, this);
        selectRecycler.setAdapter(createSchZoneRecyclerAdapter);

    }

    public void setSubmit(View v){
        submit = true;
        finish();
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        if (submit == true) {
            for (int i = 0; i < selectRecycler.getChildCount(); i++) {
                zoneSelectList.get(i).setSelected(selectRecycler.getChildAt(i).isSelected());
            }
            for (int i =0; i < zoneSelectList.size(); i++){
                if(zoneSelectList.get(i).isSelected()){
                    zoneNameList.add(zoneSelectList.get(i).getName());
                }
            }
            data.putExtra(ZONE_CHOICE,zoneNameList);
            setResult(RESULT_OK, data);
        }else{setResult(RESULT_CANCELED, data);}

        super.finish();
    }

    //todo get image properly
    private void fillZoneSelectList() {
        for (Zone z : zoneList) {
            SchedZoneItem newSZS = new SchedZoneItem(z.getzImage(), z.getzName(), NOT_SELECTED);
            zoneSelectList.add(newSZS);
        }
    }

    public class SchedZoneItem {
        private File zImage;
        private String name;
        private boolean selected;

        public SchedZoneItem(File zImage, String name, boolean selected) {
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
