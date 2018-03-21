package com.chae_s_j.alarmapp.Activity.MainActivityPager;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chae_s_j.alarmapp.Activity.MapActivity;
import com.chae_s_j.alarmapp.Activity.addressActivity;
import com.chae_s_j.alarmapp.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import static com.chae_s_j.alarmapp.Util.constant.ActivityContext;

/**
 * Created by chae_s_j on 2018-01-03.
 */

public class GPSPager extends Fragment implements View.OnClickListener {

    View view;
    RelativeLayout mapButton;
    RelativeLayout addressButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.gpspager, container, false);
        findViewByIdInit();
        return view;
    }

    private void findViewByIdInit() {
        mapButton = view.findViewById(R.id.map_button);
        addressButton = view.findViewById(R.id.location_button);
        mapButton.setOnClickListener(this);
        addressButton.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        //권한 체크 후 결과 처리
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Intent intent = null;
                if (v.getId() == mapButton.getId()) intent = new Intent(ActivityContext, MapActivity.class);
                else if (v.getId() == addressButton.getId()) intent = new Intent(ActivityContext, addressActivity.class);
                startActivity(intent);
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(ActivityContext, R.string.onPermissionDenied, Toast.LENGTH_SHORT).show();
            }
        };
        //권한 획득 실패 시 나오는 메세지
        TedPermission
                .with(ActivityContext)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage(R.string.setDeniedMessage)
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }
}