package com.itclimb.lastfmlist.data;

import android.content.Context;

import com.itclimb.lastfmlist.data.model.AlbumItem;
import com.itclimb.lastfmlist.data.model.ArtistItem;
import com.itclimb.lastfmlist.injection.ApplicationContext;
import com.itclimb.lastfmlist.network.LastFmRemoteService;
import com.itclimb.lastfmlist.network.LastFmSpiceService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Scheduler;
import rx.functions.Func1;

@Singleton
public class DataManager {

    private DatabaseHelper mDatabaseHelper;

    @Inject
    public DataManager(@ApplicationContext Context context) {
        mDatabaseHelper = new DatabaseHelper(context);
    }

    public void saveArtists(List<ArtistItem> artists, String country) {
        mDatabaseHelper.setArtists(artists, country);
    }

    public List<ArtistItem> getArtists(String country) {
        return mDatabaseHelper.getArtists(country);
    }

    public void saveAlbums(List<AlbumItem> albums, String mNameArtist) {
        mDatabaseHelper.setAlbums(albums, mNameArtist);
    }

    public List<AlbumItem> getAlbums(String mNameArtist) {
        return mDatabaseHelper.getAlbums(mNameArtist);
    }
}