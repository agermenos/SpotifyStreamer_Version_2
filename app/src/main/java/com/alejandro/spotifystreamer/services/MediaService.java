package com.alejandro.spotifystreamer.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.widget.SeekBar;
import android.widget.TextView;
import com.alejandro.spotifystreamer.helpers.PlayerConstants;

/**
 * Created by Alejandro on 8/9/2015.
 */
public class MediaService extends Service implements MediaPlayer.OnPreparedListener, PlayerConstants, MediaPlayer.OnErrorListener {

    private final IBinder mBinder = new LocalBinder();
    MediaPlayer mMediaPlayer = null;
    private SeekBar seekBar;
    private TextView startText;
    private TextView endText;

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public MediaService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MediaService.this;
        }
    }


    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(ACTION_PLAY)) {
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnErrorListener(this);
            mMediaPlayer.prepareAsync(); // prepare async to not block main thread
        }
        return START_NOT_STICKY;
    }

    public void setmMediaPlayer(MediaPlayer mMediaPlayer) {
        this.mMediaPlayer = mMediaPlayer;
    }

    public void setSeekBar(SeekBar seekBar) {
        this.seekBar = seekBar;
    }

    public void setStartText(TextView startText) {
        this.startText = startText;
    }

    public void setEndText(TextView endText) {
        this.endText = endText;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    public class PlayerBinder extends Binder {
        public MediaService getService() {
            return MediaService.this;
        }
    }

}
