package com.example.mydiary.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.mydiary.R;
import com.example.mydiary.utils.Pef;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class FinishActivity extends AppCompatActivity {
    private AppCompatButton mCreate;
    private AppCompatButton mDone;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.id_test2));
        MobileAds.initialize(this, getResources().getString(R.string.app_test));
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("C04B1BFFB0774708339BC273F8A43708")
                .build();
        mInterstitialAd.loadAd(adRequest);


        Pef.getReference(this);
        Pef.setFullScreen(FinishActivity.this);
        init();
        initClick();

    }


    private void init() {
        mDone = findViewById(R.id.mDone);
        mCreate = findViewById(R.id.mCreate);
    }

    private void initClick() {
        int i = getIntent().getIntExtra("I", 0);
        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 0) {
                    startActivity(new Intent(getApplication(), CountDownActivity.class));
                    finish();
                    overridePendingTransition(R.anim.out_left, R.anim.in_left);
                } else if (i == 1) {
                    startActivity(new Intent(getApplication(), NoteActivity.class));
                    finish();
                    overridePendingTransition(R.anim.out_left, R.anim.in_left);
                }
            }
        });
        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    finish();
                    overridePendingTransition(R.anim.out_left, R.anim.in_left);
                } else {
                    finish();
                    overridePendingTransition(R.anim.out_left, R.anim.in_left);
                }

//                if (mInterstitialAd.isLoading()) {
//                    mInterstitialAd.setAdListener(new AdListener() {
//                        @Override
//                        public void onAdLoaded() {
//                            showInterstitial();
//                        }
//
//                        @Override
//                        public void onAdClosed() {
//                            finish();
//                            overridePendingTransition(R.anim.out_left, R.anim.in_left);
//                        }
//
//                        @Override
//                        public void onAdFailedToLoad(int errorCode) {
//                            finish();
//                            overridePendingTransition(R.anim.out_left, R.anim.in_left);
//                        }
//
//                        @Override
//                        public void onAdLeftApplication() {
//                            finish();
//                            overridePendingTransition(R.anim.out_left, R.anim.in_left);
//                        }
//
//                        @Override
//                        public void onAdOpened() {
//                            finish();
//                            overridePendingTransition(R.anim.out_left, R.anim.in_left);
//                        }
//                    });
//                } else {
//                    finish();
//                    overridePendingTransition(R.anim.out_left, R.anim.in_left);
//                }

            }
        });
    }


    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.out_left, R.anim.in_left);
    }
}