package com.example.boo10.term_project;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    Button bt_Date;
    Button bt_Friend;
    Button bt_Meal;
    Button bt_Extra;
    Button bt_Study;
    Button bt_Insert;
    TextView v_Date;
    EditText et_Detail;
    Button bt_Back;
    Button bt_Love;
    Button bt_Save;
    Button bt_Show;
    TextView tv_result;

    SQLiteDatabase db;
    String dbName = "dataList.db"; // name of Database;
    String tableName = "dataListTable"; // name of Table;
    int dbMode = Context.MODE_PRIVATE;

    ArrayAdapter<String> musicAdapter;
    ArrayList<String> nameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box);

        db = openOrCreateDatabase(dbName, dbMode, null);

        createTable();

        final Intent pass_Data = new Intent(this, BoxActivity.class);

        bt_Date = (Button) findViewById(R.id.date);
        bt_Friend = (Button) findViewById(R.id.bt_friend);
        bt_Meal = (Button) findViewById(R.id.bt_meal);
        bt_Extra = (Button) findViewById(R.id.bt_extra);
        bt_Study = (Button) findViewById(R.id.bt_study);
        bt_Insert = (Button) findViewById(R.id.bt_insert);
        et_Detail = (EditText) findViewById(R.id.et_detail);
        v_Date = (TextView) findViewById(R.id.tv_date);
        bt_Back = (Button) findViewById(R.id.bt_back);
        bt_Love = (Button) findViewById(R.id.bt_love);
        bt_Save = (Button) findViewById(R.id.bt_save);
        bt_Show = (Button) findViewById(R.id.bt_array);
        //tv_result = (TextView) findViewById(R.id.tv_result);
        final ListView mList = (ListView) findViewById(R.id.list_view);

        final String[] str = new String[6];

        bt_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Toast.makeText(getApplicationContext(),
                                year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일 을 선택했습니다",
                                Toast.LENGTH_SHORT).show();
                        v_Date.setText(year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일");
                        pass_Data.putExtra("year", Integer.toString(year));
                        str[0] = Integer.toString(year);
                        pass_Data.putExtra("month", Integer.toString(monthOfYear + 1));
                        str[1] = Integer.toString(monthOfYear + 1);
                        pass_Data.putExtra("date", Integer.toString(dayOfMonth));
                        str[2] = Integer.toString(dayOfMonth);

                    }
                }, 2015, 11, 19);
                dialog.show();
            }
        });

        bt_Love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass_Data.putExtra("What", "데이트");
                Toast.makeText(getApplicationContext(), "데이트를 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                str[3] = "데이트";
            }
        });

        bt_Friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass_Data.putExtra("What", "친구");
                Toast.makeText(getApplicationContext(), "친구를 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                str[3] = "친구";
            }
        });

        bt_Meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass_Data.putExtra("What", "식사");
                Toast.makeText(getApplicationContext(), "식사를 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                str[3] = "식사";
            }
        });

        bt_Study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass_Data.putExtra("What", "공부");
                Toast.makeText(getApplicationContext(), "공부를 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                str[3] = "공부";
            }
        });

        bt_Extra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass_Data.putExtra("What", "기타");
                Toast.makeText(getApplicationContext(), "기타를 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                str[3] = "기타";
            }
        });

        bt_Insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String details = et_Detail.getText().toString();
                pass_Data.putExtra("Detail", details);
                Toast.makeText(getApplicationContext(), "저장 되었습니다.", Toast.LENGTH_SHORT).show();
                str[4] = details;
            }
        });

        bt_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(pass_Data);
                //finish();
            }
        });

        bt_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData(str[0], str[1], str[2], str[3], str[4]);
                Toast.makeText(getApplicationContext(), "모두 저장 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        bt_Show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameList.clear();
                selectAll();
                musicAdapter.notifyDataSetChanged();
            }
        });

        nameList = new ArrayList<String>();
        musicAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, nameList);
        mList.setAdapter(musicAdapter);
    }

    public void createTable() {
        try {
            String sql = "create table " + tableName + "(id integer primary key autoincrement, " +
                    "year text," + " month text," + " date text," + " did text," +
                    " detail text);";
            db.execSQL(sql);

        } catch (android.database.sqlite.SQLiteException e) {
            Log.d("box", "error: " + e);
        }
    }

    public void insertData(String year, String month, String date, String did, String detail) {
        String sql = "insert into " + tableName + " values(NULL, '" +
                year + "', '" + month + "', '" + date + "', '" + did + "', '" + detail + "');";

        db.execSQL(sql);
    }

    public void selectAll() {
        String sql = "select * from " + tableName + ";";
        Cursor results = db.rawQuery(sql, null);

        results.moveToFirst();


        while (!results.isAfterLast()) {
            int id = results.getInt(0);
            String year = results.getString(1);
            String month = results.getString(2);
            String date = results.getString(3);
            String name = results.getString(4);
            String detail = results.getString(5);
            //Toast.makeText(this, "index= " + id + " name=" + name, Toast.LENGTH_LONG).show();
            Log.d("lab_sqlite", "index= " + id + " name=" + name);

            nameList.add(year+ "년 " + month + "월 " +date + "일");
            nameList.add(name);
            nameList.add(detail);

            results.moveToNext();
        }
        results.close();
    }

}
