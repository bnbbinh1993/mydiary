package com.example.mydiary.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mydiary.R;
import com.example.mydiary.database.DatabaseCount;
import com.example.mydiary.models.Count;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class ShowFollowActivity extends AppCompatActivity {
    private ArrayList<Count> list = new ArrayList<>();
    private ImageButton mBack,mEdit;
    private TextView mName,mFilter,mStatus,mDay,mHours,mMinute,mSeconds,mDate,mDes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_follow);
        init();
        getData();
        initClick();
        CountDown();
    }

    private void initClick() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ShowFollowActivity.this, "wait...", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void CountDown (){
        CountDownTimer count = new CountDownTimer(1800000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                getData();
            }

            @Override
            public void onFinish() {
                CountDown();
            }
        }.start();
    }

    private void init() {
        mName = findViewById(R.id.mName);
        mFilter = findViewById(R.id.mFilter);
        mStatus = findViewById(R.id.mStatus);
        mHours = findViewById(R.id.mHours);
        mDay = findViewById(R.id.mDay);
        mMinute = findViewById(R.id.mMinute);
        mSeconds = findViewById(R.id.mSeconds);
        mDate = findViewById(R.id.mDate);
        mBack = findViewById(R.id.mBack);
        mEdit = findViewById(R.id.mEdit);
        mDes = findViewById(R.id.mDes);
    }

    private void getData() {
        list = new DatabaseCount(this).getData();
        Collections.sort(list);
        int position = getIntent().getIntExtra("POSITION",0);
        mName.setText(list.get(position).getTitle());
        //mFilter.setText(String.valueOf(list.get(position).getFilter()));
        switch (list.get(position).getFilter()){
            case 0:{
                mFilter.setText("Sự kiện");
                break;
            }
            case 1:{
                mFilter.setText("Công việc");
                break;
            }
            case 2:{
                mFilter.setText("Du lịch");
                break;
            }
            case 3:{
                mFilter.setText("Sinh nhật");
                break;
            }
        }

        mDate.setText(list.get(position).getDate());
        mDes.setText(list.get(position).getDes());

        SimpleDateFormat format = new SimpleDateFormat("hh:mm - dd.MM.yyyy");
        try {
            Date date = format.parse(list.get(position).getDate());
            long timeCount = date.getTime();
            long timeRes = timeCount - System.currentTimeMillis();
            if (timeRes > 0) {
                long day = (timeRes / 1000) / 86400;
                long hours = (timeRes / 1000) % 86400 / 60 / 60;
                long minute = (timeRes / 1000) % 86400 / 60 % 60;
                long seconds = (timeRes / 1000) % 86400 % 60;
                mDay.setText(String.valueOf(day));
                mHours.setText(String.valueOf(hours));
                mMinute.setText(String.valueOf(minute));
                mSeconds.setText(String.valueOf(seconds));
                mStatus.setText(getResources().getString(R.string._status_false));

            } else {
                mStatus.setText(getResources().getString(R.string._status_true));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}