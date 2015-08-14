package com.alejandro.spotifystreamer.activities;

import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alejandro.spotifystreamer.helpers.ParcelableTracks;
import com.alejandro.spotifystreamer.services.MediaService;
import com.example.alejandro.spotifystreamer.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlayerActivityFragment extends DialogFragment {
    private static final boolean FROM_SCRATCH = false;
    private static final boolean FROM_PREVIOUS = true;
    private static SeekBar seekBar;
    private final static String LOG_TAG=PlayerActivityFragment.class.getSimpleName();
    private static final int TIME_DIFFERENTIAL=50;
    private ImageButton playButton;
    private ImageButton pauseButton;
    private List<ParcelableTracks> pTracks;
    private TextView album;
    private TextView song;
    private TextView artist;
    private TextView startTime;
    private TextView endTime;
    private ImageView picture;
    private View rootView;
    private static int currentSong;
    private static boolean userTracking;
    private MediaService mediaService;
    ServiceConnection mConnection;

    public PlayerActivityFragment() {
    }

    @Override
    public synchronized View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mediaService!=null && mediaService.mediaPlayer!=null) {
            if (mediaService.mediaPlayer.isPlaying()) {
                mediaService.mediaPlayer.stop();
                mediaService.mediaPlayer.reset();
            }

            mediaService.resetMediaPlayer();
        }

        Intent intent = getActivity().getIntent();
        pTracks = intent.getParcelableArrayListExtra("tracks");
        currentSong = intent.getIntExtra("position", 0);
        final ParcelableTracks pTrack = pTracks.get(currentSong);

        // Finding stuff on the layout
        rootView = inflater.inflate(R.layout.fragment_player, container, false);
        album = (TextView)rootView.findViewById(R.id.text_player_album);
        song = (TextView)rootView.findViewById(R.id.text_player_song);
        artist = (TextView)rootView.findViewById(R.id.text_player_artist);
        picture = (ImageView)rootView.findViewById(R.id.album_image);
        ImageButton prevButton = (ImageButton)rootView.findViewById(R.id.rewind_button);
        playButton = (ImageButton)rootView.findViewById(R.id.play_button);
        pauseButton = (ImageButton)rootView.findViewById(R.id.pause_button);
        startTime = (TextView) rootView.findViewById(R.id.text_player_start_time);
        endTime = (TextView) rootView.findViewById(R.id.text_player_end_time);
        endTime.setText(getTime(0));
        startTime.setText(getTime(0));
        ImageButton forwardButton = (ImageButton)rootView.findViewById(R.id.forward_button);
        seekBar = (SeekBar)rootView.findViewById(R.id.seekBar);

        if (mediaService==null) {

            mConnection = new ServiceConnection() {
                public void onServiceConnected(ComponentName className,
                                               IBinder binder) {
                    MediaService.MyBinder b = (MediaService.MyBinder) binder;
                    mediaService = b.getService();
                    startMediaPlayer(pTrack.previewUrl);
                }

                public void onServiceDisconnected(ComponentName className) {
                    mediaService=null;
                }
            };

            Intent musicIntent = new Intent(this.getActivity(), MediaService.class);
            ComponentName myService = getActivity().startService(new Intent(this.getActivity(), MediaService.class));
            getActivity().bindService(musicIntent, mConnection, Context.BIND_AUTO_CREATE);
        }

        loadPlayerUI(pTrack);

        /**
         *          Setting behavior
         */
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                startTime.setText(getTime(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                userTracking=true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaService.mediaPlayer.seekTo(seekBar.getProgress());
                startSong(FROM_PREVIOUS);
                userTracking=false;
                setPlayPause();
            }
        });
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaService.mediaPlayer.reset();
                if (currentSong>0) {
                    currentSong--;
                }
                else {
                    currentSong=pTracks.size()-1;
                }
                ParcelableTracks pTrack = pTracks.get(currentSong);
                loadPlayerUI(pTrack);
                startMediaPlayer(pTrack.previewUrl);
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startSong(FROM_PREVIOUS);
                    setPlayPause();
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaService.mediaPlayer.pause();
                setPlayPause();
            }
        });

        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goNextSong();
            }
        });

        return rootView;
    }

    private String getTime(int milliseconds) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds))
        );
    }

    private void goNextSong() {
        mediaService.mediaPlayer.reset();
        if (currentSong==pTracks.size() - 1) {
            currentSong=0;
        }
        else {
            currentSong++;
        }
        ParcelableTracks pTrack = pTracks.get(currentSong);
        loadPlayerUI(pTrack);
        startMediaPlayer(pTrack.previewUrl);
    }

    private void loadPlayerUI(ParcelableTracks pTrack) {
        song.setText(pTrack.name);
        album.setText(pTrack.album);
        artist.setText(pTrack.artist);
        Picasso.with(getActivity()).load(pTrack.url).into(picture);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mediaService != null) {
            getActivity().unbindService(mConnection);
        }


    }

    /**
     * Automates hiding/showing play and pause buttons
     */
    private void setPlayPause(){
        if (mediaService.mediaPlayer.isPlaying()) {
            playButton.setVisibility(ImageButton.GONE);
            pauseButton.setVisibility(ImageButton.VISIBLE);
        }
        else {
            playButton.setVisibility(ImageButton.VISIBLE);
            pauseButton.setVisibility(ImageButton.GONE);
        }
    }

    private void startMediaPlayer(String url){
        userTracking = false;
        mediaService.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        setPlayPause();
        try {
            if (!mediaService.mediaPlayer.isPlaying()) {
                mediaService.resetMediaPlayer();
                mediaService.mediaPlayer.setDataSource(url);
                mediaService.mediaPlayer.prepareAsync();
                mediaService.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        endTime.setText(getTime(mediaService.mediaPlayer.getDuration()));
                        seekBar.setMax(mediaService.mediaPlayer.getDuration());
                        startSong(FROM_SCRATCH);
                    }
                });

                mediaService.mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        Log.e(LOG_TAG, "Error searching for the track: " + what + ":" + extra);
                        return false;
                    }
                });
            }
            else {
                setSongStatus();
                startSong(FROM_PREVIOUS);
            }
        }
        catch (IOException ioException) {
            Log.e(LOG_TAG, ioException.getMessage());
        }
    }

    private void setSongStatus() {
        endTime.setText(getTime(mediaService.mediaPlayer.getDuration()));
        seekBar.setMax(mediaService.mediaPlayer.getDuration());
    }

    /**
     * Starts the player, and at the same time, starts the progress bar
     */
    private void startSong(boolean restart) {
        int duration = mediaService.mediaPlayer.getDuration() - mediaService.mediaPlayer.getCurrentPosition();
        new CountDownTimer(duration, TIME_DIFFERENTIAL) {
            public void onTick(long millisUntilFinished) {
                if (mediaService.mediaPlayer.isPlaying() && !userTracking) {
                    seekBar.setProgress(mediaService.mediaPlayer.getCurrentPosition());
                    startTime.setText(getTime(mediaService.mediaPlayer.getCurrentPosition()));
                }
            }
            public void onFinish() {
                Log.i ("MEDIA_SPLIT", mediaService.mediaPlayer.getDuration()-seekBar.getProgress()+" millis");
                if (mediaService.mediaPlayer.getDuration()-seekBar.getProgress()<500) {
                    goNextSong();
                }
            }
        }.start();

        mediaService.mediaPlayer.start();
        setPlayPause();
    }

}



