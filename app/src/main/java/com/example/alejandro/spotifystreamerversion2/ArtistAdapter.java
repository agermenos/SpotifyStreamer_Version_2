package com.example.alejandro.spotifystreamerversion2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Artist;

public class ArtistAdapter extends ArrayAdapter<Artist> {

    public ArtistAdapter(Context context, ArrayList<Artist> artists) {
        super(context, 0, artists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Artist artist = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_artist, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.artist_name);
        ImageView icon = (ImageView) convertView.findViewById(R.id.artist_icon);
        // Populate the data into the template view using the data object
        tvName.setText(artist.name);
        if (artist!=null) {
            if (artist.images!=null && !artist.images.isEmpty()) {
                String url = artist.images.get(0).url;
                Picasso.with(getContext()).load(url).into(icon);
            }
        }
        // Return the completed view to render on screen
        return convertView;
    }
}
