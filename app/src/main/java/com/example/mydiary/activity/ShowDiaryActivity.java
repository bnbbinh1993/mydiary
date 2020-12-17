package com.example.mydiary.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
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
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mydiary.R;
import com.example.mydiary.adapters.ImageAdapter;
import com.example.mydiary.adapters.ImageAdapterEdit;
import com.example.mydiary.database.DatabaseHelper;
import com.example.mydiary.models.Diary;
import com.example.mydiary.utils.ImageFilePath;
import com.example.mydiary.utils.OnClickItem;
import com.example.mydiary.utils.Pef;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

public class ShowDiaryActivity extends AppCompatActivity {
    private ImageButton edit;
    private ImageButton back;
    private ImageButton imageEdit;
    private ImageButton saveEdit;
    private ImageButton btnMic;
    private ImageButton mDelete;
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
    private String path;
    private String employees[];
    private String s;
    private ArrayList<String> listPath = new ArrayList<>();
    private ArrayList<String> listPath2 = new ArrayList<>();
    private ArrayList<Diary> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_diary);
        init();
        setUp();
        setSpinner();
        initClick();
        setLayoutShow();

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
        Pef.setFullScreen(ShowDiaryActivity.this);
        employees = new String[]{
                getResources().getString(R.string._event),
                getResources().getString(R.string._mood),
                getResources().getString(R.string._work),
                getResources().getString(R.string._shopping),
                getResources().getString(R.string._travel),
                getResources().getString(R.string._celebration)};

        list.clear();
        listPath.clear();
        listPath2.clear();


        int i = getIntent().getIntExtra("position", 0);
        list = new DatabaseHelper(this).getData();
        Collections.reverse(list);
        String s[] = list.get(i).getImage().trim().split("<->");
        if (s.length > 0) {
            for (int j = 0; j < s.length; j++) {
                if (!s[j].isEmpty()) {
                    listPath.add(s[j]);
                    listPath2.add(s[j]);
                }
            }
        }


        adapter = new ImageAdapter(listPath, this);
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerview.setAdapter(adapter);
        title.setText(list.get(i).getTitle());
        body.setText(list.get(i).getContent());
        date.setText(list.get(i).getDate());
        //filter.setText(list.get(i).getFilter());
        filterEdit = list.get(i).getFilter();
        updateFilter(filterEdit);
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

        titleEdit.setText(list.get(i).getTitle());
        bodyEdit.setText(list.get(i).getContent());
        dateEdit.setText(list.get(i).getDate());
        //spinnerEdit.setPromptId(list.get(i).getFilter());
        adapterEdit = new ImageAdapterEdit(listPath2, this);
        mRecyclerviewEdit.setHasFixedSize(true);
        mRecyclerviewEdit.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerviewEdit.setAdapter(adapterEdit);
        adapterEdit.setOnClickItem(new OnClickItem() {
            @Override
            public void click(int position) {
                Log.d("TAG", "click: " + listPath2.size());
                Log.d("TAG", "click: " + position);
                daleteImage(listPath2, position);
                updateUI();
            }

            @Override
            public void longClick(int position) {

            }
        });
    }

    private void daleteImage(ArrayList<String> listImage, int position) {
        listImage.remove(position);
    }

    private void updateUI() {
        setViewRecyclerview();
        adapter.notifyDataSetChanged();
        adapterEdit.notifyDataSetChanged();
    }

    private void updateFilter(int filterEdit) {
        switch (filterEdit) {
            case 1: {
                filter.setText(getResources().getString(R.string._event));
                background.setBackgroundColor(getResources().getColor(R.color.event));
                break;
            }
            case 2: {
                filter.setText(getResources().getString(R.string._mood));
                background.setBackgroundColor(getResources().getColor(R.color.mood));
                break;
            }
            case 3: {
                filter.setText(getResources().getString(R.string._work));
                background.setBackgroundColor(getResources().getColor(R.color.work));
                break;
            }
            case 4: {
                filter.setText(getResources().getString(R.string._shopping));
                background.setBackgroundColor(getResources().getColor(R.color.shopping));
                break;
            }
            case 5: {
                filter.setText(getResources().getString(R.string._travel));
                background.setBackgroundColor(getResources().getColor(R.color.travel));
                break;
            }
            case 6: {
                filter.setText(getResources().getString(R.string._celebration));
                background.setBackgroundColor(getResources().getColor(R.color.cele));
                break;
            }
            default: {
                filter.setText(getResources().getString(R.string._event));
                background.setBackgroundColor(getResources().getColor(R.color.event));
                break;
            }
        }
    }

    private void updateData(ArrayList<String> viewModels) {
        listPath.clear();
        listPath.addAll(viewModels);
        adapterEdit.notifyDataSetChanged();
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
                updateFilter(filterEdit);
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
                showdialog();
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

        setNumberPickerTextColor(day, Color.BLACK);
        setNumberPickerTextColor(month, Color.BLACK);
        setNumberPickerTextColor(year, Color.BLACK);

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
                new DatabaseHelper(ShowDiaryActivity.this).delete(list.get(getIntent().getIntExtra("position", 0)));
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

    private void showdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ShowDiaryActivity.this);
        builder.setTitle("Lưu dữ liệu");
        builder.setTitle(getResources().getString(R.string._messenger));
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
        int i = getIntent().getIntExtra("position", 0);
        list = new DatabaseHelper(this).getData();
        Collections.reverse(list);
        String image = "";
        for (String s : listPath2) {
            image = image + s + "<->";
        }
        Log.d("TAG", "save: " + image);
        Diary diary = list.get(i);
        diary.setTitle(titleEdit.getText().toString());
        diary.setContent(bodyEdit.getText().toString());
        diary.setDate(dateEdit.getText().toString());
        diary.setFilter(filterEdit);
        diary.setVote(filterEdit);
        diary.setImage(image.trim());
        boolean update = new DatabaseHelper(this).update(diary);
        if (update) {
            Toast.makeText(this, getResources().getString(R.string._update_successful), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getResources().getString(R.string._update_failded), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (layout_edit.getVisibility() == View.VISIBLE) {
            showdialog();
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