package com.alejandro.spotifystreamer.tasks;

import android.os.AsyncTask;

import com.alejandro.spotifystreamer.activities.MainActivityFragment;
import com.alejandro.spotifystreamer.activities.TopHitsActivityFragment;

import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;

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
        return spotify.searchTracks(params[0]).tracks.items;
    }

    @Override
    protected void onPostExecute(List<Track> tracks) {
        super.onPostExecute(tracks);
        caller.callback(tracks);
    }
}
