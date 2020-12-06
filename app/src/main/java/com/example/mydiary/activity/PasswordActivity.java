package com.example.mydiary.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mydiary.MainActivity;
import com.example.mydiary.R;
import com.example.mydiary.receiver.AlarmReceiver;
import com.example.mydiary.utils.Pef;
import com.hanks.passcodeview.PasscodeView;

import java.util.Calendar;

public class PasswordActivity extends AppCompatActivity {
    private PasscodeView passcodeView;
    private int mSyntaxerror = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        passcodeView = findViewById(R.id.passcodeView);
        Pef.getReference(this);
        Pef.setFullScreen(PasswordActivity.this);


        initNotification();
        if (Pef.getBoolean("isBands")) {
            startActivity(new Intent(getApplicationContext(), BandsActivity.class));
            overridePendingTransition(R.anim.out_left, R.anim.in_left);
            finish();
        } else {
            if (Pef.getBoolean("isPassWord")) {
                passcodeView.setPasscodeLength(6)
                        .setLocalPasscode(Pef.getString("PassWord", "isPassWord"))
                        .setListener(new PasscodeView.PasscodeViewListener() {
                            @Override
                            public void onFail() {
                                Log.d("TAG", "onFail: đang ở đây nè <3");
                                mSyntaxerror++;
                                if (mSyntaxerror >= 5) {
                                    Pef.setBoolean("isBands", true);
                                    startActivity(new Intent(getApplicationContext(), BandsActivity.class));
                                    overridePendingTransition(R.anim.out_left, R.anim.in_left);
                                    finish();
                                }
                            }


                            @Override
                            public void onSuccess(String number) {
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                                overridePendingTransition(R.anim.out_left, R.anim.in_left);
                            }
                        });

            } else {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                overridePendingTransition(R.anim.out_left, R.anim.in_left);
            }
        }


    }

    private void hideSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }


    private void initNotification() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE, 0);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(PasswordActivity.this, AlarmReceiver.class);
        intent.putExtra("key", getString(R.string._messenger_remind));
        intent.putExtra("id", 1000);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1000, intent, 0);
        manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
        ComponentName receiver = new ComponentName(PasswordActivity.this, AlarmReceiver.class);
        PackageManager pm = this.getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }
}