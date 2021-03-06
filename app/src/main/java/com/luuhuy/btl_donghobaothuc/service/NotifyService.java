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
                .addAction(R.drawable.ic_alarms, "D???ng", pendingIntent)
                .build();

        manager.notify(1, builder);
        TimerSQLiteOpenHelpper timerSQL = new TimerSQLiteOpenHelpper(this);
        itemTime.setChecked(false);
        timerSQL.updateTimer(itemTime);

        int music = 0;
        switch (itemTime.getNameMusic()){
            case "K?? ???c":
                music = R.raw.ky_uc; break;
            case "Cu???c s???ng m??u s???c":
                music = R.raw.cuoc_song; break;
            case "S??n ch??i":
                music = R.raw.san_choi; break;
            case "??i???u nh???y ng???u h???ng":
                music = R.raw.ngau_hung; break;
            case "????? ch??i":
                music = R.raw.doi_choi; break;
            case "??om ????m":
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
