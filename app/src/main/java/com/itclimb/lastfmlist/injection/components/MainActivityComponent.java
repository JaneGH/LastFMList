package com.itclimb.lastfmlist.injection.components;

import com.itclimb.lastfmlist.MainActivity;
import com.itclimb.lastfmlist.injection.ActivityScope;
import com.itclimb.lastfmlist.injection.modules.MainActivityModule;
import com.itclimb.lastfmlist.views.AlbumFragment;
import com.itclimb.lastfmlist.views.ArtistFragment;

import dagger.Component;

/**
 * Created by MyComp on 18.04.2017.
 */

@ActivityScope
@Component(
        dependencies = LastFmListAppComponent.class,
        modules = MainActivityModule.class
)
public interface MainActivityComponent {
    void inject(MainActivity activity);
    void inject(AlbumFragment albumFragment);
    void inject(ArtistFragment artistFragment);
}