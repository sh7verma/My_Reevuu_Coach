package com.myreevuuCoach.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.myreevuuCoach.firebase.FirebaseChatConstants;
import com.myreevuuCoach.firebase.FirebaseListeners;
import com.myreevuuCoach.interfaces.InterConst;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressLint("NewApi")
public class Foreground implements Application.ActivityLifecycleCallbacks {
    public static final long CHECK_DELAY = 500;
    public static final String TAG = Foreground.class.getName();
    SharedPreferences sp;
    Utils mUtil;
    DatabaseReference mFirebaseConfig;


    public interface Listener {

        public void onBecameForeground();

        public void onBecameBackground();

    }

    private static Foreground instance;

    private boolean foreground = false, paused = true;
    private Handler handler = new Handler();
    private List<Listener> listeners = new CopyOnWriteArrayList<Listener>();
    private Runnable check;

    /**
     * Its not strictly necessary to use this method - _usually_ invoking get
     * with a Context gives us a path to retrieve the Application and
     * initialise, but sometimes (e.g. in view_tul harness) the ApplicationContext
     * is != the Application, and the docs make no guarantees.
     *
     * @param application
     * @return an initialised Foreground instance
     */
    @SuppressLint("NewApi")
    public static Foreground init(Application application) {
        if (instance == null) {
            instance = new Foreground();
            application.registerActivityLifecycleCallbacks(instance);
        }
        return instance;
    }

    public static Foreground get(Application application) {
        if (instance == null) {
            init(application);
        }
        return instance;
    }

    public static Foreground get(Context ctx) {
        if (instance == null) {
            Context appCtx = ctx.getApplicationContext();
            if (appCtx instanceof Application) {
                init((Application) appCtx);
            }
            throw new IllegalStateException(
                    "Foreground is not initialised and "
                            + "cannot obtain the Application object");
        }
        return instance;
    }

    public static Foreground get() {
        if (instance == null) {
            throw new IllegalStateException(
                    "Foreground is not initialised - invoke "
                            + "at least once with parameterised init/get");
        }
        return instance;
    }

    public boolean isForeground() {
        return foreground;
    }

    public boolean isBackground() {
        return !foreground;
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        sp = activity.getSharedPreferences(activity.getPackageName(),
                Context.MODE_PRIVATE);
        mUtil = new Utils(activity);
        mFirebaseConfig = FirebaseDatabase.getInstance().getReference().child(FirebaseChatConstants.USERS);
        paused = false;
        boolean wasBackground = !foreground;
        foreground = true;

        if (check != null)
            handler.removeCallbacks(check);

        if (wasBackground) {
            Log.i(TAG, "went foreground");
            setOnline();
            for (Listener l : listeners) {
                try {
                    l.onBecameForeground();
                } catch (Exception exc) {
                    Log.e(TAG, "Listener threw exception!", exc);
                }
            }
        } else {
            Log.i(TAG, "still foreground");
            setOnline();
        }


    }

    @Override
    public void onActivityPaused(final Activity activity) {
        sp = activity.getSharedPreferences(activity.getPackageName(),
                Context.MODE_PRIVATE);
        paused = true;
        mFirebaseConfig = FirebaseDatabase.getInstance().getReference().child(FirebaseChatConstants.USERS);

        if (check != null)
            handler.removeCallbacks(check);

        handler.postDelayed(check = new Runnable() {
            @Override
            public void run() {
                if (foreground && paused) {
                    foreground = false;
                    Log.i(TAG, "went background");
                    setOffline();

                    for (Listener l : listeners) {
                        try {
                            l.onBecameBackground();
                        } catch (Exception exc) {
                            Log.e(TAG, "Listener threw exception!", exc);
                        }
                    }
                } else {
                    Log.i(TAG, "still foreground");
                    setOnline();
                }
            }
        }, CHECK_DELAY);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    private void setOnline() {
        //Inside
        sp.edit().putInt(InterConst.BACKGROUND, InterConst.APP_ONLINE).apply();
        Log.e("Inside ", "Yes");

        FirebaseListeners.getTime(mUtil);
        if (!TextUtils.isEmpty(sp.getString(InterConst.ACCESS_TOKEN, ""))
                && sp.getInt(InterConst.PROFILE_STATUS, 0) == 2) {
            mFirebaseConfig
                    .child("id_" + String.valueOf(sp.getInt(FirebaseChatConstants.user_id, -1)))
                    .child("online_status")
                    .setValue(FirebaseChatConstants.ONLINE_LONG);
        }
    }

    private void setOffline() {
        // Outside
        sp.edit().putInt(InterConst.BACKGROUND, InterConst.APP_OFFLINE).apply();
        Log.e("OutSide  ", "Yes");

        if (!TextUtils.isEmpty(sp.getString(InterConst.ACCESS_TOKEN, ""))
                && sp.getInt(InterConst.PROFILE_STATUS, 0) == 2) {
            mFirebaseConfig
                    .child("id_" + String.valueOf(sp.getInt(FirebaseChatConstants.user_id, -1)))
                    .child("online_status")
                    .setValue(ServerValue.TIMESTAMP);
        }
    }
}

