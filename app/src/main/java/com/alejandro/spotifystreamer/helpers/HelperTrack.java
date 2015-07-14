package com.alejandro.spotifystreamer.helpers;

import android.content.Intent;

/**
 * Created by agermenos on 7/14/15.
 */
public class HelperTrack {
    private String id;
    private String artist;
    private String name;
    private String album;
    private String url;
    private String previewUrl;

    public static final String KEY="tracks";
    public static final String ID = "id";
    public static final String NAME="name";
    public static final String URL = "url";
    public static final String PREVIEW_URL = "previewUrl";
    public static final String ALBUM = "album";
    public static final String ARTIST = "artist";

    public HelperTrack(Intent origin){
        this.id = origin.getStringExtra(ID);
        this.name = origin.getStringExtra(NAME);
        this.artist = origin.getStringExtra(ARTIST);
        this.album = origin.getStringExtra(ALBUM);
        this.url = origin.getStringExtra(URL);
        this.previewUrl = origin.getStringExtra(PREVIEW_URL);

    }

    public HelperTrack(String id, String artist, String name, String album, String url, String previewUrl) {
        this.id = id;
        this.artist = artist;
        this.name = name;
        this.album = album;
        this.url = url;
        this.previewUrl = previewUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getArtist() { return artist;}

    public void setArtist(String artist) {this.artist = artist;}
}
