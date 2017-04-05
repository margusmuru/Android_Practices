package com.muru.margus.activitydemo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView mTextView;

    private BroadcastReceiver mBroadcastReceiver;
    private IntentFilter mIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.textView);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("com.muru.margus.TimeFromService");

        mBroadcastReceiver = new BroadcastReceiverMain();

        Intent intentStartService = new Intent(this, BackgroundService.class);
        startService(intentStartService);
    }

    public void buttonCallRemoteReceiver(View view){
        Intent intentCallRemoteReceiver = new Intent();
        intentCallRemoteReceiver.setAction("com.muru.margus.timeRequest");
        intentCallRemoteReceiver.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);

        sendOrderedBroadcast(
                intentCallRemoteReceiver,
                null,
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String responseData = getResultData();
                        mTextView.setText(responseData);
                    }
                },
                null,
                Activity.RESULT_OK,
                null,
                null
        );
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");

        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, mIntentFilter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");

        Intent intentStopService = new Intent(this, BackgroundService.class);
        startService(intentStopService);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
    }

    private class BroadcastReceiverMain extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "BroadcastReceiverMain.onReceive" + intent.getAction());

            if(intent.getAction() == "com.muru.margus.TimeFromService"){
                mTextView.setText(intent.getStringExtra("time"));
            }
        }
    }

}
