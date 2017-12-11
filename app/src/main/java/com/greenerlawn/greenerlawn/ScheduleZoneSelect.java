package com.greenerlawn.greenerlawn;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScheduleZoneSelect extends AppCompatActivity {
    public static final String ZONE_CHOICE = "Zone Choice";
    // list of zones from user
    private List<Zone> zoneList;
    static ArrayList<String> zoneNameList = new ArrayList<>();
    //list of objects this activity uses to fill recycler
    private ArrayList<SchedZoneItem> zoneSelectList = new ArrayList<SchedZoneItem>();
    private final boolean NOT_SELECTED = false;

    private boolean submit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        zoneList = User.getInstance().zoneListGet();
        Log.e("33", "onCreate: "+User.getInstance().zoneListGet().get(1).getzName() );
        super.onCreate(savedInstanceState);
//        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.schedule_zone_select_activity);

        // TO GET THE BACK ARROW ON THE ACTION BAR
        int transparent = ContextCompat.getColor(this, R.color.transparent);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(transparent));
        getSupportActionBar().setTitle("Choose zones to schedule");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final RecyclerView selectRecycler = (RecyclerView) findViewById(R.id.zoneSelect_Recycler);
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
//            for (int i = 0; i < selectRecycler.getChildCount(); i++) {
//                zoneSelectList.get(i).setSelected(selectRecycler.getChildAt(i).isSelected());
//            }
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


    private void fillZoneSelectList() {
        for (Zone z : zoneList) {
            //SchedZoneItem newSZS = new SchedZoneItem(z.getzImage(), z.getzName(), NOT_SELECTED);
            SchedZoneItem newSZS = new SchedZoneItem( z.getzName(), NOT_SELECTED);
            zoneSelectList.add(newSZS);
            Log.e("74", "fillZoneSelectList: "+ z.getzName() );
        }

    }

    public ArrayList<String> getZoneNameList() {
        return zoneNameList;
    }

    public void setZoneNameList(ArrayList<String> zoneNameList) {
        this.zoneNameList = zoneNameList;
    }

    public class SchedZoneItem {

        private String name;
        private boolean selected;

        public SchedZoneItem(String name, boolean selected) {

            this.name = name;
            this.selected = selected;
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
//        overridePendingTransition( R.anim.animation_enter, R.anim.animation_leave);
    }
}
