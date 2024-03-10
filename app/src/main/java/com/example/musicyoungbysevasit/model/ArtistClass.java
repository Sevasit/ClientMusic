package com.example.musicyoungbysevasit.model;

public class ArtistClass {
    public String artistId;
    public String name;
    public String genre;
    public String url;

    public ArtistClass() {
    }

    public ArtistClass(String artistId, String name, String genre, String url) {
        this.artistId = artistId;
        this.name = name;
        this.genre = genre;
        this.url = url;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
