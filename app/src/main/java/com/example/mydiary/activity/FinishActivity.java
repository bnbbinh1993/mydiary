package com.example.mydiary.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.mydiary.R;
import com.example.mydiary.utils.Pef;

public class FinishActivity extends AppCompatActivity {
    private AppCompatButton mCreate;
    private AppCompatButton mDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        Pef.getReference(this);
        Pef.setFullScreen(FinishActivity.this);
        init();
        initClick();

    }

    private void init() {
        mDone = findViewById(R.id.mDone);
        mCreate = findViewById(R.id.mCreate);
    }
    private void initClick(){
        int i = getIntent().getIntExtra("I",0);
        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 0){
                    startActivity(new Intent(getApplication(),CountDownActivity.class));
                    finish();
                    overridePendingTransition(R.anim.out_left, R.anim.in_left);
                }else if (i==1){
                    startActivity(new Intent(getApplication(),NoteActivity.class));
                    finish();
                    overridePendingTransition(R.anim.out_left, R.anim.in_left);
                }
            }
        });
        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.out_left, R.anim.in_left);
            }
        });
    }



    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.out_left, R.anim.in_left);
    }
}