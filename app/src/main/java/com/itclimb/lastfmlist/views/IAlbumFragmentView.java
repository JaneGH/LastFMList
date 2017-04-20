package com.itclimb.lastfmlist.views;

import com.itclimb.lastfmlist.data.model.AlbumItem;

import java.util.List;

public interface IAlbumFragmentView {
    void addListToAdapter(List<AlbumItem> itemAlbumList);
    void showProgressDialog();
    void hideProgressDialog();
    void startService();
    void stopService();
    void setAlbumListAdapter(List<AlbumItem> albums, int totalTalks);
    void notifyPictureChanged();
}
