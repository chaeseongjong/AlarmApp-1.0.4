package com.chae_s_j.alarmapp.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.chae_s_j.alarmapp.Util.constant.callbackTimeUtil;

/**
 * Created by chaeseongjong on 2018. 3. 7..
 */

public class AlarmBroadcastReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        callbackTimeUtil.timeFinish();
    }

}
