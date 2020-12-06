package com.example.mydiary.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mydiary.R;
import com.example.mydiary.utils.Pef;
import com.hanks.passcodeview.PasscodeView;

public class CreatePassWordActivity extends AppCompatActivity {

    private PasscodeView passcodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pass_word);

        Pef.getReference(this);
        Pef.setFullScreen(CreatePassWordActivity.this);

        passcodeView = findViewById(R.id.passcodeView);
        Pef.getReference(getApplication());

        passcodeView.setPasscodeLength(6).setListener(new PasscodeView.PasscodeViewListener() {
            @Override
            public void onFail() {
                Toast.makeText(CreatePassWordActivity.this, "Faild", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String number) {
                Pef.setBoolean("isPassWord", true);
                Pef.setString("PassWord", passcodeView.getLocalPasscode());
                finish();
                overridePendingTransition(R.anim.out_left, R.anim.in_left);
            }
        });

    }

    private void hideSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.out_left, R.anim.in_left);
    }
}