package com.example.mydiary.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mydiary.R;
import com.example.mydiary.database.MyDatabaseHelper;
import com.example.mydiary.models.Show;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

public class NoteActivity extends AppCompatActivity {
    private ImageButton mBack;
    private ImageButton mSave;
    private ImageButton mTrue;
    private ImageButton mCancel;
    private ImageButton btnColor;
    private TextView mDate;
    private EditText mTitle;
    private EditText mContent;
    private RadioGroup group;
    private RadioButton rd1;
    private RadioButton rd2;
    private RadioButton rd3;
    private RadioButton rd4;
    private RadioButton rd5;
    private RadioButton rd6;
    private MyDatabaseHelper helper;
    private BottomSheetBehavior behavior;
    private CoordinatorLayout background;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
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


        init();
        initSetUp();
        setOnclick();
        RDGroup();

    }

    private void initSetUp() {
        helper = new MyDatabaseHelper(this);
        View bottomSheet = findViewById(R.id.viewColor);
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setPeekHeight(0);
        behavior.setHideable(true);
    }

    private void init() {
        mBack = findViewById(R.id.mBack);
        mSave = findViewById(R.id.mSave);
        mDate = findViewById(R.id.mDate);
        mTitle = findViewById(R.id.mTitle);
        mContent = findViewById(R.id.mContent);
        btnColor = findViewById(R.id.btnColor);
        mTrue = findViewById(R.id.mTrue);
        mCancel = findViewById(R.id.mCancel);
        background = findViewById(R.id.background);
        group = findViewById(R.id.group);
        rd1 = findViewById(R.id.rd1);
        rd2 = findViewById(R.id.rd2);
        rd3 = findViewById(R.id.rd3);
        rd4 = findViewById(R.id.rd4);
        rd5 = findViewById(R.id.rd5);
        rd6 = findViewById(R.id.rd6);
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
                finish();
            }
        });
        btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        mTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

    }
    private void RDGroup(){
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rd1:{
                        background.setBackgroundColor(getResources().getColor(R.color.note));
                        break;
                    }
                    case R.id.rd2:{
                        background.setBackgroundColor(getResources().getColor(R.color.event));
                        break;
                    }
                    case R.id.rd3:{
                        background.setBackgroundColor(getResources().getColor(R.color.mood));
                        break;
                    }
                    case R.id.rd4:{
                        background.setBackgroundColor(getResources().getColor(R.color.work));
                        break;
                    }
                    case R.id.rd5:{
                        background.setBackgroundColor(getResources().getColor(R.color.travel));
                        break;
                    }
                    case R.id.rd6:{
                        background.setBackgroundColor(getResources().getColor(R.color.shopping));
                        break;
                    }
                }
            }
        });
    }
    private void save(){
        String title = mTitle.getText().toString();
        String content = mContent.getText().toString();
        String date = mDate.getText().toString();
        int filter = 1;
        int vote = 0;
        Show show = new Show();
        show.setTitle(title);
        show.setContent(content);
        show.setDate(date);
        show.setFilter(filter);
        show.setVote(vote);
        helper.adđ(show);
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