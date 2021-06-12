package com.luuhuy.btl_donghobaothuc;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.luuhuy.btl_donghobaothuc.adapter.ItemTime;
import com.luuhuy.btl_donghobaothuc.receiver.AlarmReceiver;
import com.luuhuy.btl_donghobaothuc.service.AlarmForegroundService;
import com.luuhuy.btl_donghobaothuc.sql.TimerSQLiteOpenHelpper;

import java.util.Calendar;

public class AddAlarmActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 10;
    public static final int RESULT_TITLE_CODE = 9;
    public static final int RESULT_MUSIC_CODE = 8;

    private Button btnHuy, btnLuu;
    private TimePicker timePicker;
    private TextView txtTenbaothuc, txtLaplai, txtAmbao;
    private SwitchCompat swBaolai;

    private ItemTime itemTime;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Calendar calendar;
    private TimerSQLiteOpenHelpper timerSQL;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);
        // khoi tao doi tuong trong view
        btnHuy = findViewById(R.id.btnHuy);
        btnLuu = findViewById(R.id.btnLuu);
        timePicker = findViewById(R.id.tpTime);
        timePicker.setIs24HourView(true);
        txtTenbaothuc = findViewById(R.id.txtTenBaoThuc);
        txtAmbao = findViewById(R.id.txtAmBao);
        txtLaplai = findViewById(R.id.txtLaplai);
        swBaolai = findViewById(R.id.swBaoLai);
        // khoi tao doi tuong ho tro
        calendar = Calendar.getInstance();
        timerSQL = new TimerSQLiteOpenHelpper(this);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
            }
        });


        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);;
                String timer = "";
                if(minute < 10) {
                    timer = hour + ":0" + minute;
                }else{
                    timer = hour + ":" + minute;
                }

                String title = txtTenbaothuc.getText().toString();
                String nameMusic = txtAmbao.getText().toString();
                String noti_repeat [] = txtLaplai.getText().toString().split(",");
                Boolean repeat = swBaolai.isChecked();
                itemTime = new ItemTime(timer, title, nameMusic, noti_repeat, repeat, true);
                int id = (int) timerSQL.addTimer(itemTime);
                itemTime.setId(id);

                // hẹn hôm sau mới báo
                if (calendar.before(Calendar.getInstance())){
                    calendar.add(Calendar.DATE, 1);
                }
                Log.e("itemTime", itemTime.toString());
                intent = new Intent(AddAlarmActivity.this, AlarmReceiver.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("item", itemTime);
                intent.putExtra("bundle",bundle);

                pendingIntent = PendingIntent.getBroadcast(
                        AddAlarmActivity.this, id , intent, PendingIntent.FLAG_CANCEL_CURRENT
                );

                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                Intent servieItent = new Intent(v.getContext(), AlarmForegroundService.class);
                servieItent.putExtra("bundle",bundle);
                ContextCompat.startForegroundService(v.getContext(), servieItent);

                Intent intentToActivity = new Intent(AddAlarmActivity.this, MainActivity.class);
                startActivity(intentToActivity);
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMain = new Intent(v.getContext(), MainActivity.class);
                startActivity(intentMain);
            }
        });

        txtTenbaothuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTitle = new Intent(v.getContext(), EditTitleActivity.class);
                String title = txtTenbaothuc.getText().toString();
                intentTitle.putExtra("title", title);
                startActivityForResult(intentTitle, REQUEST_CODE);
            }
        });

        txtAmbao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTitle = new Intent(v.getContext(), MusicActivity.class);
//                String nameMusic = txtAmbao.getText().toString();
//                intentTitle.putExtra("nameMusic", nameMusic);
                startActivityForResult(intentTitle, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE){
            if (resultCode == RESULT_TITLE_CODE){
                String name = data.getStringExtra("ResultTitle");
                txtTenbaothuc.setText(name);
            }else if (requestCode == RESULT_MUSIC_CODE){
                String music = data.getStringExtra("nameMusic");
                txtAmbao.setText(music);
            }
        }
    }
}