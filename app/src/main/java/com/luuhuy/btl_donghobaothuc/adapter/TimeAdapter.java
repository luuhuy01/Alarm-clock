package com.luuhuy.btl_donghobaothuc.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.luuhuy.btl_donghobaothuc.R;
import com.luuhuy.btl_donghobaothuc.sql.TimerSQLiteOpenHelpper;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;


public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.TimeViewHolder>{

    private Context context;
    private List<ItemTime> timeList;
    private OnItemClickListener listener;

    public List<ItemTime> getTimeList() {
        return timeList;
    }

    public void setTimeList(List<ItemTime> timeList) {
        this.timeList = timeList;
    }

    public TimeAdapter(Context context, OnItemClickListener listener) {
        this.context = context;
        this.timeList = timeList;
        this.listener = listener;
    }

    public TimeAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<ItemTime> itemTimes){
        this.timeList = itemTimes;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public TimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_card, parent, false);

        return new TimeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeViewHolder holder, int position) {
        ItemTime itemTime = timeList.get(position);
        if(itemTime != null){
            holder.txtTime.setText(itemTime.getTimer());
            holder.txtTitle.setText(itemTime.getTitle());
            holder.checked.setChecked(itemTime.isChecked());
            holder.checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    itemTime.setChecked(isChecked);
                    listener.switchItem(itemTime);
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(itemTime);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onItemLongClick(itemTime, position);
                    return true;
                }
            });
        }

    }

    public void add(ItemTime itemTime){
        timeList.add(itemTime);
        notifyDataSetChanged();
    }

//    public void remove(int position){
//        timeList.remove(position);
//        notifyItemRemoved(position);
//    }


    @Override
    public int getItemCount() {
        if(timeList != null){
            return timeList.size();
        }
        return 0;
    }

    public static class TimeViewHolder extends RecyclerView.ViewHolder {

        private TextView txtTime;
        private TextView txtTitle;
        private SwitchCompat checked;

        public TimeViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            checked = itemView.findViewById(R.id.swCheck);

        }
    }
}
