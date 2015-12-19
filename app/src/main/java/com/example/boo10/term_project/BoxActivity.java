package com.example.boo10.term_project;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class BoxActivity extends AppCompatActivity {

private GoogleMap map;
Intent get_Data;
    SQLiteDatabase db2;
    String dbName = "mapList.db"; // name of Database;
    String tableName = "mapListTable"; // name of Table;
    int dbMode = Context.MODE_PRIVATE;

static final LatLng SEOUL = new LatLng(37.5174140, 127.093234);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db2 = openOrCreateDatabase(dbName,dbMode,null);

        createTable();

        MapFragment mapFragment = (MapFragment) this.getFragmentManager()
                .findFragmentById(R.id.map);
        map = mapFragment.getMap();

        //현재 위치로 가는 버튼 표시
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 15));//초기 위치...수정필요

        MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
            @Override
            public void gotLocation(Location location) {

                String msg = "lon: "+location.getLongitude()+" -- lat: "+location.getLatitude();
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                drawMarker(location);

            }
        };

        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(getApplicationContext(), locationResult);

    }


    private void drawMarker(Location location) {

        //기존 마커 지우기
        //map.clear();
        final LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());

        final String lat = Double.toString(location.getLatitude());
        final String lng = Double.toString(location.getLongitude());
        get_Data = this.getIntent();

        //currentPosition 위치로 카메라 중심을 옮기고 화면 줌을 조정한다. 줌범위는 2~21, 숫자클수록 확대
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 17));
        map.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);

        //마커 추가
        //map.addMarker(new MarkerOptions().position(currentPosition).snippet("Lat:" + location.getLatitude() + " Lng:" + location.getLongitude()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("현재위치"));
        Button bt_Box;

        bt_Box = (Button) findViewById(R.id.box);
        bt_Box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "저장 되었습니다.", Toast.LENGTH_SHORT).show();
                //LatLng savePosition = new LatLng(37.5174140 ,127.093234);

                //map.addMarker(new MarkerOptions().position(currentPosition).snippet(get_Data.getStringExtra("year") + "년 " +
                 //       get_Data.getStringExtra("month") + "월 " + get_Data.getStringExtra("date") + "일" + "\n" +
                  //      get_Data.getStringExtra("What") + "을 했습니다.\n" +
                   //     get_Data.getStringExtra("Detail")).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title(get_Data.getStringExtra("What")));

                insertData(lat, lng, get_Data.getStringExtra("What"), get_Data.getStringExtra("month"), get_Data.getStringExtra("date"));
                //Cursor cursor = db.rawQuery("select * from " + tableName, null);
                //cursor.moveToFirst();
                LatLng data_Position;/*
                while(cursor.moveToNext()){
                    data_Position = new LatLng(cursor.getDouble(1), cursor.getDouble(2));
                    map.addMarker(new MarkerOptions().position(data_Position).snippet(cursor.getString(4)+ cursor.getString(5)).
                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title(cursor.getString(3)));
                }*/
                //cursor.close();
            }
        });
    }

    public void createTable() {
        try {
            String sql = "create table " + tableName + "(id integer primary key autoincrement, " +
                    "lat text, " + "lng text," + " what text," + " month text," + " date text);";
            db2.execSQL(sql);

        } catch (android.database.sqlite.SQLiteException e) {
            Log.d("term_project", "error: " + e);
        }
    }

    public void insertData(String lat, String lng, String what, String month, String date) {
        String sql = "insert into " + tableName + " values(NULL, '" +
                lat + "', '" + lng + "', '" +  what + "', '" + month +"', '" + date + "');";

        db2.execSQL(sql);
    }
}