package com.alejandro.spotifystreamer.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
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
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class TopHitsActivityFragment extends Fragment {

    private static final String LOG_TAG = TopHitsActivityFragment.class.getSimpleName();
    TopHitsAdapter topHitsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the message from the intent
        Intent intent = getActivity().getIntent();
        String artistId = intent.getStringExtra(Intent.EXTRA_TEXT);
        SpotifyApi api = new SpotifyApi();
        SpotifyService spotify = api.getService();
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put(SpotifyService.OFFSET, 0);
        options.put(SpotifyService.LIMIT, 10);
        options.put(SpotifyService.COUNTRY, getActivity().getResources().getConfiguration().locale.getCountry());
        spotify.getArtistTopTrack(artistId, options, new Callback<Tracks>() {
            @Override
            public void success(Tracks tracks, Response response) {
                callback(tracks.tracks);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, error.getMessage());
            }
        });
        topHitsAdapter = new TopHitsAdapter(this.getActivity(), new ArrayList<Track>());
        View rootView = inflater.inflate(R.layout.fragment_top_hits, container, false);
        ListView listView = (ListView)rootView.findViewById(R.id.listview_hits);
        listView.setAdapter(topHitsAdapter);
        return rootView;
    }

    public void callback(List<Track> tracks) {
        if(tracks!=null) {
            topHitsAdapter.clear();
            topHitsAdapter.addAll(tracks);
            topHitsAdapter.notifyDataSetChanged();
        }
    }
}
