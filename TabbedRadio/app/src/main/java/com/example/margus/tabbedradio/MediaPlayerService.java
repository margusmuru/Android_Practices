package com.example.margus.tabbedradio;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Margus Muru on 07.05.2017.
 */

public class MediaPlayerService extends Service implements
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnInfoListener
{

    private static final String TAG = MediaPlayerService.class.getSimpleName();

    //TODO use exoplayer
    private MediaPlayer mMediaPlayer = new MediaPlayer();
    private String mMediaSource = "";

    private PhoneStateListener mPhoneStateListener;
    private TelephonyManager telephonyManager;

    private ScheduledExecutorService mScheduledExecutorService;

    private boolean mMusicIsPausedInCall;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "OnCreate");

        //set up the media player
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnInfoListener(this);
        mMediaPlayer.reset();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        if(intent == null){
            return START_NOT_STICKY;
        }

        mMediaPlayer.reset();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mMediaSource = intent.getExtras().getString(C.INTENT_STREAM_SOURCE);
        Log.d(TAG, "onStartCommand: Media Source " + mMediaSource);

        telephonyManager = (TelephonyManager) getSystemService(getBaseContext().TELEPHONY_SERVICE);
        
        mPhoneStateListener = new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                //super.onCallStateChanged(state, incomingNumber);
                Log.d(TAG, "onCallStateChanged: " + state);

                switch (state){
                    //waiting for a call/ call ended
                    case TelephonyManager.CALL_STATE_IDLE:
                        Log.d(TAG, "onCallStateChanged: IDLE");
                        if(mMediaPlayer != null && mMusicIsPausedInCall){

                            try {
                                mMediaPlayer.reset();
                                mMediaPlayer.setDataSource(mMediaSource);
                                mMediaPlayer.prepareAsync();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        break;

                    //call ongoing
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        Log.d(TAG, "onCallStateChanged: OFFHOOK");
                        break;

                    //incoming call
                    case TelephonyManager.CALL_STATE_RINGING:
                        Log.d(TAG, "onCallStateChanged: RINGING");
                        if(mMediaPlayer != null){
                            mMediaPlayer.stop();
                            mMusicIsPausedInCall = true;
                            LocalBroadcastManager.getInstance(getApplicationContext())
                                    .sendBroadcast(new Intent(C.INTENT_STREAM_STATUS_STOPPED));
                        }
                        break;

                    default:
                        Log.d(TAG, "onCallStateChanged: UNKNOWN");
                        break;
                }
            }
        };
        telephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        
        try {
            mMediaPlayer.setDataSource(mMediaSource);
            mMediaPlayer.prepareAsync();
            LocalBroadcastManager.getInstance(getApplicationContext())
                    .sendBroadcast(new Intent(C.INTENT_STREAM_STATUS_BUFFERING));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //================================== MEDIAPLAYER callbacks ========================================================


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Mediaplayer service");
        if(mMediaPlayer != null){
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        if(mPhoneStateListener != null){
            telephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
        }

        LocalBroadcastManager
                .getInstance(getApplicationContext())
                .sendBroadcast(new Intent(C.INTENT_STREAM_STATUS_STOPPED));
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(TAG, "MediaPlayer OnCompleteion");
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.d(TAG, "MediaPlayer OnError");
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        Log.d(TAG, "MediaPlayer OnInfo");
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d(TAG, "MediaPlayer OnPrepared");

        mMediaPlayer.start();
        LocalBroadcastManager.getInstance(getApplicationContext())
                .sendBroadcast(new Intent(C.INTENT_STREAM_STATUS_PLAYING));

        startMediaInfoService();
    }

    private void startMediaInfoService(){
        mScheduledExecutorService = Executors.newScheduledThreadPool(5);

        mScheduledExecutorService.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        //this will be executed at fixed rate
                        new GetSongInfo().execute();
                    }
                },
                0, //initial delay
                15, //execute interval
                TimeUnit.SECONDS //time units
        );
    }

    private class GetSongInfo extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            String url = "http://dad.akaver.com/api/songtitles/SP";

            StringRequest stringRequest = new StringRequest(
                    Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "onResponse: http get response: " + response);

                            String artist = "";
                            String title = "";

                            try{
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("SongHistoryList");
                                JSONObject songInfoObject = jsonArray.getJSONObject(0);
                                artist = songInfoObject.getString("Artist");
                                title = songInfoObject.getString("Title");
                            }catch (Exception e){
                                Log.e(TAG, "onResponse: JSON", e);
                            }

                            Intent infoIntent = new Intent(C.INTENT_STREAM_INFO);
                            infoIntent.putExtra(C.INTENT_STREAM_INFO_ARTIST, artist);
                            infoIntent.putExtra(C.INTENT_STREAM_INFO_TITLE, title);
                            LocalBroadcastManager.getInstance(getApplicationContext())
                                    .sendBroadcast(new Intent(infoIntent));
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "onErrorResponse: http get failed: " + error.toString());
                        }
                    }
            );

            WebApiSingletonServiceHandler
                    .getmInstance(getApplicationContext())
                    .addToRequestQueue(stringRequest);

            return null;
        }
    }

}
