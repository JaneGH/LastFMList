package com.itclimb.lastfmlist.injection.modules;

import com.itclimb.lastfmlist.presenters.ArtistFragmentPresenterImpl;
import com.itclimb.lastfmlist.presenters.MainActivityPresenterImpl;
import com.itclimb.lastfmlist.views.IMainActivityView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MyComp on 18.04.2017.
 */
@Module
public class MainActivityModule {

    private IMainActivityView view;

    public MainActivityModule(IMainActivityView view) {
        this.view = view;
    }

    @Provides
    public IMainActivityView provideView() {
        return view;
    }

    @Provides
    public MainActivityPresenterImpl provideMainActivityPresenterImpl (IMainActivityView view){
        return  new MainActivityPresenterImpl(view);
    }

    @Provides
    public ArtistFragmentPresenterImpl provideListFragmentPresenterImpl() {
        return new ArtistFragmentPresenterImpl();
    }

}