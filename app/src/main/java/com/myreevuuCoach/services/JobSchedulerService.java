package com.myreevuuCoach.services;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import com.myreevuuCoach.interfaces.InterConst;
import com.myreevuuCoach.utils.Utils;

/**
 * Created by dev on 18/12/18.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class JobSchedulerService extends JobService {

    private Handler mJobHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            jobFinished((JobParameters) msg.obj, false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                scheduleRefresh();
            if (checkServiceRunning()) {
                Utils utils = new Utils(getApplicationContext());
                if (!utils.getBoolean(InterConst.SERVICE_RUNNING, false)) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                        startService(new Intent(getApplicationContext(), ListenerService.class));
                    } else {
                        startForegroundService(new Intent(getApplicationContext(), ListenerService.class));
                    }
                }
            } else {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                    startService(new Intent(getApplicationContext(), ListenerService.class));
                } else {
                    startForegroundService(new Intent(getApplicationContext(), ListenerService.class));
                }
            }
            return true;
        }
    });

    @Override
    public boolean onStartJob(JobParameters params) {
        mJobHandler.sendMessage(Message.obtain(mJobHandler, 1, params));
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        mJobHandler.removeMessages(1);
        return false;
    }

    public boolean checkServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.myreevuuCoach.services.ListenerService"
                    .equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    private void scheduleRefresh() {
        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        ComponentName jobService = new ComponentName(getPackageName(), JobSchedulerService.class.getName());
        JobInfo jobInfo = new JobInfo.Builder(1, jobService).setMinimumLatency(5000).build();
        jobScheduler.schedule(jobInfo);
    }

}