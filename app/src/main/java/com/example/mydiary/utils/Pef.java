package com.example.mydiary.utils;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class Pef {
    private static final String SQL = "CREATE TABLE IF NOT EXISTS binh(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(250), time VARCHAR(250),image BLOB)";
    private static final String GETDATA = "SELECT * FROM binh";
    public static final String TAG = "BNB";
    public static final String dayList[] = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
    public static final String monthList[] = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    public static final String hoursList[] = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
    public static final String minuteList[] = {"00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55"};
    private static final String listYear[] = new String[100];

    private static Context context;
    private static String result;
    private static SharedPreferences preferences;

    public Pef(Context context) {
        this.context = context;
    }

    public static void setLong(String name, long gt) {
        preferences = context.getSharedPreferences("HIHI", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(name, gt);
        editor.apply();

    }

    public static long getLong(String name) {
        preferences = context.getSharedPreferences("HIHI", MODE_PRIVATE);
        long result = preferences.getLong(name, 0);
        return result;
    }

    public static void setInt(String name, int gt) {
        preferences = context.getSharedPreferences("HIHI", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(name, gt);
        editor.apply();

    }

    public static int getInt(String name) {
        preferences = context.getSharedPreferences("HIHI", MODE_PRIVATE);
        int result = preferences.getInt(name, 0);
        return result;
    }

    public static void setString(String name, String gt) {
        preferences = context.getSharedPreferences("HIHI", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(name, gt);
        editor.apply();
    }

    public static String getString(String name, String er) {
        preferences = context.getSharedPreferences("HIHI", MODE_PRIVATE);
        String result = preferences.getString(name, er);
        return result;
    }

    public static void setBoolean(String name, boolean gt) {
        preferences = context.getSharedPreferences("HIHI", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(name, gt);
        editor.apply();

    }

    public static boolean getBoolean(String name) {
        preferences = context.getSharedPreferences("HIHI", MODE_PRIVATE);
        boolean result = preferences.getBoolean(name, false);
        return result;
    }

    public static void showMessenger(String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Hí anh em");
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setMessage(content);
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public static String getDatePicker(Context context) {

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener date_listener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {

                String dateResult = String.valueOf(day) + "/" + String.valueOf(month + 1)
                        + "/" + String.valueOf(year);
                result = dateResult;

            }
        };

        DatePickerDialog a = new DatePickerDialog(context, date_listener, year, month, day);
        a.create();
        a.show();
        return result;
    }

    public static String[] isListYear() {
        int j = 0;
        for (int i = 2020; i < 2120; i++) {
            listYear[j] = String.valueOf(i);
            j++;
        }
        return listYear;
    }

}
