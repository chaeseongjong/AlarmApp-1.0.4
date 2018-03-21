package com.chae_s_j.alarmapp.Activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.chae_s_j.alarmapp.R;

public class IntroActivity extends AppCompatActivity {

    TextView version;
    ImageView imageView;

    private int Count = 0;
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            updateThread();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        Init();
    }

    private void Init() {
        version = findViewById(R.id.version);
        version.setText("ver " + versionCheck());
        imageView = findViewById(R.id.loadingView);

        Thread myThread = new Thread(new Runnable() {
            public void run() {
                while (Count != 5) {
                    try {
                        handler.sendMessage(handler.obtainMessage());
                        Thread.sleep(300);
                    } catch (Throwable t) {

                    }
                }
            }
        });

        myThread.start();
    }

    private String versionCheck() {

        String versionName = "";
        try {
            PackageInfo info = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {

        }
        return versionName;
    }

    private void updateThread() {

        int Check = Count % 5;

        switch (Check) {
            case 0:
                Count++;
                imageView.setImageResource(R.drawable.loading01);
                break;
            case 1:
                Count++;
                imageView.setImageResource(R.drawable.loading02);
                break;
            case 2:
                Count++;
                imageView.setImageResource(R.drawable.loading03);
                break;
            case 3:
                Count++;
                imageView.setImageResource(R.drawable.loading04);
                break;
            case 4:
                Count++;
                Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }


}
