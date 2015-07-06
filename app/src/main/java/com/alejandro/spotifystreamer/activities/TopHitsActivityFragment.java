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
import com.alejandro.spotifystreamer.helpers.TrackCallBack;
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

/**
 * A placeholder fragment containing a simple view.
 */
public class TopHitsActivityFragment extends Fragment {

    private static final String LOG_TAG = TopHitsActivityFragment.class.getSimpleName();
    private List<Track> lTracks=null;
    private View rootView=null;
    TopHitsAdapter topHitsAdapter;

    public TopHitsActivityFragment() {
    }

    public View getRootView() {
        return rootView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the message from the intent
        Intent intent = getActivity().getIntent();

        String artistId = intent.getStringExtra(Intent.EXTRA_TEXT);
        getTracks(artistId);
        topHitsAdapter = new TopHitsAdapter(this.getActivity(), new ArrayList<Track>());
        rootView = inflater.inflate(R.layout.fragment_top_hits, container, false);
        ListView listView = (ListView)rootView.findViewById(R.id.listview_hits);
        listView.setAdapter(topHitsAdapter);
        return rootView;
    }

    private void getTracks(String artistId) {
        SpotifyApi api = new SpotifyApi();
        SpotifyService spotify = api.getService();
        Map<String, Object> params = new HashMap<String, Object>();
        Tracks returnTracks = null;
        params.put("country", getActivity().getResources().getConfiguration().locale.getCountry());
        try {
            spotify.getArtistTopTrack(artistId, params, new TrackCallBack(this));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callback(List<Track> tracks) {
        ArrayList<Track> mTracks = new ArrayList<>();
        mTracks.addAll(tracks);
        topHitsAdapter = new TopHitsAdapter(this.getActivity(), mTracks);
        topHitsAdapter.notifyDataSetChanged();
    }

    public void error(RetrofitError error) {
        Log.e(LOG_TAG, error.getMessage());
    }
}
