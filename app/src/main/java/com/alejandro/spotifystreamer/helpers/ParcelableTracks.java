package com.alejandro.spotifystreamer.helpers;

import android.os.Parcel;
import android.os.Parcelable;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by agermenos on 7/6/15.
 */
public class ParcelableTracks implements Parcelable {

    public String id;
    public String artist;
    public String name;
    public String album;
    public String url;
    public String previewUrl;

    public ParcelableTracks(Track track) {
        this.id = track.id;
        this.artist = track.artists.get(0).name;
        this.name = track.name;
        this.album = track.album.name;
        if (track.album.images!=null && track.album.images.size()>0){
            this.url = track.album.images.get(0).url;
        }
        this.previewUrl = track.preview_url;
    }

    private ParcelableTracks(Parcel in) {
        id = in.readString();
        artist = in.readString();
        name = in.readString();
        album = in.readString();
        url = in.readString();
        previewUrl = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return id + ": " + name;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(id);
        out.writeString(artist);
        out.writeString(name);
        out.writeString(album);
        out.writeString(url);
        out.writeString(previewUrl);
    }

    public static final Parcelable.Creator<ParcelableTracks> CREATOR = new Parcelable.Creator<ParcelableTracks>() {
        public ParcelableTracks createFromParcel(Parcel in) {
            return new ParcelableTracks(in);
        }

        public ParcelableTracks[] newArray(int size) {
            return new ParcelableTracks[size];
        }
    };
}
