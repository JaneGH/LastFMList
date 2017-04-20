package com.itclimb.lastfmlist.injection.components;

import com.itclimb.lastfmlist.LastFmListApp;
import com.itclimb.lastfmlist.injection.modules.LastFmListAppModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                LastFmListAppModule.class
        }
)
public interface LastFmListAppComponent {
    void inject(LastFmListApp app);
}
