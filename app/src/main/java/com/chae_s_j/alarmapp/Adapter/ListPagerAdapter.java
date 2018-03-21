package com.chae_s_j.alarmapp.Adapter;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chae_s_j.alarmapp.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.chae_s_j.alarmapp.Util.constant.ActivityContext;

/**
 * Created by chaeseongjong on 2018. 2. 3..
 */

public class ListPagerAdapter extends BaseAdapter {

    private ArrayList<ListPagerItem> listViewItemList = new ArrayList<ListPagerItem>() ;


    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview, parent, false);
        }

        ListPagerItem listViewItem = listViewItemList.get(position);

        TextView routeTextView = convertView.findViewById(R.id.routeText) ;
        TextView typeTextView = convertView.findViewById(R.id.typeText) ;
        ImageView imageView = convertView.findViewById(R.id.imageView);

        routeTextView.setText(listViewItem.RouteNM);
        if(listViewItem.Type.equals("1")) {
            typeTextView.setText(findAddress(Double.parseDouble(listViewItem.Latitude),Double.parseDouble(listViewItem.Longitude)));
            imageView.setSelected(true);
        }
        else if(listViewItem.Type.equals("2")) {
            typeTextView.setText(listViewItem.Time+"분");
            imageView.setSelected(false);
        }
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    public void addItem(ListPagerItem item) {
        listViewItemList.add(item);
    }

//    public void remove(int number){
//        listViewItemList.remove(number);
//    }

    private String findAddress(double lat, double lng) {
        String Address = "";
        Geocoder geocoder = new Geocoder(ActivityContext, Locale.KOREA);
        List<Address> address;
        try {
            if (geocoder != null) {
                address = geocoder.getFromLocation(lat, lng, 1);
                if (address != null && address.size() > 0) {
                    Address = address.get(0).getAddressLine(0).toString();
                }
            }

        } catch (IOException e) {
            Address = "주소 찾기 실패";
        }
        return Address;
    }

}