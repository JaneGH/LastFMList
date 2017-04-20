package com.itclimb.lastfmlist.presenters;

import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.itclimb.lastfmlist.LastFmListApp;
import com.itclimb.lastfmlist.Utils.FileUtils;
import com.itclimb.lastfmlist.Utils.NetworkUtils;
import com.itclimb.lastfmlist.data.DataManager;
import com.itclimb.lastfmlist.data.model.ArtistItem;
import com.itclimb.lastfmlist.data.model.Tags;
import com.itclimb.lastfmlist.network.LastFmSpiceService;
import com.itclimb.lastfmlist.views.IArtistFragmentView;
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

public class ArtistFragmentPresenterImpl implements IArtistFragmentPresenter {

    private int offset = 0;

    private IArtistFragmentView view;
    private SpiceManager spiceManager;
    private static final String LARGE_SIZE = "large";
    private DataManager mDataManager;
    private String mCountry;

    @Inject
    public ArtistFragmentPresenterImpl() {
    }

    @Override
    public void init(IArtistFragmentView view) {
        this.view = view;
    }

    @Override
    public void onResume(SpiceManager spiceManager) {
        mDataManager = LastFmListApp.get().getDataManager();
    }

    public void loadArtists(String country, SpiceManager spiceManager) {
        this.mCountry = country;
        if (!NetworkUtils.isNetworkConnected(LastFmListApp.get())) {
            List<ArtistItem> artists = mDataManager.getArtists(mCountry);
            view.setArtistListAdapter(artists, artists.size());
            view.hideProgressDialog();
        } else {
            view.startService();
            this.spiceManager = spiceManager;
            String url = LastFmSpiceService.getURL(country);
            sendRequest(url, spiceManager, false);
        }
    }


    @Override
    public void onPause() {
        view.stopService();
    }

    @Override
    public void onLoadMore() {
//        if (offset <= mTotalTalks -30) {
//            offset+=30;
//            String url = URL_LIST_TALKS_API + "&offset=" + String.valueOf(offset);
//            sendRequest(url, spiceManager, true);
//        }
    }

    @Override
    public void onItemClick(ArtistItem itemTalk) {
        String name = itemTalk.name;
        view.replaceToArtistFragment(name);
    }

    @Override
    public void addListToAdapter(List<ArtistItem> artistItemList) {
        view.addListToAdapter(artistItemList);
    }

    private void sendRequest(String url, SpiceManager spiceManager, boolean isLoadMore) {
        SimpleTextRequest request = new SimpleTextRequest(url);
        if (!isLoadMore) {
            view.showProgressDialog();
        }
        spiceManager.execute(request, "jsonLastFm" + String.valueOf(offset), DurationInMillis.ONE_SECOND, new LastFmApiJsonRequestListener());
    }

    private class LastFmApiJsonRequestListener implements RequestListener<String> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            view.hideProgressDialog();
            List<ArtistItem> artists = mDataManager.getArtists(mCountry);
            view.setArtistListAdapter(artists, artists.size());
        }

        @Override
        public void onRequestSuccess(String result) {
            view.hideProgressDialog();
            List<ArtistItem> artists = getListFromJson(result);
            mDataManager.saveArtists(artists, mCountry);
            view.setArtistListAdapter(mDataManager.getArtists(mCountry), artists.size());
        }

        private List<ArtistItem> getListFromJson(String jsonString) {
            List<ArtistItem> itemTalkList = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONObject artists = jsonObject.getJSONObject(Tags.TAG_TOP_ARTISTS);
                JSONArray artist = artists.getJSONArray(Tags.TAG_ARTIST);
                JSONObject counts = artists.getJSONObject(Tags.TAG_ATTR);

                for (int i = 0; i < artist.length(); i++) {
                    JSONObject jsonItem = artist.getJSONObject(i);
                    String name = jsonItem.getString(Tags.TAG_NAME);
                    String listeners = jsonItem.getString(Tags.TAG_LISTENERS);
                    JSONArray images = jsonItem.getJSONArray(Tags.TAG_IMAGE);
                    ArtistItem itemTalk = new ArtistItem();

                    for (int j = 0; j < images.length(); j++) {
                        JSONObject image = images.getJSONObject(j);
                        if (image.getString(Tags.TAG_SIZE).equals(LARGE_SIZE)) {
                            setBitmapImage(image.getString(Tags.TAG_PATH), itemTalk);
                        }
                    }

                    itemTalk.name = name;
                    itemTalk.country = mCountry;
                    itemTalk.listeners = listeners;
                    itemTalk.imageUrl = FileUtils.getFileName(itemTalk.name);
                    itemTalkList.add(itemTalk);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return itemTalkList;
        }

        private void setBitmapImage(final String imageUrl, final ArtistItem itemTalk) {

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
                        FileUtils.saveFile(LastFmListApp.get(), bitmap, itemTalk.imageUrl);
                        view.notifyPictureChanged();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
