package com.chae_s_j.alarmapp.Activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.chae_s_j.alarmapp.R;
import com.chae_s_j.alarmapp.Util.GPSListener;

import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;
import static com.chae_s_j.alarmapp.Util.constant.Uriring;
import static com.chae_s_j.alarmapp.Util.constant.coloring;
import static com.chae_s_j.alarmapp.Util.constant.coloringBar;
import static com.chae_s_j.alarmapp.Util.constant.oscillation;

public class PopupActivity extends AppCompatActivity implements View.OnClickListener {

    Button button;
    Vibrator vibrator;
    long[] pattern = {100, 300, 100, 700, 300, 2000};
    Intent intent;
    TextView textView;


    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        init();
    }

    public void init() {
        textView = findViewById(R.id.routeName);
        intent = getIntent();
        textView.setText(intent.getStringExtra("RouteNM"));
        button = findViewById(R.id.alarmfinish);
        button.setOnClickListener(this);

        if(oscillation) {
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(pattern, 0);
        }

        if(coloring) {
            mediaPlayer = MediaPlayer.create(this, Uriring);
            mediaPlayer.setVolume(coloringBar * 0.1f,coloringBar * 0.1f);
            mediaPlayer.start();
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Intent intent = new Intent(PopupActivity.this, GPSListener.class);
        stopService(intent);

        if(vibrator!=null) vibrator.cancel();

        if(mediaPlayer!=null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        intent = new Intent(PopupActivity.this,MainActivity.class).addFlags(FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
