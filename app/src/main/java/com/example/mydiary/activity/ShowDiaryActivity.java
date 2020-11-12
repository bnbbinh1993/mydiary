package com.example.mydiary.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.example.mydiary.utils.OnClickItem;

import java.util.ArrayList;
import java.util.Collections;

public class ShowDiaryActivity extends AppCompatActivity {
    private ImageButton edit;
    private ImageButton back;
    private TextView date;
    private TextView filter;
    private TextView title;
    private TextView body;
    private RecyclerView mRecyclerview;
    private RelativeLayout layout_show;
    private ImageAdapterEdit adapterEdit;
    private ImageAdapter adapter;
    private Spinner spinnerEdit;
    private ImageButton imageEdit;
    private ImageButton saveEdit;
    private TextView dateEdit;

    private RecyclerView mRecyclerviewEdit;
    private RelativeLayout layout_edit;
    private TextView titleEdit;
    private TextView bodyEdit;

    private int filterEdit;
    private String employees[] = {"Vui", "Buồn", "Hạnh phúc"};
    private ArrayList<String> listPath = new ArrayList<>();
    private ArrayList<String> resPath = new ArrayList<>();

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
        layout_show.setVisibility(View.VISIBLE);
        layout_edit.setVisibility(View.GONE);
    }

    private void setLayoutEdit() {
        layout_edit.setVisibility(View.VISIBLE);
        layout_show.setVisibility(View.GONE);
    }

    private void setUp() {
        int i = getIntent().getIntExtra("position", 0);
        list = new DatabaseHelper(this).getData();
        Collections.reverse(list);
        resPath.clear();
        String s[] = list.get(i).getImage().split("\\s+");
        if (s.length > 0) {
            for (int j = 0; j < s.length; j++) {
                listPath.add(s[j]);
                resPath.add(s[j]);
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

        titleEdit.setText(list.get(i).getTitle());
        bodyEdit.setText(list.get(i).getContent());
        dateEdit.setText(list.get(i).getDate());
        //spinnerEdit.setPromptId(list.get(i).getFilter());
        adapterEdit = new ImageAdapterEdit(listPath, this);
        mRecyclerviewEdit.setHasFixedSize(true);
        mRecyclerviewEdit.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerviewEdit.setAdapter(adapterEdit);
        adapterEdit.setOnClickItem(new OnClickItem() {
            @Override
            public void click(int position) {
                Toast.makeText(ShowDiaryActivity.this, ""+position, Toast.LENGTH_SHORT).show();
                resPath.remove(position);
                updateData(resPath);
            }
            @Override
            public void longClick(int position) {

            }
        });
    }

    public void updateData(ArrayList<String> viewModels) {
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
        spinnerEdit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterEdit = position;
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

    }

    @Override
    public void onBackPressed() {
        if (layout_edit.getVisibility() == View.VISIBLE) {
            showdialog();
        } else {
            super.onBackPressed();
        }

    }
}