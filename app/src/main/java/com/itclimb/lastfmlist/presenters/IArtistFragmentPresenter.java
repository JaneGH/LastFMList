package com.itclimb.lastfmlist.presenters;

import com.itclimb.lastfmlist.base.BaseFragmentPresenter;
import com.itclimb.lastfmlist.data.model.ArtistItem;
import com.itclimb.lastfmlist.views.IArtistFragmentView;
import com.octo.android.robospice.SpiceManager;

import java.util.List;

public interface IArtistFragmentPresenter extends BaseFragmentPresenter<IArtistFragmentView> {
    void onResume(SpiceManager spiceManager);
    void onPause();
    void onLoadMore();
    void onItemClick(ArtistItem itemTalk);
    void addListToAdapter(List<ArtistItem> artistItemList);
    void loadArtists(String country,  SpiceManager spiceManager);
}
