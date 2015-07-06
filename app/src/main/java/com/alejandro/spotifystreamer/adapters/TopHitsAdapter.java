package com.alejandro.spotifystreamer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alejandro.spotifystreamer.helpers.ParcelableTracks;
import com.example.alejandro.spotifystreamer.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by Alejandro on 7/5/2015.
 */
public class TopHitsAdapter extends ArrayAdapter<ParcelableTracks> {

    public TopHitsAdapter(Context context, ArrayList<ParcelableTracks> tracks) {
        super(context, 0, tracks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ParcelableTracks track = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_hits, parent, false);
        }
        // Lookup view for data population
        TextView tvSong = (TextView) convertView.findViewById(R.id.text_song);
        TextView tvAlbum = (TextView) convertView.findViewById(R.id.text_album);
        ImageView icAlbum = (ImageView) convertView.findViewById(R.id.album_icon);
        // Populate the data into the template view using the data object
        tvSong.setText(track.name);
        tvAlbum.setText(track.album);
        if (track!=null) {
            if (track.url!=null) {
                Picasso.with(getContext()).load(track.url).into(icAlbum);
            }
        }
        // Return the completed view to render on screen
        return convertView;
    }
}