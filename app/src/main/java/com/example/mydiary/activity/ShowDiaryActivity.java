package com.example.mydiary.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mydiary.R;
import com.example.mydiary.adapters.ShowAdapter;
import com.example.mydiary.database.DatabaseHelper;
import com.example.mydiary.models.Diary;

import java.util.ArrayList;

public class ShowDiaryActivity extends AppCompatActivity {
    private ImageButton edit;
    private ImageButton back;
    private TextView date;
    private TextView filter;
    private TextView title;
    private TextView body;
    private RecyclerView mRecyclerview;
    private RelativeLayout layout_show;
    private ShowAdapter adapter;

    private Spinner spinnerEdit;
    private ImageButton imageEdit;
    private ImageButton saveEdit;
    private TextView dateEdit;
    private ImageButton backEdit;
    private RecyclerView mRecyclerviewEdit;
    private RelativeLayout layout_edit;
    private TextView titleEdit;
    private TextView bodyEdit;

    private ArrayList<Diary> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_diary);
        init();
        setUp();
    }


    private void init() {
        edit = findViewById(R.id.edit);
        back = findViewById(R.id.back);
        filter = findViewById(R.id.filter);
        back = findViewById(R.id.back);
        body = findViewById(R.id.body);
        title = findViewById(R.id.title);
        date = findViewById(R.id.date);
        mRecyclerview = findViewById(R.id.mRecyclerview);
        layout_show = findViewById(R.id.layout_show);

        backEdit = findViewById(R.id.backEdit);
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

    private void setLayoutEdi() {
        layout_edit.setVisibility(View.VISIBLE);
        layout_show.setVisibility(View.GONE);
    }

    private void setUp() {
        int i = getIntent().getIntExtra("position", 0);
        list = new DatabaseHelper(this).getData();
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerview.setAdapter(adapter);
        title.setText(list.get(i).getTitle());
        body.setText(list.get(i).getContent());
        date.setText(list.get(i).getDate());
        filter.setText(list.get(i).getFilter());


    }

}