package com.example.mydiary.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydiary.R;
import com.example.mydiary.activity.NoteActivity;
import com.example.mydiary.activity.ShowDiaryActivity;
import com.example.mydiary.adapters.ShowAdapter;
import com.example.mydiary.database.DatabaseHelper;
import com.example.mydiary.models.Diary;
import com.example.mydiary.utils.OnClickItem;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;

public class ShowFragment extends Fragment {
    private RecyclerView recyclerView;
    private ShowAdapter adapter;
    private static ArrayList<Diary> list;
    private TabLayout tabs2;
    private LinearLayout no_item;
    private DatabaseHelper helper;
    private int filter;
    private com.getbase.floatingactionbutton.FloatingActionButton fbutton1;
    private com.getbase.floatingactionbutton.FloatingActionButton fbutton2;
    private FloatingActionsMenu fab;

    public static ArrayList<Diary> getList() {
        return list;
    }

    public static ShowFragment newInstance() {
        ShowFragment fragment = new ShowFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setUp();
        Filter();
        initClick();
        fabRecyclerview();
    }

    private void initClick() {

        fbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
                fab.collapse();
            }
        });
        fbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), NoteActivity.class));
                getActivity().overridePendingTransition(R.anim.out_bottom, R.anim.in_bottom);
            }
        });
    }

    private void setUp() {
        filter = 0;

        fab.setVisibility(View.VISIBLE);

        helper = new DatabaseHelper(getContext());
        list = new ArrayList<>();
        setView();
    }

    private void init(View view) {
        tabs2 = view.findViewById(R.id.tabs2);
        no_item = view.findViewById(R.id.no_item);
        recyclerView = view.findViewById(R.id.mRecyclerview);
        fab = view.findViewById(R.id.fab);
        fbutton1 = view.findViewById(R.id.fbutton1);
        fbutton2 = view.findViewById(R.id.fbutton2);
    }

    private void delete() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getResources().getString(R.string._delete_all));
        builder.setMessage(getResources().getString(R.string._messenger_delete_all));
        builder.setPositiveButton(getResources().getString(R.string._yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                helper.deleteAll(list);
                list.clear();
                updateUI();
                Filter();
                Toast.makeText(getContext(), getResources().getString(R.string._deleted), Toast.LENGTH_SHORT).show();
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

    private void fabRecyclerview() {
        Animation FabMenu_fadOut = AnimationUtils.loadAnimation(getActivity(),
                R.anim.in_left);
        Animation FabMenu_fadIn = AnimationUtils.loadAnimation(getActivity(),
                R.anim.out_left);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.startAnimation(FabMenu_fadOut);
                    fab.collapse();
                    fab.setVisibility(View.GONE);

                } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                    fab.startAnimation(FabMenu_fadIn);
                    fab.collapse();
                    fab.setVisibility(View.VISIBLE);
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
                } else if (tab.getPosition() == 5) {
                    filter = 5;
                } else if (tab.getPosition() == 6) {
                    filter = 6;
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
                if (!fab.isExpanded()) {
                    Intent intent = new Intent(getContext(), ShowDiaryActivity.class);
                    intent.putExtra("position", position);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.out_bottom, R.anim.in_bottom);
                } else {
                    fab.collapse();
                }
            }

            @Override
            public void longClick(int position) {

            }
        });
    }

    private void updateUI() {
        if (list.size() <= 0) {
            no_item.setVisibility(View.VISIBLE);
            fab.setVisibility(View.GONE);
        } else {
            no_item.setVisibility(View.GONE);
            fab.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setView();
        fab.collapse();
    }

    @Override
    public void onStart() {
        super.onStart();
        fab.collapse();
    }
}