package com.myreevuuCoach.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.myreevuuCoach.R;
import com.myreevuuCoach.activities.BroadcastActivity;
import com.myreevuuCoach.activities.ConversationActivity;
import com.myreevuuCoach.activities.FeedDetailActivity;
import com.myreevuuCoach.activities.LandingActivity;
import com.myreevuuCoach.activities.RegisterCoachActivity;
import com.myreevuuCoach.activities.RequestDetailActivity;
import com.myreevuuCoach.interfaces.InterConst;
import com.myreevuuCoach.utils.Constants;
import com.myreevuuCoach.utils.Utils;

import java.util.Map;

import io.intercom.android.sdk.push.IntercomPushClient;
import me.leolin.shortcutbadger.ShortcutBadger;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private static final String TAG = "MyFirebaseMsgService";
    private final IntercomPushClient intercomPushClient = new IntercomPushClient();
    Utils utils;
    private LocalBroadcastManager broadcaster;

    @Override
    public void onNewToken(String s) {
        Log.e(TAG, "Device Token " + s);
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        utils = new Utils(getApplicationContext());
        broadcaster = LocalBroadcastManager.getInstance(getBaseContext());
        // Check if message contains a data payload.

        if (intercomPushClient.isIntercomPush(remoteMessage.getData())) {
            intercomPushClient.handlePush(getApplication(), remoteMessage.getData());
        } else {
            if (remoteMessage.getData().size() > 0) {
                Map<String, String> data = remoteMessage.getData();
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());
                sendNotification(data);
            }
        }

    }

    private void sendNotification(Map<String, String> messageBody) {
        Intent intent = null;
        String message = messageBody.get("message");
        int notificationId;

        if (messageBody.get("push_type").equalsIgnoreCase("13")) {
            if (utils.getInt("Background", 0) == 1) {// background
                intent = new Intent(this, LandingActivity.class);
                intent.putExtra("BROADCAST_DATA_INTENT", "Yes");
                intent.putExtra("broadcastTitle", messageBody.get("title"));
                intent.putExtra("broadcastMessage", message);
                intent.putExtra(InterConst.NotificationID, messageBody.get("broadcast_id"));

                ringNotification(intent, message, 0, messageBody.get("title"));
            } else {
                intent = new Intent(this, BroadcastActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                intent.putExtra("broadcastTitle", messageBody.get("title"));
                intent.putExtra("broadcastMessage", message);
                intent.putExtra(InterConst.NotificationID, messageBody.get("broadcast_id"));
                startActivity(intent);
            }
        } else if (messageBody.get("push_type").equals("14")) {

            if (utils.getInt(InterConst.BACKGROUND, InterConst.APP_OFFLINE) == InterConst.APP_ONLINE) {
                Intent notificationIntent = new Intent(InterConst.BROADCAST_VIDEO_ADDED_RECIVER);
                notificationIntent.putExtra(InterConst.INTEND_EXTRA,"");
                LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(notificationIntent);
            }

            intent = new Intent(this, FeedDetailActivity.class);
            intent.putExtra("id", messageBody.get("video_id") + "");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ringNotification(intent, message, 0, messageBody.get("title"));
        } else if (messageBody.get("push_type").equalsIgnoreCase("1")) {
            if (utils.getInt("inside_verify", 0) == 0) {
                /// outside verify email activity
                intent = new Intent(this, RegisterCoachActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ringNotification(intent, message, 1, messageBody.get("title"));
            } else {
                /// inside verify email activity
                Intent notificationIntent = new Intent(Constants.Companion.getEMAIL_VERIFY());
                broadcaster.sendBroadcast(notificationIntent);
            }
        } else if (messageBody.get("push_type").equalsIgnoreCase("2")) {

            intent = new Intent(this, LandingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ringNotification(intent, message, 2, messageBody.get("title"));

            utils.setInt(InterConst.PROFILE_APPROVED, 1);

        } else if (messageBody.get("push_type").equalsIgnoreCase("4")) {
            utils.setInt(InterConst.UNREAD_NOTIFICATION_DOT, 1);
            LocalBroadcastManager.getInstance(getBaseContext()).
                    sendBroadcast(new Intent(Constants.Companion.getNEW_MESSAGE()));

            intent = new Intent(this, RequestDetailActivity.class);
            intent.putExtra("reviewRequestId", messageBody.get("review_request_id").toString());
            intent.putExtra(InterConst.NotificationID, messageBody.get("id") + "");
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            ringNotification(intent, message, 4, messageBody.get("title"));

            int count = utils.getInt(InterConst.NEW_REQUEST_COUNT, 0);
            count++;
            utils.setInt(InterConst.NEW_REQUEST_COUNT, count);

            if (utils.getInt(InterConst.BACKGROUND, InterConst.APP_OFFLINE) == InterConst.APP_ONLINE) {
                Intent notificationIntent = new Intent(InterConst.BROADCAST_VIDEO_ADDED_RECIVER);
                LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(notificationIntent);
            }

        } else if (messageBody.get("push_type").equalsIgnoreCase("8")) {
            if (!utils.getString("chat_dialog_id", "").equals(messageBody.get("chat_dialog_id"))) {

                if (utils.getInt(InterConst.BACKGROUND, InterConst.APP_OFFLINE) == InterConst.APP_OFFLINE) {
                    int APP_ICON_BADGE_COUNT;
                    APP_ICON_BADGE_COUNT = utils.getInt(InterConst.APP_ICON_BADGE_COUNT, 0);
                    APP_ICON_BADGE_COUNT++;
                    utils.setInt(InterConst.APP_ICON_BADGE_COUNT, APP_ICON_BADGE_COUNT);
                    ShortcutBadger.applyCount(getApplicationContext(), APP_ICON_BADGE_COUNT);
                }

                utils.setString("participant_ids", messageBody.get("chat_dialog_id"));
                intent = new Intent(this, ConversationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                ringNotification(intent, message, 0, messageBody.get("sender_name"));
            }
        } else if (messageBody.get("push_type").equalsIgnoreCase("12")) {
            if (utils.getInt(InterConst.BACKGROUND, InterConst.APP_OFFLINE) == InterConst.APP_ONLINE) {
                Intent notificationIntent = new Intent(InterConst.BROADCAST_VIDEO_PROCESSED);
                LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(notificationIntent);
            }
            intent = new Intent(this, RequestDetailActivity.class);
            intent.putExtra("reviewRequestId", messageBody.get("review_request_id").toString());
            intent.putExtra(InterConst.NotificationID, messageBody.get("id") + "");
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            ringNotification(intent, message, 4, messageBody.get("title"));

        } else if (messageBody.get("push_type").equalsIgnoreCase("15")) {
            if (utils.getInt(InterConst.BACKGROUND, InterConst.APP_OFFLINE) == InterConst.APP_ONLINE) {
                LocalBroadcastManager.getInstance(getBaseContext()).
                        sendBroadcast(new Intent(InterConst.BROADCAST_PROFILE_APPROVED));
            }
            intent = new Intent(this, LandingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ringNotification(intent, message, 4, messageBody.get("title"));

        } else {
            intent = new Intent(this, LandingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ringNotification(intent, message, 0, messageBody.get("title"));
        }


    }

    void ringNotification(Intent intent, String mess, int notificationId, String title) {
        int mIcon;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            mIcon = R.mipmap.ic_logo;
        else
            mIcon = R.mipmap.ic_logo;

        int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, uniqueInt, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(mIcon)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentTitle(title)
                .setContentText(mess)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(mess))
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
            notificationChannel.enableVibration(true);
            notificationChannel.setShowBadge(true);
            notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(notificationId, notificationBuilder.build());
    }
}