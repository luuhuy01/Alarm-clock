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

public class EditAlarmActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_edit_alarm);

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
        Intent intentTimer = getIntent();
        ItemTime time = (ItemTime) intentTimer.getSerializableExtra("dataItemTime"); // lấy data trong intent gửi đi bên recycleview

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        String timer = time.getTimer();
        // xử lý lấy hour và minute
        String [] temp = timer.split(":");
        int hourRe = Integer.parseInt(temp[0]);
        int minuteRe = Integer.parseInt(temp[1]);
        timePicker.setHour(hourRe);
        timePicker.setMinute(minuteRe);

        Log.e("lấy thử", calendar.getFirstDayOfWeek()+"");
        calendar.clear(Calendar.HOUR_OF_DAY);
        calendar.clear(Calendar.MINUTE);
        calendar.set(Calendar.HOUR_OF_DAY, hourRe);
        calendar.set(Calendar.MINUTE, minuteRe);
        calendar.set(Calendar.SECOND, 0);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
            }
        });
        // lấy các thuộc tính khác.
        txtTenbaothuc.setText(time.getTitle());
        txtAmbao.setText(time.getNameMusic());
        txtLaplai.setText(convertString(time.getRepeat()));
        swBaolai.setChecked(time.isNotiRepeat());

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
                itemTime.setId(time.getId());
                timerSQL.updateTimer(itemTime);

                // hẹn hôm sau mới báo
                if (calendar.before(Calendar.getInstance())){
                    calendar.add(Calendar.DATE, 1);
                }

                intent = new Intent(EditAlarmActivity.this, AlarmReceiver.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("item", itemTime);
                intent.putExtra("bundle",bundle);

                pendingIntent = PendingIntent.getBroadcast(
                        EditAlarmActivity.this, time.getId() , intent, PendingIntent.FLAG_CANCEL_CURRENT
                );
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);


                Intent servieItent = new Intent(v.getContext(), AlarmForegroundService.class);
                servieItent.putExtra("bundle",bundle);
                ContextCompat.startForegroundService(v.getContext(), servieItent);
                Intent intentToActivity = new Intent(EditAlarmActivity.this, MainActivity.class);
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
                startActivityForResult(intentTitle, AddAlarmActivity.REQUEST_CODE);
            }
        });

        txtAmbao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMusic = new Intent(v.getContext(), MusicActivity.class);
//                String nameMusic = txtAmbao.getText().toString();
//                intentTitle.putExtra("nameMusic", nameMusic);
                startActivityForResult(intentMusic, AddAlarmActivity.REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == AddAlarmActivity.REQUEST_CODE){
            if (resultCode == AddAlarmActivity.RESULT_TITLE_CODE){
                String name = data.getStringExtra("ResultTitle");
                txtTenbaothuc.setText(name);
            }else if (resultCode == AddAlarmActivity.RESULT_MUSIC_CODE){
                String music = data.getStringExtra("nameMusic");
                txtAmbao.setText(music);
            }
        }
    }

    private String convertString(String [] repeat){
        int n = repeat.length;
        String result ="";
        if (n <= 0){
            return null;
        }
        for(int i=0; i< n; i++){
            result += repeat[i];
            if(i != n - 1)
                result += ",";
        }
        return result;
    }
}