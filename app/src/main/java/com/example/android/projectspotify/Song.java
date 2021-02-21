package com.example.android.projectspotify;

public class Song {
    private String mSongName;
    private String mSongURL;
    private String mImageURL;
    private String mSongArtist;
    private String mSongDuration;

    public Song(){

    }

    public Song(String songName, String songURL, String imageURL, String songArtist, String songDuration){
        this.mSongName = songName;
        this.mSongURL = songURL;
        this.mImageURL = imageURL;
        this.mSongArtist = songArtist;
        this.mSongDuration = songDuration;
    }

    public String getSongName(){
        return mSongName;
    }

    public void setSongName(String songName){
        this.mSongName = songName;
    }

    public String getSongURL(){
        return mSongURL;
    }

    public void setSongURL(String songURL){
        this.mSongURL = songURL;
    }

    public String getImageURL(){
        return mImageURL;
    }

    public void setImageURL(String imageURL){
        this.mImageURL = imageURL;
    }

    public String getSongArtist(){
        return mSongArtist;
    }

    public void setSongArtist(String songArtist){
        this.mSongArtist = songArtist;
    }

    public String getSongDuration(){
        return mSongDuration;
    }

    public void setSongDuration(String songDuration){
        this.mSongDuration = songDuration;
    }
}
