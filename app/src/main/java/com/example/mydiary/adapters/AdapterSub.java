package com.example.mydiary.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydiary.R;
import com.example.mydiary.activity.ShowDiaryActivity;
import com.example.mydiary.models.Diary;
import com.example.mydiary.models.ItemSub;
import com.example.mydiary.utils.OnClickItem;

import java.util.List;

public class AdapterSub extends RecyclerView.Adapter<AdapterSub.ViewHolder> {


    private List<ItemSub> list;
    protected int i = 0;
    private Activity activity;

    public AdapterSub(List<ItemSub> list, Activity activity) {
        this.list = list;
        this.activity = activity;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sub, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemSub model = list.get(i);
        holder.mDate.setText(model.getmDate());
        List<Diary> diaryList = model.getList();
        ShowAdapter showAdapter = new ShowAdapter(diaryList);
        holder.mRecyclerview.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        holder.mRecyclerview.setHasFixedSize(true);
        holder.mRecyclerview.setAdapter(showAdapter);
        showAdapter.setOnClickItem(new OnClickItem() {
            @Override
            public void click(int position) {
                Intent intent = new Intent(activity, ShowDiaryActivity.class);
                intent.putExtra("position", position);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.out_bottom, R.anim.in_bottom);
            }

            @Override
            public void longClick(int position) {

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

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mDate;
        private RecyclerView mRecyclerview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mRecyclerview = itemView.findViewById(R.id.mRecyclerview);
            mDate = itemView.findViewById(R.id.txtDate);

        }
    }
}
