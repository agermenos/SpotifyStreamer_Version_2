package com.alejandro.spotifystreamer.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alejandro.spotifystreamer.helpers.HelperTrack;
import com.alejandro.spotifystreamer.helpers.ParcelableTracks;
import com.example.alejandro.spotifystreamer.R;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlayerActivityFragment extends Fragment {

    public PlayerActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        HelperTrack track=new HelperTrack(intent);

        View rootView = inflater.inflate(R.layout.fragment_player, container, false);
        TextView album = (TextView)rootView.findViewById(R.id.text_player_album);
        TextView song = (TextView)rootView.findViewById(R.id.text_player_song);
        TextView artist = (TextView)rootView.findViewById(R.id.text_player_artist);
        ImageView picture = (ImageView)rootView.findViewById(R.id.album_image);
        song.setText(track.getName());
        album.setText(track.getAlbum());
        artist.setText(track.getArtist());
        Picasso.with(getActivity()).load(track.getUrl()).into(picture);
        return rootView;
    }

    private void getPlayer(String stringExtra) {

    }


}


