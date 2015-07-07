package com.alejandro.spotifystreamer.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.alejandro.spotifystreamer.activities.MainActivityFragment;

import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.RetrofitError;

/**
 * Created by agermenos on 7/1/15.
 */
public class FindArtistTask extends AsyncTask<String, Void, List<Artist>> {
    private MainActivityFragment caller = null;
    private static final String LOG_TAG = FindArtistTask.class.getSimpleName();

    public FindArtistTask(MainActivityFragment fragment){
        super();
        this.caller = fragment;
    }

    @Override
    protected List<Artist> doInBackground(String... params) {
        SpotifyApi api = new SpotifyApi();
        SpotifyService spotify = api.getService();
        try {
            ArtistsPager results = spotify.searchArtists(params[0]);
            return results.artists.items;
        }
        catch (RetrofitError error) {
            SpotifyError spotifyError = SpotifyError.fromRetrofitError(error);
            Log.e(LOG_TAG, spotifyError.getMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Artist> artists) {
        super.onPostExecute(artists);
        caller.callback(artists);
    }
}
