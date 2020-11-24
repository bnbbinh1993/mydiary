package com.example.mydiary.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

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
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            Pef.setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            Pef.setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
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