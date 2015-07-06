package com.alejandro.spotifystreamer.tasks;

import android.os.AsyncTask;

import com.alejandro.spotifystreamer.activities.MainActivityFragment;
import com.alejandro.spotifystreamer.activities.TopHitsActivityFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

/**
 * Created by Alejandro on 7/5/2015.
 */
public class FindTracksTask extends AsyncTask<String, Void, List<Track>> {

    private TopHitsActivityFragment caller = null;


    public FindTracksTask(TopHitsActivityFragment caller){
        super();
        this.caller=caller;
    }

    @Override
    protected List<Track> doInBackground(String... params) {
        SpotifyApi api = new SpotifyApi();
        SpotifyService spotify = api.getService();
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put(SpotifyService.OFFSET, 0);
        options.put(SpotifyService.LIMIT, 10);
        options.put(SpotifyService.COUNTRY, caller.getActivity().getResources().getConfiguration().locale.getISO3Country());
        Tracks tracks = spotify.getArtistTopTrack(params[0], options);
        return tracks.tracks;
    }

    @Override
    protected void onPostExecute(List<Track> tracks) {
        super.onPostExecute(tracks);
        caller.callback(tracks);
    }
}