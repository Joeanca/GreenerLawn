package com.greenerlawn.greenerlawn;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jason on 12/4/2017.
 */

public class CreateSchZoneRecyclerAdapter extends RecyclerView.Adapter<CreateSchZoneRecyclerAdapter.ViewHolder> {
    private final LayoutInflater zoneSelectInflater;
    private final ArrayList<ScheduleZoneSelect.SchedZoneItem> zoneSelects;
    private Activity context;

    public CreateSchZoneRecyclerAdapter(ArrayList<ScheduleZoneSelect.SchedZoneItem> zoneSelects, Activity context) {
        this.zoneSelectInflater = LayoutInflater.from(context);
        this.zoneSelects = zoneSelects;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View zoneSelectView = zoneSelectInflater.inflate(R.layout.sch_zone_select_item, parent, false);
        return new ViewHolder(zoneSelectView);
    }

    @Override
    public void onBindViewHolder(CreateSchZoneRecyclerAdapter.ViewHolder holder, int position) {
        ScheduleZoneSelect.SchedZoneItem selectInstance = zoneSelects.get(position);
        RadioButton radBTN = holder.select;
        holder.title.setText(selectInstance.getName());
        holder.select.setChecked(selectInstance.isSelected());
    }

    @Override
    public int getItemCount() {
        return zoneSelects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView picture;
        private TextView title;
        private RadioButton select;
        public ViewHolder(View itemView) {
            super(itemView);
            picture = (ImageView) itemView.findViewById(R.id.schZoneItem_IMG);
            title = (TextView) itemView.findViewById(R.id.schZoneItem_TV);
            select = (RadioButton) itemView.findViewById(R.id.schZoneItem_RB);
        }
    }
}
