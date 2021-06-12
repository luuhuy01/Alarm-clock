package com.luuhuy.btl_donghobaothuc.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.luuhuy.btl_donghobaothuc.adapter.ItemTime;

import java.util.ArrayList;
import java.util.List;

public class TimerSQLiteOpenHelpper extends SQLiteOpenHelper {

    private static final String DATABASE_ALARM = "alarm.db";
    public TimerSQLiteOpenHelpper(@Nullable Context context) {
        super(context, DATABASE_ALARM, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE alarm( id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " timer TEXT, " +
                "title TEXT, " +
                "name_music TEXT, " +
                "repeat TEXT, " +
                "noti_repeat INTEGER, " +
                "checked INTEGER );";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private String convertString(String [] repeat){
        int n = repeat.length;
        String result ="";
        if (n <= 0){
            return null;
        }
        for(int i=0; i< n; i++){
            result += repeat[i];
            if(i != n - 1)
                result += ",";
        }
        return result;
    }

    public long addTimer(ItemTime time){
        ContentValues values = new ContentValues();
        values.put("timer", time.getTimer());
        values.put("title", time.getTitle());
        values.put("name_music", time.getNameMusic());
        values.put("repeat", convertString(time.getRepeat()));
        if (time.isNotiRepeat())
            values.put("noti_repeat", 1);
        else
            values.put("noti_repeat", 0);
        if(time.isChecked())
            values.put("checked", 1);
        else
            values.put("checked", 0);
        SQLiteDatabase db = getWritableDatabase();

        return db.insert("alarm", null, values);
    }

    public ItemTime findTimer(int id){
        String whereClause = "id = ?";
        String whereArgs[] = {Integer.toString(id)};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("alarm", null, whereClause, whereArgs, null, null,null);
        if(cursor != null && cursor.moveToFirst()){
            int idTime = cursor.getInt(cursor.getColumnIndex("id"));
            String timer = cursor.getString(cursor.getColumnIndex("timer"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String nameMusic = cursor.getString(cursor.getColumnIndex("name_music"));
            String [] repeat = cursor.getString(cursor.getColumnIndex("repeat")).split(",");
            boolean noti_repeat = false;
            if (cursor.getString(cursor.getColumnIndex("noti_repeat")).equals("1")){
                noti_repeat = true;
            }
            boolean checked = false;
            if (cursor.getString(cursor.getColumnIndex("checked")).equals("1")){
                checked = true;
            }
            return new ItemTime(idTime, timer, title, nameMusic, repeat, noti_repeat, checked);
        }
        return null;
    }

    public List<ItemTime> getAllTimer(){
        List<ItemTime> itemTimes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM alarm", null);
        if(cursor == null){
            return null;
        }
        while (cursor.moveToNext()){
            int idTime = cursor.getInt(cursor.getColumnIndex("id"));
            String timer = cursor.getString(cursor.getColumnIndex("timer"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String nameMusic = cursor.getString(cursor.getColumnIndex("name_music"));
            String [] repeat = cursor.getString(cursor.getColumnIndex("repeat")).split(",");
            boolean noti_repeat = false;
            if (cursor.getString(cursor.getColumnIndex("noti_repeat")).equals("1")){
                noti_repeat = true;
            }
            boolean checked = false;
            if (cursor.getString(cursor.getColumnIndex("checked")).equals("1")){
                checked = true;
            }
            ItemTime itemTime = new  ItemTime(idTime, timer, title, nameMusic, repeat, noti_repeat, checked);
            itemTimes.add(itemTime);
        }
        return itemTimes;
    }

    // sua timer
    public int updateTimer(ItemTime time){
        ContentValues values = new ContentValues();
        String whereClause = "id = ?";
        String whereArgs [] = {Integer.toString(time.getId())};
        values.put("timer", time.getTimer());
        values.put("title", time.getTitle());
        values.put("name_music", time.getNameMusic());
        values.put("repeat", convertString(time.getRepeat()));
        if (time.isNotiRepeat())
            values.put("noti_repeat", 1);
        else
            values.put("noti_repeat", 0);
        if(time.isChecked())
            values.put("checked", 1);
        else
            values.put("checked", 0);
        SQLiteDatabase db = getWritableDatabase();

        return db.update("alarm", values, whereClause, whereArgs);
    }

    //delete timer
    public int deleteTimer(int id){
        String whereClause = "id = ?";
        String whereArgs[] = {Integer.toString(id)};
        SQLiteDatabase db = getWritableDatabase();
        return db.delete("alarm", whereClause, whereArgs);
    }

    // bật tắt báo thức
    public void turnSwitchTimer(ItemTime time){
        ContentValues values = new ContentValues();
        String whereClause = "id = ?";
        String whereArgs [] = {Integer.toString(time.getId())};
        if(time.isChecked())
            values.put("checked", 1);
        else
            values.put("checked", 0);
        SQLiteDatabase db = getWritableDatabase();

        db.update("alarm", values, whereClause, whereArgs);
    }
}
