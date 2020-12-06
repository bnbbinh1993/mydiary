package com.example.mydiary.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mydiary.R;
import com.example.mydiary.utils.Pef;

public class BandsActivity extends AppCompatActivity {
    private EditText edtCode;
    private ImageButton btnSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bands);
        Pef.getReference(getApplication());
        Pef.getReference(this);
        Pef.setFullScreen(BandsActivity.this);
        edtCode = findViewById(R.id.edtCode);
        btnSend = findViewById(R.id.btnSend);
        edtCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length()==9){
                    btnSend.setVisibility(View.VISIBLE);
                }else {
                    btnSend.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()==9){
                    btnSend.setVisibility(View.VISIBLE);
                }else {
                    btnSend.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()==9){
                    btnSend.setVisibility(View.VISIBLE);
                }else {
                    btnSend.setVisibility(View.GONE);
                }
            }
        });


    }
}