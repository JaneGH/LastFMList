package com.itclimb.lastfmlist.presenters;

import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.itclimb.lastfmlist.LastFmListApp;
import com.itclimb.lastfmlist.Utils.FileUtils;
import com.itclimb.lastfmlist.data.DataManager;
import com.itclimb.lastfmlist.data.model.AlbumItem;
import com.itclimb.lastfmlist.data.model.Tags;
import com.itclimb.lastfmlist.network.LastFmRemoteService;
import com.itclimb.lastfmlist.network.LastFmSpiceService;
import com.itclimb.lastfmlist.views.IAlbumFragmentView;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.simple.SimpleTextRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class AlbumFragmentPresenterImpl implements IAlbumFragmentPresenter {

    private IAlbumFragmentView view;
    private DataManager mDataManager;

    private static final String URL_LIST_TALKS_API = LastFmRemoteService.ENDPOINT + "?method=artist.gettopalbums&artist=";
    private String mNameArtist;


    @Inject
    public AlbumFragmentPresenterImpl() {
    }

    @Override
    public void init(IAlbumFragmentView view) {
        this.view = view;
    }

    @Override
    public void onResume(SpiceManager spiceManager, String name) {
        this.mNameArtist = name;
        mDataManager = LastFmListApp.get().getDataManager();
        view.startService();
        String url = URL_LIST_TALKS_API + String.valueOf(name) + "&api_key=" + LastFmRemoteService.API_KEY + "&format=json";
        sendRequest(url, spiceManager);
    }

    @Override
    public void onPause() {
        view.stopService();
    }

    @Override
    public void addListToAdapter(List<AlbumItem> itemAlbumList) {
        view.addListToAdapter(itemAlbumList);
    }

    private void sendRequest(String url, SpiceManager spiceManager) {
        SimpleTextRequest request = new SimpleTextRequest(url);
        spiceManager.execute(request, url, DurationInMillis.ONE_HOUR, new LastFmApiJsonRequestListener());
        view.showProgressDialog();
    }

    private final class LastFmApiJsonRequestListener implements RequestListener<String> {

        private String LARGE_SIZE = "large";

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            view.hideProgressDialog();
        }

        @Override
        public void onRequestSuccess(String result) {
            List<AlbumItem> albums = getListFromJson(result);
            mDataManager.saveAlbums(albums, mNameArtist);
            view.setAlbumListAdapter(mDataManager.getAlbums(mNameArtist), albums.size());
            view.hideProgressDialog();
        }

        private List<AlbumItem> getListFromJson(String result) {
            List<AlbumItem> albumItemList = new ArrayList<>();
            if (result==null) return albumItemList;
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject topAlbums = jsonObject.getJSONObject(Tags.TAG_TOP_ALBUMS);
                JSONArray album = topAlbums.getJSONArray(Tags.TAG_ALBUM);
                for (int i = 0; i < album.length(); i++) {
                    JSONObject jsonItem = album.getJSONObject(i);
                    AlbumItem albumItem = new AlbumItem();
                    albumItem.title = jsonItem.getString(Tags.TAG_NAME);
                    if (albumItem.title.equals(LastFmSpiceService.EMPTY_ALBUM)){
                        continue;
                    }
                    albumItem.playCount = jsonItem.getString(Tags.TAG_PLAY_COUNT);
                    albumItem.artist = mNameArtist;
                    albumItem.imageUrl = FileUtils.getFileName(albumItem.artist + "_" + albumItem.title);
                    JSONArray images = jsonItem.getJSONArray(Tags.TAG_IMAGE);
                    for (int j = 0; j < images.length(); j++) {
                        JSONObject image = images.getJSONObject(j);
                        if (image.getString(Tags.TAG_SIZE).equals(LARGE_SIZE)) {
                            setBitmapImage(image.getString(Tags.TAG_PATH), albumItem);
                        }
                    }
                    albumItemList.add(albumItem);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return albumItemList;
        }

        private void setBitmapImage(final String imageUrl, final AlbumItem albumItem) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap;
                    try {
                        bitmap = Glide.
                                with(LastFmListApp.get()).
                                load(imageUrl).
                                asBitmap().
                                into(-1, -1).
                                get();
                        albumItem.imageUrl = FileUtils.saveFile(LastFmListApp.get(), bitmap, albumItem.imageUrl);
                        view.notifyPictureChanged();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
