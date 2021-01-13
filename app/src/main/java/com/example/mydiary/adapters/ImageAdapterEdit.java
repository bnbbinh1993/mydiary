package com.example.mydiary.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mydiary.R;
import com.example.mydiary.callback.OnClickItem;

import java.io.File;
import java.util.ArrayList;

public class ImageAdapterEdit extends RecyclerView.Adapter<ImageAdapterEdit.ViewHolder>{
    private ArrayList<String> list = new ArrayList<>();
    private Context context;
    private int choice;
    private static OnClickItem onClickItem;

    public ImageAdapterEdit(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_image,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String s = list.get(choice).trim();
        if (!s.isEmpty()){
            File file = new File(s);
            if (!s.isEmpty()){
                Glide.with(context)
                        .load(file)
                        .centerCrop()
                        .into(holder.imageView);
            }else {
                holder.layout.setVisibility(View.GONE);
            }
        }else {
            holder.layout.setVisibility(View.GONE);
        }

        holder.mDetele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItem.click(position);
            }
        });


    }

    @Override
    public int getItemViewType(int position) {
        choice = position;
        return choice;
    }
    public void setOnClickItem(OnClickItem onClickItem1) {
        this.onClickItem = onClickItem1;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageButton mDetele;
        LinearLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            mDetele = itemView.findViewById(R.id.mDetele);
            layout = itemView.findViewById(R.id.layout);

        }


    }
}
