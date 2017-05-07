package com.example.margus.tabbedradio;

/**
 * Created by Margus Muru on 07.05.2017.
 * define and keep all constants here
 */

public class C {

    public static final int STREAM_STATUS_STOPPED = 0;
    public static final int STREAM_STATUS_BUFFERING = 1;
    public static final int STREAM_STATUS_PLAYING = 2;

    public static final String INTENT_STREAM_SOURCE = "com.example.margus.tabbedradio.intent.streamSource";
    public static final String INTENT_STREAM_STATUS_BUFFERING = "com.example.margus.tabbedradio.intent.streamstatus.bufferid";
    public static final String INTENT_STREAM_STATUS_STOPPED = "com.example.margus.tabbedradio.intent.streamstatus.stopped";
    public static final String INTENT_STREAM_STATUS_PLAYING = "com.example.margus.tabbedradio.intent.streamstatus.playing";

    public static final String INTENT_STREAM_VOLUME_CHANGED = "com.example.margus.tabbedradio.intent.volume.changed";

    public static final String INTENT_STREAM_INFO = "com.example.margus.tabbedradio.intent.media.info";
    public static final String INTENT_STREAM_INFO_ARTIST = "com.example.margus.tabbedradio.intent.media.artist";
    public static final String INTENT_STREAM_INFO_TITLE = "com.example.margus.tabbedradio.intent.media.title";

}
