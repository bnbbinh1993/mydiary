package com.example.mydiary.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mydiary.R;
import com.example.mydiary.database.DatabaseEvent;
import com.example.mydiary.models.EventCalendar;
import com.example.mydiary.utils.DatePef;
import com.example.mydiary.utils.Pef;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddEventActivity extends AppCompatActivity {
    private ImageButton btnTime;
    private ImageButton mBack;
    private ImageButton mSave;
    private TextView mDate;
    private TextView mTime;
    private EditText body;
    private String res;
    private long loc = 1000;
    private long toDay = 1000;
    private DatabaseEvent helper = new DatabaseEvent(this);
    private SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        toDay = getIntent().getLongExtra("keyTime", 1000);

        initUI();
        initEvent();


    }

    private void initEvent() {
        Calendar calendar = Calendar.getInstance();
        int h = calendar.get(Calendar.HOUR_OF_DAY);
        int p = calendar.get(Calendar.MINUTE);
        calendar.setTimeInMillis(toDay);
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        int m = calendar.get(Calendar.MONTH) + 1;
        int y = calendar.get(Calendar.YEAR);
        try {
            loc = f.parse(d + "." + m + "." + y).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        body.requestFocus();
        res = String.format("%02d", h) + ":" + String.format("%02d", p);
        mDate.setText(d + "." + m + "." + y);
        Date dateaa = new Date();
        dateaa.setTime(toDay);
        SimpleDateFormat format = new SimpleDateFormat("EEE");
        mTime.setText(format.format(dateaa) + " - " + String.format("%02d", h) + ":" + String.format("%02d", p));
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date();
            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.out_bottom, R.anim.in_bottom);
            }
        });
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });

    }

    private void add() {
        if (!body.getText().toString().trim().isEmpty()) {
            EventCalendar model = new EventCalendar();
            model.setContent(body.getText().toString());
            model.setTime(res);
            model.setLoc(loc);
            helper.add(model);
            finish();
            overridePendingTransition(R.anim.out_left, R.anim.in_left);

        } else {
            body.requestFocus();
            Toast.makeText(this, "Lỗi! Nội dung còn trống!", Toast.LENGTH_SHORT).show();
        }
    }

    private void initUI() {
        btnTime = findViewById(R.id.btnTime);
        mDate = findViewById(R.id.date);
        mTime = findViewById(R.id.time);
        mBack = findViewById(R.id.mBack);
        mSave = findViewById(R.id.save);
        body = findViewById(R.id.body);
    }

    private void ui(String time, String date, String ngay, String thang, String nam) {
        Calendar calendar = Calendar.getInstance();
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        int m = calendar.get(Calendar.MONTH) + 1;
        int y = calendar.get(Calendar.YEAR);
        mDate.setText(date);
        if (d == Integer.parseInt(ngay) && m == Integer.parseInt(thang) && y == Integer.parseInt(nam)) {
            mTime.setText(getResources().getString(R.string._today) + " - " + time);
        } else {
            Date dateaa = new Date();
            SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy");
            try {
                dateaa.setTime(f.parse(date).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            SimpleDateFormat format = new SimpleDateFormat("EEE");
            mTime.setText(format.format(dateaa) + " - " + time);
        }
        try {
            loc = f.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void date() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(AddEventActivity.this);
        ViewGroup viewGroup = AddEventActivity.this.findViewById(android.R.id.content);
        View view = LayoutInflater.from(AddEventActivity.this).inflate(R.layout.number_picker, viewGroup, false);
        final NumberPicker day = view.findViewById(R.id.numberDay);
        final NumberPicker month = view.findViewById(R.id.numberMonth);
        final NumberPicker year = view.findViewById(R.id.numberYear);
        final NumberPicker hours = view.findViewById(R.id.numberHours);
        final NumberPicker minute = view.findViewById(R.id.numberminute);

        setNubmerPicker(day, Pef.dayList);
        setNubmerPicker(month, Pef.monthList);
        setNubmerPicker(year, Pef.isListYear());
        setNubmerPicker(hours, Pef.hoursList);
        setNubmerPicker(minute, Pef.minuteList);

        setNumberPickerTextColor(day, Color.BLACK);
        setNumberPickerTextColor(month, Color.BLACK);
        setNumberPickerTextColor(year, Color.BLACK);

        Calendar calendar = Calendar.getInstance();
        int h = calendar.get(Calendar.HOUR_OF_DAY);
        int p = calendar.get(Calendar.MINUTE);
        calendar.setTimeInMillis(toDay);
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        int m = calendar.get(Calendar.MONTH) + 1;
        int y = calendar.get(Calendar.YEAR);
        Log.d("D", "onClick: " + d);
        Log.d("M", "onClick: " + m);
        Log.d("Y", "onClick: " + y);

        day.setValue(isPositon(d, Pef.dayList));
        month.setValue(isPositon(m, Pef.monthList));
        year.setValue(isPositon(y, Pef.isListYear()));
        hours.setValue(isPositon(h, Pef.hoursList));
        minute.setValue(isPositon(p, Pef.minuteList));

        builder.setPositiveButton(R.string._yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int p1 = day.getValue();
                int p2 = month.getValue();
                int p3 = year.getValue();
                int p4 = hours.getValue();
                int p5 = minute.getValue();
                if (isDateFormat(Pef.dayList[p1], Pef.monthList[p2])) {
                    String time = Pef.hoursList[p4] + ":" +
                            Pef.minuteList[p5];
                    String date = Pef.dayList[p1] + "." + Pef.monthList[p2]
                            + "." + Pef.isListYear()[p3];
                    String ngay = Pef.dayList[p1];
                    String thang = Pef.monthList[p2];
                    String nam = Pef.isListYear()[p3];
                    ui(time, date, ngay, thang, nam);
                    res = Pef.hoursList[p4] + ":" + Pef.minuteList[p5];

                    dialog.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "ERROR!", Toast.LENGTH_SHORT).show();
                    Log.d("TEST", "ERROR!");
                }

            }
        });
        builder.setNegativeButton(R.string._close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setView(view);
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private boolean checkDate(String s) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm - dd.MM.yyy");
        try {
            if (format.parse(s).getTime() > (System.currentTimeMillis() + 60000)) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void setNubmerPicker(NumberPicker nubmerPicker, String[] numbers) {
        nubmerPicker.setMaxValue(numbers.length - 1);
        nubmerPicker.setMinValue(0);
        nubmerPicker.setWrapSelectorWheel(true);
        nubmerPicker.setDisplayedValues(numbers);
    }

    private boolean isDateFormat(String day, String month) {
        int isDay = Integer.parseInt(day);
        int isMonth = Integer.parseInt(month);
        if (isMonth == 2 || isMonth == 4 || isMonth == 6 || isMonth == 9 || isMonth == 11) {
            if (isDay > 30) {
                return false;
            }
        }
        return true;
    }

    private int isPositon(int check, String[] list) {
        int res = 1;
        for (int i = 0; i < list.length; i++) {
            if (list[i].equals(String.format("%02d", check))) {
                res = i;
            }

        }
        return res;
    }

    private static void setNumberPickerTextColor(NumberPicker numberPicker, int color) {

        try {
            Field selectorWheelPaintField = numberPicker.getClass()
                    .getDeclaredField("mSelectorWheelPaint");
            selectorWheelPaintField.setAccessible(true);
            ((Paint) selectorWheelPaintField.get(numberPicker)).setColor(color);
        } catch (NoSuchFieldException e) {
            Log.w("hihi", e);
        } catch (IllegalAccessException e) {
            Log.w("hihi", e);
        } catch (IllegalArgumentException e) {
            Log.w("hihi", e);
        }

        final int count = numberPicker.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = numberPicker.getChildAt(i);
            if (child instanceof EditText)
                ((EditText) child).setTextColor(color);
        }
        numberPicker.invalidate();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.out_bottom, R.anim.in_bottom);
    }

}