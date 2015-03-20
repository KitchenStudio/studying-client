package com.example.xiner.sqlite;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by xiner on 15-3-16.
 */
public class SqliteBase extends SQLiteOpenHelper {

    SQLiteDatabase db;

    public static final int VERSION = 1;
    private static final String TAG = "SqliteBase";
    private static final String DBNAME="xueyou.db";
    private static final String ITEM_TABEL="item";
    private static final String DBCREATE="create database"+DBNAME;
    private static final String ITEMCREATE="create table  if not exists "+ITEM_TABEL+"(id integer  PRIMARY KEY autoincrement,content text,createdTime date,starNumber long,praiseNumber long)";

    public SqliteBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v(TAG,"create");
//        db.execSQL(DBCREATE);
        db.execSQL(ITEMCREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS item");
        onCreate(db);
    }
}
