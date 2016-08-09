package com.example.lovem.databasetest2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by lovem on 2016/8/8.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper
{

    public static final String CREATE_BOOK = "create table Book("
            + "id integer primary key autoincrement,"
            + "author text,"
            + "price real,"
            + "pages integer,"
            +"category_id integer,"//建表语句的位置
            + "name text)";

    public static final String CREATE_CATEGORY = "create table Category("
            + "id integer primary key autoincrement,"
            + "category_name text,"
            + "category_code integer)";
    public static final String CREATE_APPS = "create table Apps("
            + "id integer primary key autoincrement,"
            + "name text,"
            + "packageName text,"
            + "icon text)";


    private Context mContext;

    public MyDatabaseHelper(Context context, String name,
                            SQLiteDatabase.CursorFactory factory,
                            int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK);
        db.execSQL(CREATE_CATEGORY);
        db.execSQL(CREATE_APPS);
        Toast.makeText(mContext, "create succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //糟糕的升级数据库方法，先删除，在全新创建
//        db.execSQL("drop table if exists Book");
//        db.execSQL("drop table if exists Category");
//        onCreate(db);

        switch (oldVersion){
            case 2:
                db.execSQL(CREATE_APPS);
            case 3:
                db.execSQL("alter table Book add column category_id integer");//add 添加到最后位置，和建表语句的实际位置不同
                Log.d("alter","column category_id added");
                default:
        }
        Log.d("test"," table apps created");
        MainActivity.mLog("table apps created");
    }
}

































































