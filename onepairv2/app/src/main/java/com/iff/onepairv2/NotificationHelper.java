package com.iff.onepairv2;

import android.content.Context;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

/**
 * Helps to format and craft a notification
 */
public class NotificationHelper {

    /**
     * Channel ID
     */
    private static final String CHANNEL_ID = "simplified coding";

    /**
     * Displays a notification
     * @param context
     * @param title
     * @param body
     */
    public static void displayNotification(Context context, String title, String body) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.appicon)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat mNotificationManagerCompat = NotificationManagerCompat.from(context);
        mNotificationManagerCompat.notify(1, mBuilder.build());

    }
}
