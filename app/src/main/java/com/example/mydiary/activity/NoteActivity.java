package com.example.mydiary.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mydiary.R;
import com.example.mydiary.adapters.ImageAdapter;
import com.example.mydiary.adapters.ImageAdapterEdit;
import com.example.mydiary.database.DatabaseHelper;
import com.example.mydiary.models.Count;
import com.example.mydiary.models.Diary;
import com.example.mydiary.utils.ImageFilePath;
import com.example.mydiary.utils.OnClickItem;
import com.example.mydiary.utils.Pef;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class NoteActivity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE = 1;
    private int vote;
    private int filter;
    private ImageButton mBack;
    private ImageButton mSave;
    private ImageButton btnImage;
    private TextView mDate;
    private EditText mTitle;
    private EditText mContent;
    private DatabaseHelper helper;
    private BottomSheetBehavior behavior;
    private CoordinatorLayout background;
    private Spinner spinner;
    private String employees[];
    private String title;
    private String path;
    private static final int SELECT_PICTURES = 1;
    private ArrayList<String> resPath;
    private ArrayList<String> list = new ArrayList<>();
    private RecyclerView test_image;
    private ImageAdapterEdit adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        init();
        initSetUp();
        setOnclick();
        setSpinner();
        setRecyclerview();

    }

    private void setRecyclerview() {
        test_image.setHasFixedSize(true);
        test_image.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        Log.d("TEST", "setRecyclerview: " + resPath.size());
        adapter = new ImageAdapterEdit(list, this);
        test_image.setAdapter(adapter);
        adapter.setOnClickItem(new OnClickItem() {
            @Override
            public void click(int position) {
                resPath.remove(position);
                updateData(resPath);
            }

            @Override
            public void longClick(int position) {

            }
        });
    }

    public void updateData(ArrayList<String> viewModels) {
        list.clear();
        list.addAll(viewModels);
        adapter.notifyDataSetChanged();
    }

    private void setSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                employees);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                title = employees[position];
                filter = position + 1;
                switch (position+1){
                    case 1:{
                        background.setBackgroundColor(getResources().getColor(R.color.event));
                        break;
                    }
                    case 2:{
                        background.setBackgroundColor(getResources().getColor(R.color.mood));
                        break;
                    }
                    case 3:{
                        background.setBackgroundColor(getResources().getColor(R.color.work));
                        break;
                    }
                    case 4:{
                        background.setBackgroundColor(getResources().getColor(R.color.shopping));
                        break;
                    }
                    case 5:{
                        background.setBackgroundColor(getResources().getColor(R.color.travel));
                        break;
                    }
                    case 6:{
                        background.setBackgroundColor(getResources().getColor(R.color.cele));
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initSetUp() {

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
        helper = new DatabaseHelper(this);
        resPath = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        int m = calendar.get(Calendar.MONTH) + 1;
        int y = calendar.get(Calendar.YEAR);
        int h = calendar.get(Calendar.HOUR);
        int p = calendar.get(Calendar.MINUTE);
        mDate.setText(String.format("%02d",h)+":"+String.format("%02d",p)+" - "+d+"."+m+"."+y);
        employees = new String[]{
                getResources().getString(R.string._event),
                getResources().getString(R.string._mood),
                getResources().getString(R.string._work),
                getResources().getString(R.string._shopping),
                getResources().getString(R.string._travel),
                getResources().getString(R.string._celebration)};

    }

    private void init() {
        test_image = findViewById(R.id.test_image);
        mBack = findViewById(R.id.mBack);
        mSave = findViewById(R.id.save);
        mDate = findViewById(R.id.date);
        mTitle = findViewById(R.id.title);
        mContent = findViewById(R.id.body);
        background = findViewById(R.id.background);
        spinner = findViewById(R.id.spinner);
        btnImage = findViewById(R.id.btnImage);

    }


    private void setOnclick() {
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMultipleImage();
            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
        String title = mTitle.getText().toString();
        String content = mContent.getText().toString();
        String date = mDate.getText().toString();
        String image = "";
        for (String s : resPath) {
            image += " " + s;
        }
        if (title.isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string._notification_title), Toast.LENGTH_SHORT).show();
        } else if (content.isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string._notification_des), Toast.LENGTH_SHORT).show();
        } else {
            Diary diary = new Diary();
            diary.setTitle(title);
            diary.setContent(content);
            diary.setDate(date);
            diary.setFilter(filter);
            diary.setImage(image.trim());
            diary.setVote(filter);
            helper.adđ(diary);
            try {
                Thread.sleep(500);
                Intent intent = new Intent(NoteActivity.this, FinishActivity.class);
                intent.putExtra("I", 1);
                startActivity(intent);
                finish();
            } catch (InterruptedException e) {
                Intent intent = new Intent(NoteActivity.this, FinishActivity.class);
                intent.putExtra("I", 1);
                startActivity(intent);
                finish();
            }
        }

    }


    private void date() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(NoteActivity.this);
        ViewGroup viewGroup = NoteActivity.this.findViewById(android.R.id.content);
        View view = LayoutInflater.from(NoteActivity.this).inflate(R.layout.number_picker, viewGroup, false);
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
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private boolean checkDate(String s) {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm - dd.MM.yyy");
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
        AlertDialog.Builder builder = new AlertDialog.Builder(NoteActivity.this);
        builder.setTitle("Lưu dữ liệu");
        builder.setTitle(getResources().getString(R.string._messenger_back));
        builder.setPositiveButton(getResources().getString(R.string._yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string._no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void getImageFromAlbum() {
        try {
            startActivityForResult(new Intent().setAction(Intent.ACTION_GET_CONTENT).setType("image/*"), RESULT_LOAD_IMAGE);
        } catch (Exception exp) {
            Log.i("Error", exp.toString());
        }
    }

    private void getMultipleImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURES);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURES) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        path = ImageFilePath.getPath(NoteActivity.this, imageUri);
                        resPath.add(path);
                    }
                } else if (data.getData() != null) {
                    String imagePath = data.getData().getPath();
                    Uri imageUri = data.getData();
                    path = ImageFilePath.getPath(NoteActivity.this, imageUri);
                    resPath.add(path);
                }
                updateData(resPath);
            }
        }
        Log.d("SIZE", "onActivityResult: " + resPath.size());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        showdialog();
    }
}

