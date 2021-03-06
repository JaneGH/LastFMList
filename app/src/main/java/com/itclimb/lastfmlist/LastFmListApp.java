package com.itclimb.lastfmlist;

import android.app.Application;

import com.itclimb.lastfmlist.data.DataManager;
import com.itclimb.lastfmlist.injection.components.DaggerLastFmListAppComponent;
import com.itclimb.lastfmlist.injection.components.LastFmListAppComponent;

public class LastFmListApp extends Application {

    protected  LastFmListAppComponent appComponent;

    private DataManager mDataManager;
    private static LastFmListApp mMLastFmListApp;

    public static LastFmListApp get() {
        return mMLastFmListApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mDataManager = new DataManager(this);
        mMLastFmListApp = this;
        buildGraphAndInject();
    }

    public LastFmListAppComponent getAppComponent() {
        return appComponent;
    }

    public LastFmListAppComponent buildGraphAndInject() {
        appComponent = DaggerLastFmListAppComponent.builder()
                .build();
        appComponent.inject(this);
        return appComponent;
    }

    public DataManager getDataManager() { return mDataManager; }

}
