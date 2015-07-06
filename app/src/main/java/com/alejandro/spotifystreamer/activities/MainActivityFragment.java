package com.alejandro.spotifystreamer.activities;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.alejandro.spotifystreamer.adapters.ArtistAdapter;
import com.alejandro.spotifystreamer.adapters.TopHitsAdapter;
import com.alejandro.spotifystreamer.helpers.CustomTextWatcher;
import com.example.alejandro.spotifystreamer.R;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    ArtistAdapter mArtistAdapter;
    TopHitsAdapter mTopHitsAdapter;


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        EditText queryText = (EditText)rootView.findViewById(R.id.query_text);
        queryText.addTextChangedListener(new CustomTextWatcher(this));

        final ArrayList<Artist> mArtists = new ArrayList<>();
        Artist one = new Artist();one.name="Alex";
        Artist two = new Artist();two.name="Fito";
        mArtists.add(one); mArtists.add(two);
        mArtistAdapter = new ArtistAdapter(this.getActivity(), new ArrayList<Artist>());

        final ListView listView = (ListView)rootView.findViewById(R.id.listview_artists);
        listView.setAdapter(mArtistAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), TopHitsActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, mArtists.get(position).id);
                getActivity().startActivity(intent);
            }
        });

        return rootView;
    }

    public void callbackArtists(List<Artist> artists){

        // Create the adapter to convert the array to views
        if (mArtistAdapter!=null) {
            mArtistAdapter.clear();
        }
        else{
            mArtistAdapter = new ArtistAdapter(this.getActivity(), new ArrayList<Artist>());
        }
        if (artists!=null && artists.size()>0) {
            ArrayList<Artist> mArtists = new ArrayList<>();
            mArtists.addAll(artists);
            mArtistAdapter.addAll(mArtists);
            mArtistAdapter.notifyDataSetChanged();
        }
        else {
            Context context = this.getActivity();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, R.string.no_artist, duration);
            toast.show();
        }
    }

    public void callbackTracks(List<Track> tracks) {

    }
}
