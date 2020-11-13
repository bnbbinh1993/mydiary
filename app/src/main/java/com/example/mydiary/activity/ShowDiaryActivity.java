package com.example.mydiary.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mydiary.R;
import com.example.mydiary.adapters.ImageAdapter;
import com.example.mydiary.adapters.ImageAdapterEdit;
import com.example.mydiary.adapters.ShowAdapter;
import com.example.mydiary.database.DatabaseHelper;
import com.example.mydiary.models.Diary;
import com.example.mydiary.utils.ImageFilePath;
import com.example.mydiary.utils.OnClickItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

public class ShowDiaryActivity extends AppCompatActivity {
    private ImageButton edit;
    private ImageButton back;
    private ImageButton imageEdit;
    private ImageButton saveEdit;
    private TextView date;
    private TextView filter;
    private TextView title;
    private TextView body;
    private TextView titleEdit;
    private TextView bodyEdit;
    private TextView dateEdit;
    private FloatingActionButton fab;
    private RecyclerView mRecyclerviewEdit;
    private RecyclerView mRecyclerview;
    private RelativeLayout layout_show;
    private RelativeLayout layout_edit;
    private RelativeLayout background;
    private ImageAdapterEdit adapterEdit;
    private ImageAdapter adapter;
    private Spinner spinnerEdit;
    private static final int SELECT_PICTURES = 1;
    private int filterEdit;
    private String path;
    private String employees[];
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
        fab = findViewById(R.id.fab);
        background = findViewById(R.id.background);


        layout_edit = findViewById(R.id.layout_edit);
        titleEdit = findViewById(R.id.titleEdit);
        bodyEdit = findViewById(R.id.bodyEdit);
        imageEdit = findViewById(R.id.imageEdit);
        saveEdit = findViewById(R.id.saveEdit);
        dateEdit = findViewById(R.id.dateEdit);
        spinnerEdit = findViewById(R.id.spinnerEdit);
        mRecyclerviewEdit = findViewById(R.id.mRecyclerviewEdit);

    }

    private void setLayoutShow() {
        setUp();
        Log.d("TAG", "setLayoutShow: " + listPath.size());
        layout_show.setVisibility(View.VISIBLE);
        layout_edit.setVisibility(View.GONE);

    }

    private void setLayoutEdit() {
        setUp();
        Log.d("TAG", "setLayoutEdit: " + listPath.size());
        layout_edit.setVisibility(View.VISIBLE);
        layout_show.setVisibility(View.GONE);
//        if (listPath.size()>0){
//            if (listPath.get(0).equals(" ")){
//                listPath.clear();
//                mRecyclerviewEdit.setVisibility(View.GONE);
//                mRecyclerview.setVisibility(View.GONE);
//            }else {
//                mRecyclerviewEdit.setVisibility(View.VISIBLE);
//                mRecyclerview.setVisibility(View.VISIBLE);
//            }
//        }
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

    private void setUp() {

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
        String s[] = list.get(i).getImage().trim().split("\\s+");
        if (s.length > 0) {
            for (int j = 0; j < s.length; j++) {
                listPath.add(s[j]);
                listPath2.add(s[j]);
            }
        }

        adapter = new ImageAdapter(listPath, this);
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerview.setAdapter(adapter);
        title.setText(list.get(i).getTitle());
        body.setText(list.get(i).getContent());
        date.setText(list.get(i).getDate());
        //filter.setText(list.get(i).getFilter());
        filterEdit = list.get(i).getFilter();
        updateFilter(filterEdit);

        titleEdit.setText(list.get(i).getTitle());
        bodyEdit.setText(list.get(i).getContent());
        dateEdit.setText(list.get(i).getDate());
        //spinnerEdit.setPromptId(list.get(i).getFilter());
        adapterEdit = new ImageAdapterEdit(listPath2, this);
        mRecyclerviewEdit.setHasFixedSize(true);
        mRecyclerviewEdit.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerviewEdit.setAdapter(adapterEdit);
        adapterEdit.setOnClickItem(new OnClickItem() {
            @Override
            public void click(int position) {
                daleteImage(listPath2,position);
            }

            @Override
            public void longClick(int position) {

            }
        });
    }
    private void daleteImage(ArrayList<String> listImage,int position){
        listImage.remove(position);
        updateUI();
    }
    private void updateUI(){
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
            }
        });
        imageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMultipleImage();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });

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
            image += " " + s;
        }
        Log.d("TAG", "save: " + image);
        Diary diary = list.get(i);
        diary.setTitle(title.getText().toString());
        diary.setContent(body.getText().toString());
        diary.setDate(date.getText().toString());
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
                        path = ImageFilePath.getPath(ShowDiaryActivity.this, imageUri);
                        listPath2.add(path);
                    }
                } else if (data.getData() != null) {
                    String imagePath = data.getData().getPath();
                    Uri imageUri = data.getData();
                    path = ImageFilePath.getPath(ShowDiaryActivity.this, imageUri);
                    listPath2.add(path);
                }

            }
        }
        updateUI();

    }
}