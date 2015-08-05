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
import com.alejandro.spotifystreamer.helpers.CustomTextWatcher;
import com.example.alejandro.spotifystreamer.R;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    ArtistAdapter mArtistAdapter;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(String artistSelected);
    }

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        EditText queryText = (EditText)rootView.findViewById(R.id.query_text);
        queryText.addTextChangedListener(new CustomTextWatcher(this));

        mArtistAdapter = new ArtistAdapter(this.getActivity(), new ArrayList<Artist>());

        final ListView listView = (ListView)rootView.findViewById(R.id.listview_artists);
        listView.setAdapter(mArtistAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((Callback)getActivity()).onItemSelected(mArtistAdapter.getItem(position).id);
            }
        });
        return rootView;
    }

    /**
     * Updates the artist adapter
     * It's public, since an outside class (even out of the package) calls it
     * @param artists
     */
    public void callback(List<Artist> artists){
        // Create the adapter to convert the array to views
        if (mArtistAdapter!=null) {
            mArtistAdapter.clear();
        }
        else{
            mArtistAdapter = new ArtistAdapter(this.getActivity(), new ArrayList<Artist>());
        }
        if (artists!=null) {updateAdapter(artists);}
        else {
            Context context = this.getActivity();
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, R.string.no_artist, duration);
            toast.show();
        }
    }

    private void updateAdapter(List<Artist> artists) {
        if (artists!=null && artists.size()>0) {
            ArrayList<Artist> mArtists = new ArrayList<>();
            mArtists.addAll(artists);
            mArtistAdapter.addAll(mArtists);
            mArtistAdapter.notifyDataSetChanged();
        }
    }


}
