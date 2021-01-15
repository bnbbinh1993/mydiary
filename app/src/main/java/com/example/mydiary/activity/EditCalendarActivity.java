package com.example.mydiary.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
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
import com.example.mydiary.utils.Pef;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

public class EditCalendarActivity extends AppCompatActivity {
    private ImageButton btnTime;
    private ImageButton mBack;
    private ImageButton mSave;
    private TextView mDate;
    private TextView mTime;
    private EditText body;
    private DatabaseEvent helper;
    private SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyy");
    private int id = 0;
    private long loc = 0;
    private long today = 0;
    private List<EventCalendar> list = new ArrayList<>();
    private EventCalendar model;
    private String res;
    private String timekey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_calendar);
        helper = new DatabaseEvent(EditCalendarActivity.this);
        id = getIntent().getIntExtra("key", 0);
        list = helper.getDataById(id);
        model = list.get(0);
        initUI();
        initEvent();
    }

    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    private void initEvent() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(model.getLoc());
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        int m = calendar.get(Calendar.MONTH) + 1;
        int y = calendar.get(Calendar.YEAR);

        String keydate = model.getContent() + " - " + d + "." + m + "." + y;
        SimpleDateFormat f = new SimpleDateFormat("hh:mm - dd.MM.yyyy");
        SimpleDateFormat fm = new SimpleDateFormat("EEE");
        try {
            today = f.parse(keydate).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date = new Date();
        date.setTime(today);
        loc = model.getLoc();
        timekey = model.getContent();
        mDate.setText(d + "." + m + "." + y);
        mTime.setText(fm.format(date) + ", " + model.getContent());
        body.setText(model.getTime());
        body.requestFocus();
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date();
            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog();

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
            model.setContent(body.getText().toString());
            model.setTime(timekey);
            model.setLoc(loc);
            helper.update(model);
            Toast.makeText(this, getResources().getString(R.string._update_successful), Toast.LENGTH_SHORT).show();
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

    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    private void ui(String time, String date, String ngay, String thang, String nam) {
        Calendar calendar = Calendar.getInstance();
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        int m = calendar.get(Calendar.MONTH) + 1;
        int y = calendar.get(Calendar.YEAR);
        mDate.setText(date);
        timekey = time;
        if (d == Integer.parseInt(ngay) && m == Integer.parseInt(thang) && y == Integer.parseInt(nam)) {
            mTime.setText(getResources().getString(R.string._today) + ", " + time);
        } else {
            Date dateaa = new Date();
            SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy");
            try {
                dateaa.setTime(f.parse(date).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            SimpleDateFormat format = new SimpleDateFormat("EEE");
            mTime.setText(format.format(dateaa) + ", " + time);
        }
        try {
            loc = f.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void date() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(EditCalendarActivity.this);
        ViewGroup viewGroup = EditCalendarActivity.this.findViewById(android.R.id.content);
        View view = LayoutInflater.from(EditCalendarActivity.this).inflate(R.layout.number_picker, viewGroup, false);
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
        calendar.setTimeInMillis(today);
        int h = calendar.get(Calendar.HOUR_OF_DAY);
        int p = calendar.get(Calendar.MINUTE);
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
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("HH:mm - dd.MM.yyy");
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

    @SuppressLint("DefaultLocale")
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
        } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
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

    private void showdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditCalendarActivity.this);
        builder.setTitle("Lưu dữ liệu");
        builder.setTitle(getResources().getString(R.string._messenger));
        builder.setPositiveButton(getResources().getString(R.string._yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                add();
                dialog.dismiss();

            }
        });
        builder.setNegativeButton(getResources().getString(R.string._no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
                overridePendingTransition(R.anim.out_bottom, R.anim.in_bottom);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    public void onBackPressed() {
        showdialog();
    }

}