package com.chae_s_j.alarmapp.Util;

import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;

/**
 * Created by chaeseongjong on 2018. 3. 6..
 */

public class constant {

    public static CallbackGPSUtil callbackGPSUtil;
    public static CallbackTimeUtil callbackTimeUtil;
    public static Context ActivityContext; // 다이얼로그를 띄울라면 ApplcationContext말고 ActivityContext가 필요해서 걍 스테틱 ActivityContext로 만들어 최상위 context를 저장하도록하게 만듬
    public static final int Ringtoneresult = 1;

    //Settingpager
    public static SharedPreference preference = null;
    public static boolean oscillation = true;
    public static boolean coloring = true;
    public static int coloringBar = 5;
    public static Uri Uriring = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
    public static PowerManager.WakeLock sCpuWakeLock;

}
