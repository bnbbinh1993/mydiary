package com.example.mydiary.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mydiary.R;
import com.example.mydiary.utils.Pef;
import com.hanks.passcodeview.PasscodeView;

public class ChangePasswordActivity extends AppCompatActivity {
    private PasscodeView passcodeView;
    private PasscodeView passcodeView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Pef.getReference(this);
        Pef.setFullScreen(ChangePasswordActivity.this);

        passcodeView = findViewById(R.id.passcodeView);
        passcodeView2 = findViewById(R.id.passcodeView2);
        Pef.getReference(getApplication());
        passcodeView.setPasscodeLength(6).setLocalPasscode(Pef.getString("PassWord", "isPassWord")).setListener(new PasscodeView.PasscodeViewListener() {
            @Override
            public void onFail() {
            }
            @Override
            public void onSuccess(String number) {
                passcodeView.setVisibility(View.GONE);
                passcodeView2.setVisibility(View.VISIBLE);
                passcodeView2.setPasscodeLength(6).setListener(new PasscodeView.PasscodeViewListener() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(String number) {
                        Pef.setString("PassWord", number);
                        finish();
                        overridePendingTransition(R.anim.out_left, R.anim.in_left);
                    }
                });
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}