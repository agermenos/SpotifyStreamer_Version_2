package com.alejandro.spotifystreamer.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alejandro.spotifystreamer.helpers.HelperTrack;
import com.alejandro.spotifystreamer.tasks.TryMusicTask;
import com.example.alejandro.spotifystreamer.R;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlayerActivityFragment extends Fragment {
    private MediaPlayer mediaPlayer;
    private ProgressBar progressBar;
    private boolean isPlaying=true;
    private int playbackPosition;
    private final static String LOG_TAG=PlayerActivityFragment.class.getSimpleName();

    public PlayerActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mediaPlayer==null) {
            mediaPlayer = new MediaPlayer();
        }
        else {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        Intent intent = getActivity().getIntent();
        HelperTrack track=new HelperTrack(intent);
        TryMusicTask tryMusicTask = new TryMusicTask();

        // Finding stuff on the layout
        View rootView = inflater.inflate(R.layout.fragment_player, container, false);
        TextView album = (TextView)rootView.findViewById(R.id.text_player_album);
        TextView song = (TextView)rootView.findViewById(R.id.text_player_song);
        TextView artist = (TextView)rootView.findViewById(R.id.text_player_artist);
        ImageView picture = (ImageView)rootView.findViewById(R.id.album_image);
        ImageButton prevButton = (ImageButton)rootView.findViewById(R.id.rewind_button);
        final ImageButton playButton = (ImageButton)rootView.findViewById(R.id.play_button);
        final ImageButton pauseButton = (ImageButton)rootView.findViewById(R.id.pause_button);
        ImageButton forwardButton = (ImageButton)rootView.findViewById(R.id.forward_button);
        progressBar = (ProgressBar)rootView.findViewById(R.id.progressBar);
        tryMusicTask.execute(track.getPreviewUrl(), mediaPlayer, progressBar);

        // Attaching values
        song.setText(track.getName());
        album.setText(track.getAlbum());
        artist.setText(track.getArtist());
        Picasso.with(getActivity()).load(track.getUrl()).into(picture);

        /**
         *          Setting behavior
         */

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.seekTo(playbackPosition);
                mediaPlayer.start();
                isPlaying=true;
                setPlayPause(playButton, pauseButton);
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playbackPosition = mediaPlayer.getCurrentPosition();
                mediaPlayer.pause();
                isPlaying=false;
                setPlayPause(playButton, pauseButton);
            }
        });

        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.seekTo(mediaPlayer.getDuration());
            }
        });

        setPlayPause(playButton, pauseButton);
        return rootView;
    }

    private void setPlayPause(View playButton, View pauseButton){
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



