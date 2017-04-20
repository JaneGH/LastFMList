package com.itclimb.lastfmlist.network;

import android.app.Application;

import com.octo.android.robospice.SpiceService;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.string.InFileStringObjectPersister;

public class LastFmSpiceService extends SpiceService {

    public static final String ENDPOINT = "http://ws.audioscrobbler.com/2.0/";
    public static final String API_KEY = "e81f61890b7ff8633ca024d0faa449e7";

    public static String getURL(String country) {
        return ENDPOINT + "?method=geo.gettopartists&country="+country+"&api_key="+API_KEY+"&format=json";
    }

    @Override
    public CacheManager createCacheManager(Application application) throws CacheCreationException {
        CacheManager cacheManager = new CacheManager();
        InFileStringObjectPersister inFileStringObjectPersister = new InFileStringObjectPersister(application);
        cacheManager.addPersister(inFileStringObjectPersister);
        return cacheManager;
    }

    @Override
    public int getThreadCount() {
        return 3;
    }
}
