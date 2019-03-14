package com.voole.app;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.voole.app.provider.UserProvider;

public class MainActivity extends AppCompatActivity {
    Uri userUri = UserProvider.USER_CONTENT_URI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     /*   ContentValues values = new ContentValues();
        values.put("_id", 1);
        values.put("name", "杰");
        values.put("sex", 2);
        getContentResolver().insert(userUri,values);*/
//        Cursor cursor = getContentResolver().query(userUri, new String[]{"_id","name","sex"}, null, null, null);
       Cursor cursor = getContentResolver().query(userUri, null, null, null, null);
        if(cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                int sex = cursor.getInt(3);
                Log.d("MainActivity","onCreate(MainActivity.java:21)--Info-->> id: "+id +" name: "+name+" sex: "+sex);

            }
        } else {
            Log.d("MainActivity","onCreate(MainActivity.java:34)--Info-->> cursor is null ");
            
        }

    }
}
