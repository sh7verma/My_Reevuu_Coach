package com.myreevuuCoach.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.myreevuuCoach.interfaces.InterConst;

/**
 * Created by dev on 28/11/18.
 */

public class ScreenOffReceiver extends BroadcastReceiver {
    public static Call call;

    public static void setCall(Call call) {
        ScreenOffReceiver.call = call;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            Log.i("[BroadcastReceiver]", "Screen ON");
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Log.i("[BroadcastReceiver]", "Screen OFF");
            context.sendBroadcast(new Intent(InterConst.BROADCAST_VIDEO_STOP_RECIVER));
        }
    }
    interface Call{
        void callback(Intent intent);
    }
}
