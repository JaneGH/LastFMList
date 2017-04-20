package com.itclimb.lastfmlist.presenters;

import com.itclimb.lastfmlist.base.BaseFragmentPresenter;
import com.itclimb.lastfmlist.data.model.AlbumItem;
import com.itclimb.lastfmlist.views.IAlbumFragmentView;
import com.octo.android.robospice.SpiceManager;

import java.util.List;

public interface IAlbumFragmentPresenter extends BaseFragmentPresenter<IAlbumFragmentView> {
    void onResume(SpiceManager spiceManager, String name);
    void onPause();
    void addListToAdapter(List<AlbumItem> itemAlbumList);
}
