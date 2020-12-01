package com.example.mydiary.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.mydiary.R;
import com.example.mydiary.adapters.DairyAdapter;
import com.google.android.material.tabs.TabLayout;


public class DairyFragment extends Fragment {
    private ViewPager viewPager;
    private TabLayout tablayout;
    private DairyAdapter adapter;

    public static DairyFragment newInstance() {
        DairyFragment fragment = new DairyFragment();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_dairy, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        viewPager = view.findViewById(R.id.viewpager);
        tablayout = view.findViewById(R.id.tablayout);
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new DairyAdapter(getChildFragmentManager());
        adapter.addFragment(new ShowFragment(),"Daily");
        adapter.addFragment(new ImageFragment(),"Image");
        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);
    }

}