package com.luuhuy.btl_donghobaothuc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

public class MusicActivity extends AppCompatActivity {
    private RadioButton rdKyUc, rdCuocSong, rdSanChoi, rdNgauHung, rdDoChoi, rdDomDom;
    private ImageView back;
    private String nameMusic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        rdKyUc = findViewById(R.id.ky_uc);
        rdCuocSong = findViewById(R.id.cuoc_song);
        rdSanChoi = findViewById(R.id.san_choi);
        rdNgauHung = findViewById(R.id.ngau_hung);
        rdDoChoi = findViewById(R.id.do_choi);
        rdDomDom = findViewById(R.id.dom_dom);
        back = findViewById(R.id.backFromMusic);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rdKyUc.isChecked()){
                    nameMusic = rdKyUc.getText().toString();
                }else if(rdCuocSong.isChecked()){
                    nameMusic =  rdCuocSong.getText().toString();
                }else if(rdSanChoi.isChecked()){
                    nameMusic =  rdSanChoi.getText().toString();
                }else if(rdNgauHung.isChecked()){
                    nameMusic =  rdNgauHung.getText().toString();
                }else if(rdDoChoi.isChecked()){
                    nameMusic = rdDoChoi.getText().toString();
                }else if (rdDomDom.isChecked()){
                    nameMusic = rdDomDom.getText().toString();
                }
                Intent intent = new Intent();
                intent.putExtra("nameMusic", nameMusic);
                setResult(AddAlarmActivity.RESULT_MUSIC_CODE, intent);
                finish();
            }
        });
    }
}