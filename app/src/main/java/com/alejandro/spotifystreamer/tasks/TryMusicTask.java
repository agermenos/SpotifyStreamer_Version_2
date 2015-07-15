package com.alejandro.spotifystreamer.tasks;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ProgressBar;

import java.io.IOException;

/**
 * Created by agermenos on 7/14/15.
 */
public class TryMusicTask extends AsyncTask<Object, Void, Void> {
    private static final int TIME_DIFERENTIAL=50;
    private static final String LOG_TAG = TryMusicTask.class.getSimpleName();
    private MediaPlayer mediaPlayer;
    private ProgressBar progressBar;

    /**
     * Prepares the media player to run...
     * @param params
     * @return
     */
    public Void doInBackground(Object... params){
        mediaPlayer = (MediaPlayer)params[1];
        progressBar = (ProgressBar)params[2];
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            String url = (String)params[0];
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync(); // might take long! (for buffering, etc)

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    startTimer(mp.getDuration());
                }
            });

            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.e(LOG_TAG, "Error searching for the track: " + what + ":"+ extra);
                    return false;
                }
            });
        }
        catch (IOException ioException) {
            Log.e(LOG_TAG, ioException.getMessage());
        }
        return null;
    }

    public void cancel(Boolean cancel){
        super.cancel(cancel);
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    /**
     * Starts the player, and at the same time, starts the progress bar
     * @param duration
     */
    private void startTimer(final int duration) {
        progressBar.setMax(duration-TIME_DIFERENTIAL);
        mediaPlayer.start();
        new CountDownTimer(duration, TIME_DIFERENTIAL) {

            public void onTick(long millisUntilFinished) {
                progressBar.setProgress(mediaPlayer.getCurrentPosition());
                if(!mediaPlayer.isPlaying()) {
                    Log.e("MEDIA_PLAYER_STOPPED", mediaPlayer.getCurrentPosition()+"");
                }
            }

            public void onFinish() {
                progressBar.setProgress(duration);
            }
        }.start();
    }
}
