package com.example.mydiary.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mydiary.R;
import com.example.mydiary.database.DatabaseCount;
import com.example.mydiary.models.Count;
import com.example.mydiary.utils.Pef;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class ShowFollowActivity extends AppCompatActivity {
    public static Count model;
    private ImageButton mBack, mEdit, mDelete, mPrioritize;
    private TextView mName, mFilter, mStatus, mDay, mHours, mMinute, mSeconds, mDate, mDes, mPlace;
    private LinearLayout background;
    private CountDownTimer count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_follow);
        Pef.getReference(this);
        Pef.setFullScreen(ShowFollowActivity.this);
        init();
        getData();
        initClick();
        CountDown();
    }

    private void initEvent() {
        mName.setText(model.getTitle());
        //mFilter.setText(String.valueOf(list.get(position).getFilter()));
        switch (model.getFilter()) {
            case 0: {
                mFilter.setText(getResources().getString(R.string._event));
                background.setBackgroundColor(getResources().getColor(R.color.event));
                break;
            }
            case 1: {
                mFilter.setText(getResources().getString(R.string._mood));
                background.setBackgroundColor(getResources().getColor(R.color.mood));
                break;
            }
            case 2: {
                mFilter.setText(getResources().getString(R.string._work));
                background.setBackgroundColor(getResources().getColor(R.color.work));
                break;
            }
            case 3: {
                mFilter.setText(getResources().getString(R.string._shopping));
                background.setBackgroundColor(getResources().getColor(R.color.shopping));
                break;
            }
            case 4: {
                mFilter.setText(getResources().getString(R.string._travel));
                background.setBackgroundColor(getResources().getColor(R.color.travel));
                break;
            }
            case 5: {
                mFilter.setText(getResources().getString(R.string._celebration));
                background.setBackgroundColor(getResources().getColor(R.color.cele));
                break;
            }
        }

        mDate.setText(model.getDate());
        mDes.setText(model.getDes());
        mPlace.setText(model.getPlace());
        if (model.getPrioritize() == 1) {
            mPrioritize.setImageResource(R.drawable.ic_badge_1_);
        } else {
            mPrioritize.setImageResource(R.drawable.ic_badge);
        }

        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("HH:mm - dd.MM.yyyy");
        try {
            Date date = format.parse(model.getDate());
            assert date != null;
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
                mEdit.setVisibility(View.VISIBLE);

            } else {
                mStatus.setText(getResources().getString(R.string._finished));
                mEdit.setVisibility(View.GONE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
                Intent intent = new Intent(getApplicationContext(), EditCountActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", model.getId());
                bundle.putString("title", model.getTitle());
                bundle.putString("des", model.getDes());
                bundle.putString("place", model.getPlace());
                bundle.putString("date", model.getDate());
                bundle.putInt("filter", model.getFilter());
                bundle.putInt("vote", model.getVote());
                bundle.putInt("prioritize", model.getPrioritize());
                intent.putExtras(bundle);
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
        mPrioritize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prioritize();
            }
        });
    }

    private void prioritize() {
        if (model.getPrioritize() == 1) {
            model.setPrioritize(0);
            mPrioritize.setImageResource(R.drawable.ic_badge);
        } else {
            model.setPrioritize(1);
            mPrioritize.setImageResource(R.drawable.ic_badge_1_);
        }
        new DatabaseCount(this).update(model);
        Log.d("TAG", "prioritize: " + model.getPrioritize());
    }

    private void delete() {
        new DatabaseCount(this).delete(model);
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

    private void CountDown() {
        count = new CountDownTimer(1800000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                initEvent();
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
        mPrioritize = findViewById(R.id.mPrioritize);
    }

    private void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            model = new Count(bundle.getInt("id")
                    , bundle.getString("title")
                    , bundle.getString("des")
                    , bundle.getString("place")
                    , bundle.getString("date")
                    , bundle.getInt("filter")
                    , bundle.getInt("vote")
                    , bundle.getInt("prioritize"));
        } else {
            model = new Count();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.out_bottom, R.anim.in_bottom);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (count != null) {
            count.cancel();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (count != null) {
            count.cancel();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (count != null) {
            count.start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (count != null) {
            count.start();
        }
    }
}