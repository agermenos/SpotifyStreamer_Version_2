package com.alejandro.spotifystreamer.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.*;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alejandro.spotifystreamer.helpers.PlayerConstants;

import java.io.FileDescriptor;
import java.io.IOException;

/**
 * Created by Alejandro on 8/9/2015.
 */
public class MediaService extends IntentService implements PlayerConstants {

    MediaPlayer mediaPlayer = null;
    private boolean isPlaying=false;
    private static final String LOG_TAG = MediaService.class.getSimpleName();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MediaService(String name) {
        super(name);
    }

    public MediaService(){
        super("test");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle data = intent.getBundleExtra(DATA);
        Integer instruction = data.getInt(COMMAND);
        switch (instruction){
            case ACTION_INITIALIZE:
                String url = data.getString(MEDIA_URL);
                startMediaPlayer(url);
                break;
        }
    }

    private void startMediaPlayer(String url){
        isPlaying=true;
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
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
}
