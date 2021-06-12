package com.luuhuy.btl_donghobaothuc.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.TextView;

import com.luuhuy.btl_donghobaothuc.R;

import java.util.ArrayList;
import java.util.List;


public class StopwatchFragment extends Fragment {

    private TextView txtTime;
    private ListView listView;
    private Button btnStart, btnWrite;
    private List<String> listTime;
    private boolean isRunning = false;
//    private boolean isWrite = false;

    private Handler handler;
    private long tMiliSec, tStart, tPause = 0, tUpdate = 0;
    private int count = 0;
    public StopwatchFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_stopwatch, container, false);
        txtTime = v.findViewById(R.id.timeStopWatch);
        listView = v.findViewById(R.id.listTimeStop);
        btnStart = v.findViewById(R.id.btnStart);
        btnWrite = v.findViewById(R.id.btnWrite);
        listTime = new ArrayList<>();
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                String timeWrite = msg.getData().getString("time");
                txtTime.setText(timeWrite);
            }
        };

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isRunning) {
                    isRunning = true;
                    btnStart.setText("Dừng");
//                    btnStart.setBackgroundColor(R.color.red);
                    btnWrite.setEnabled(true);
                    btnWrite.setText("Ghi");
                    tStart = SystemClock.uptimeMillis();
                    handler.postDelayed(runnable, 0);
                }else {
                    isRunning = false;
                    btnStart.setText("Bắt đầu");
                    btnWrite.setText("Đặt lại");
                    tPause = tUpdate;
                    handler.removeCallbacks(runnable);
                }
            }
        });

        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRunning){
                    String timeWrite = txtTime.getText().toString();
                    count++;
                    String sortTime = count +"      "+ timeWrite;
                    listTime.add(sortTime);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, listTime);
                    listView.setAdapter(adapter);
//                    listView.deferNotifyDataSetChanged();
                }else{
                    count = 0;
                    listTime.clear();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, listTime);
                    listView.setAdapter(adapter);
                    btnWrite.setText("Ghi");
                    btnWrite.setEnabled(false);
                    tPause = 0;
                    txtTime.setText("00:00,00");
//                    btnStart.setText("Bắt đầu");
                }
            }
        });
        return v;
    }

    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            tMiliSec = SystemClock.uptimeMillis() - tStart;
            tUpdate = tPause + tMiliSec;
            int sec = (int) (tUpdate/1000);
            int min = sec /60;
            sec = sec % 60;
            int percentSec = (int) ((tUpdate /10) % 100);
            String time = String.format("%02d", min)+":"+String.format("%02d",sec) +","+String.format("%02d", percentSec);
//            txtTime.setText(time);

            // gửi thời gian ra ngoài
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("time", time);
            msg.setData(bundle);;
            handler.sendMessage(msg);

            handler.postDelayed(this, 0);
        }
    };

}