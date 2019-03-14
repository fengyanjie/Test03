package com.voole.app.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author fengyanjie
 * @desc
 * @time 2018-3-9 13:23
 */

public class DbOpenHelper extends SQLiteOpenHelper {
    private static  final String DB_NAME = "test.db";
    private static final int DB_VERSION = 1;
    public static final String USER_TABLE_NAME = "user";

    private String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS "+USER_TABLE_NAME +""+"(id_ INTEGER PRIMARY KEY,"+"name TEXT,"+"sex INT)";



    public DbOpenHelper(Context context) {
        super(context, DB_NAME,null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        /*CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS user" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT," +
                "sex INTEGER)";
        db.execSQL(CREATE_USER_TABLE);*/
        db.execSQL("CREATE TABLE IF NOT EXISTS user" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR," +
                "age INTEGER,info TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
