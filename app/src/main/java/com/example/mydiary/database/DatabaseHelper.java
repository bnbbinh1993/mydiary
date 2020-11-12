package com.example.mydiary.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mydiary.models.Count;
import com.example.mydiary.models.Diary;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;

    private static final String DATABASE_NAME = "data";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "binh";
    private static final String COL_1 = "title";
    private static final String COL_2 = "content";
    private static final String COL_3 = "date";
    private static final String COL_4 = "address";
    private static final String COL_5 = "image";
    private static final String COL_6 = "vote";
    private static final String COL_7 = "filter";
    private static final String ID = "id";
    public DatabaseHelper(Context context) {
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
                COL_5 + " TEXT, " +
                COL_6 + " INTEGER, "+
                COL_7 + " INTEGER)";
        db.execSQL(sqlQuery);
      //  Toast.makeText(context, "Create successfylly", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
       // Toast.makeText(context, "Drop successfylly", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<Diary> getData(){
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery,null);
        ArrayList<Diary> list = new ArrayList<>();
        if (cursor != null && cursor.getCount()>0){
            while (cursor.moveToNext()){
                list.add(new Diary(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getInt(6),
                        cursor.getInt(7)));
            }
        }

        return list;
    }
    public void adđ(Diary event){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_1, event.getTitle());
        values.put(COL_2, event.getContent());
        values.put(COL_3, event.getDate());
        values.put(COL_4, event.getAddress());
        values.put(COL_5, event.getImage());
        values.put(COL_6, event.getVote());
        values.put(COL_7, event.getFilter());
        db.insert(TABLE_NAME,null,values);
        db.close();
        //Toast.makeText(context, R.string._success, Toast.LENGTH_SHORT).show();
    }
    public boolean update(Diary event){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("title",event.getTitle());
        values.put("content",event.getContent());
        values.put("date",event.getDate());
        values.put("address",event.getAddress());
        values.put("image",event.getImage());
        values.put("vote",event.getVote());
        values.put("filter",event.getFilter());
        return db.update(TABLE_NAME,values,ID+"=?",new String[]{String.valueOf(event.getId())})>0;
    }
    public void delete(Diary event) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID + " = ?",
                new String[] { String.valueOf(event.getId()) });
        db.close();
    }
    public void deleteAll(ArrayList<Diary> list) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0;i<list.size();i++){
            Diary diary = list.get(i);
            db.delete(TABLE_NAME, ID + " = ?",
                    new String[] { String.valueOf(diary.getId()) });
        }
        db.close();
    }
}
