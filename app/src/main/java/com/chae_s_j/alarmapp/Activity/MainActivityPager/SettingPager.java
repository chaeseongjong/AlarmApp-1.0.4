package com.chae_s_j.alarmapp.Activity.MainActivityPager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.chae_s_j.alarmapp.R;
import com.chae_s_j.alarmapp.Util.SharedPreference;
import static com.chae_s_j.alarmapp.Util.constant.*;

/**
 * Created by chaeseongjong on 2018. 3. 9..
 */

public class SettingPager extends Fragment implements View.OnClickListener,CompoundButton.OnCheckedChangeListener,SeekBar.OnSeekBarChangeListener{

    Switch oscillationSwitch;
    Switch coloringSwitch;
    SeekBar coloringSeekBar;
    BootstrapButton RingtoneManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.settingpager, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        oscillationSwitch = view.findViewById(R.id.oscillationSwitch);
        coloringSwitch = view.findViewById(R.id.coloringSwitch);
        coloringSeekBar = view.findViewById(R.id.coloringBar);
        RingtoneManager = view.findViewById(R.id.ringtoneButton);
        RingtoneManager.setOnClickListener(this);
        oscillationSwitch.setOnCheckedChangeListener(this);
        coloringSwitch.setOnCheckedChangeListener(this);
        coloringSeekBar.setOnSeekBarChangeListener(this);
        oscillationSwitch.setChecked(oscillation);
        coloringSwitch.setChecked(coloring);
        coloringSeekBar.setProgress(coloringBar);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ringtoneButton) {
            Intent intent = new Intent(android.media.RingtoneManager.ACTION_RINGTONE_PICKER);
            intent.putExtra(android.media.RingtoneManager.EXTRA_RINGTONE_TITLE, "벨소리 선택")
                    .putExtra(android.media.RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false)
                    .putExtra(android.media.RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, false)
                    .putExtra(android.media.RingtoneManager.EXTRA_RINGTONE_TYPE, android.media.RingtoneManager.TYPE_ALARM)
                    .putExtra(android.media.RingtoneManager.EXTRA_RINGTONE_TYPE, android.media.RingtoneManager.TYPE_NOTIFICATION)
                    .putExtra(android.media.RingtoneManager.EXTRA_RINGTONE_TYPE, android.media.RingtoneManager.TYPE_RINGTONE)
                    .putExtra(android.media.RingtoneManager.EXTRA_RINGTONE_PICKED_URI, false)
                    .putExtra(android.media.RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, false);
            getActivity().startActivityForResult(intent, Ringtoneresult);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        preference = SharedPreference.getInstance(getContext());
        if(buttonView.getId()==R.id.oscillationSwitch){
            preference.getsharedPreference("oscillation",isChecked);
            oscillation = isChecked;
        }
        else if(buttonView.getId()==R.id.coloringSwitch){
            preference.getsharedPreference("coloring",isChecked);
            coloring = isChecked;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        coloringBar = progress;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        preference.getsharedPreference("coloringBar",coloringBar);

    }
}