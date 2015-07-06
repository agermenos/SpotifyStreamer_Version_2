package com.alejandro.spotifystreamer.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alejandro.spotifystreamer.adapters.TopHitsAdapter;
import com.example.alejandro.spotifystreamer.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

/**
 * A placeholder fragment containing a simple view.
 */
public class TopHitsActivityFragment extends Fragment {

    private static final String LOG_TAG = TopHitsActivityFragment.class.getSimpleName();
    private List<Track> lTracks=null;
    TopHitsAdapter topHitsAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the message from the intent
        Intent intent = getActivity().getIntent();

        String artistId = intent.getStringExtra(Intent.EXTRA_TEXT);
        List<Track> lTracks = getTracks(artistId);
        ArrayList<Track> mTracks = new ArrayList<>();
        mTracks.addAll(lTracks);
        topHitsAdapter = new TopHitsAdapter(this.getActivity(), mTracks);
        View rootView = inflater.inflate(R.layout.fragment_top_hits, container, false);
        ListView listView = (ListView)rootView.findViewById(R.id.listview_hits);
        listView.setAdapter(topHitsAdapter);
        return rootView;
    }

    private List<Track> getTracks(String artistId) {
        SpotifyApi api = new SpotifyApi();
        SpotifyService spotify = api.getService();
        Tracks returnTracks = null;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put(SpotifyService.OFFSET, 0);
        options.put(SpotifyService.LIMIT, 10);
        options.put(SpotifyService.COUNTRY, getActivity().getResources().getConfiguration().locale.getISO3Country());
        Tracks tracks = spotify.getArtistTopTrack(artistId, options);
        return tracks.tracks;
    }

}
