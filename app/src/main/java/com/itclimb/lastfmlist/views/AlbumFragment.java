/*
 * Copyright (2015) Alexey Mitutov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.itclimb.lastfmlist.views;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.itclimb.lastfmlist.R;
import com.itclimb.lastfmlist.adapters.AlbumListAdapter;
import com.itclimb.lastfmlist.base.BaseFragment;
import com.itclimb.lastfmlist.data.model.AlbumItem;
import com.itclimb.lastfmlist.injection.components.MainActivityComponent;
import com.itclimb.lastfmlist.network.LastFmSpiceService;
import com.itclimb.lastfmlist.presenters.AlbumFragmentPresenterImpl;
import com.octo.android.robospice.SpiceManager;

import java.util.List;

import javax.inject.Inject;

public class AlbumFragment extends BaseFragment implements IAlbumFragmentView {

    @Inject
    AlbumFragmentPresenterImpl presenter;

    protected SpiceManager spiceManager = new SpiceManager(LastFmSpiceService.class);

    public static final String BUNDLE_NAME = "bundleName";

    private Activity activity;
    private String mName;

    private AlbumListAdapter mAlbumListAdapter;
    private ListView mAdapterAlbumListView;

    public AlbumFragment() {
    }

    public static AlbumFragment newInstance(String name) {
        AlbumFragment albumFragment = new AlbumFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_NAME, name);
        albumFragment.setArguments(bundle);
        return albumFragment;
    }

    // ----- Lifecycle override method

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         if (getArguments() != null && getArguments().containsKey(BUNDLE_NAME)) {
            this.mName = getArguments().getString(BUNDLE_NAME);
        } else {
            throw new IllegalArgumentException("Must be created through newInstance(String mName)");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.getComponent(MainActivityComponent.class).inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.init(this);
        presenter.onResume(spiceManager, mName);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_album, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mAdapterAlbumListView = (ListView) view.findViewById(R.id.lv_album);
        TextView toolBarText = (TextView) activity.findViewById(R.id.tv_toolbar);
        toolBarText.setText(mName);
    }

    @Override
    public void onPause() {
        presenter.onPause();
        super.onPause();
    }

    // -----  IAlbumFragmentView implement method

    @Override
    public void showProgressDialog() {
        ProgressBar progressBar = (ProgressBar) activity.findViewById(R.id.toolbar_progress_bar);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressDialog() {
        ProgressBar progressBar = (ProgressBar) activity.findViewById(R.id.toolbar_progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void startService() {
        if (!spiceManager.isStarted()){
            spiceManager.start(activity);
        }
    }

    @Override
    public void stopService() {
        if (spiceManager.isStarted()){
            spiceManager.shouldStop();
        }
    }

    @Override
    public void setAlbumListAdapter(List<AlbumItem> itemAlbumList, int totalAlbums) {
        if (mAlbumListAdapter == null) {
            mAlbumListAdapter = new AlbumListAdapter(this, itemAlbumList, totalAlbums);
            mAdapterAlbumListView.setAdapter(mAlbumListAdapter);
        } else {
            presenter.addListToAdapter(itemAlbumList);
        }
    }

    @Override
    public void notifyPictureChanged() {
        if(mAlbumListAdapter!=null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAlbumListAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public void addListToAdapter(List<AlbumItem> itemTalkList) {
        mAlbumListAdapter.add(itemTalkList);
    }
}
