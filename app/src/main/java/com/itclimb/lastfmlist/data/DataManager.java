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

/**
 * Created by MyComp on 19.04.2017.
 */
@Singleton
//public class DataManager {
//    private final LastFmRemoteService mLastFmRemoteService;
//    private final Context mContext;
//    private final DatabaseHelper mDatabaseHelper;
//
//    @Inject
//    public DataManager(@ApplicationContext Context context, LastFmRemoteService mLastFmRemoteService, DatabaseHelper mDatabaseHelper) {
//        this.mContext = context;
//        this.mLastFmRemoteService = mLastFmRemoteService;
//        this.mDatabaseHelper = mDatabaseHelper;
//    }
//
//    public Observable<ArtistItem> syncArtists() {
//        final long requestStarted = System.currentTimeMillis();
//        return mLastFmRemoteService.getTopArtists()
//                .concatMap(new Func1<List<ArtistItem>, Observable<ArtistItem>>() {
//                    @Override
//                    public Observable<ArtistItem> call(List<ArtistItem> artists) {
//                        String url = LastFmSpiceService.ENDPOINT + "?method=geo.gettopartists&country=spain&api_key="+LastFmSpiceService.API_KEY+"&format=json&limit=30";
//                        return mDatabaseHelper.setArtists(artists);
//                    }
//                });
//    }
//}
public class DataManager {

    private DatabaseHelper mDatabaseHelper;
    private Scheduler mScheduler;

    public DataManager(Context context, Scheduler scheduler) {
        mDatabaseHelper = new DatabaseHelper(context);
        mScheduler = scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        mScheduler = scheduler;
    }

    public Scheduler getScheduler() {
        return mScheduler;
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