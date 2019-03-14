package com.voole.app.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.voole.app.sqlite.DbOpenHelper;

/**
 * @author fengyanjie
 * @desc
 * @time 2018-3-8 19:58
 */

public class UserProvider extends ContentProvider{
    private static final String TAG = "UserProvider";
    public static final String AUTHORITY = "com.voole.app";
    public static final Uri USER_CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/user");
    public static final int USER_URI_CODE = 1;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // 数据集的MIME类型字符串则应该以vnd.android.cursor.dir/开头
    public static final String MIME_DIR_PREFIX = "vnd.android.cursor.dir";
    static{
        sUriMatcher.addURI(AUTHORITY, "user", USER_URI_CODE);
    }
    private Context mContext;
    private SQLiteDatabase mDb;
    @Override
    public boolean onCreate() {
        Log.d("BookProvider","onCreate(BookProvider.java:32)--Info-->>");
        mContext = getContext();
        initProviderData();
        return true;
    }

    private void initProviderData() {
        mDb = new DbOpenHelper(mContext).getWritableDatabase();
        mDb.execSQL("insert into user ( name, age)values('jack',0);");
//        mDb.execSQL("insert into user values(2,'jack1',1);");
        ContentValues values = new ContentValues();
        values.put("_id", 4);
        values.put("name", "jie");
        values.put("age", 0);
//        values.put("info", "test");
        mDb.insert("user", "name", values);
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.d("BookProvider","query(BookProvider.java:50)--Info-->>");
        String table = getTableName(uri);
        if(table == null) {
            throw new IllegalArgumentException("Unsupported URI: "+uri);
        }
        return mDb.query(table, projection, selection, selectionArgs, null, null, sortOrder, null);
    }
    public static final String CONTENT_LIST = "vnd.android.cursor.dir/vnd.userprovider.user";
    public static final String CONTENT_ITEM = "vnd.android.cursor.item/vnd.userprovider.user";
    private String getTableName(Uri uri) {
        String tableName = "";
        switch (sUriMatcher.match(uri)) {
            case USER_URI_CODE:
                tableName = DbOpenHelper.USER_TABLE_NAME;
                break;
        }
        return tableName;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Log.d("UserProvider","getType(UserProvider.java:78)--Info-->>");
      /*  switch (sUriMatcher.match(uri)){
            case USER_URI_CODE:
                return CONTENT_LIST;
        }*/
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.d("UserProvider","insert(UserProvider.java:80)--Info-->>");
       String table = getTableName(uri);
        long id = mDb.insert(table,null,values);
        if(id>0){
            Uri newUri = ContentUris.withAppendedId(USER_CONTENT_URI,id);
            getContext().getContentResolver().notifyChange(newUri, null);
            return newUri;
        }
        throw new SQLException("failed to insert row into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
