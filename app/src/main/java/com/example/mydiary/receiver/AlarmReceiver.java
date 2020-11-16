package com.example.mydiary.receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.mydiary.MainActivity;
import com.example.mydiary.R;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent hihi = new Intent(context, MainActivity.class);
        String title  = intent.getStringExtra("title");
        int id  = intent.getIntExtra("id",100);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,id,hihi,0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"study_reminder")
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Có sự kiện sắp diễn ra. Hãy vào để kiểm tra nào."))
                .setContentText("Có sự kiện sắp diễn ra. Hãy vào để kiểm tra nào.")
                .setContentInfo("Có sự kiện sắp diễn ra. Hãy vào để kiểm tra nào.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        builder.setDefaults(Notification.DEFAULT_ALL);
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            int impor = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("study_reminder","my diary",impor);
            manager.createNotificationChannel(channel);
        }

        Notification noti = builder.build();
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        manager.notify(id,noti);


    }




}
