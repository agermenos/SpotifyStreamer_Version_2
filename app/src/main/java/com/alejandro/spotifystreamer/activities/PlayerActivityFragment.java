package com.alejandro.spotifystreamer.activities;

import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alejandro.spotifystreamer.helpers.ParcelableTracks;
import com.alejandro.spotifystreamer.helpers.PlayerConstants;
import com.alejandro.spotifystreamer.services.MediaService;
import com.example.alejandro.spotifystreamer.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A placeholder fragment containing a simple view. Being a Dialog Fragment, it can be displayed as a dialog
 */
public class PlayerActivityFragment extends DialogFragment implements PlayerConstants{
    private SeekBar seekBar;
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
    private boolean mBound = false;
    protected MediaService mediaService;
    private int clocksTicking;
    private ParcelableTracks pTrack;

    public PlayerActivityFragment() {
    }

    @Override
    public synchronized View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();

        pTracks = intent.getParcelableArrayListExtra("tracks");
        currentSong = intent.getIntExtra("position", 0);
        pTrack = pTracks.get(currentSong);

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

            ServiceConnection mConnection = new ServiceConnection() {

                public void onServiceConnected(ComponentName className,
                                               IBinder binder) {
                    MediaService.MyBinder b = (MediaService.MyBinder) binder;
                    mediaService = b.getService();
                    mBound = true;
                    loadPlayerUI();
                    if (mediaService.isPlaying()) {
                        startSong();
                    }
                }

                public void onServiceDisconnected(ComponentName className) {
                    mediaService = null;
                    mBound = false;
                }
            };

            Intent musicIntent = new Intent(this.getActivity(), MediaService.class);
            getActivity().bindService(musicIntent, mConnection, Context.BIND_AUTO_CREATE);
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                startTime.setText(getTime(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                userTracking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaService.playFromPosition(seekBar.getProgress());
                userTracking = false;
                setPlayPause();
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPreviousSong();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    setPlayPause();
                    if (mediaService!=null){
                        startSong();
                    }
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaService!=null){
                    mediaService.pauseMediaPlayer();
                }
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

    private void goPreviousSong(){
        if (currentSong>0) {
            currentSong--;
        }
        else {
            currentSong=pTracks.size()-1;
        }
        pTrack = pTracks.get(currentSong);
        loadPlayerUI();
        startSong();
    }

    private void goNextSong() {
        if (currentSong==pTracks.size() - 1) {
            currentSong=0;
        }
        else {
            currentSong++;
        }
        pTrack = pTracks.get(currentSong);
        loadPlayerUI();
        startSong();
    }

    private void loadPlayerUI() {
        song.setText(pTrack.name);
        album.setText(pTrack.album);
        artist.setText(pTrack.artist);
        Picasso.with(getActivity()).load(pTrack.url).into(picture);
        if (mediaService!=null) {
            /*
            if (mediaService.isPlaying()) {
                mediaService.resetMediaPlayer();
            }
             */
            mediaService.startMediaPlayer(pTrack.previewUrl);
            endTime.setText(getTime(mediaService.getDuration()));
            setPlayPause();
        }

    }

    /**
     * Automates hiding/showing play and pause buttons
     */
    private void setPlayPause(){
        if (mediaService.isPlaying()) {
            playButton.setVisibility(ImageButton.GONE);
            pauseButton.setVisibility(ImageButton.VISIBLE);
        }
        else {
            playButton.setVisibility(ImageButton.VISIBLE);
            pauseButton.setVisibility(ImageButton.GONE);
        }
    }

    private String getTime(int milliseconds) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds))
        );
    }

    /**
     * Starts the player, and at the same time, starts the progress bar
     */
    private void startSong() {
        int duration = mediaService.getDuration() - mediaService.getCurrentPosition();
        seekBar.setMax(duration);
        clocksTicking++;
        mediaService.playFromPosition(mediaService.getCurrentPosition());
        new CountDownTimer(duration, TIME_DIFFERENTIAL) {
            public void onTick(long millisUntilFinished) {
                if (mediaService!=null && !userTracking) {
                    seekBar.setProgress(mediaService.getCurrentPosition());
                    startTime.setText(getTime(mediaService.getCurrentPosition()));
                }
            }
            public void onFinish() {
                clocksTicking--;
                if (clocksTicking==0) {
                    goNextSong();
                }
            }
        }.start();

    }
}



