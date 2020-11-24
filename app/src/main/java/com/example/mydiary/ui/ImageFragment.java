package com.example.mydiary.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.mydiary.R;
import com.example.mydiary.database.DatabaseHelper;
import com.example.mydiary.models.Diary;

import java.util.ArrayList;
import java.util.Collections;

public class ImageFragment extends Fragment {
    private GridView mGridView;
    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<Diary> res = new ArrayList<>();
    private DatabaseHelper helper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_image, container, false);
        initUI(v);
        initAction();
        return v;
    }

    private void initAction() {
        helper = new DatabaseHelper(getContext());
        res = helper.getData();
        for (int i = 0;i<res.size();i++){
            String s[] = res.get(i).getImage().trim().split("<->");
            if (s.length > 0) {
                for (int j = 0; j < s.length; j++) {
                    if (!s[j].isEmpty()) {
                        list.add(s[j]);
                    }
                }
            }
        }
        Collections.reverse(list);
        CustomAdapter customAdapter = new CustomAdapter();
        mGridView.setAdapter(customAdapter);
    }

    private void initUI(View v) {
        mGridView = v.findViewById(R.id.mGridView);
    }
    private class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = getLayoutInflater().inflate(R.layout.item_wallpager, null);

            ImageView image = view1.findViewById(R.id.image);

            Glide.with(getContext())
                    .load(list.get(i))
                    .centerCrop()
                    .into(image);
            return view1;

        }
    }
}