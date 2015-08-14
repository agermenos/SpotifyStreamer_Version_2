package com.alejandro.spotifystreamer.services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.*;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alejandro.spotifystreamer.helpers.PlayerConstants;

import java.io.IOException;

/**
 * Created by Alejandro on 8/9/2015.
 */
public class MediaService extends Service implements PlayerConstants, MediaPlayer.OnErrorListener {

    public MediaPlayer mediaPlayer = null;
    private static final String LOG_TAG = MediaService.class.getSimpleName();
    private final IBinder mBinder = new MyBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        return mBinder;
    }

    public void resetMediaPlayer(){
        mediaPlayer = new MediaPlayer();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e(LOG_TAG, "WHAT : " + what + " EXTRA: " + extra);
        return false;
    }



    public class MyBinder extends Binder {
        public MediaService getService() {
            return MediaService.this;
        }
    }
}
