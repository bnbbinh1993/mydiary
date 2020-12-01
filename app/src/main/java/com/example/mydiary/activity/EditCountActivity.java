package com.example.mydiary.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mydiary.R;
import com.example.mydiary.database.DatabaseCount;
import com.example.mydiary.models.Count;
import com.example.mydiary.utils.Pef;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class EditCountActivity extends AppCompatActivity {
    private Spinner spinnerEmployee;
    private String employees[];
    private ImageButton mBack;
    private ImageButton mSave;
    private TextView mDate;
    private String title;
    private String resultTime;
    private DatabaseCount helper;
    private LinearLayout layout;
    private EditText mTitle;
    private EditText mPlace;
    private EditText mDes;
    private int filter;
    private ArrayList<Count> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_count);
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
        setUp();
        setSpinner();
        setOnclick();

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


    private void init() {
        spinnerEmployee = findViewById(R.id.spinner_employee);
        mBack = findViewById(R.id.mBack);
        mSave = findViewById(R.id.mSave);
        mDate = findViewById(R.id.mDate);
        layout = findViewById(R.id.layout);
        mTitle = findViewById(R.id.mTitle);
        mPlace = findViewById(R.id.mPlace);
        mDes = findViewById(R.id.mDes);


        employees = new String[]{
                getResources().getString(R.string._event),
                getResources().getString(R.string._mood),
                getResources().getString(R.string._work),
                getResources().getString(R.string._shopping),
                getResources().getString(R.string._travel),
                getResources().getString(R.string._celebration)};


    }

    private void setUp() {
        helper = new DatabaseCount(this);
        int i = getIntent().getIntExtra("POSITION", 0);
        list = helper.getData();
        Collections.sort(list);
        filter = list.get(i).getFilter();
        mTitle.setText(list.get(i).getTitle());
        mDes.setText(list.get(i).getDes());
        mPlace.setText(list.get(i).getPlace());
        mDate.setText(list.get(i).getDate());
        spinnerEmployee.setSelection(list.get(i).getFilter());
    }

    private void setOnclick() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog();
            }
        });
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date();
            }
        });
    }

    private void save() {
        String title = mTitle.getText().toString().trim();
        String des = mDes.getText().toString().trim();
        String place = mPlace.getText().toString();
        String date = mDate.getText().toString();

        if (title.isEmpty()) {
            Toast.makeText(this, "Tiêu đề không được để trống", Toast.LENGTH_SHORT).show();
        } else if (des.isEmpty()) {
            Toast.makeText(this, "Mô tả ngắn không được để trống", Toast.LENGTH_SHORT).show();
        } else if (!checkDate(date)) {
            Toast.makeText(this, "Không được chọn thời gian trong quá khứ", Toast.LENGTH_SHORT).show();
        } else {
            Count count = list.get(getIntent().getIntExtra("POSITION", 0));
            count.setTitle(title);
            count.setDes(des);
            count.setPlace(place);
            count.setDate(date);
            count.setFilter(filter);
            count.setVote(0);
            boolean update = helper.update(count);

            if (update) {
                Toast.makeText(this, getResources().getString(R.string._update_successful), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getResources().getString(R.string._update_failded), Toast.LENGTH_SHORT).show();
            }
            finish();
            overridePendingTransition(R.anim.out_left, R.anim.in_left);
        }

    }

    private void setSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, employees);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEmployee.setAdapter(adapter);
        spinnerEmployee.setSelection(filter);
        spinnerEmployee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onItemSelectedHandler(parent, view, position, id);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void onItemSelectedHandler(AdapterView<?> adapterView, View view, int position, long id) {
        title = employees[position];
        switch (position) {
            case 0: {
                filter = 0;
                layout.setBackgroundColor(getResources().getColor(R.color.event));
                break;
            }
            case 1: {
                filter = 1;
                layout.setBackgroundColor(getResources().getColor(R.color.mood));
                break;
            }
            case 2: {
                filter = 2;
                layout.setBackgroundColor(getResources().getColor(R.color.work));
                break;
            }
            case 3: {
                filter = 3;
                layout.setBackgroundColor(getResources().getColor(R.color.shopping));
                break;
            }
            case 4: {
                filter = 4;
                layout.setBackgroundColor(getResources().getColor(R.color.travel));
                break;
            }
            case 5: {
                filter = 5;
                layout.setBackgroundColor(getResources().getColor(R.color.cele));
                break;
            }
        }
    }

    private void date() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditCountActivity.this);
        ViewGroup viewGroup = EditCountActivity.this.findViewById(android.R.id.content);
        View view = LayoutInflater.from(EditCountActivity.this).inflate(R.layout.number_picker, viewGroup, false);
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
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        int m = calendar.get(Calendar.MONTH) + 1;
        int y = calendar.get(Calendar.YEAR);
        int h = calendar.get(Calendar.HOUR);
        int p = calendar.get(Calendar.MINUTE);

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
                    String dateResult = Pef.hoursList[p4] + ":" +
                            Pef.minuteList[p5] + " - " + Pef.dayList[p1] + "." + Pef.monthList[p2]
                            + "." + Pef.isListYear()[p3];
                    mDate.setText(dateResult);
                    dialog.dismiss();
                    Log.d("TEST", "onClick: " + dateResult);
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
        AlertDialog alertDialog = builder.create();
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

    private void showdialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EditCountActivity.this);
        builder.setTitle(getResources().getString(R.string._messenger_back));
        builder.setPositiveButton(getResources().getString(R.string._yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
                overridePendingTransition(R.anim.out_left, R.anim.in_left);
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
        showdialog();
    }

}