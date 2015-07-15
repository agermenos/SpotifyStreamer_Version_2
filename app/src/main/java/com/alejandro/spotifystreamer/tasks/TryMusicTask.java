package com.alejandro.spotifystreamer.tasks;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

/**
 * Created by agermenos on 7/14/15.
 */
public class TryMusicTask extends AsyncTask<Object, Void, Void> {
    private static final String LOG_TAG = TryMusicTask.class.getSimpleName();
    private  MediaPlayer mediaPlayer;

    public Void doInBackground(Object... params){
        mediaPlayer = (MediaPlayer)params[1];
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            String url = (String)params[0];
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare(); // might take long! (for buffering, etc)

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });

            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.e(LOG_TAG, "Error searchign for the track: ");
                    return false;
                }
            });
        }
        catch (IOException ioException) {
            Log.e(LOG_TAG, ioException.getMessage());
        }
        return null;
    }
}
