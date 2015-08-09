package com.alejandro.spotifystreamer.activities;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.alejandro.spotifystreamer.adapters.ArtistAdapter;
import com.alejandro.spotifystreamer.helpers.ParcelableArtist;
import com.alejandro.spotifystreamer.tasks.FindArtistTask;
import com.example.alejandro.spotifystreamer.R;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private static final String KEY="artist";
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
        void onItemSelected(String artistSelected);
    }

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        SearchView queryText = (SearchView) rootView.findViewById(R.id.search_artist);
        final MainActivityFragment current = this;
        queryText.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        FindArtistTask fat = new FindArtistTask(current);
                        fat.execute(query);
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        // return onQueryTextSubmit(newText);
                        return true;
                    }
                });
        if (mArtistAdapter==null) {
            mArtistAdapter = new ArtistAdapter(this.getActivity(), new ArrayList<Artist>());
        }
        if (savedInstanceState!=null && savedInstanceState.containsKey(KEY)){
            List<ParcelableArtist> pArtists = savedInstanceState.getParcelableArrayList(KEY);
            updateParcelableAdapter(pArtists);
        }
        final ListView listView = (ListView) rootView.findViewById(R.id.listview_artists);
        listView.setAdapter(mArtistAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((Callback) getActivity())
                        .onItemSelected(mArtistAdapter.getItem(position).id);
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
        if (artists!=null && artists.size()>0) {updateAdapter(artists);}
        else {
            Context context = this.getActivity();
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, R.string.no_artist, duration);
            toast.show();
        }
    }

    private void updateParcelableAdapter(List<ParcelableArtist> parcelableArtists){
        List<Artist> artists =new ArrayList<>();
        for (ParcelableArtist pa:parcelableArtists){
            Artist a = new Artist();
            a.id = pa.getId();
            a.name = pa.getName();
            if (pa.getUrl()!=null){
                List<Image> images=new ArrayList<Image>();
                Image image = new Image();
                image.url = pa.getUrl();
                images.add(image);
            }
            artists.add(a);
        }
        updateAdapter(artists);
    }

    private void updateAdapter(List<Artist> artists) {
        if (artists!=null && artists.size()>0) {
            ArrayList<Artist> mArtists = new ArrayList<>();
            mArtists.addAll(artists);
            mArtistAdapter.addAll(mArtists);
            mArtistAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        List<ParcelableArtist> pArtist = retrieveArtistsFromAdapter(mArtistAdapter);
        outState.putParcelableArrayList(KEY, (ArrayList<? extends Parcelable>) pArtist);
        super.onSaveInstanceState(outState);
    }

    private List<ParcelableArtist> retrieveArtistsFromAdapter(ArtistAdapter artistAdapter) {
        List<ParcelableArtist> returnList = new ArrayList<>();
        for (int k=0; k<artistAdapter.getCount(); k++){
            returnList.add(new ParcelableArtist(artistAdapter.getItem(k)));
        }
        return returnList;
    }


}
