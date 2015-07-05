package com.alejandro.spotifystreamer.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alejandro.spotifystreamer.tasks.FindArtistTask;
import com.alejandro.spotifystreamer.tasks.FindTracksTask;
import com.example.alejandro.spotifystreamer.R;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

/**
 * A placeholder fragment containing a simple view.
 */
public class TopHitsActivityFragment extends Fragment {

    public TopHitsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the message from the intent
        Intent intent = getActivity().getIntent();

        String artist = intent.getStringExtra(Intent.EXTRA_TEXT);
        FindTracksTask fat = new FindTracksTask(this);
        fat.execute(artist);
        View rootView = inflater.inflate(R.layout.fragment_top_hits, container, false);
        return rootView;
    }

    public void callback(List<Track> tracks) {
    }
}
