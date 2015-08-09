package com.alejandro.spotifystreamer.helpers;

import android.os.Parcel;
import android.os.Parcelable;
import kaaes.spotify.webapi.android.models.Artist;

/**
 * Created by Alejandro on 8/8/2015.
 */
public class ParcelableArtist implements Parcelable{
    public String id;
    public String url;
    public String name;

    public ParcelableArtist(Artist artist){
        this.id = artist.id;
        this.name = artist.name;
        if (artist.images!=null && artist.images.size()>0) {
            this.url = artist.images.get(0).url;
        }
    }

    public ParcelableArtist(Parcel in){
        this.id = in.readString();
        this.url=in.readString();
        this.name=in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
          dest.writeString(id);
          dest.writeString(url);
          dest.writeString(name);
    }
}
