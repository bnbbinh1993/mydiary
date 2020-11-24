package com.example.mydiary.ui;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.mydiary.R;
import com.example.mydiary.database.DatabaseHelper;
import com.example.mydiary.models.Diary;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class ImageFragment extends Fragment {
    private GridView mGridView;
    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<Diary> res = new ArrayList<>();
    private DatabaseHelper helper;
    private CustomAdapter customAdapter;
    private LinearLayout no_item;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_image, container, false);
        initUI(v);
        initAction();
        checkUI();
        return v;
    }

    private void checkUI() {
        list.clear();
        res = helper.getData();
        for (int i = 0; i < res.size(); i++) {
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
        if (list.size() > 0) {
            mGridView.setVisibility(View.VISIBLE);
            no_item.setVisibility(View.GONE);
        } else {
            mGridView.setVisibility(View.GONE);
            no_item.setVisibility(View.VISIBLE);
        }
    }

    private void initAction() {
        list.clear();
        helper = new DatabaseHelper(getContext());
        res = helper.getData();
        for (int i = 0; i < res.size(); i++) {
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
        customAdapter = new CustomAdapter();
        mGridView.setAdapter(customAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder build = new AlertDialog.Builder(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                ViewGroup viewGroup = getActivity().findViewById(android.R.id.content);
                View view1 = LayoutInflater.from(getContext()).inflate(R.layout.item_full_image, viewGroup, false);
                ImageButton btnCancel = view1.findViewById(R.id.btnCancel);
                PhotoView imageShowFull = view1.findViewById(R.id.photo_view);
                File file = new File(list.get(position));
                Glide.with(getContext())
                        .load(file)
                        .error(R.mipmap.ic_launcher)
                        .into(imageShowFull);


                build.setView(view1);
                final AlertDialog dialog = build.create();
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    private void initUI(View v) {
        mGridView = v.findViewById(R.id.mGridView);
        no_item = v.findViewById(R.id.no_item);
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

    @Override
    public void onStart() {
        super.onStart();
        checkUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkUI();
    }

    @Override
    public void onPause() {
        super.onPause();
        checkUI();
    }

}