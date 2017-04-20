package com.itclimb.lastfmlist;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.itclimb.lastfmlist.base.BaseActivity;
import com.itclimb.lastfmlist.injection.IHasComponent;
import com.itclimb.lastfmlist.injection.components.DaggerMainActivityComponent;
import com.itclimb.lastfmlist.injection.components.LastFmListAppComponent;
import com.itclimb.lastfmlist.injection.components.MainActivityComponent;
import com.itclimb.lastfmlist.injection.modules.MainActivityModule;
import com.itclimb.lastfmlist.presenters.MainActivityPresenterImpl;
import com.itclimb.lastfmlist.views.IMainActivityView;
import com.itclimb.lastfmlist.views.ArtistFragment;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements IMainActivityView, IHasComponent<MainActivityComponent> {

    @Inject
    MainActivityPresenterImpl presenter;

    private MainActivityComponent mMainActivityComponent;
    private android.support.v4.app.FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentManager = getSupportFragmentManager();
        ArtistFragment artistFragment = (ArtistFragment) mFragmentManager.findFragmentByTag("ArtistFragment");
        if (artistFragment == null) {
            artistFragment = new ArtistFragment();
        }
        if (savedInstanceState == null) {
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, artistFragment)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (mFragmentManager.getBackStackEntryCount() > 0) {
            presenter.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void setupComponent(LastFmListAppComponent appComponent) {
        if (mMainActivityComponent == null) {
            mMainActivityComponent = DaggerMainActivityComponent.builder()
                    .lastFmListAppComponent(LastFmListApp.get().getAppComponent())
                    .mainActivityModule(new MainActivityModule(this))
                    .build();
            mMainActivityComponent.inject(this);
        }
    }

    @Override
    public MainActivityComponent getComponent() {
        return mMainActivityComponent;
    }

    @Override
    public void popFragmentFromStack() {
        mFragmentManager.popBackStack();
    }
}
