package com.luuhuy.btl_donghobaothuc.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.luuhuy.btl_donghobaothuc.MainActivity;
import com.luuhuy.btl_donghobaothuc.R;
import com.luuhuy.btl_donghobaothuc.adapter.ItemTime;
import com.luuhuy.btl_donghobaothuc.sql.TimerSQLiteOpenHelpper;

public class NotifyService extends Service {

    public static final String CHANNEL_ID = "channel1";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getBundleExtra("bundle");
        ItemTime itemTime = (ItemTime) bundle.getSerializable("item");

        NotificationManager manager =(NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"Channel",NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Channel");
            manager.createNotificationChannel(channel);
        }
        Intent i = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_ONE_SHOT);
        Notification builder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarms)
                .setContentTitle(itemTime.getTimer())
                .setContentText(itemTime.getTitle())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_alarms, "Dừng", pendingIntent)
                .build();

        manager.notify(1, builder);
        TimerSQLiteOpenHelpper timerSQL = new TimerSQLiteOpenHelpper(this);
        itemTime.setChecked(false);
        timerSQL.updateTimer(itemTime);

        int music = 0;
        switch (itemTime.getNameMusic()){
            case "Ký ức":
                music = R.raw.ky_uc; break;
            case "Cuộc sống màu sắc":
                music = R.raw.cuoc_song; break;
            case "Sân chơi":
                music = R.raw.san_choi; break;
            case "Điệu nhảy ngẫu hứng":
                music = R.raw.ngau_hung; break;
            case "Đồ chơi":
                music = R.raw.doi_choi; break;
            case "Đom đóm":
                music = R.raw.dom_dom; break;
            default:
                music = R.raw.ky_uc; break;
        }
        MediaPlayer mediaPlayer = MediaPlayer.create(this, music);
        mediaPlayer.start();


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void stopMusic(){

    }
}
