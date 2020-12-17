package com.example.mydiary.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mydiary.R;
import com.example.mydiary.database.DatabaseEvent;
import com.example.mydiary.models.EventCalendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ShowCalendarActivity extends AppCompatActivity {
    private ImageButton mBack;
    private ImageButton mDelete;
    private ImageButton mEdit;
    private ImageButton btnTime;
    private TextView date;
    private TextView date2;
    private TextView body;
    private DatabaseEvent helper;
    private EventCalendar model;
    private List<EventCalendar> list = new ArrayList<>();
    private int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_calendar);
        initUI();
        initSetUp();
        initFunction();
    }

    private void initSetUp() {
        list.clear();
        helper = new DatabaseEvent(ShowCalendarActivity.this);
        id = getIntent().getIntExtra("keyPosition", 0);
        list = helper.getDataById(id);
        model = list.get(0);
        body.setText(model.getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(model.getLoc());
        int d = cal.get(Calendar.DAY_OF_MONTH);
        int m = cal.get(Calendar.MONTH) + 1;
        int y = cal.get(Calendar.YEAR);

        SimpleDateFormat f = new SimpleDateFormat("EEE");
        Date aaa = new Date();
        aaa.setTime(model.getLoc());
        date.setText(d + "." + m + "." + y);
        date2.setText(f.format(aaa) + ", " + model.getContent());

    }

    private void initFunction() {
        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog();
            }
        });
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
                Intent intent = new Intent(getApplicationContext(), EditCalendarActivity.class);
                intent.putExtra("key", id);
                startActivity(intent);
                overridePendingTransition(R.anim.out_bottom, R.anim.in_bottom);
            }
        });
    }

    private void delete() {
        helper.delete(model);
    }

    private void initUI() {
        mBack = findViewById(R.id.mBack);
        mDelete = findViewById(R.id.mDelete);
        mEdit = findViewById(R.id.mEdit);
        date = findViewById(R.id.date);
        date2 = findViewById(R.id.date2);
        btnTime = findViewById(R.id.btnTime);
        body = findViewById(R.id.body);
    }

    private void showdialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ShowCalendarActivity.this);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.out_bottom, R.anim.in_bottom);
    }

    @Override
    protected void onResume() {
        initSetUp();
        super.onResume();
    }
}