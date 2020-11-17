package com.example.mydiary.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewpager.widget.PagerAdapter;


import com.bumptech.glide.Glide;
import com.example.mydiary.R;

import java.util.ArrayList;

public class ViewPageAdapter extends PagerAdapter {
    private ArrayList<String> list = new ArrayList<>();

    public ViewPageAdapter(ArrayList<String> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_viewpager,container,false);
        ImageView image = view.findViewById(R.id.image);
        Glide.with(container.getContext()).load(list.get(position)).into(image);
        container.addView(view);
        return view;
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
