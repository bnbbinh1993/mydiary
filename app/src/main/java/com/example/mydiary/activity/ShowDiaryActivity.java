package com.example.mydiary.activity;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mydiary.R;
import com.example.mydiary.adapters.ImageAdapter;
import com.example.mydiary.adapters.ImageAdapterEdit;
import com.example.mydiary.database.DatabaseHelper;
import com.example.mydiary.models.Diary;
import com.example.mydiary.utils.ImageFilePath;
import com.example.mydiary.callback.OnClickItem;
import com.example.mydiary.utils.Pef;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class ShowDiaryActivity extends AppCompatActivity {
    private ImageButton edit;
    private ImageButton back;
    private ImageButton imageEdit;
    private ImageButton saveEdit;
    private ImageButton btnMic;
    private ImageButton mDelete;
    private ImageButton btnColor;
    private ImageButton btnClock;
    private TextView date;
    private TextView filter;
    private TextView title;
    private TextView body;
    private TextView titleEdit;
    private TextView bodyEdit;
    private TextView dateEdit;
    private RecyclerView mRecyclerviewEdit;
    private RecyclerView mRecyclerview;
    private RelativeLayout layout_show;
    private RelativeLayout layout_edit;
    private RelativeLayout background;
    private ImageAdapterEdit adapterEdit;
    private ImageAdapter adapter;
    private Spinner spinnerEdit;
    private static final int SELECT_PICTURES = 1;
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private int filterEdit;
    private int mColor;
    private String[] employees;
    private final ArrayList<String> listPath = new ArrayList<>();
    private final ArrayList<String> listPath2 = new ArrayList<>();
    private Diary model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_diary);
        init();
        getData();
        setUp();
        setSpinner();
        initClick();
        setLayoutShow();

    }

    private void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            model = new Diary(bundle.getInt("id"), bundle.getString("title"), bundle.getString("content"), bundle.getString("date"), bundle.getString("address"), bundle.getString("image"), bundle.getInt("vote"), bundle.getInt("filter"), bundle.getLong("realtime"));
            title.setText(model.getTitle());
            body.setText(model.getContent());
            date.setText(model.getDate());
            filterEdit = model.getFilter();
            mColor = model.getVote();
        } else {
            model = new Diary();
        }
    }

    private void init() {
        edit = findViewById(R.id.edit);
        filter = findViewById(R.id.filter);
        back = findViewById(R.id.back);
        body = findViewById(R.id.body);
        title = findViewById(R.id.title);
        date = findViewById(R.id.date);
        mRecyclerview = findViewById(R.id.mRecyclerview);
        layout_show = findViewById(R.id.layout_show);
        mDelete = findViewById(R.id.mDelete);
        background = findViewById(R.id.background);
        btnColor = findViewById(R.id.btnColor);
        btnClock = findViewById(R.id.btnClock);


        layout_edit = findViewById(R.id.layout_edit);
        titleEdit = findViewById(R.id.titleEdit);
        bodyEdit = findViewById(R.id.bodyEdit);
        imageEdit = findViewById(R.id.imageEdit);
        saveEdit = findViewById(R.id.saveEdit);
        dateEdit = findViewById(R.id.dateEdit);
        spinnerEdit = findViewById(R.id.spinnerEdit);
        mRecyclerviewEdit = findViewById(R.id.mRecyclerviewEdit);
        btnMic = findViewById(R.id.btnMic);

    }

    private void setLayoutShow() {
        setUp();
        Log.d("TAG", "setLayoutShow: " + listPath.size());
        layout_show.setVisibility(View.VISIBLE);
        layout_edit.setVisibility(View.GONE);


    }

    private void setLayoutEdit() {
        setUp();
        layout_edit.setVisibility(View.VISIBLE);
        layout_show.setVisibility(View.GONE);
        setViewRecyclerview();
        titleEdit.requestFocus();

    }

    private void setViewRecyclerview() {
        Log.d("TAG", "setViewRecyclerview: " + listPath2.size());
        if (listPath2.size() > 0) {
            mRecyclerviewEdit.setVisibility(View.VISIBLE);
            mRecyclerview.setVisibility(View.VISIBLE);
        } else {
            mRecyclerviewEdit.setVisibility(View.GONE);
            mRecyclerview.setVisibility(View.GONE);
        }
    }


    private void setUp() {
        Pef.getReference(this);
        employees = new String[]{getResources().getString(R.string._event), getResources().getString(R.string._mood), getResources().getString(R.string._work), getResources().getString(R.string._shopping), getResources().getString(R.string._travel), getResources().getString(R.string._celebration)};
        listPath.clear();
        listPath2.clear();

        String s[] = model.getImage().split("<->");
        if (s.length > 0) {
            for (String value : s) {
                if (!value.isEmpty()) {
                    listPath.add(value);
                    listPath2.add(value);
                }
            }
        }

        adapter = new ImageAdapter(listPath, this);
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerview.setAdapter(adapter);
        updateFilter(filterEdit);
        updateVote(mColor);
        adapter.setOnClickItem(new OnClickItem() {
            @Override
            public void click(int position) {
                android.app.AlertDialog.Builder build = new android.app.AlertDialog.Builder(ShowDiaryActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View view1 = LayoutInflater.from(ShowDiaryActivity.this).inflate(R.layout.item_full_image, viewGroup, false);
                ImageButton btnCancel = view1.findViewById(R.id.btnCancel);
                PhotoView imageShowFull = view1.findViewById(R.id.photo_view);
                File file = new File(listPath.get(position));
                Glide.with(ShowDiaryActivity.this)
                        .load(file)
                        .error(R.mipmap.ic_launcher)
                        .into(imageShowFull);


                build.setView(view1);
                final android.app.AlertDialog dialog = build.create();
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }

            @Override
            public void longClick(int position) {

            }
        });
        titleEdit.setText(model.getTitle());
        bodyEdit.setText(model.getContent());
        dateEdit.setText(model.getDate());
        //spinnerEdit.setPromptId(list.get(i).getFilter());
        adapterEdit = new ImageAdapterEdit(listPath2, this);
        mRecyclerviewEdit.setHasFixedSize(true);
        mRecyclerviewEdit.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerviewEdit.setAdapter(adapterEdit);
        adapterEdit.setOnClickItem(new OnClickItem() {
            @Override
            public void click(int position) {
                Log.d("TAG", "click: " + listPath2.size());
                Log.d("TAG", "click: " + position);
                deleteImage(listPath2, position);
                updateUI();
            }

            @Override
            public void longClick(int position) {

            }
        });

        updateUI();

    }

    private void deleteImage(ArrayList<String> listImage, int position) {
        listImage.remove(position);
    }

    private void updateUI() {
        adapter.notifyDataSetChanged();
        adapterEdit.notifyDataSetChanged();
        setViewRecyclerview();
    }

    private void updateFilter(int a) {
        switch (a) {
            case 1: {
                filter.setText(getResources().getString(R.string._event));
                break;
            }
            case 2: {
                filter.setText(getResources().getString(R.string._mood));

                break;
            }
            case 3: {
                filter.setText(getResources().getString(R.string._work));

                break;
            }
            case 4: {
                filter.setText(getResources().getString(R.string._shopping));

                break;
            }
            case 5: {
                filter.setText(getResources().getString(R.string._travel));

                break;
            }
            case 6: {
                filter.setText(getResources().getString(R.string._celebration));

                break;
            }
            default: {
                filter.setText(getResources().getString(R.string._celebration));
            }
        }
    }

    private void updateVote(int color) {

        switch (color) {
            case 2: {
                background.setBackgroundResource(R.drawable.bg_gradent_2);
                break;
            }
            case 3: {
                background.setBackgroundResource(R.drawable.bg_gradent_3);

                break;
            }
            case 4: {
                background.setBackgroundResource(R.drawable.bg_gradent_4);

                break;
            }
            case 5: {
                background.setBackgroundResource(R.drawable.bg_gradent_5);

                break;
            }
            case 6: {
                background.setBackgroundResource(R.drawable.bg_gradent_6);

                break;
            }
            case 7: {
                background.setBackgroundResource(R.drawable.bg_gradent_7);
                break;
            }
            case 8: {
                background.setBackgroundResource(R.drawable.bg_gradent_8);

                break;
            }
            case 9: {
                background.setBackgroundResource(R.drawable.bg_gradent_9);

                break;
            }
            case 10: {
                background.setBackgroundResource(R.drawable.bg_gradent_10);

                break;
            }
            case 11: {
                background.setBackgroundResource(R.drawable.bg_gradent_11);

                break;
            }
            case 12: {
                background.setBackgroundResource(R.drawable.bg_gradent_12);

                break;
            }
            default: {
                background.setBackgroundResource(R.drawable.bg_gradent_1);

                break;
            }
        }

    }

    private void setSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                employees);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEdit.setAdapter(adapter);
        spinnerEdit.setSelection(filterEdit - 1);
        spinnerEdit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterEdit = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void initClick() {
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLayoutEdit();
            }
        });
        saveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoddily();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.out_bottom, R.anim.in_bottom);
            }
        });
        imageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromAlbum();
            }
        });
        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
        btnMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mic();
            }
        });
        dateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateClick();
            }
        });
        btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color();
            }
        });
        btnClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateClick();
            }
        });

    }

    private void color() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ShowDiaryActivity.this);
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

    private void dateClick() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ShowDiaryActivity.this);
        ViewGroup viewGroup = ShowDiaryActivity.this.findViewById(android.R.id.content);
        View view = LayoutInflater.from(ShowDiaryActivity.this).inflate(R.layout.number_picker, viewGroup, false);
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
                    dateEdit.setText(dateResult);
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
            return isDay <= 30;
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

    private void delete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ShowDiaryActivity.this);
        builder.setTitle(getResources().getString(R.string._delete));
        builder.setMessage(getResources().getString(R.string._messenger_delete));
        builder.setPositiveButton(getResources().getString(R.string._yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                new DatabaseHelper(ShowDiaryActivity.this).delete(model);
                Toast.makeText(ShowDiaryActivity.this, getResources().getString(R.string._deleted), Toast.LENGTH_SHORT).show();
                finish();
                dialog.dismiss();
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

    private void shoddily() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ShowDiaryActivity.this);
        builder.setTitle(getResources().getString(R.string._save));
        builder.setMessage(getResources().getString(R.string._messenger));
        builder.setPositiveButton(getResources().getString(R.string._yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                save();
                setLayoutShow();
                dialog.dismiss();
                // finish();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string._no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setLayoutShow();
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void save() {
        String image = "";
        for (String s : listPath2) {
            image = image + s + "<->";
        }
        Log.d("TAG", "save: " + image);
        model.setTitle(titleEdit.getText().toString());
        model.setContent(bodyEdit.getText().toString());
        model.setDate(dateEdit.getText().toString());
        model.setFilter(filterEdit);
        model.setVote(mColor);
        model.setImage(image.trim());
        boolean update = new DatabaseHelper(this).update(model);
        if (update) {
            Toast.makeText(this, getResources().getString(R.string._update_successful), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getResources().getString(R.string._update_failded), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (layout_edit.getVisibility() == View.VISIBLE) {
            shoddily();
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.out_bottom, R.anim.in_bottom);
        }

    }

    private void getMultipleImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURES);
    }

    private void getImageFromAlbum() {
        try {
            Intent i = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, SELECT_PICTURES);
        } catch (Exception exp) {
            Log.i("Error", exp.toString());
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_PICTURES: {
                if (resultCode == RESULT_OK) {
                    String path;
                    if (data.getClipData() != null) {
                        int count = data.getClipData().getItemCount();
                        for (int i = 0; i < count; i++) {
                            Uri imageUri = data.getClipData().getItemAt(i).getUri();
                            path = ImageFilePath.getPath(ShowDiaryActivity.this, imageUri);
                            listPath2.add(path);
                        }
                    } else if (data.getData() != null) {
                        Uri imageUri = data.getData();
                        path = ImageFilePath.getPath(ShowDiaryActivity.this, imageUri);
                        listPath2.add(path);
                    }

                }

                updateUI();
                break;
            }
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //txtSpeechInput.setText(result.get(0));
                    if (titleEdit.isFocused()) {
                        titleEdit.setText((titleEdit.getText().toString() + " " + result.get(0)).trim());
                        titleEdit.requestFocusFromTouch();
                    } else if (bodyEdit.isFocused()) {
                        bodyEdit.setText((bodyEdit.getText().toString() + " " + result.get(0)).trim());
                        bodyEdit.requestFocusFromTouch();
                    }

                }
                break;
            }
        }


    }
}