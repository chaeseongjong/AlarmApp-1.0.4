package com.chae_s_j.alarmapp.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.chae_s_j.alarmapp.R;

public class SelectDialog extends Dialog {

    private TextView mTitleView;
    private BootstrapButton mLeftButton;
    private BootstrapButton mRightButton;
    private String mTitle;

    private View.OnClickListener mLeftClickListener;
    private View.OnClickListener mRightClickListener;

    public SelectDialog(Context context, String title, View.OnClickListener leftListener, View.OnClickListener rightListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mTitle = title;
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

        setContentView(R.layout.select_dialog);

        mTitleView = findViewById(R.id.select_text);
        mLeftButton = findViewById(R.id.btn_left);
        mRightButton = findViewById(R.id.btn_right);

        mTitleView.setText(mTitle);

        if (mLeftClickListener != null && mRightClickListener != null) {
            mLeftButton.setOnClickListener(mLeftClickListener);
            mRightButton.setOnClickListener(mRightClickListener);
        }
    }

}