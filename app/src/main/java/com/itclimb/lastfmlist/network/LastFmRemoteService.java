package com.itclimb.lastfmlist.network;

import com.itclimb.lastfmlist.data.model.ArtistItem;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Max Chervatiuk on 24.03.16.
 * Email: duo.blood@gmail.com
 */
public interface LastFmRemoteService {

    public static final String ENDPOINT = "http://ws.audioscrobbler.com/2.0/";
    public static final String API_KEY = "e81f61890b7ff8633ca024d0faa449e7";

    @GET("store/getTopArtists")
    Observable<List<ArtistItem>> getTopArtists();

    /********
     * Helper class that sets up a new services
     *******/
    class Creator {

        public static LastFmRemoteService newLastFmRemoteService() {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//            loggingInterceptor.setLevel(BuildConfig.DEBUG ?
//                    HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(LastFmRemoteService.ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
            return retrofit.create(LastFmRemoteService.class);
        }
    }
}
