package pt.simov.pl4;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.messaging.RemoteMessage;


public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService { //creates this class to get the foreground notification also(before this class we only had notification in the background)
    //this means that this class will make it possible to get a notification even though the app is running

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String notification_title = remoteMessage.getData().get("title");
        String notification_body = remoteMessage.getData().get("body");

        String CHANNEL_ID = "my_channel";



            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "My_chanel_1";
                String description = "Channel for notification";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                channel.setDescription(description);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);


        String click_action = remoteMessage.getData().get("click_action");
        String from_sender_id = remoteMessage.getData().get("from_sender_id"); //here we retrieve code from our firebase functions

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.mywishlist)
                .setContentTitle(notification_title)
                .setContentText(notification_body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


                Intent resultIntent = new Intent(click_action);
                resultIntent.putExtra("visit_user_id", from_sender_id);

                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                this,
                                0,
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );

                mBuilder.setContentIntent(resultPendingIntent);





        int mNotificationId= (int) System.currentTimeMillis();
        notificationManager.notify(mNotificationId, mBuilder.build());




        }



    }
}
