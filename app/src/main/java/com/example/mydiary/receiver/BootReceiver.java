package com.example.mydiary.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.SystemClock;

import java.util.Calendar;

public class BootReceiver {
    private Context context;
    public BootReceiver(Context context){
        this.context = context;
    }
    public void start(long key,String title){
        long timedelay = key - System.currentTimeMillis();
        int id = (int) key/1000;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(key);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("title",title);
        intent.putExtra("id",id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,id,intent,0);
        manager.setExact(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime()+timedelay, pendingIntent);
        ComponentName receiver = new ComponentName(context, AlarmReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }
    public void stop(int id){
        Intent intent = new Intent(context, AlarmReceiver.class);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent =
                PendingIntent.getService(context, id, intent,
                        PendingIntent.FLAG_NO_CREATE);
        if (pendingIntent != null && manager != null) {
            manager.cancel(pendingIntent);
        }

    }
}
