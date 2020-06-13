package com.example.c196;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    static int notificationId;
    String channelId = "channel1";

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, intent.getStringExtra("notificationTitle"), Toast.LENGTH_LONG).show();
        createNotificationChannel(context, channelId);

        Notification notification = new NotificationCompat.Builder(context, channelId)
                .setContentTitle(intent.getStringExtra("notificationTitle"))
                .setContentText(intent.getStringExtra("notificationText"))
                .setSmallIcon(R.drawable.ic_launcher_background)
                .build();

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        synchronized (notification) { notificationManager.notify(1, notification); }

    }

    private void createNotificationChannel(Context context, String CHANNEL_ID) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getResources().getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}