package com.greenerlawn.greenerlawn;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 10/28/2017.
 */

public class ZoneSettingsRecyclerAdapter extends RecyclerView.Adapter<ZoneSettingsRecyclerAdapter.ViewHolder> {
    private final Context adapterContext;
    private final LayoutInflater zoneInflater;
    private final ArrayList<Zone> zoneList;



    public ZoneSettingsRecyclerAdapter(Context context, ArrayList<Zone> zList) {
        adapterContext = context;
        zoneList = zList;
        zoneInflater = LayoutInflater.from(adapterContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // creates the views or cards that show up when the activity is launched
        View zoneItemView = zoneInflater.inflate(R.layout.zone_setting_list_item, parent,false);
        return new ViewHolder(zoneItemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Zone zoneInstance = zoneList.get(position);
        holder.zoneTitle_tv.setText(zoneInstance.getzName());
        holder.zoneStatus_sw.setChecked(zoneInstance.iszOnOff());
    }

    @Override
    public int getItemCount() {
        return zoneList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView zoneTitle_tv;
        public final Switch zoneStatus_sw;
        public final ImageButton zoneImage_ib;

        public ViewHolder(View itemView) {
            super(itemView);
            zoneTitle_tv = (TextView) itemView.findViewById(R.id.zoneItemTitle_TextView);
            zoneStatus_sw = (Switch) itemView.findViewById(R.id.zoneItem_Switch);
            zoneImage_ib = (ImageButton) itemView.findViewById(R.id.zoneITem_ImageBtn);

        }
    }

}
