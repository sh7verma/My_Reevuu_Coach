package com.myreevuuCoach;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.bugsee.library.Bugsee;
import com.google.firebase.FirebaseApp;
import com.myreevuuCoach.utils.ConnectivityReceiver;
import com.myreevuuCoach.utils.Foreground;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import io.intercom.android.sdk.Intercom;
import timber.log.Timber;

public class MainApplication extends MultiDexApplication {

    private static MainApplication instance;
    static final String YOUR_API_KEY = "android_sdk-4c975c4c49bfdda79ba8ecc1f3118cad41bbee40";
    static final String YOUR_APP_ID = "xm0m87hr";
    @Override
    public void onCreate() {
        FirebaseApp.initializeApp(this);
        Timber.plant(new Timber.DebugTree());
        Foreground.init(this);
        MultiDex.install(this);
        instance = this;
        HashMap<String, Object> options = new HashMap<>();
        options.put(Bugsee.Option.MaxRecordingTime, 60);
        options.put(Bugsee.Option.ShakeToTrigger, false);
        Bugsee.launch(this, "ea6958be-adfe-4ce0-a861-205869ac4594",options);
        Intercom.initialize(this, YOUR_API_KEY, YOUR_APP_ID);
        super.onCreate();
    }


    public static MainApplication getInstance() {
        return instance;
    }

    public static boolean hasNetwork() {
        return instance.checkIfHasNetwork();
    }

    public boolean checkIfHasNetwork() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

}
