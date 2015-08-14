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
    private int currentPosition=0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        return mBinder;
    }

    public void startMediaPlayer(String url){
        isPlaying=true;
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                    currentPosition=0;
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

    public void playFromPosition(int position){
        if (mediaPlayer!=null){
            mediaPlayer.seekTo(position);
            mediaPlayer.start();
        }
    }

    public void pauseMediaPlayer(){
        if (mediaPlayer!=null && mediaPlayer.isPlaying()){
            currentPosition=mediaPlayer.getCurrentPosition();
            mediaPlayer.stop();
        }
    }

    public void stopMediaPlayer(){
        if (mediaPlayer!=null && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
    }

    public class MyBinder extends Binder {
        public MediaService getService() {
            return MediaService.this;
        }
    }
}
