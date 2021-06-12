package com.luuhuy.btl_donghobaothuc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class EditTitleActivity extends AppCompatActivity {

    private EditText editTitle;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_title);

        editTitle = findViewById(R.id.editTitle);
        back = findViewById(R.id.back);
        String title = getIntent().getStringExtra("title");
        editTitle.setText(title);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ResultTitle =  editTitle.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("ResultTitle", ResultTitle);
                setResult(AddAlarmActivity.RESULT_TITLE_CODE, intent);
                finish();
            }
        });
    }
}