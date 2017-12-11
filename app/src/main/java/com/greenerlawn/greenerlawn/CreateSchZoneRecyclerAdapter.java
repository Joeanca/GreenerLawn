package com.greenerlawn.greenerlawn;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
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
    public void onBindViewHolder(final CreateSchZoneRecyclerAdapter.ViewHolder holder, int position) {
        final ScheduleZoneSelect.SchedZoneItem selectInstance = zoneSelects.get(position);
        RadioButton radBTN = holder.select;
        radBTN.setChecked(zoneSelects.get(position).isSelected());
        Bitmap tempPic =  User.getInstance().zoneListGet().get(position).getzImage();
        if (tempPic!=null) {
            RoundedBitmapDrawable dr =
                    RoundedBitmapDrawableFactory.create(context.getResources(), tempPic);
            dr.setCornerRadius(Math.max(tempPic.getWidth(), tempPic.getHeight()) / 2.0f);
            holder.picture.setBackground(dr);

        }
        holder.title.setText(selectInstance.getName());
        holder.select.setChecked(selectInstance.isSelected());
        View.OnClickListener zoneSelectListener = new View.OnClickListener() {
            public void onClick(View v) {
                if (holder.select.isChecked()){
                    Log.e("SCHZONERECYCLER line 59", "onClick: " + holder.select.isChecked() );

                    for (int i = 0; i <ScheduleZoneSelect.zoneNameList.size();i++ ){
                        if (ScheduleZoneSelect.zoneNameList.get(i).equals(holder.title.getText().toString())){
                            ScheduleZoneSelect.zoneNameList.remove(i);
                            Log.e("SCHZONERECYCLER line 62", "onClick: " + holder.select.isChecked() );
                        }
                    }
                }
                else{ScheduleZoneSelect.zoneNameList.add(holder.title.getText().toString());}

            }
        };
        holder.select.setOnClickListener(zoneSelectListener);
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
