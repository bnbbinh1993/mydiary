package com.example.mydiary.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mydiary.R;
import com.example.mydiary.callback.OnClickItem;

import java.io.File;
import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>{
    private ArrayList<String> list = new ArrayList<>();
    private Context context;
    private int choice;
    private OnClickItem clickItem;

    public ImageAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_image_recyclerview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String s = list.get(choice).trim();
        File file = new File(s);
        if (!s.isEmpty()){
            Glide.with(context)
                    .load(file)
                    .centerCrop()
                    .into(holder.imageView);
        }else {
            holder.imageView.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickItem.click(position);
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        choice = position;
        return choice;
    }
    public void setOnClickItem(OnClickItem onClickItem1) {
        this.clickItem = onClickItem1;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
