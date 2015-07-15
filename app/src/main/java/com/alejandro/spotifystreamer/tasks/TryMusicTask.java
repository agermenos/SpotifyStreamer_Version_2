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
    private static final int DEFAULT_TIME=30000;
    private static final String LOG_TAG = TryMusicTask.class.getSimpleName();
    private MediaPlayer mediaPlayer;
    private ProgressBar progressBar;

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
        //runLola();
        return null;
    }

    //@Override
    //protected void onPostExecute(Void empty) {
    private void startTimer(final int duration) {
        progressBar.setMax(duration-100);
        mediaPlayer.start();
        new CountDownTimer(duration, 100) {

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
