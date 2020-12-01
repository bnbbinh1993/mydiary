package com.example.mydiary.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.mydiary.R;


public class AnalysisFragment extends Fragment {



    public static AnalysisFragment newInstance() {
        AnalysisFragment fragment = new AnalysisFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analysis, container, false);

        return view;
    }

}