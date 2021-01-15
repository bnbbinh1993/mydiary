package com.example.mydiary.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mydiary.R;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageSubAdapter extends RecyclerView.Adapter<ImageSubAdapter.ViewHodel> {
    private List<String> list = new ArrayList<>();
    private Activity context;
    private int i = 0;

    public ImageSubAdapter(List<String> list, Activity context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHodel(LayoutInflater.from(context).inflate(R.layout.item_wallpager, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHodel holder, int position) {
        String model = list.get(i);
        File file = new File(model);
        Glide.with(context)
                .load(file)
                .centerCrop()
                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                ViewGroup viewGroup = context.findViewById(android.R.id.content);
                View view1 = LayoutInflater.from(context).inflate(R.layout.item_full_image, viewGroup, false);
                ImageButton btnCancel = view1.findViewById(R.id.btnCancel);
                PhotoView imageShowFull = view1.findViewById(R.id.photo_view);
                File file = new File(model);
                Glide.with(context)
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

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        i = position;
        return i;
    }

    static class ViewHodel extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public ViewHodel(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }

}
