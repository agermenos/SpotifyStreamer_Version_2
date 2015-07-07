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
    private static final int VIEW = 1;
    Viewholder holder;

    static class Viewholder {
        Viewholder(TextView tvSong, TextView tvAlbum, ImageView icAlbum) {
            this.tvSong = tvSong;
            this.tvAlbum = tvAlbum;
            this.icAlbum = icAlbum;
        }
        TextView tvSong;
        TextView tvAlbum;
        ImageView icAlbum;
    }

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
            holder = new Viewholder(
                    (TextView) convertView.findViewById(R.id.text_song),
                    (TextView) convertView.findViewById(R.id.text_album),
                    (ImageView) convertView.findViewById(R.id.album_icon)
            );
            convertView.setTag(holder);
        }
        else {
            holder = (Viewholder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        holder.tvSong.setText(track.name);
        holder.tvAlbum.setText(track.album);
        if (track.url!=null) {
            Picasso.with(getContext()).load(track.url).into(holder.icAlbum);
        }
        // Return the completed view to render on screen
        return convertView;
    }
}

