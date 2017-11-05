package com.greenerlawn.greenerlawn;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jason on 10/28/2017.
 */

public class ZoneSettingsRecyclerAdapter extends RecyclerView.Adapter<ZoneSettingsRecyclerAdapter.ViewHolder> {
    private final Context adapterContext;
    private final LayoutInflater zoneInflater;
    private final List<Zone> zoneList



    public ZoneSettingsRecyclerAdapter(Context context, List<Zone> zList) {
        adapterContext = context;
        zoneList = zList;
        zoneInflater = LayoutInflater.from(adapterContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // creates the views or cards that show up when the activity is launched
        View zoneItemView = zoneInflater.inflate(R.layout.zone_setting_list_item, parent, false);
        return new ViewHolder(zoneItemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Zone zoneInstance = zoneList.get(position);
        holder.zoneTitle_tv.setText(zoneInstance.getzName());

    }

    @Override
    public int getItemCount() {
        return zoneList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //pulls the titles from the zones to use as a reference
        //TODO add fields to pull picture and status for zone
        public final TextView zoneTitle_tv;

        public ViewHolder(View itemView) {
            super(itemView);
            zoneTitle_tv = (TextView) itemView.findViewById(R.id.zoneItemTitle_TextView);

        }
    }

}
