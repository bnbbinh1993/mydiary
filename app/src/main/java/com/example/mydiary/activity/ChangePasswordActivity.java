package com.example.mydiary.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

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
}