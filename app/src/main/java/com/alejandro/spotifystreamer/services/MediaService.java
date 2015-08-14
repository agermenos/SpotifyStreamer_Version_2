package com.alejandro.spotifystreamer.services;

import android.app.IntentService;
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
public class MediaService extends Service implements PlayerConstants {

    MediaPlayer mediaPlayer = null;
    private boolean isPlaying=false;
    private static final String LOG_TAG = MediaService.class.getSimpleName();
    private final IBinder mBinder = new MyBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Bundle data = intent.getBundleExtra(DATA);
        Integer instruction = data.getInt(COMMAND);
        switch (instruction){
            case ACTION_INITIALIZE:
                String url = data.getString(MEDIA_URL);
                startMediaPlayer(url);
                break;
        }
        return mBinder;
    }

    protected void onHandleIntent(Intent intent) {

    }

    private void startMediaPlayer(String url){
        isPlaying=true;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });

            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.e(LOG_TAG, "Error searching for the track: " + what + ":" + extra);
                    return false;
                }
            });
        }
        catch (IOException ioException) {
            Log.e(LOG_TAG, ioException.getMessage());
        }
    }

    public class MyBinder extends Binder {
        public MediaService getService() {
            return MediaService.this;
        }
    }
}
