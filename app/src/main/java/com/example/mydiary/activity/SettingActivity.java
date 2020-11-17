package com.example.mydiary.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.mydiary.R;

public class SettingActivity extends AppCompatActivity {
    private TextView mFeedback, mLogin, mPollicy, mRate;
    private Switch mSound;
    private ImageButton mBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
        initClick();
    }
    private void init() {
        mFeedback = findViewById(R.id.mFeedback);
        mLogin = findViewById(R.id.mLogin);
        mPollicy = findViewById(R.id.mPollicy);
        mSound = findViewById(R.id.mSound);
        mRate = findViewById(R.id.mRate);
        mBack = findViewById(R.id.mBack);
    }
    private void feedback() {
        Intent mailIntent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:?subject=" + "title: " + "&body=" + "content: " + "&to=" + "bnbbinh@mail.com");
        mailIntent.setData(data);
        startActivity(Intent.createChooser(mailIntent, "Send Gmail"));
    }
    private void initClick() {
        mFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedback();
            }
        });
        mPollicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pollicy();
            }
        });
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        mSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sound();
            }
        });
        mRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.example.mydiary"));

                try {
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.out_left, R.anim.in_left);
            }
        });
    }

    private void pollicy() {

    }

    private void login() {

    }

    private void sound() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.out_left, R.anim.in_left);
    }
}