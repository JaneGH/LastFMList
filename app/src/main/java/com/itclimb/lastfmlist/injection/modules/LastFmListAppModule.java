package com.itclimb.lastfmlist.injection.modules;

import android.app.Application;
import android.content.Context;

import com.itclimb.lastfmlist.LastFmListApp;
import com.itclimb.lastfmlist.network.LastFmRemoteService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MyComp on 18.04.2017.
 */

@Module
public class LastFmListAppModule {

    private final LastFmListApp app;

    public LastFmListAppModule(LastFmListApp app) {
        this.app = app;
    }

    @Provides
    @Singleton
    public Application provideApplication() {
        return app;
    }

    @Provides
    @Singleton
    LastFmRemoteService provideGygService() {
        return LastFmRemoteService.Creator.newLastFmRemoteService();
    }

    @Provides
    @Singleton
    Context provideContext() {
        return app.getApplicationContext();
    }

}
