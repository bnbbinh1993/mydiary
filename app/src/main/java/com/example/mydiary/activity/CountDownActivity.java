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
import com.example.mydiary.database.MyDatabaseHelper;

public class CountDownActivity extends AppCompatActivity {
    private Spinner spinnerEmployee;
    private String employees[] = {"Sự kiện", "Du lịch", "Công việc","Sinh nhật"};
    private ImageButton mBack;
    private ImageButton mSave;
    private TextView mDate;
    private String title;
    private String aaaa;
    private MyDatabaseHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down);
        helper = new MyDatabaseHelper(this);
        init();
        setSpinner();
        setOnclick();
    }

    private void init() {
        spinnerEmployee = findViewById(R.id.spinner_employee);
        mBack = findViewById(R.id.mBack);
        mSave = findViewById(R.id.mSave);
        mDate = findViewById(R.id.mDate);

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
                //save();
            }
        });
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