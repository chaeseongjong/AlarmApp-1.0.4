package com.chae_s_j.alarmapp.Activity.MainActivityPager;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.chae_s_j.alarmapp.R;
import com.chae_s_j.alarmapp.SQLite.AlarmDatabaseHelper;
import com.chae_s_j.alarmapp.Util.AlarmUtil;

/**
 * Created by chae_s_j on 2018-01-03.
 */

public class TimerPager extends Fragment {

    BootstrapEditText routeText;
    BootstrapEditText minuteText;
    BootstrapButton button;
    AlarmDatabaseHelper alarmDatabaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        alarmDatabaseHelper = AlarmDatabaseHelper.getInstance(getContext());
        final View view = inflater.inflate(R.layout.timerpager, container, false);
        routeText = view.findViewById(R.id.routeText);
        minuteText =view.findViewById(R.id.route_name);
        button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AlarmUtil.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);


                AlarmUtil alarmUtil = new AlarmUtil();
                alarmUtil.startAlram(getContext(),pendingIntent, 5 * 1000);
                if(!routeText.getText().toString().matches("")||!minuteText.getText().toString().matches("")){
                    SaveData();
                }else {
                    Toast.makeText(getContext(),"경로 명 및 시간을 설정해 주세요.",Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;

    }

    private void SaveData() {
        int RouteID = alarmDatabaseHelper.DidSelectRoute();

        alarmDatabaseHelper.DidInsetRoute(RouteID+"",routeText.getText().toString(),"2");
        alarmDatabaseHelper.DidInsetTimeRoute(RouteID+"",minuteText.getText().toString());

        routeText.setText(""); minuteText.setText("");
        Toast.makeText(getContext(),"등록 완료! 리스트 탭에서 확인해 주세요.",Toast.LENGTH_SHORT).show();
    }

}