package com.example.mydiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mydiary.adapters.ViewPagerAdapter;
import com.example.mydiary.ui.AnalysisFragment;
import com.example.mydiary.ui.CreateFragment;
import com.example.mydiary.ui.DairyFragment;
import com.example.mydiary.ui.FollowFragment;
import com.example.mydiary.ui.SettingFragment;
import com.example.mydiary.ui.ShowFragment;
import com.example.mydiary.utils.ZoomOutPageTransformer;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {
    private boolean rate  = false;
    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    private MenuItem prevMenuItem;
    private CreateFragment createFragment;
    private DairyFragment dairyFragment;
    private FollowFragment followFragment;
    private SettingFragment settingFragment;
    private AnalysisFragment analysisFragment;
    private int backClick = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        init();
        bottmNavigationView();

        //viewpagerShow();
        //showdialog();
    }

    private void showdialog() {

        if (!rate){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            ViewGroup viewGroup = findViewById(R.id.container);
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_daily,viewGroup,false);
            LinearLayout normal = view.findViewById(R.id.normal);
            LinearLayout sad = view.findViewById(R.id.sad);
            LinearLayout happy = view.findViewById(R.id.happy);
            builder.setView(view);
            AlertDialog alertDialog = builder.create();

            sad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "sad", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            });
            happy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "happy", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            });

            normal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "normal", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            });

            alertDialog.show();
        }

    }

    private void init() {
        bottomNavigationView = findViewById(R.id.bottomId);
        //viewPager = findViewById(R.id.viewpager);
        //viewPager.setEnabled(false);
       // viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        Fragment selectedFragment = CreateFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, selectedFragment).commit();
    }

    private void bottmNavigationView() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.workId:
                        if (selectedFragment != CreateFragment.newInstance()){
                            selectedFragment = CreateFragment.newInstance();
                        }

                        //viewPager.setCurrentItem(0);
                        break;
                    case R.id.homeId:
                        if (selectedFragment != DairyFragment.newInstance()){
                            selectedFragment = DairyFragment.newInstance();
                        }

                       // viewPager.setCurrentItem(1);
                        break;
                    case R.id.historyId:
                        if (selectedFragment != FollowFragment.newInstance()){
                            selectedFragment = FollowFragment.newInstance();
                        }
                        //viewPager.setCurrentItem(2);
                        break;
//                    case R.id.analysisId:
//                        if (selectedFragment != AnalysisFragment.newInstance()){
//                            selectedFragment = AnalysisFragment.newInstance();
//                        }
//                        //viewPager.setCurrentItem(3);
//                        break;
                    case R.id.profileId:
                        selectedFragment = SettingFragment.newInstance();
                        //viewPager.setCurrentItem(4);
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.content, selectedFragment).commit();
                return true;
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        createFragment = new CreateFragment();
        dairyFragment = new DairyFragment();
        followFragment = new FollowFragment();
        settingFragment = new SettingFragment();
        analysisFragment = new AnalysisFragment();
        adapter.addFragment(createFragment);
        adapter.addFragment(dairyFragment);
        adapter.addFragment(followFragment);
        adapter.addFragment(analysisFragment);
        adapter.addFragment(settingFragment);
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

    }

    private void viewpagerShow() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: " + position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setupViewPager(viewPager);

    }

    @Override
    public void onBackPressed() {
        if (backClick == 1) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, R.string._toast, Toast.LENGTH_SHORT).show();
            backClick++;
        }
    }
}