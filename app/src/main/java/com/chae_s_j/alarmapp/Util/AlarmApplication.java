package com.chae_s_j.alarmapp.Util;

import android.app.Application;
import com.beardedhen.androidbootstrap.TypefaceProvider;

/**
 * Created by chaeseongjong on 2018. 3. 12..
 */

public class AlarmApplication extends Application {
    @Override public void onCreate() {
        super.onCreate();
        TypefaceProvider.registerDefaultIconSets();
    }
}