package com.greenerlawn.greenerlawn;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.app.ActivityCompat.startActivityForResult;


/**
 * Created by jason on 10/28/2017.
 */

public class ZoneSettingsRecyclerAdapter extends RecyclerView.Adapter<ZoneSettingsRecyclerAdapter.ViewHolder> {
    private final LayoutInflater zoneInflater;
    private final List<Zone> zoneList;
    private DatabaseFunctions db = new DatabaseFunctions();
    private static final int GALLERY_INTENT = 2;
    private Activity context;


    public ZoneSettingsRecyclerAdapter(Activity context, List<Zone> zList) {
//        adapterContext = context;
        this.context = context;
        zoneList = zList;
        zoneInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // creates the views or cards that show up when the activity is launched
        View zoneItemView = zoneInflater.inflate(R.layout.zone_setting_list_item, parent, false);
        return new ViewHolder(zoneItemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Zone zoneInstance = zoneList.get(position);
        holder.zoneTitle_tv.setText(zoneInstance.getZoneNumber());
        holder.zoneStatus_sw.setChecked(zoneInstance.getzOnOff());
        holder.zoneStatus_sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                db.SwitchToggleZone(Integer.parseInt(zoneInstance.getZoneNumber()), b);
                Log.e("HOLDER", "onCheckedChanged: " + zoneInstance.getZoneNumber());
            }
        });
        holder.zoneItem_ImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setType("image/*");
                ((Activity) context).startActivityForResult(galleryIntent, GALLERY_INTENT);
            }
        });

    }

    @Override
    public int getItemCount() {
        return zoneList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        //pulls the titles from the zones to use as a reference
        //TODO add fields to pull picture and status for zone
        public final TextView zoneTitle_tv;
        public final Switch zoneStatus_sw;
        public ImageButton zoneItem_ImageBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            zoneTitle_tv = (TextView) itemView.findViewById(R.id.zoneItemTitle_TextView);
            zoneStatus_sw = (Switch) itemView.findViewById(R.id.zoneItem_Switch);
            zoneItem_ImageBtn = (ImageButton) itemView.findViewById(R.id.zoneItem_ImageBtn);
        }
    }
}

