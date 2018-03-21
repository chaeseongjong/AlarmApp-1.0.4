package com.chae_s_j.alarmapp.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.chae_s_j.alarmapp.R;

public class routeTitleDialog extends Dialog {

    private BootstrapButton mLeftButton;
    private BootstrapButton mRightButton;
    private View.OnClickListener mLeftClickListener;
    private View.OnClickListener mRightClickListener;

    public routeTitleDialog(Context context, View.OnClickListener leftListener, View.OnClickListener rightListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mLeftClickListener = leftListener;
        this.mRightClickListener = rightListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.routetitledialog);

        mLeftButton = findViewById(R.id.route_btn_left);
        mRightButton = findViewById(R.id.route_btn_right);

        if (mLeftClickListener != null && mRightClickListener != null) {
            mLeftButton.setOnClickListener(mLeftClickListener);
            mRightButton.setOnClickListener(mRightClickListener);
        }
    }

}