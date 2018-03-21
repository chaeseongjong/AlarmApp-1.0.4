package com.chae_s_j.alarmapp.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import static android.content.Context.MODE_PRIVATE;
import static com.chae_s_j.alarmapp.Util.constant.Uriring;
import static com.chae_s_j.alarmapp.Util.constant.coloring;
import static com.chae_s_j.alarmapp.Util.constant.coloringBar;
import static com.chae_s_j.alarmapp.Util.constant.oscillation;
import static com.chae_s_j.alarmapp.Util.constant.preference;

/**
 * Created by chaeseongjong on 2018. 3. 13..
 */

public class SharedPreference {

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    public static SharedPreference getInstance(Context context) {
        if (preference == null) {
            sharedPreferences = context.getSharedPreferences("setting", MODE_PRIVATE);
            editor = sharedPreferences.edit();
            oscillation = sharedPreferences.getBoolean("oscillation", true);
            coloring = sharedPreferences.getBoolean("coloring", true);
            coloringBar = sharedPreferences.getInt("coloringBar", 5);
            Uriring = Uri.parse(sharedPreferences.getString("Uriring", Uriring.toString()));
            preference = new SharedPreference();
        }
        return preference;
    }

    public void getsharedPreference(String name, Object value) {
        if (name.equals("oscillation")) editor.putBoolean("oscillation", (Boolean) value);
        else if (name.equals("coloring")) editor.putBoolean("coloring", (Boolean) value);
        else if (name.equals("coloringBar")) editor.putInt("coloringBar", Integer.parseInt(value.toString()));
        else if (name.equals("Uriring")) editor.putString("Uriring", value.toString());
        editor.commit();
    }
}
