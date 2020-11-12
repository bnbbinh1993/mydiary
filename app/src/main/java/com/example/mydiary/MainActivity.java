package com.example.mydiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import com.example.mydiary.adapters.ViewPagerAdapter;
import com.example.mydiary.ui.CreateFragment;
import com.example.mydiary.ui.FollowFragment;
import com.example.mydiary.ui.SettingFragment;
import com.example.mydiary.ui.ShowFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    private MenuItem prevMenuItem;
    private CreateFragment createFragment;
    private ShowFragment showFragment;
    private FollowFragment followFragment;
    private SettingFragment settingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,new String[]{WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        init();
        bottmNavigationView();
        viewpagerShow();
    }

    private void init() {
        bottomNavigationView = findViewById(R.id.bottomId);
        viewPager = findViewById(R.id.viewpager);
    }
    private void bottmNavigationView() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.workId:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.homeId:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.historyId:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.profileId:
                        viewPager.setCurrentItem(3);
                        break;
                }

                return false;
            }
        });
    }

    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        createFragment = new CreateFragment();
        showFragment = new ShowFragment();
        followFragment = new FollowFragment();
        settingFragment = new SettingFragment();
        adapter.addFragment(createFragment);
        adapter.addFragment(showFragment);
        adapter.addFragment(followFragment);
        adapter.addFragment(settingFragment);
        viewPager.setAdapter(adapter);

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
}