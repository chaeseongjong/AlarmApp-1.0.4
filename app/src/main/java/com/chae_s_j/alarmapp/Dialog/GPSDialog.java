package com.chae_s_j.alarmapp.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import com.chae_s_j.alarmapp.R;

/**
 * Created by chaeseongjong on 2018. 3. 14..
 */

public class GPSDialog extends Dialog {

    ImageView Loading;
    private int Count = 0;
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            updateThread();
        }
    };


    public GPSDialog(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);
        setContentView(R.layout.gpsloading);
        Loading = findViewById(R.id.LoadingImageView);
        Toast.makeText(getContext(),"현재 위치를 찾고 있습니다.",Toast.LENGTH_LONG).show();
        Thread myThread = new Thread(new Runnable() {
            public void run() {
                while (true) {
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

    private void updateThread() {

        int Check = Count % 4;

        switch (Check) {
            case 0:
                Count++;
                Loading.setImageResource(R.drawable.loading01);
                break;
            case 1:
                Count++;
                Loading.setImageResource(R.drawable.loading02);
                break;
            case 2:
                Count++;
                Loading.setImageResource(R.drawable.loading03);
                break;
            case 3:
                Count = 0;
                Loading.setImageResource(R.drawable.loading04);
                break;
        }
    }
}