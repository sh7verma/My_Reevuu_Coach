package com.myreevuuCoach.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.myreevuuCoach.R;
import com.myreevuuCoach.firebase.FirebaseChatConstants;
import com.myreevuuCoach.firebase.FirebaseListeners;
import com.myreevuuCoach.utils.Utils;

/**
 * Created by dev on 18/12/18.
 */

public class ListenerService extends Service {

    Context mContext;
    Utils util;

    @Override
    public void onCreate() {
        super.onCreate();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_CANCEL_CURRENT);
//            Notification notification =
//                    new Notification.Builder(this)
//                            .setSmallIcon(R.mipmap.ic_launcher_round)
//                            .setContentIntent(pendingIntent)
//                            .setColor(ContextCompat.getColor(getApplicationContext(), R.color.black))
//                            .build();
//            startForeground(63, notification);
//        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyOwnForeground();
        }
//        else
//            startForeground(1, new Notification());
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "com.myreevuuCoach";
        String channelName = "My Background Service";
        @SuppressLint("WrongConstant") NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = getApplicationContext();
        util = new Utils(getApplicationContext());
        if (util.getInt(FirebaseChatConstants.user_id, -1) != -1) {
            FirebaseListeners.getListenerClass(getApplicationContext()).setProfileListener(String.valueOf(util.getInt(FirebaseChatConstants.user_id, -1)));
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Intent broadcastIntent = new Intent("ActivityRecognition.RestartService");
//        sendBroadcast(broadcastIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true); //true will remove notification
        }
    }

}
