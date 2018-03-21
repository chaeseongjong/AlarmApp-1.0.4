package com.chae_s_j.alarmapp.Util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * Created by chaeseongjong on 2018. 2. 14..
 */


public class AlarmUtil {

    private static AlarmUtil alarmUtil;

    public static AlarmUtil getInstance() {
        if (alarmUtil == null) alarmUtil = new AlarmUtil();
        return alarmUtil;
    }


    public void setAlram(Context context,int minute) {

        Intent alarmIntent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
        startAlram(context, pendingIntent, minute);

    }

    public void startAlram(Context context, PendingIntent pendingIntent, int delay) {

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            manager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pendingIntent);
        } else {
            manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pendingIntent);
        }

    }

    public void cancelAlarm(Context context) {

        Intent intent = new Intent(context, AlarmUtil.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);

    }
}