package com.chae_s_j.alarmapp.Activity.MainActivityPager;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.chae_s_j.alarmapp.Activity.AlarmActivity;
import com.chae_s_j.alarmapp.Adapter.ListPagerAdapter;
import com.chae_s_j.alarmapp.Adapter.ListPagerItem;
import com.chae_s_j.alarmapp.Dialog.SelectDialog;
import com.chae_s_j.alarmapp.R;
import com.chae_s_j.alarmapp.SQLite.AlarmDatabaseHelper;
import com.chae_s_j.alarmapp.Util.GPSListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import static com.chae_s_j.alarmapp.Util.constant.ActivityContext;

/**
 * Created by chae_s_j on 2018-01-03.
 */

public class ListPager extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    ListView listView;
    ListPagerAdapter adapter;
    View view;
    AlarmDatabaseHelper alarmDatabaseHelper;
    JSONArray jsonArray;
    SelectDialog mCustomDialog;
    View.OnClickListener leftListener;
    View.OnClickListener rightListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        DBInit();
        view = inflater.inflate(R.layout.listpager, container, false);
        listView = view.findViewById(R.id.listView);
        SetListView();
        return view;

    }

    private void DBInit() {

        alarmDatabaseHelper = AlarmDatabaseHelper.getInstance(getContext());// 디비 연결
        adapter = new ListPagerAdapter();
        try {

            jsonArray = alarmDatabaseHelper.setAllRoute();// 모든 디비 가져오기

            for (int list = 0; list < jsonArray.length(); list++) {
                adapter.addItem(getItem(jsonArray.getJSONObject(list)));//리스트뷰에 추가
            }

        } catch (JSONException e) {
            Toast.makeText(getContext(), "데이터를 가져오는대에 실패 하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
        }

    }

    private void SetListView() {
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
    }

    private ListPagerItem getItem(JSONObject jsonObject) throws JSONException {
        ListPagerItem item = new ListPagerItem();
        if (jsonObject.getString("Type").equals("1")) {

            item.RouteID = jsonObject.getInt("RouteID");
            item.RouteNM = jsonObject.getString("RouteNM");
            item.Type = jsonObject.getString("Type");
            item.Area = jsonObject.getString("Area");
            item.Latitude = jsonObject.getString("Latitude");
            item.Longitude = jsonObject.getString("Longitude");

        } else if (jsonObject.getString("Type").equals("2")) {

            item.RouteID = jsonObject.getInt("RouteID");
            item.RouteNM = jsonObject.getString("RouteNM");
            item.Type = jsonObject.getString("Type");
            item.Time = jsonObject.getString("Time");

        }
        return item;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        if (jsonArray != null) {

            try {
                final ListPagerItem item = getItem(jsonArray.getJSONObject(position));
                if (item.Type.equals("1")) {

                    GPSListener check = new GPSListener();
                    check.mContext = getContext();
                    if (check.GPSCheck()) {
                        PermissionListener permissionlistener = new PermissionListener() {
                            @Override
                            public void onPermissionGranted() {

                                Intent intent = new Intent(getContext(), AlarmActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("alarm", item);
                                intent.putExtras(bundle);
                                startActivity(intent);

                            }

                            @Override
                            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                                Toast.makeText(ActivityContext, R.string.onPermissionDenied, Toast.LENGTH_SHORT).show();
                            }
                        };
                        TedPermission
                                .with(ActivityContext)
                                .setPermissionListener(permissionlistener)
                                .setDeniedMessage(R.string.setDeniedMessage)
                                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                                .check();
                    } else check.showSettingsAlert();

                } else if (item.Type.equals("2")) {

                    Intent intent = new Intent(getContext(), AlarmActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("alarm", item);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        leftListener = new View.OnClickListener() {
            public void onClick(View v) {
                mCustomDialog.dismiss();
                mCustomDialog = null;
            }
        };
        rightListener = new View.OnClickListener() {
            public void onClick(View v) {
                ListPagerItem item = null;
                try {
                    item = getItem(jsonArray.getJSONObject(position));
                    alarmDatabaseHelper.DeleteAlarm(item.RouteID + "", item.Type);
                    DBInit();
                    SetListView();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "삭제가 완료 되었습니다.", Toast.LENGTH_LONG).show();
                    mCustomDialog.dismiss();
                    mCustomDialog = null;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        mCustomDialog = new SelectDialog(view.getContext(), "알람을 삭제하시겠습니까?", leftListener, rightListener); // 오른쪽 버튼 이벤트
        mCustomDialog.show();
        return true;
    }
}