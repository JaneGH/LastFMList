package com.itclimb.lastfmlist.views;

import com.itclimb.lastfmlist.data.model.ArtistItem;

import java.util.List;

public interface IArtistFragmentView {
    void setArtistListAdapter(List<ArtistItem> itemTalkList, int totalTalks);
    void addListToAdapter(List<ArtistItem> itemTalkList);
    void showProgressDialog();
    void hideProgressDialog();
    void replaceToArtistFragment(String id);
    void startService();
    void stopService();
    void notifyPictureChanged();
}
