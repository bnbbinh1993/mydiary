package com.example.mydiary.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydiary.R;
import com.example.mydiary.activity.ShowDiaryActivity;
import com.example.mydiary.adapters.ShowAdapter;
import com.example.mydiary.database.DatabaseHelper;
import com.example.mydiary.models.Diary;
import com.example.mydiary.utils.OnClickItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;

public class ShowFragment extends Fragment {
    private RecyclerView recyclerView;
    private ShowAdapter adapter;
    private ArrayList<Diary> list;
    private TabLayout tabs2;
    private LinearLayout no_item;
    private DatabaseHelper helper;
    private int filter;
    private FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show, container, false);
        init(view);
        setUp();
        Filter();
        initClick();
        fabRecyclerview();
        return view;
    }

    private void initClick() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
    }

    private void setUp() {
        filter = 0;
        helper = new DatabaseHelper(getContext());
        list = new ArrayList<>();
        setView();
    }

    private void init(View view) {
        tabs2 = view.findViewById(R.id.tabs2);
        no_item = view.findViewById(R.id.no_item);
        recyclerView = view.findViewById(R.id.mRecyclerview);
        fab = view.findViewById(R.id.fab);
    }

    private void delete() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getResources().getString(R.string._delete));
        builder.setMessage(getResources().getString(R.string._messenger_delete_all));
        builder.setPositiveButton(getResources().getString(R.string._yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                helper.deleteAll(list);
                list.clear();
                updateUI();
                Filter();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string._no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void fabRecyclerview(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.hide();
                } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                    fab.show();
                }
            }
        });
    }


    private void Filter() {
        tabs2.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    filter = 0;
                } else if (tab.getPosition() == 1) {
                    filter = 1;
                } else if (tab.getPosition() == 2) {
                    filter = 2;
                } else if (tab.getPosition() == 3) {
                    filter = 3;
                } else if (tab.getPosition() == 4) {
                    filter = 4;
                }
                setView();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setView() {
        list.clear();
        if (filter == 0) {
            list = helper.getData();
        } else {
            for (Diary diary : helper.getData()) {
                if (diary.getFilter() == filter) {
                    list.add(diary);
                }
            }
        }

        updateUI();

        Collections.reverse(list);
        adapter = new ShowAdapter(getContext(), list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnClickItem(new OnClickItem() {
            @Override
            public void click(int position) {
                Intent intent = new Intent(getContext(), ShowDiaryActivity.class);
                intent.putExtra("position",position);
                startActivity(intent);
            }

            @Override
            public void longClick(int position) {

            }
        });
    }

    private void updateUI() {
        if (list.size() <= 0){
            no_item.setVisibility(View.VISIBLE);
        }else {
            no_item.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setView();
    }
}