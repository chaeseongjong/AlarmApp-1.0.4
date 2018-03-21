package com.chae_s_j.alarmapp.Activity;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.chae_s_j.alarmapp.Dialog.GPSDialog;
import com.chae_s_j.alarmapp.Dialog.routeTitleDialog;
import com.chae_s_j.alarmapp.R;
import com.chae_s_j.alarmapp.SQLite.AlarmDatabaseHelper;
import com.chae_s_j.alarmapp.Util.CallbackGPSUtil;
import com.chae_s_j.alarmapp.Util.GPSListener;
import com.chae_s_j.alarmapp.Util.constant;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class MapActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, GoogleMap.OnMapClickListener, CallbackGPSUtil {

    private String Message = "m 반경에서 알람이 울립니다.";

    private SeekBar seekBar;
    private TextView textViewMeter;
    private Button mapRegisterButton;
    private SupportMapFragment mapFragment;
    private GoogleMap googleMap;
    private Marker marker;
    private Circle mCircle;
    private CircleOptions circleOptions;
    private MarkerOptions markerOptions;
    final private int strokeColor = 0xffff0000;
    final private int shadeColor = 0x44ff0000;

    AlarmDatabaseHelper alarmDatabaseHelper;
    LatLng sydney = null;
    GPSListener gpsUtil;
    Bundle bundle;
    GPSDialog gpsDialog;
    routeTitleDialog routeTitleDialog;
    BootstrapEditText routeEditText;
    View.OnClickListener leftListener;
    View.OnClickListener rightListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        constant.ActivityContext = this;
        idInit();
        setEventListener();
        bundle = getIntent().getExtras();
        gpsDialog = new GPSDialog(MapActivity.this);
        if (bundle != null) setAddressData(bundle.getString("location"));
        else{
            gpsDialog.show();
            gpsUtil = new GPSListener(getApplicationContext(), this);
        }


    }

    private void idInit() {
        seekBar = findViewById(R.id.seekBar);
        textViewMeter = findViewById(R.id.textViewMeter);
        mapRegisterButton = findViewById(R.id.mapRegisterButton);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
        mapRegisterButton.setOnClickListener(this);
        alarmDatabaseHelper = AlarmDatabaseHelper.getInstance(this);

    }

    private void setEventListener() {

        textViewMeter.setText(seekBar.getProgress() + Message);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewMeter.setText(progress + Message);
                if (mCircle != null) mCircle.setRadius(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }


    private void setmarKerAndCicle(LatLng latLng) {

        if (marker != null) marker.remove();
        if (mCircle != null) mCircle.remove();
        markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        circleOptions = new CircleOptions().center(latLng).radius(seekBar.getProgress()).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(8);
        mCircle = googleMap.addCircle(circleOptions);
        marker = googleMap.addMarker(markerOptions);

    }

    private void setAddressData(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            sydney = new LatLng(Double.parseDouble(jsonObject.getString("Latitude")), Double.parseDouble(jsonObject.getString("Longitude")));
        } catch (JSONException e) {
            gpsDialog.show();
            gpsUtil = new GPSListener(getApplicationContext(), this);
            Toast.makeText(getApplicationContext(), "주소 위치 찾기를 실패하여 지도로 위치 설정하기 모드로 변경 되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setOnMapClickListener(this);
        if (sydney != null) {
            this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
            setmarKerAndCicle(sydney);
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        setmarKerAndCicle(latLng);
    }

    @Override
    public void GPS(Location location) {
        sydney = new LatLng(location.getLatitude(), location.getLongitude());
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
        this.googleMap.setOnMapClickListener(this);
        gpsUtil.stopUsingGPS();
        gpsDialog.dismiss();
        setmarKerAndCicle(sydney);
    }

    @Override
    public void GPSfinish() {

    }

    @Override
    public void onClick(View v) {
        leftListener = new View.OnClickListener() {
            public void onClick(View v) {
                routeTitleDialog.dismiss();
            }
        };

        rightListener = new View.OnClickListener() {
            public void onClick(View v) {
                routeEditText = routeTitleDialog.findViewById(R.id.route_name);

                if (!routeEditText.getText().toString().matches("")) {
                    int RouteID = alarmDatabaseHelper.DidSelectRoute();
                    alarmDatabaseHelper.DidInsetRoute(RouteID + "", routeEditText.getText().toString(), "1");
                    alarmDatabaseHelper.DidInsertRouteMap(RouteID + "", markerOptions.getPosition().latitude + "", markerOptions.getPosition().longitude + "", seekBar.getProgress() + "");
                    routeTitleDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"등록 완료! 리스트 탭에서 확인해 주세요.",Toast.LENGTH_SHORT).show();
                    finish();
                } else
                    Toast.makeText(getApplicationContext(), "경로 정확히 입력해 주세요.", Toast.LENGTH_SHORT).show();
            }
        };
        routeTitleDialog = new routeTitleDialog(MapActivity.this, leftListener, rightListener); // 오른쪽 버튼 이벤트
        routeTitleDialog.show();
    }

}
