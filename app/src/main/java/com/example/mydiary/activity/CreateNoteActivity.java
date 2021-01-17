package com.example.mydiary.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydiary.R;
import com.example.mydiary.adapters.ImageAdapterEdit;
import com.example.mydiary.database.DatabaseHelper;
import com.example.mydiary.models.Diary;
import com.example.mydiary.utils.ImageFilePath;
import com.example.mydiary.callback.OnClickItem;
import com.example.mydiary.utils.Pef;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class CreateNoteActivity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private static final int SELECT_PICTURES = 1;
    private int vote;
    private int filter;
    private ImageButton mBack;
    private ImageButton mSave;
    private ImageButton btnImage;
    private ImageButton btnMic;
    private ImageButton btnColor;
    private ImageButton btnClock;
    private TextView mDate;
    private EditText mTitle;
    private EditText mContent;
    private DatabaseHelper helper;
    private BottomSheetBehavior behavior;
    private CoordinatorLayout background;
    private Spinner spinner;
    private String employees[];
    private String title;
    private ArrayList<String> resPath;
    private final ArrayList<String> list = new ArrayList<>();
    private RecyclerView test_image;
    private ImageAdapterEdit adapter;
    private int mColor = 0;

    public CreateNoteActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        init();
        initSetUp();
        setOnclick();
        setSpinner();
        setRecyclerview();

    }

    private void setRecyclerview() {
        test_image.setHasFixedSize(true);
        test_image.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
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

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void initSetUp() {
        Pef.getReference(this);

        helper = new DatabaseHelper(this);
        resPath = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        int m = calendar.get(Calendar.MONTH) + 1;
        int y = calendar.get(Calendar.YEAR);
        int h = calendar.get(Calendar.HOUR_OF_DAY);
        int p = calendar.get(Calendar.MINUTE);
        mDate.setText(String.format("%02d", h) + ":" + String.format("%02d", p) + " - " + d + "." + m + "." + y);
        employees = new String[]{
                getResources().getString(R.string._event),
                getResources().getString(R.string._mood),
                getResources().getString(R.string._work),
                getResources().getString(R.string._shopping),
                getResources().getString(R.string._travel),
                getResources().getString(R.string._celebration)};


        if (mTitle.getText().toString().isEmpty()) {
            mTitle.requestFocus();
        } else if (mContent.getText().toString().isEmpty()) {
            mContent.requestFocus();
        }

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
        btnMic = findViewById(R.id.btnMic);
        btnColor = findViewById(R.id.btnColor);
        btnClock = findViewById(R.id.btnClock);

    }


    private void setOnclick() {
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromAlbum();
                //getMultipleImage();
            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showily();
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
        btnClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date();
            }
        });
        btnMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mic();
            }
        });
        btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color();
            }
        });


    }

    private void color() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(CreateNoteActivity.this);
        ViewGroup viewGroup = findViewById(R.id.container);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_color, viewGroup, false);
        TextView tv1 = view.findViewById(R.id.tv1);
        TextView tv2 = view.findViewById(R.id.tv2);
        TextView tv3 = view.findViewById(R.id.tv3);
        TextView tv4 = view.findViewById(R.id.tv4);
        TextView tv5 = view.findViewById(R.id.tv5);
        TextView tv6 = view.findViewById(R.id.tv6);
        TextView tv7 = view.findViewById(R.id.tv7);
        TextView tv8 = view.findViewById(R.id.tv8);
        TextView tv9 = view.findViewById(R.id.tv9);
        TextView tv10 = view.findViewById(R.id.tv10);
        TextView tv11 = view.findViewById(R.id.tv11);
        TextView tv12 = view.findViewById(R.id.tv12);
        builder.setView(view);
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mColor = 1;
                background.setBackgroundResource(R.drawable.bg_gradent_1);
                alertDialog.dismiss();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mColor = 2;
                background.setBackgroundResource(R.drawable.bg_gradent_2);
                alertDialog.dismiss();
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mColor = 3;
                background.setBackgroundResource(R.drawable.bg_gradent_3);
                alertDialog.dismiss();
            }
        });
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mColor = 4;
                background.setBackgroundResource(R.drawable.bg_gradent_4);
                alertDialog.dismiss();
            }
        });
        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mColor = 5;
                background.setBackgroundResource(R.drawable.bg_gradent_5);
                alertDialog.dismiss();
            }
        });
        tv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mColor = 6;
                background.setBackgroundResource(R.drawable.bg_gradent_6);
                alertDialog.dismiss();
            }
        });
        tv7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mColor = 7;
                background.setBackgroundResource(R.drawable.bg_gradent_7);
                alertDialog.dismiss();
            }
        });
        tv8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mColor = 8;
                background.setBackgroundResource(R.drawable.bg_gradent_8);
                alertDialog.dismiss();
            }
        });
        tv9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mColor = 9;
                background.setBackgroundResource(R.drawable.bg_gradent_9);
                alertDialog.dismiss();
            }
        });
        tv10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mColor = 10;
                background.setBackgroundResource(R.drawable.bg_gradent_10);
                alertDialog.dismiss();
            }
        });
        tv11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mColor = 11;
                background.setBackgroundResource(R.drawable.bg_gradent_11);
                alertDialog.dismiss();
            }
        });
        tv12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mColor = 12;
                background.setBackgroundResource(R.drawable.bg_gradent_12);
                alertDialog.dismiss();
            }
        });


        alertDialog.show();

    }


    private void mic() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PackageManager.PERMISSION_GRANTED);
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something…");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "Sorry! Your device doesn\\'t support speech input",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void save() {
        String title = mTitle.getText().toString();
        String content = mContent.getText().toString();
        String date = mDate.getText().toString();
        StringBuilder image = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        String dateRealTime = day + "/" + month + "/" + year;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatRealTime = new SimpleDateFormat("dd/MM/yyyy");
        try {
            calendar.setTime(Objects.requireNonNull(formatRealTime.parse(dateRealTime)));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        for (String s : resPath) {
            image.append(s).append("<->");
        }
        if (title.isEmpty()) {
            mTitle.requestFocus();
            Toast.makeText(this, getResources().getString(R.string._notification_title), Toast.LENGTH_SHORT).show();
        } else {
            Diary model = new Diary();
            model.setTitle(title);
            model.setContent(content);
            model.setDate(date);
            model.setFilter(filter);
            model.setImage(image.toString().trim());
            model.setVote(mColor);
            model.setRealtime(calendar.getTimeInMillis());
            helper.add(model);
            Intent intent = new Intent(CreateNoteActivity.this, FinishActivity.class);
            intent.putExtra("I", 1);
            startActivity(intent);
            overridePendingTransition(R.anim.out_left, R.anim.in_left);
            finish();

        }

    }


    private void date() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(CreateNoteActivity.this);
        ViewGroup viewGroup = CreateNoteActivity.this.findViewById(android.R.id.content);
        View view = LayoutInflater.from(CreateNoteActivity.this).inflate(R.layout.number_picker, viewGroup, false);
        final NumberPicker day = view.findViewById(R.id.numberDay);
        final NumberPicker month = view.findViewById(R.id.numberMonth);
        final NumberPicker year = view.findViewById(R.id.numberYear);
        final NumberPicker hours = view.findViewById(R.id.numberHours);
        final NumberPicker minute = view.findViewById(R.id.numberminute);

        setNumberPicker(day, Pef.dayList);
        setNumberPicker(month, Pef.monthList);
        setNumberPicker(year, Pef.isListYear());
        setNumberPicker(hours, Pef.hoursList);
        setNumberPicker(minute, Pef.minuteList);

        setNumberPickerTextColor(day);
        setNumberPickerTextColor(month);
        setNumberPickerTextColor(year);

        Calendar calendar = Calendar.getInstance();
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        int m = calendar.get(Calendar.MONTH) + 1;
        int y = calendar.get(Calendar.YEAR);
        int h = calendar.get(Calendar.HOUR_OF_DAY);
        int p = calendar.get(Calendar.MINUTE);

        Log.d("D", "onClick: " + d);
        Log.d("M", "onClick: " + m);
        Log.d("Y", "onClick: " + y);

        day.setValue(isPosition(d, Pef.dayList));
        month.setValue(isPosition(m, Pef.monthList));
        year.setValue(isPosition(y, Pef.isListYear()));
        hours.setValue(isPosition(h, Pef.hoursList));
        minute.setValue(isPosition(p, Pef.minuteList));

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
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("HH:mm - dd.MM.yyy");
        try {
            if (Objects.requireNonNull(format.parse(s)).getTime() > (System.currentTimeMillis() + 60000)) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void setNumberPicker(NumberPicker nubmerPicker, String[] numbers) {
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
    private int isPosition(int check, String[] list) {
        int res = 1;
        for (int i = 0; i < list.length; i++) {
            if (list[i].equals(String.format("%02d", check))) {
                res = i;
            }

        }
        return res;
    }

    private static void setNumberPickerTextColor(NumberPicker numberPicker) {

        try {
            Field selectorWheelPaintField = numberPicker.getClass()
                    .getDeclaredField("mSelectorWheelPaint");
            selectorWheelPaintField.setAccessible(true);
            ((Paint) Objects.requireNonNull(selectorWheelPaintField.get(numberPicker))).setColor(Color.BLACK);
        } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
            Log.w("hihi", e);
        }

        final int count = numberPicker.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = numberPicker.getChildAt(i);
            if (child instanceof EditText)
                ((EditText) child).setTextColor(Color.BLACK);
        }
        numberPicker.invalidate();
    }


    private void showily() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateNoteActivity.this);
        builder.setTitle(getResources().getString(R.string._exit));
        builder.setMessage(getResources().getString(R.string._messenger_back));
        builder.setPositiveButton(getResources().getString(R.string._yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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

        AlertDialog dialog = builder.create();
        dialog.show();

    }


    private void getImageFromAlbum() {
        try {
            Intent i = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_IMAGE);
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
        switch (requestCode) {
            case SELECT_PICTURES: {
                if (resultCode == Activity.RESULT_OK) {
                    String path;
                    if (data.getClipData() != null) {
                        int count = data.getClipData().getItemCount();
                        for (int i = 0; i < count; i++) {
                            Uri imageUri = data.getClipData().getItemAt(i).getUri();
                            path = ImageFilePath.getPath(CreateNoteActivity.this, imageUri);
                            resPath.add(path);
                        }
                    } else if (data.getData() != null) {
                        String imagePath = data.getData().getPath();
                        Uri imageUri = data.getData();
                        path = ImageFilePath.getPath(CreateNoteActivity.this, imageUri);
                        resPath.add(path);
                    }
                    updateData(resPath);
                }


                break;
            }
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //txtSpeechInput.setText(result.get(0));
                    if (mTitle.isFocused()) {
                        mTitle.setText((mTitle.getText().toString() + " " + result.get(0)).trim());
                        mTitle.requestFocusFromTouch();
                        mTitle.requestFocus(mTitle.length() + 1);
                    } else if (mContent.isFocused()) {
                        mContent.setText((mContent.getText().toString() + " " + result.get(0)).trim());
                        mContent.requestFocusFromTouch();
                        mContent.requestFocus();
                    }

                }
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
        showily();
    }
}

