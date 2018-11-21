package com.example.lexar.projecthbo_ictbv.helper;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.lexar.projecthbo_ictbv.R;
import com.example.lexar.projecthbo_ictbv.activity.QuestionActivity;
import com.example.lexar.projecthbo_ictbv.model.Location;
import com.example.lexar.projecthbo_ictbv.model.LocationStatus;
import com.example.lexar.projecthbo_ictbv.model.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * NotificationManager
 *
 * This class handles all our notifications; it sends them, cancels them and creates notification
 * channels when used on Android Oreo (or above).
 *
 * Created by wesselperik on 24/06/2018.
 */

public class NotificationManager {

    /**
     * Send a notification to the user if an GPS/location error occurs.
     * @param context app context
     * @param status the status of the location
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void sendNotification(Context context, LocationStatus status) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        android.app.NotificationManager notificationManager = (android.app.NotificationManager)
                context.getSystemService(Service.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager,
                    Constants.NOTIFICATION_CHANNEL_LOCATION_STATUS,
                    context.getString(
                            R.string.notification_channel_location_status_name),
                    context.getString(R.string.notification_channel_location_status_description));
        }

        String notificationTitle = "";
        String notificationText = "";

        if (status == LocationStatus.GPS_DISCONNECTED) {
            notificationTitle = context.getString(R.string.error_gps_disconnected_title);
            notificationText = context.getString(R.string.error_gps_disconnected_text);
        } else if (status == LocationStatus.GPS_DISABLED) {
            notificationTitle = context.getString(R.string.error_gps_disabled_title);
            notificationText = context.getString(R.string.error_gps_disabled_text);
        } else if (status == LocationStatus.GPS_FAILED) {
            notificationTitle = context.getString(R.string.error_gps_failed_title);
            notificationText = context.getString(R.string.error_gps_failed_text);
        }

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(
                        context, Constants.NOTIFICATION_CHANNEL_LOCATION_STATUS)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setSmallIcon(R.drawable.ibiparrot)
                        .setContentTitle(notificationTitle)
                        .setContentText(notificationText)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(false);
        notificationManager.notify(
                Constants.NOTIFICATION_ID_LOCATION_STATUS, notificationBuilder.build());
        Log.d("NotificationManager", "showing GPS error notification");
    }

    /**
     * Send a notification to the user if there are new questions available.
     * @param context app context
     * @param location the location which has questions
     * @param questions the questions
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void sendNotification(Context context, Location location, JSONArray questions) {
        Intent intent = new Intent(context, QuestionActivity.class);
        ArrayList<Question> questionList = new ArrayList<>();
        for (int i = 0; i < questions.length(); i++) {
            try {
                questionList.add(new Question(questions.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        intent.putExtra("location", location);
        intent.putParcelableArrayListExtra("questions", questionList);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        android.app.NotificationManager notificationManager = (android.app.NotificationManager)
                context.getSystemService(Service.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel
                    (notificationManager, Constants.NOTIFICATION_CHANNEL_QUESTIONS,
                            context.getString(R.string.notification_channel_questions_name),
                            context.getString(R.string.notification_channel_questions_description));
        }

        String notificationTitle = "Je bent bij " + location.getName();
        String notificationText =
                "We hebben een paar vragen voor je over deze locatie.";


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder
                (context, Constants.NOTIFICATION_CHANNEL_QUESTIONS)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setSmallIcon(R.drawable.ibiparrot)
                        .setContentTitle(notificationTitle)
                        .setContentText(notificationText)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(false);
        notificationManager.notify(
                Constants.NOTIFICATION_ID_QUESTIONS, notificationBuilder.build());
        Log.d("NotificationManager", "showing questions notification");
    }

    /**
     * Method to cancel a notification.
     */
    public static void cancelNotification(Context context, int notificationId) {
        android.app.NotificationManager notificationManager =
                (android.app.NotificationManager) context.getSystemService
                        (Service.NOTIFICATION_SERVICE);
        notificationManager.cancel(notificationId);
    }

    /**
     * Create a notification channel for the notifications. Required in Android 26 (Oreo) and up.
     * @param notificationManager the notification manager
     * @param id ID for the notification channel
     * @param name name for the notification channel
     * @param description description for the notification channel
     */
    @TargetApi(26)
    private static void createNotificationChannel(
            android.app.NotificationManager notificationManager,
            String id, String name, String description) {
        int importance = android.app.NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel mChannel = new NotificationChannel(id, name, importance);
        mChannel.setDescription(description);
        mChannel.enableLights(true);
        mChannel.setLightColor(Color.BLUE);
        notificationManager.createNotificationChannel(mChannel);
    }
}
