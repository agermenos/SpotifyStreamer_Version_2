package com.alejandro.spotifystreamer.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alejandro.spotifystreamer.adapters.TopHitsAdapter;
import com.alejandro.spotifystreamer.helpers.ParcelableTracks;
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
    private static final String KEY="tracks";
    private static final String LOG_TAG = TopHitsActivityFragment.class.getSimpleName();
    TopHitsAdapter topHitsAdapter;
    List<ParcelableTracks> pTracks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the message from the intent
        Intent intent = getActivity().getIntent();
        if( savedInstanceState == null){
            getTopHits(intent.getStringExtra(Intent.EXTRA_TEXT));
        }
        topHitsAdapter = new TopHitsAdapter(this.getActivity(), new ArrayList<ParcelableTracks>());
        View rootView = inflater.inflate(R.layout.fragment_top_hits, container, false);
        ListView listView = (ListView)rootView.findViewById(R.id.listview_hits);
        listView.setAdapter(topHitsAdapter);
        if (savedInstanceState!=null && savedInstanceState.containsKey(KEY)){
            pTracks = savedInstanceState.getParcelableArrayList(KEY);
            updateAdapter(pTracks);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (pTracks==null) {
                    pTracks = new ArrayList<>();
                    for (int k=0; k<topHitsAdapter.getCount(); k++) {
                        pTracks.add(topHitsAdapter.getItem(k));
                    }
                }
                Intent intent = new Intent(getActivity(), PlayerActivity.class)
                        .putExtra("position", position)
                        .putParcelableArrayListExtra("tracks", new ArrayList<Parcelable>(pTracks));
                getActivity().startActivity(intent);
            }
        });
        return rootView;
    }

    /**
     * Retrieves top tracks through spotify api.
     * It calls a local callback method, which updates the adapter.
     * @param artistId
     */
    private void getTopHits(String artistId){
        SpotifyApi api = new SpotifyApi();
        SpotifyService spotify = api.getService();
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put(SpotifyService.OFFSET, 0);
        options.put(SpotifyService.LIMIT, 10);
        options.put(SpotifyService.COUNTRY, getActivity().getResources().getConfiguration().locale.getCountry());
        spotify.getArtistTopTrack(artistId, options, new Callback<Tracks>() {
            @Override
            public void success(Tracks tracks, Response response) {
                callback(convertToParcelableTracks(tracks.tracks));
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, error.getMessage());
            }
        });
    }

    private List<ParcelableTracks> convertToParcelableTracks(List<Track> tracks) {
        List<ParcelableTracks> pTracks = new ArrayList<>();
        for (Track t: tracks){
            pTracks.add(new ParcelableTracks(t));
        }
        return pTracks;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        List<ParcelableTracks> pTracks = retrieveTracksFromAdapter(topHitsAdapter);
        outState.putParcelableArrayList(KEY, (ArrayList<? extends Parcelable>) pTracks);
        super.onSaveInstanceState(outState);
    }

    private List<ParcelableTracks> retrieveTracksFromAdapter(TopHitsAdapter topHitsAdapter) {
        List<ParcelableTracks> returnList = new ArrayList<>();
        for (int k=0; k<topHitsAdapter.getCount(); k++){
           returnList.add(topHitsAdapter.getItem(k));
        }
        return returnList;
    }

    /**
     * The callback updates the adapter.
     * It's private, since no one outside the class should call it.
     * @param tracks
     */
    private void callback(List<ParcelableTracks> tracks) {
        if(tracks!=null && tracks.size()>0) {
            updateAdapter(tracks);
        }
        else {
            Context context = this.getActivity();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, R.string.no_hits, duration);
            toast.show();
        }
    }

    /**
     * Cleans and updates the Adapter with the new list of tracks
     * @param tracks
     */
    private void updateAdapter(List<ParcelableTracks> tracks){
        topHitsAdapter.clear();
        topHitsAdapter.addAll(tracks);
        topHitsAdapter.notifyDataSetChanged();
    }
}
