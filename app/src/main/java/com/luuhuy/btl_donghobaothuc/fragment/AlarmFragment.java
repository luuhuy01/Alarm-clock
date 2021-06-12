package com.luuhuy.btl_donghobaothuc.fragment;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.luuhuy.btl_donghobaothuc.AddAlarmActivity;
import com.luuhuy.btl_donghobaothuc.EditAlarmActivity;
import com.luuhuy.btl_donghobaothuc.MainActivity;
import com.luuhuy.btl_donghobaothuc.R;
import com.luuhuy.btl_donghobaothuc.adapter.ItemTime;
import com.luuhuy.btl_donghobaothuc.adapter.OnItemClickListener;
import com.luuhuy.btl_donghobaothuc.adapter.TimeAdapter;

import com.luuhuy.btl_donghobaothuc.receiver.AlarmReceiver;
import com.luuhuy.btl_donghobaothuc.service.AlarmForegroundService;
import com.luuhuy.btl_donghobaothuc.sql.TimerSQLiteOpenHelpper;


import java.util.Calendar;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;


public class AlarmFragment extends Fragment implements OnItemClickListener {

    private List<ItemTime> timeList;
    private TimeAdapter timeAdapter;
    private RecyclerView listAlarm;
    private FloatingActionButton btnFloatAction;
    private TimerSQLiteOpenHelpper timerSQL;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private  Intent intent;

    public AlarmFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_alarm, container, false);
        this.listAlarm = v.findViewById(R.id.listAlarm);

        timerSQL = new TimerSQLiteOpenHelpper(v.getContext());
        this.timeList = timerSQL.getAllTimer();
        Log.e("db" , timeList.toString());
        this.timeAdapter = new TimeAdapter(v.getContext(), this);
        timeAdapter.setData(timeList);
        LinearLayoutManager manager = new LinearLayoutManager(v.getContext(), RecyclerView.VERTICAL, false);
        listAlarm.setLayoutManager(manager);
        listAlarm.setAdapter(timeAdapter);

        alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        intent= new Intent(getContext(), AlarmReceiver.class);

        this.btnFloatAction = v.findViewById(R.id.floatingActionButton);
        btnFloatAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddAlarmActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }

    @Override
    public void onItemClick(ItemTime itemTime) {
        Intent intent = new Intent(getContext(), EditAlarmActivity.class);
        intent.putExtra("dataItemTime", itemTime);

        startActivity(intent);
    }

    @Override
    public void onItemLongClick(ItemTime itemTime, int position) {

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Xoá báo thức")
                .setMessage("Bạn có chắc chắn muốn xoá báo thức không?")
                .setPositiveButton("Xoá", null)
                .setNegativeButton("Huỷ", null)
                .show();

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerSQL.deleteTimer(itemTime.getId());
                timeList.remove(itemTime);
                timeAdapter.notifyDataSetChanged();
                cancelAlarm(itemTime);
                dialog.dismiss();
            }
        });
    }

    @Override
    public void switchItem(ItemTime itemTime) {
  //      intent= new Intent(getContext(), AlarmReceiver.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("item", itemTime);
        intent.putExtra("bundle",bundle);

        if(itemTime.isChecked()){
            // báo thức mở
            timerSQL.turnSwitchTimer(itemTime);
            startAlarm(itemTime);
        }else{
            timerSQL.turnSwitchTimer(itemTime);
            cancelAlarm(itemTime);
        }
    }

    private void startAlarm(ItemTime time) {
        String timer = time.getTimer();
        String [] temp = timer.split(":");
        int hourRe = Integer.parseInt(temp[0]);
        int minuteRe = Integer.parseInt(temp[1]);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourRe);
        c.set(Calendar.MINUTE, minuteRe);
        c.set(Calendar.SECOND, 0);

        // hẹn hôm sau mới báo
        if (c.before(Calendar.getInstance())){
            c.add(Calendar.DATE, 1);
        }
        pendingIntent = PendingIntent.getBroadcast(
                getContext(), time.getId() , intent, PendingIntent.FLAG_CANCEL_CURRENT
        );
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);

        Bundle bundle = new Bundle();
        bundle.putSerializable("item", time);


        Intent servieItent = new Intent(getContext(), AlarmForegroundService.class);
        servieItent.putExtra("bundle",bundle);
        ContextCompat.startForegroundService(getContext(), servieItent);
    }

    private void cancelAlarm(ItemTime itemTime){
        pendingIntent =  PendingIntent.getBroadcast(getContext(), itemTime.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
        pendingIntent.cancel();
        alarmManager.cancel(pendingIntent);
    }
}