package com.example.mydiary.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
    private ImageButton mBack,mEdit,mDelete;
    private TextView mName,mFilter,mStatus,mDay,mHours,mMinute,mSeconds,mDate,mDes,mPlace;
    private LinearLayout background;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_follow);
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        init();
        getData();
        initClick();
        CountDown();
    }

    private static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void initClick() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.out_bottom, R.anim.in_bottom);
            }
        });
        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),EditCountActivity.class);
                intent.putExtra("POSITION",getIntent().getIntExtra("POSITION",0));
                startActivity(intent);
                overridePendingTransition(R.anim.out_left, R.anim.in_left);
            }
        });
        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showdialog();
            }
        });
    }

    private void delete() {
        list = new DatabaseCount(this).getData();
        Collections.sort(list);
        int position = getIntent().getIntExtra("POSITION",0);
        Count count = list.get(position);
        new DatabaseCount(this).delete(count);

    }
    private void showdialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ShowFollowActivity.this);
        builder.setMessage(getResources().getString(R.string._messenger_delete));
        builder.setTitle(getResources().getString(R.string._delete));
        builder.setPositiveButton(getResources().getString(R.string._yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delete();
                dialog.dismiss();
                finish();
                overridePendingTransition(R.anim.out_bottom, R.anim.in_bottom);
            }
        });
        builder.setNegativeButton(getResources().getString(R.string._no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        android.app.AlertDialog dialog = builder.create();
        dialog.show();

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
        mPlace = findViewById(R.id.mPlace);
        background = findViewById(R.id.background);
        mDelete = findViewById(R.id.mDelete);
    }

    private void getData() {
        list = new DatabaseCount(this).getData();
        Collections.sort(list);
        int position = getIntent().getIntExtra("POSITION",0);
        if (list.size()>0){
            mName.setText(list.get(position).getTitle());
            //mFilter.setText(String.valueOf(list.get(position).getFilter()));
            switch (list.get(position).getFilter()){
                case 0:{
                    mFilter.setText(getResources().getString(R.string._event));
                    background.setBackgroundColor(getResources().getColor(R.color.event));
                    break;
                }
                case 1:{
                    mFilter.setText(getResources().getString(R.string._mood));
                    background.setBackgroundColor(getResources().getColor(R.color.mood));
                    break;
                }
                case 2:{
                    mFilter.setText(getResources().getString(R.string._work));
                    background.setBackgroundColor(getResources().getColor(R.color.work));
                    break;
                }
                case 3:{
                    mFilter.setText(getResources().getString(R.string._shopping));
                    background.setBackgroundColor(getResources().getColor(R.color.shopping));
                    break;
                }
                case 4:{
                    mFilter.setText(getResources().getString(R.string._travel));
                    background.setBackgroundColor(getResources().getColor(R.color.travel));
                    break;
                }
                case 5:{
                    mFilter.setText(getResources().getString(R.string._celebration));
                    background.setBackgroundColor(getResources().getColor(R.color.cele));
                    break;
                }
            }

            mDate.setText(list.get(position).getDate());
            mDes.setText(list.get(position).getDes());
            mPlace.setText(list.get(position).getPlace());

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.out_bottom, R.anim.in_bottom);
    }
}