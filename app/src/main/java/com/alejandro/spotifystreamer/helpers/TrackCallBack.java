package com.alejandro.spotifystreamer.helpers;

import android.util.Log;

import com.alejandro.spotifystreamer.activities.TopHitsActivityFragment;

import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Alejandro on 7/5/2015.
 */
public class TrackCallBack implements Callback<Tracks> {
    private TopHitsActivityFragment caller = null;
    public TrackCallBack (TopHitsActivityFragment caller) {
        this.caller = caller;
    }
    @Override
    public void success(Tracks tracks, Response response) {
        caller.callback(tracks.tracks);
    }

    @Override
    public void failure(RetrofitError error) {
        caller.error(error);
    }
}
