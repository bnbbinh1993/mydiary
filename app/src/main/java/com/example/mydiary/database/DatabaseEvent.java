package com.example.mydiary.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mydiary.models.Count;
import com.example.mydiary.models.EventCalendar;

import java.util.ArrayList;

public class DatabaseEvent extends SQLiteOpenHelper {

    private Context context;

    private static final String DATABASE_NAME = "event";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "event";
    private static final String COL_1 = "time";
    private static final String COL_2 = "cotent";
    private static final String COL_3 = "loc";
    private static final String ID = "id";

    public DatabaseEvent(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                ID + " integer primary key, " +
                COL_1 + " TEXT, " +
                COL_2 + " TEXT, " +
                COL_3 + " long)";
        db.execSQL(sqlQuery);
        //  Toast.makeText(context, "Create successfylly", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        // Toast.makeText(context, "Drop successfylly", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<EventCalendar> getData() {
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        ArrayList<EventCalendar> list = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                list.add(new EventCalendar(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getLong(3)));
            }
        }

        return list;
    }
    public ArrayList<EventCalendar> getDataById(int id) {
        String selectQuery = "SELECT  * FROM " + TABLE_NAME+" WHERE ID="+id+"";
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        ArrayList<EventCalendar> list = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                list.add(new EventCalendar(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getLong(3)));
            }
        }

        return list;
    }

    public void add(EventCalendar event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_1, event.getContent());
        values.put(COL_2, event.getTime());
        values.put(COL_3, event.getLoc());
        db.insert(TABLE_NAME, null, values);
        db.close();
        //Toast.makeText(context, R.string._success, Toast.LENGTH_SHORT).show();
    }

    public boolean update(EventCalendar event) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_1, event.getContent());
        values.put(COL_2, event.getTime());
        values.put(COL_3, event.getLoc());

        return db.update(TABLE_NAME, values, ID + "=?", new String[]{String.valueOf(event.getId())}) > 0;
    }

    public void delete(EventCalendar event) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID + " = ?",
                new String[]{String.valueOf(event.getId())});
        db.close();
    }

    public void deleteAll(ArrayList<EventCalendar> list) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < list.size(); i++) {
            EventCalendar e = list.get(i);
            db.delete(TABLE_NAME, ID + " = ?",
                    new String[]{String.valueOf(e.getId())});
        }
        db.close();
    }
}
