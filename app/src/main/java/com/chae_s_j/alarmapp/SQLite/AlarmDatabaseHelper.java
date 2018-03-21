package com.chae_s_j.alarmapp.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chae_s_j on 2018-01-03.
 */

public class AlarmDatabaseHelper extends SQLiteOpenHelper {

    final private String routeSQL = "CREATE TABLE `Route` ( `RouteID` INTEGER NOT NULL, `RouteNM` TEXT NOT NULL, `Type` INTEGER NOT NULL, PRIMARY KEY(`RouteID`) )";
    final private String RouteMapSQL = "CREATE TABLE `RouteMap` ( `RouteID` INTEGER NOT NULL, `Latitude` TEXT NOT NULL, `Longitude` TEXT NOT NULL, `Area` INTEGER NOT NULL, PRIMARY KEY(`RouteID`) )";
    final private String TimeRouteSQL = "CREATE TABLE `TimeRoute` ( `RouteID` INTEGER NOT NULL, `Time` TEXT NOT NULL, PRIMARY KEY(`RouteID`) )";

    final private String selectAllSQL = "select Route.RouteID,Route.RouteNM,Route.Type,RouteMap.Area,RouteMap.Latitude,RouteMap.Longitude,TimeRoute.Time\n" +
                                        "from Route\n" +
                                        "left join RouteMap on Route.RouteID = RouteMap.RouteID\n" +
                                        "left join TimeRoute on Route.RouteID = TimeRoute.RouteID\n" +
                                        "ORDER BY Route.RouteID";


    private static SQLiteDatabase DB;
    private static AlarmDatabaseHelper alarmDatabaseHelper;

    public AlarmDatabaseHelper(Context context) {
        super(context, "Alram.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(routeSQL);
        db.execSQL(RouteMapSQL);
        db.execSQL(TimeRouteSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 데이터베이스의 버전이 바뀌었을 때 호출되는 콜백 메서드
        // 버전 바뀌었을 때 기존데이터베이스를 어떻게 변경할 것인지 작성한다
        // 각 버전의 변경 내용들을 버전마다 작성해야함
    }

    public static synchronized AlarmDatabaseHelper getInstance(Context context) {
        if (alarmDatabaseHelper == null) alarmDatabaseHelper = new AlarmDatabaseHelper(context);
        if (DB == null) DB = alarmDatabaseHelper.getReadableDatabase();
        return alarmDatabaseHelper;
    }

    public void DidInsetRoute(String... data) {
        ContentValues values = new ContentValues();
        values.put("RouteID", data[0]);
        values.put("RouteNM", data[1]);
        values.put("Type", data[2]);
        DB.insert("Route", null, values);
    }

    public void DidInsertRouteMap(String... data) {
        ContentValues values = new ContentValues();
        values.put("RouteID", data[0]);
        values.put("Latitude", data[1]);
        values.put("Longitude", data[2]);
        values.put("Area", data[3]);
        DB.insert("RouteMap", null, values);
    }

    public void DidInsetTimeRoute(String... data) {
        ContentValues values = new ContentValues();
        values.put("RouteID", data[0]);
        values.put("Time", data[1]);
        DB.insert("TimeRoute", null, values);
    }

    public int DidSelectRoute() {
        Cursor cursor = DB.rawQuery(" select * from Route", null);
        int ID = 0;
        while (cursor.moveToNext()) {
            ID = cursor.getInt(0);
        }
        return ++ID;
    }

    public JSONArray setAllRoute() throws JSONException {

        Cursor cursor = DB.rawQuery(selectAllSQL, null);
        JSONObject object;
        JSONArray jsonArray = new JSONArray();
            while (cursor.moveToNext()) {
                object = new JSONObject();
                if (cursor.getString(2).equals("1")) {

                    object.put("RouteID",cursor.getString(0));
                    object.put("RouteNM",cursor.getString(1));
                    object.put("Type",cursor.getString(2));
                    object.put("Area",cursor.getString(3));
                    object.put("Latitude",cursor.getString(4));
                    object.put("Longitude",cursor.getString(5));


                } else if (cursor.getString(2).equals("2")) {

                    object.put("RouteID",cursor.getString(0));
                    object.put("RouteNM",cursor.getString(1));
                    object.put("Type",cursor.getString(2));
                    object.put("Time",cursor.getString(6));

                }

                jsonArray.put(object);

            }

        return jsonArray;

    }

    public void DeleteAlarm(String RouteID,String Type){
        String DeleteTypeRoute = "";
        String DeleteRoute = "delete from `Route` where `RouteID` = "+RouteID;
        if(Type.equals("1")) DeleteTypeRoute = "delete from `RouteMap` where `RouteID` = "+RouteID;
        else if(Type.equals("2"))DeleteTypeRoute = "delete from `TimeRoute` where `RouteID` = "+RouteID;
        DB.execSQL(DeleteRoute);
        DB.execSQL(DeleteTypeRoute);
    }
}

