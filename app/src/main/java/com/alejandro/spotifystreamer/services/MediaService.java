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

    MediaPlayer mediaPlayer = null;
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

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e(LOG_TAG, "WHAT : " + what + " EXTRA: " + extra);
        return false;
    }


    public void resetMediaPlayer(){
        if (mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }

    public void startMediaPlayer(String url){
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
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

    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }

    public void stopMediaPlayer(){
        if (mediaPlayer!=null && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
    }

    public int getCurrentPosition(){
        if (mediaPlayer!=null){
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public int getDuration(){
        if (mediaPlayer!=null) {
            return mediaPlayer.getDuration();
        }
        return 0;
    }

    public class MyBinder extends Binder {
        public MediaService getService() {
            return MediaService.this;
        }
    }
}
