package com.alejandro.spotifystreamer.tasks;

import android.os.AsyncTask;

import com.alejandro.spotifystreamer.activities.MainActivityFragment;

import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;

/**
 * Created by agermenos on 7/1/15.
 */
public class FindArtistTask extends AsyncTask<String, Void, List<Artist>> {
    private MainActivityFragment caller = null;

    public FindArtistTask(MainActivityFragment fragment){
        super();
        caller = fragment;
    }

    @Override
    protected List<Artist> doInBackground(String... params) {
        SpotifyApi api = new SpotifyApi();
        SpotifyService spotify = api.getService();
        ArtistsPager results = spotify.searchArtists(params[0]);
        return results.artists.items;
    }

    @Override
    protected void onPostExecute(List<Artist> artists) {
        super.onPostExecute(artists);
        caller.callbackArtists(artists);
    }
}
