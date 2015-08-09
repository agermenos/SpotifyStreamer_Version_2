package com.alejandro.spotifystreamer.activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
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
import com.example.alejandro.spotifystreamer.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlayerActivityFragment extends DialogFragment {
    private static MediaPlayer mediaPlayer;
    private static SeekBar seekBar;
    private boolean isPlaying=true;
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

    public PlayerActivityFragment() {
    }

    @Override
    public synchronized View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mediaPlayer!=null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.reset();
            }
        }
        mediaPlayer = new MediaPlayer();

        Intent intent = getActivity().getIntent();
        pTracks = intent.getParcelableArrayListExtra("tracks");
        currentSong = intent.getIntExtra("position", 0);
        ParcelableTracks pTrack = pTracks.get(currentSong);

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
        startMediaPlayer(pTrack.previewUrl);

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
                mediaPlayer.seekTo(seekBar.getProgress());
                startSong();
                userTracking=false;
                setPlayPause();
            }
        });
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.reset();
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
                    isPlaying=true;
                    startSong();
                    setPlayPause();
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlaying=false;
                mediaPlayer.pause();
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
        mediaPlayer.reset();
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

    /**
     * Automates hiding/showing play and pause buttons
     */
    private void setPlayPause(){
        if (isPlaying) {
            playButton.setVisibility(ImageButton.GONE);
            pauseButton.setVisibility(ImageButton.VISIBLE);
        }
        else {
            playButton.setVisibility(ImageButton.VISIBLE);
            pauseButton.setVisibility(ImageButton.GONE);
        }
    }

    private void startMediaPlayer(String url){
        isPlaying=true;
        userTracking = false;
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        setPlayPause();
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();



            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    endTime.setText(getTime(mediaPlayer.getDuration()));
                    seekBar.setMax(mediaPlayer.getDuration());
                    startSong();
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
    }

    /**
     * Starts the player, and at the same time, starts the progress bar
     */
    private void startSong() {
        isPlaying = true;
        int duration = mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition();
        new CountDownTimer(duration, TIME_DIFFERENTIAL) {
            public void onTick(long millisUntilFinished) {
                if (mediaPlayer.isPlaying() && !userTracking) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    startTime.setText(getTime(mediaPlayer.getCurrentPosition()));
                }
            }
            public void onFinish() {
                Log.i ("MEDIA_SPLIT", mediaPlayer.getDuration()-seekBar.getProgress()+" millis");
                if (mediaPlayer.getDuration()-seekBar.getProgress()<500) {
                    goNextSong();
                }
            }
        }.start();
        mediaPlayer.start();
    }

}



