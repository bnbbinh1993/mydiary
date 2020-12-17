package com.example.mydiary.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

        View view = inflater.inflate(R.layout.fragment_dairy, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setupViewPager(viewPager);
    }

    private void init(View view) {
        viewPager = view.findViewById(R.id.viewpager);
        tablayout = view.findViewById(R.id.tablayout);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new DairyAdapter(getChildFragmentManager());
        adapter.addFragment(new ShowFragment(), getString(R.string._all));
        adapter.addFragment(new ImageFragment(), getString(R.string._image));
        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);
    }

}