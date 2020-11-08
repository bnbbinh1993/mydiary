package com.example.mydiary.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mydiary.R;
import com.example.mydiary.database.DatabaseHelper;
import com.example.mydiary.models.Diary;

public class MoodActivity extends AppCompatActivity {
    private Spinner spinnerEmployee;
    private String employees[] = {"Vui", "Buồn", "Hạnh phúc"};
    private ImageButton mBack;
    private ImageButton mSave;
    private TextView mDate;
    private String title;
    private EditText mContent;
    private DatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);
        helper = new DatabaseHelper(this);
        init();
        setSpinner();
        setOnclick();


    }
    private void setOnclick(){
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
    }
    private void init() {
        spinnerEmployee = findViewById(R.id.spinner_employee);
        mBack = findViewById(R.id.mBack);
        mSave = findViewById(R.id.mSave);
        mDate = findViewById(R.id.mDate);
        mContent = findViewById(R.id.mContent);
    }
    private void save(){
        String content = mContent.getText().toString();
        String date = mDate.getText().toString();
        int filter = 2;
        int vote = 0;
        Diary diary = new Diary();
        diary.setTitle(title);
        diary.setContent(content);
        diary.setDate(date);
        diary.setFilter(filter);
        diary.setVote(vote);
        helper.adđ(diary);
        finish();
    }

    private void setSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                employees);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEmployee.setAdapter(adapter);
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
    }
}