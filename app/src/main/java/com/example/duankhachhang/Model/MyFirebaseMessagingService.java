package com.example.duankhachhang.Model;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.duankhachhang.Fragment.Fragment_home_screenHome;
import com.example.duankhachhang.Fragment.Fragment_message_screenHome;
import com.example.duankhachhang.Home;
import com.example.duankhachhang.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        createChanelNotification();
        RemoteMessage.Notification notification = message.getNotification();
        if (notification == null) {
            return;
        }
        String title = notification.getTitle();
        String strMessage = notification.getBody();
        String tag = notification.getTag();
        System.out.println("tag:" + tag);
        sendNotification(title, strMessage, tag);
    }

    private void createChanelNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NotificationType.NotificationNormal(), "Thong bao", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void sendNotification(String title, String strMessage, String notificationType) {
        Intent intent = new Intent(this, Fragment_home_screenHome.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder notificationCompat;
        if (notificationType.equals(NotificationType.NotificationNormal())) {
            notificationCompat = new NotificationCompat.Builder(this, notificationType)
                    .setContentTitle(title)
                    .setContentText(strMessage)
                    .setSmallIcon(R.drawable.icon_product)
                    .setContentIntent(pendingIntent);
        } else if (notificationType.equals("chat")) {
            notificationCompat = new NotificationCompat.Builder(this, NotificationType.NotificationNormal())
                    .setContentTitle(title)
                    .setContentText(strMessage)
                    .setSmallIcon(R.drawable.icon_chat_notification)
                    .setContentIntent(pendingIntent);
        } else {
            notificationCompat = new NotificationCompat.Builder(this, NotificationType.NotificationNormal())
                    .setContentTitle(title)
                    .setContentText(strMessage)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentIntent(pendingIntent);
        }
        Notification notification = notificationCompat.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(1, notification);
        }

    }
}
