package com.greenerlawn.greenerlawn;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Instrumentation;
import android.arch.lifecycle.HolderFragment;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


import static android.support.v4.app.ActivityCompat.startActivityForResult;
import static java.security.AccessController.getContext;


/**
 * Created by jason on 10/28/2017.
 */

public class ZoneSettingsRecyclerAdapter extends RecyclerView.Adapter<ZoneSettingsRecyclerAdapter.ViewHolder> {
    private final LayoutInflater zoneInflater;
    private final List<Zone> zoneList;
    private DatabaseFunctions db = new DatabaseFunctions();
    private Activity context;
    private static final int GALLERY_INTENT = 2;

    public ZoneSettingsRecyclerAdapter(Activity context, List<Zone> zList) {
        this.context = context;
        zoneList = zList;
        zoneInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // creates the views or cards that show up when the activity is launched
        View zoneItemView = zoneInflater.inflate(R.layout.zone_setting_list_item, parent,false);
        return new ViewHolder(zoneItemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Zone zoneInstance = zoneList.get(position);
        ImageButton imgBtn = holder.zoneItem_ImageBtn;
        holder.zoneTitle_tv.setText(zoneInstance.getZoneNumber());
        holder.zoneStatus_sw.setChecked(zoneInstance.getzOnOff());
        holder.zoneStatus_sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                User.getInstance().zoneListGet().get(Integer.parseInt(zoneInstance.getZoneNumber())-1).setzOnOff(b);
               db.SwitchToggleZone(Integer.parseInt(zoneInstance.getZoneNumber())-1, b);
                //Log.e("HOLDER", "onCheckedChanged: "+ (Integer.parseInt(zoneInstance.getZoneNumber())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return zoneList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //pulls the titles from the zones to use as a reference
        //TODO add fields to pull picture and status for zone
        public final TextView zoneTitle_tv;
        public final Switch zoneStatus_sw;
        public final ImageButton zoneItem_ImageBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            zoneTitle_tv = (TextView) itemView.findViewById(R.id.zoneItemTitle_TextView);
            zoneStatus_sw = (Switch) itemView.findViewById(R.id.zoneItem_Switch);
            zoneItem_ImageBtn = (ImageButton) itemView.findViewById(R.id.zoneItem_ImageBtn);
            zoneItem_ImageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Log.e("THE IMAGE CLICK", "onClick: " + view.getId() + getLayoutPosition());
                    db.getImage(getLayoutPosition());
                    Intent galleryIntent = new Intent();
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    galleryIntent.putExtra("zoneNumber",""+ getLayoutPosition() );
                    galleryIntent.setType("image/*");

                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra("zoneNumber",""+ getLayoutPosition() );
                    Intent chooser = new Intent(Intent.ACTION_CHOOSER);
                    chooser.putExtra(Intent.EXTRA_INTENT, galleryIntent);
                    chooser.putExtra(Intent.EXTRA_TITLE, "Select from:");
                    chooser.putExtra("zoneNumber",""+ getLayoutPosition() );

                    Intent zoneIntent = new Intent();
                    zoneIntent.putExtra("ZONEID", getLayoutPosition());

                    Intent[] intentArray = { cameraIntent};
                    chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                    chooser.putExtra("EXTRA_ID", getLayoutPosition());
                    context.startActivityForResult(chooser, GALLERY_INTENT);
                    context.setResult(GALLERY_INTENT , zoneIntent );
                }
            });
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            // TODO IMPLEMENT THE CHANGE TEXT POP UP BOX FOR THE NAME CHANGE
            // THIS IS THE MAIN LISTENER FOR THE EMPTY SPACE BETWEEN THE IMAGE AND THE SWITCH SO YOU CAN CALL THE SCHEDULE FOR THE ZONE
            Log.e("IMAGE ON CLICK", "onClick: " + position );
            Toast.makeText(context, "Clicked Position"+position, Toast.LENGTH_SHORT).show();
        }
    }

 }



