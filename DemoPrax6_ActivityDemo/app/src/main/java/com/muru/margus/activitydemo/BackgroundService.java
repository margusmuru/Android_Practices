package com.muru.margus.activitydemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by margus on 05.04.2017.
 */

public class BackgroundService extends Service {
    private static final String TAG = BackgroundService.class.getSimpleName();

    private ScheduledExecutorService mScheduledExecutorService;

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand");

        mScheduledExecutorService = Executors.newScheduledThreadPool(5);
        mScheduledExecutorService.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "scheduleAtFixedRate run");
                        Intent timeIntent = new Intent("com.muru.margus.TimeFromService");
                        timeIntent.putExtra("time", new Date().toString() + " background service");
                        LocalBroadcastManager
                                .getInstance(getApplicationContext())
                                .sendBroadcast(timeIntent);
                    }
                },
                0,
                5,
                TimeUnit.SECONDS
        );

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
