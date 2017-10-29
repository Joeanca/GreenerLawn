package com.greenerlawn.greenerlawn;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by jason on 10/28/2017.
 */

public class ZoneSettingsRecyclerAdapter extends RecyclerView.Adapter<ZoneSettingsRecyclerAdapter.ViewHolder> {
    private final Context adapterContext;
    private final LayoutInflater zoneInflater;



    public ZoneSettingsRecyclerAdapter(Context context, List<Zones> zList) {
        adapterContext = context;
        zoneInflater = LayoutInflater.from(adapterContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
