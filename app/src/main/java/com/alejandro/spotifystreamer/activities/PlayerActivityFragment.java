package com.alejandro.spotifystreamer.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.alejandro.spotifystreamer.helpers.ParcelableTracks;
import com.alejandro.spotifystreamer.tasks.TryMusicTask;
import com.example.alejandro.spotifystreamer.R;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlayerActivityFragment extends Fragment {
    private MediaPlayer mediaPlayer;
    private boolean isPlaying=true;
    private int playbackPosition;

    public PlayerActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mediaPlayer = new MediaPlayer();
        Intent intent = getActivity().getIntent();
        HelperTrack track=new HelperTrack(intent);
        TryMusicTask tryMusicTask = new TryMusicTask();
        tryMusicTask.execute(track.getPreviewUrl(), mediaPlayer);
        // Finding stuff on the layout
        View rootView = inflater.inflate(R.layout.fragment_player, container, false);
        TextView album = (TextView)rootView.findViewById(R.id.text_player_album);
        TextView song = (TextView)rootView.findViewById(R.id.text_player_song);
        TextView artist = (TextView)rootView.findViewById(R.id.text_player_artist);
        ImageView picture = (ImageView)rootView.findViewById(R.id.album_image);
        ImageButton prevButton = (ImageButton)rootView.findViewById(R.id.rewind_button);
        ImageButton playButton = (ImageButton)rootView.findViewById(R.id.play_button);
        ImageButton pauseButton = (ImageButton)rootView.findViewById(R.id.pause_button);
        ImageButton forwardButton = (ImageButton)rootView.findViewById(R.id.forward_button);
        ProgressBar progressBar = (ProgressBar)rootView.findViewById(R.id.progressBar);
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
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playbackPosition = mediaPlayer.getCurrentPosition();
                mediaPlayer.pause();
            }
        });

        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.seekTo(mediaPlayer.getDuration());
                mediaPlayer.start();
            }
        });

        if (isPlaying){
            playButton.setVisibility(ImageButton.GONE);
            pauseButton.setVisibility(ImageButton.VISIBLE);
        }
        else {
            playButton.setVisibility(ImageButton.VISIBLE);
            pauseButton.setVisibility(ImageButton.GONE);
        }

        return rootView;
    }

    private void getPlayer(String stringExtra) {

    }


}


