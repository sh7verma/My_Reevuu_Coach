package com.myreevuuCoach;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.google.firebase.FirebaseApp;
import com.myreevuuCoach.utils.ConnectivityReceiver;
import com.myreevuuCoach.utils.Foreground;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

public class MainApplication extends MultiDexApplication {

    private static MainApplication instance;

    @Override
    public void onCreate() {
        FirebaseApp.initializeApp(this);
        Timber.plant(new Timber.DebugTree());
        Foreground.init(this);
        MultiDex.install(this);
        instance = this;

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
