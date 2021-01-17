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
import com.example.mydiary.activity.ShowDiaryActivity;
import com.example.mydiary.callback.OnClickItem;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.util.ArrayList;

public class ImageAdapterEdit extends RecyclerView.Adapter<ImageAdapterEdit.ViewHolder> {
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
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_image, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String s = list.get(choice).trim();
        if (!s.isEmpty()) {
            File file = new File(s);
            Glide.with(context)
                    .load(file)
                    .centerCrop()
                    .into(holder.imageView);
        } else {
            holder.layout.setVisibility(View.GONE);
        }

        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItem.click(position);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder build = new android.app.AlertDialog.Builder(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                ViewGroup viewGroup = v.findViewById(android.R.id.content);
                View view1 = LayoutInflater.from(context).inflate(R.layout.item_full_image, viewGroup, false);
                ImageButton btnCancel = view1.findViewById(R.id.btnCancel);
                PhotoView imageShowFull = view1.findViewById(R.id.photo_view);
                String s = list.get(choice).trim();
                if (!s.isEmpty()) {
                    File file = new File(s);
                    Glide.with(context)
                            .load(file)
                            .into(imageShowFull);


                }
                build.setView(view1);
                final android.app.AlertDialog dialog = build.create();
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

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageButton mDelete;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            mDelete = itemView.findViewById(R.id.mDelete);
            layout = itemView.findViewById(R.id.layout);

        }


    }
}
