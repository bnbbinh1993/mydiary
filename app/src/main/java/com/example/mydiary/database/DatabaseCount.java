package com.example.mydiary.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mydiary.models.Count;
import com.example.mydiary.models.Diary;

import java.util.ArrayList;

public class DatabaseCount extends SQLiteOpenHelper {

    private Context context;

    private static final String DATABASE_NAME = "count";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "count";
    private static final String COL_1 = "title";
    private static final String COL_2 = "des";
    private static final String COL_3 = "place";
    private static final String COL_4 = "date";
    private static final String COL_5 = "filter";
    private static final String COL_6 = "vote";
    private static final String ID = "id";
    public DatabaseCount(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                ID + " integer primary key, " +
                COL_1 + " TEXT, " +
                COL_2 + " TEXT, " +
                COL_3 + " TEXT, " +
                COL_4 + " TEXT, " +
                COL_5 + " INTEGER, " +
                COL_6 + " INTEGER)";
        db.execSQL(sqlQuery);
      //  Toast.makeText(context, "Create successfylly", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
       // Toast.makeText(context, "Drop successfylly", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<Count> getData(){
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery,null);
        ArrayList<Count> list = new ArrayList<>();
        if (cursor != null && cursor.getCount()>0){
            while (cursor.moveToNext()){
                list.add(new Count(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getInt(5),
                        cursor.getInt(6)));
            }
        }

        return list;
    }
    public void adđ(Count event){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_1, event.getTitle());
        values.put(COL_2, event.getDes());
        values.put(COL_3, event.getPlace());
        values.put(COL_4, event.getDate());
        values.put(COL_5, event.getFilter());
        values.put(COL_6, event.getVote());
        db.insert(TABLE_NAME,null,values);
        db.close();
        //Toast.makeText(context, R.string._success, Toast.LENGTH_SHORT).show();
    }
    public boolean update(Count event){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_1,event.getTitle());
        values.put(COL_2,event.getDes());
        values.put(COL_3,event.getPlace());
        values.put(COL_4,event.getDate());
        values.put(COL_5,event.getFilter());
        values.put(COL_6,event.getVote());

        return db.update(TABLE_NAME,values,ID+"=?",new String[]{String.valueOf(event.getId())})>0;
    }
    public void delete(Count event) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID + " = ?",
                new String[] { String.valueOf(event.getId()) });
        db.close();
    }
}
