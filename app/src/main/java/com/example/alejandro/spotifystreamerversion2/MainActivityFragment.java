package com.example.alejandro.spotifystreamerversion2;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName().toString();
    ArtistAdapter mArtistAdapter;


    public MainActivityFragment() {
    }

    public void callback(List<Artist> artists){

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        EditText queryText = (EditText)rootView.findViewById(R.id.query_text);
        queryText.addTextChangedListener(new CustomTextWatcher(this));

        ArrayList<Artist> mArtists = new ArrayList<>();
        Artist one = new Artist();one.name="Alex";
        Artist two = new Artist();two.name="Fito";
        mArtists.add(one); mArtists.add(two);
        mArtistAdapter = new ArtistAdapter(this.getActivity(), mArtists);

        final ListView listView = (ListView)rootView.findViewById(R.id.listview_artists);
        listView.setAdapter(mArtistAdapter);

        return rootView;
    }

}
