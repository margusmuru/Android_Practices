package com.muru.margus.braindemo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Date;

/**
 * Created by margu on 05.04.2017.
 */

public class TimeReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        setResultCode(Activity.RESULT_OK);
        setResultData(new Date().toString() + " - remote reciever");
    }
}
