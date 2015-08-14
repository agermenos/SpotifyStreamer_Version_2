package com.alejandro.spotifystreamer.activities;

import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
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

import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A placeholder fragment containing a simple view. Being a Dialog Fragment, it can be displayed as a dialog
 */
public class PlayerActivityFragment extends DialogFragment implements PlayerConstants{
    private SeekBar seekBar;
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
    private boolean mBound = false;
    protected MediaService mediaService;

    public PlayerActivityFragment() {
    }

    @Override
    public synchronized View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

        /*
        Setting the Media Service properties
         */

        ServiceConnection mConnection = new ServiceConnection() {

            public void onServiceConnected(ComponentName className,
                                           IBinder binder) {
                MediaService.MyBinder b = (MediaService.MyBinder) binder;
                mediaService = b.getService();

            }

            public void onServiceDisconnected(ComponentName className) {
                mediaService = null;
            }
        };

        /**
        Intent serviceIntent = new Intent(this.getActivity(), MediaService.class);
        Bundle bundle = new Bundle();
        bundle.putInt(COMMAND, ACTION_INITIALIZE);
        bundle.putString(MEDIA_URL, pTrack.previewUrl);
        serviceIntent.putExtra(DATA, bundle);
        mediaService = getActivity().startService(serviceIntent);
        */
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
                userTracking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //mediaPlayer.seekTo(seekBar.getProgress());
                userTracking = false;
                setPlayPause();
            }
        });
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSong>0) {
                    currentSong--;
                }
                else {
                    currentSong=pTracks.size()-1;
                }
                ParcelableTracks pTrack = pTracks.get(currentSong);
                loadPlayerUI(pTrack);
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    isPlaying=true;
                    setPlayPause();
                    if (mediaService!=null){
                        setPlayPause();
                    }
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlaying=false;
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
        if (currentSong==pTracks.size() - 1) {
            currentSong=0;
        }
        else {
            currentSong++;
        }
        ParcelableTracks pTrack = pTracks.get(currentSong);
        loadPlayerUI(pTrack);
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
}



