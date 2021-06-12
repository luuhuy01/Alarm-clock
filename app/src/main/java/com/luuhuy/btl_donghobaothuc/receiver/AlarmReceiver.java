package com.luuhuy.btl_donghobaothuc.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.luuhuy.btl_donghobaothuc.adapter.ItemTime;
import com.luuhuy.btl_donghobaothuc.service.NotifyService;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
//        ItemTime itemTime = (ItemTime) intent.getExtras().get("itemTimeNoti");
        Bundle bundle = intent.getBundleExtra("bundle");
        ItemTime itemTime = (ItemTime) bundle.get("item");

        Intent intentNotify = new Intent(context, NotifyService.class);
        intentNotify.putExtra("bundle", bundle);

        Log.e("item timer", itemTime.toString());
        context.startService(intentNotify);
//        ContextCompat.startForegroundService(context, intentNotify);
    }

}
