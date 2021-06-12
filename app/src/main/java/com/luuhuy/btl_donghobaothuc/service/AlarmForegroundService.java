package com.luuhuy.btl_donghobaothuc.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.luuhuy.btl_donghobaothuc.MainActivity;
import com.luuhuy.btl_donghobaothuc.R;
import com.luuhuy.btl_donghobaothuc.adapter.ItemTime;
import com.luuhuy.btl_donghobaothuc.receiver.AlarmReceiver;

import java.util.Calendar;

import static com.luuhuy.btl_donghobaothuc.service.NotifyService.CHANNEL_ID;

public class AlarmForegroundService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getBundleExtra("bundle");
        ItemTime time = (ItemTime) bundle.getSerializable("item");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Báo thức đã được đặt lúc: ")
                .setContentText(time.getTimer())
                .setSmallIcon(R.drawable.ic_alarms)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        startForeground(1, notification);

//        String timer = time.getTimer();
//        String [] temp = timer.split(":");
//        int hourRe = Integer.parseInt(temp[0]);
//        int minuteRe = Integer.parseInt(temp[1]);
//        Calendar c = Calendar.getInstance();
//        c.set(Calendar.HOUR_OF_DAY, hourRe);
//        c.set(Calendar.MINUTE, minuteRe);
//        c.set(Calendar.SECOND, 0);
//
//        // hẹn hôm sau mới báo
//        if (c.before(Calendar.getInstance())){
//            c.add(Calendar.DATE, 1);
//        }
//        Intent intent1 = new Intent(this, AlarmReceiver.class);
//        Bundle bundle1 = new Bundle();
//        bundle1.putSerializable("item", time);
//        intent1.putExtra("bundle", bundle1);
//        Log.e(time.getTimer(), time.toString());
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(
//                this, time.getId() , intent1, PendingIntent.FLAG_CANCEL_CURRENT
//        );
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent1);



        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
