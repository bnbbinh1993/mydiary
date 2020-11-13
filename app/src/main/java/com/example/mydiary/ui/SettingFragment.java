package com.example.mydiary.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.mydiary.R;

public class SettingFragment extends Fragment {
    private TextView mFeedback,mLogin,mPollicy;
    private Switch mSound;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_setting, container, false);
        init(view);
        initClick();
        return view;
    }

    private void initClick() {
        mFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedback();
            }
        });
        mPollicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pollicy();
            }
        });
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        mSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sound();
            }
        });
    }

    private void pollicy() {
        Toast.makeText(getContext(), "wait...", Toast.LENGTH_SHORT).show();
    }
    private void login() {
        Toast.makeText(getContext(), "wait...", Toast.LENGTH_SHORT).show();
    }
    private void sound() {
        Toast.makeText(getContext(), "wait...", Toast.LENGTH_SHORT).show();
    }

    private void init(View view) {
        mFeedback = view.findViewById(R.id.mFeedback);
        mLogin = view.findViewById(R.id.mLogin);
        mPollicy = view.findViewById(R.id.mPollicy);
        mSound = view.findViewById(R.id.mSound);
    }

    private void feedback(){
        Intent mailIntent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:?subject=" + "title: "+ "&body=" + "content: " + "&to=" + "bnbbinh@mail.com");
        mailIntent.setData(data);
        startActivity(Intent.createChooser(mailIntent, "Send Gmail"));
    }
}