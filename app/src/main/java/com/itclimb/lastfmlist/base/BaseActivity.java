package com.itclimb.lastfmlist.base;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.itclimb.lastfmlist.LastFmListApp;
import com.itclimb.lastfmlist.injection.components.LastFmListAppComponent;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupComponent(LastFmListApp.get().getAppComponent());
    }

    protected abstract void setupComponent(LastFmListAppComponent appComponent);

}
