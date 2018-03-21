package com.chae_s_j.alarmapp.Activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.chae_s_j.alarmapp.Adapter.TabPagerAdapter;
import com.chae_s_j.alarmapp.R;
import com.chae_s_j.alarmapp.Util.SharedPreference;
import com.chae_s_j.alarmapp.Util.constant;
import static com.chae_s_j.alarmapp.Util.constant.*;

public class MainActivity extends AppCompatActivity  {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabPagerAdapter tabPagerAdapter;

    @Override
    public void onResume() {
        super.onResume();
        tabPagerAdapter.notifyDataSetChanged();
        constant.ActivityContext = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        constant.ActivityContext = this;
        Init();
    }

    public void Init(){

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewpager);

        tabLayout.addTab((tabLayout.newTab()).setText("리스트"));
        tabLayout.addTab((tabLayout.newTab()).setText("위치 추가"));
        tabLayout.addTab((tabLayout.newTab()).setText("타이머 추가"));
        tabLayout.addTab((tabLayout.newTab()).setText("알람 설정"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(tabPagerAdapter);
        tabLayout.addOnTabSelectedListener( new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

        viewPager.addOnPageChangeListener( new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        preference = SharedPreference.getInstance(this);

    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent intent){
        if(requestCode==Ringtoneresult) {

            Uriring = intent.getParcelableExtra(android.media.RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            preference.getsharedPreference("Uriring",Uriring);

        }
    }


}
