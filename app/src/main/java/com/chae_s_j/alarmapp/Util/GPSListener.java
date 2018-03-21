package com.chae_s_j.alarmapp.Util;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import com.chae_s_j.alarmapp.Adapter.ListPagerItem;
import static com.chae_s_j.alarmapp.Util.constant.*;

/**
 * Created by chaeseongjong on 2018. 2. 11..
 */

public class GPSListener extends Service implements LocationListener {


    private LocationManager locationManager = null;
    final private long MIN_TIME_BW_UPDATES = 1000 * 1;// 지피에스 초 단위
    final private long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;// 10미터
    private boolean isGPSEnabled = false;// GPS 열림획인
    private boolean isNetworkEnabled = false;// 네트워크 신호 열심 확인 신호
    private boolean gpsServiceCheck = false;
    private ListPagerItem item;

    public Context mContext;

    private Location holdLocation = null;// 홀드 위치
    private int meter = 0;// 홀드 미터



    public GPSListener(){ }

    public GPSListener(Context context,CallbackGPSUtil Callback){
        mContext  = context;
        callbackGPSUtil = Callback;
        startGPS();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            gpsServiceCheck = true;
            item = (ListPagerItem) intent.getExtras().getSerializable("Location");
            holdLocation = new Location(item.RouteNM);
            holdLocation.setLatitude(Double.parseDouble(item.Latitude));
            holdLocation.setLongitude(Double.parseDouble(item.Longitude));
            meter = Integer.parseInt(item.Area);
            mContext = (Context) intent.getExtras().getSerializable("Activity");
            startGPS();
        }catch (Exception e) {
            onDestroy();
            //왜인지 모르지만 에러남 나중에 확인
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        stopUsingGPS();
        if(callbackGPSUtil!=null) callbackGPSUtil.GPSfinish();
    }

    @Override
    public void onLocationChanged(Location location) {
        callbackGPSUtil.GPS(location);
        if(gpsServiceCheck){
            if(meter>holdLocation.distanceTo(location)){
                onDestroy();
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @SuppressLint("MissingPermission")
    public void startGPS(){
        setLocationManager();
        if (!isGPSEnabled && !isNetworkEnabled) showSettingsAlert();
        else{
            locationManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        }
    }

    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GPSListener.this);
        }
    }

    public void setLocationManager(){

        locationManager = (LocationManager) ActivityContext.getSystemService(LOCATION_SERVICE);
        // GPS 정보 가져오기
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 현재 네트워크 상태 값 알아오기
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ActivityContext);

        alertDialog.setTitle("GPS 사용유무셋팅");
        alertDialog.setMessage("GPS 셋팅이 되지 않았을수도 있습니다.\n 설정창으로 가시겠습니까?");

        // OK 를 누르게 되면 설정창으로 이동합니다.
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        ActivityContext.startActivity(intent);
                    }
                });
        // Cancle 하면 종료 합니다.
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    public boolean GPSCheck(){
        setLocationManager();
        if (!isGPSEnabled && !isNetworkEnabled) return false;
        else return true;
    }
}
