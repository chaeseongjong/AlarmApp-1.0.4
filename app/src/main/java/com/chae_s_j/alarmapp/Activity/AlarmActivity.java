package com.chae_s_j.alarmapp.Activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.chae_s_j.alarmapp.Adapter.ListPagerItem;
import com.chae_s_j.alarmapp.Dialog.GPSDialog;
import com.chae_s_j.alarmapp.Dialog.SelectDialog;
import com.chae_s_j.alarmapp.R;
import com.chae_s_j.alarmapp.Util.AlarmUtil;
import com.chae_s_j.alarmapp.Util.CallbackGPSUtil;
import com.chae_s_j.alarmapp.Util.CallbackTimeUtil;
import com.chae_s_j.alarmapp.Util.GPSListener;
import java.util.Timer;
import java.util.TimerTask;
import static com.chae_s_j.alarmapp.Util.constant.*;

public class AlarmActivity extends AppCompatActivity implements CallbackGPSUtil, CallbackTimeUtil {

    private TextView status;
    private TextView statusNM;
    private TextView routeNM;
    private TextView KM;
    private Intent intent;
    private ListPagerItem item;
    private Location holdLocation = null;// 홀드 위치
    private GPSDialog progressDialog = null;
    private int timeCount = 0;
    AlarmUtil alarmUtil;
    Timer timer;
    private boolean alarmfinishCheck = false;


    SelectDialog mCustomDialog;
    View.OnClickListener leftListener;
    View.OnClickListener rightListener;
    NotificationCompat.Builder builder;
    NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        init();
    }

    @Override
    public void GPS(Location location) {
        int km = (int) holdLocation.distanceTo(location)/1000;
        int meter = (int)holdLocation.distanceTo(location)%1000/10;
        status.setText(km+"."+meter);
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public void onDestroy() {
        if(mCustomDialog != null)
            mCustomDialog.dismiss();
        notificationManager.cancel(0001);
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        leftListener = new View.OnClickListener() {
            public void onClick(View v) {
                mCustomDialog.dismiss();
                mCustomDialog = null;
            }
        };
        rightListener = new View.OnClickListener() {
            public void onClick(View v) {
                alarmfinishCheck = true;
                FinishCheck();
            }
        };
        mCustomDialog = new SelectDialog(AlarmActivity.this, "알람을 종료 하시겠습니까?", leftListener, rightListener); // 오른쪽 버튼 이벤트
        mCustomDialog.show();
    }

    @Override
    public void GPSfinish() {
        FinishCheck();
    }

    @Override
    public void timeFinish() {
        FinishCheck();
    }

    private void init() {

        status = findViewById(R.id.status);
        statusNM = findViewById(R.id.statusNM);
        routeNM = findViewById(R.id.routeNM);
        KM = findViewById(R.id.KM);

        ActivityContext = this;
        callbackTimeUtil = this;
        callbackGPSUtil = this;

        intent = getIntent();
        item = (ListPagerItem) intent.getExtras().getSerializable("alarm");
        builder = new NotificationCompat.Builder(this);
        if (item.Type.equals("1")) {

            statusNM.setText("Navigation");
            builder.setContentTitle("Navigation");
            routeNM.setText(item.RouteNM);
            holdLocation = new Location(item.RouteNM);
            holdLocation.setLatitude(Double.parseDouble(item.Latitude));
            holdLocation.setLongitude(Double.parseDouble(item.Longitude));
            intent = new Intent(AlarmActivity.this, GPSListener.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("Location", item);
            intent.putExtras(bundle);
            startService(intent);
            progressDialog = new GPSDialog(AlarmActivity.this);
            progressDialog.show();

        } else if (item.Type.equals("2")) {
            builder.setContentTitle("Stop watch");
            KM.setVisibility(View.GONE);
            routeNM.setText(item.RouteNM);
            timeCount = Integer.parseInt(item.Time) * 60;
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timeCount--;
                            status.setText(TimeStringFomat(timeCount/60)+":"+TimeStringFomat(timeCount%60));
                            if (timeCount == 0) {
                                timer.cancel();
                            }
                        }
                    });
                }
            }, 1000, 1000);

            alarmUtil = AlarmUtil.getInstance();
            alarmUtil.setAlram(this, timeCount * 1000);
        }

        builder.setContentText("현재 알람 서비스를 실행중입니다.")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            builder.setCategory(Notification.CATEGORY_MESSAGE)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0001, builder.build());
    }

    private void FinishCheck() {

        if (item.Type.equals("1")) {
            intent = new Intent(AlarmActivity.this, GPSListener.class);
            stopService(intent);
        } else if (item.Type.equals("2")) {
            alarmUtil.cancelAlarm(getApplicationContext());
            timer.cancel();
        }

        finish();

        if (sCpuWakeLock != null) {
            return;
        }

        PowerManager powerManager = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        sCpuWakeLock = powerManager.newWakeLock(        PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                        PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.ON_AFTER_RELEASE, "alarm");
        sCpuWakeLock.acquire();

        if (sCpuWakeLock != null) {
            sCpuWakeLock.release();
            sCpuWakeLock = null;
        }

        if (!alarmfinishCheck) {
            Intent intent = new Intent(this, PopupActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP).putExtra("RouteNM",item.RouteNM);
            startActivity(intent);
        }
    }

    private String TimeStringFomat(int number){
        if(10>number)
            return "0"+number;
            else{
            return String.valueOf(number);
        }
    }

}
