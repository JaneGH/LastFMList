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

package com.itclimb.lastfmlist.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.itclimb.lastfmlist.LastFmListApp;
import com.itclimb.lastfmlist.R;
import com.itclimb.lastfmlist.Utils.FileUtils;
import com.itclimb.lastfmlist.data.model.ArtistItem;
import com.itclimb.lastfmlist.views.ArtistFragment;

import java.util.List;

public class ArtistListAdapter extends BaseAdapter {

    private List<ArtistItem> mArtistItemList;
    private LayoutInflater mLayoutInflater;
    private Activity mActivity;
    private int mTotalListSize;

    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_ACTIVITY = 1;


    public ArtistListAdapter(ArtistFragment artistFragment, List<ArtistItem> mArtistItemList, int mTotalListSize) {
        this.mArtistItemList = mArtistItemList;
        this.mActivity = artistFragment.getActivity();
        this.mLayoutInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mTotalListSize = mTotalListSize;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItemViewType(position) == VIEW_TYPE_ACTIVITY;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mArtistItemList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return (position >= mArtistItemList.size()) ? VIEW_TYPE_LOADING : VIEW_TYPE_ACTIVITY;
    }

    @Override
    public ArtistItem getItem(int position) {
        return (getItemViewType(position) == VIEW_TYPE_ACTIVITY) ? mArtistItemList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return (getItemViewType(position) == VIEW_TYPE_ACTIVITY) ? position : -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == VIEW_TYPE_LOADING) {
            // возвращаем вместо строки с данными футер с прогрессбаром
            return getFooterView(position, convertView, parent);
        }
        View view = convertView;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.item_artist_list, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.nameText = (TextView) view.findViewById(R.id.tv_name_artist);
            holder.listenersText = (TextView) view.findViewById(R.id.tv_listeners_artist);
            holder.imageList = (ImageView) view.findViewById(R.id.iv_artist);
            view.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        ArtistItem itemTalk = getItem(position);
        holder.nameText.setText(itemTalk.name);
        holder.listenersText.setText(String.format("%s", itemTalk.listeners));
        String nameFile = FileUtils.getFileName(itemTalk.name);
        holder.imageList.setImageBitmap(FileUtils.loadBitmap(LastFmListApp.get(), nameFile));
        return view;
    }

    public void add(List<ArtistItem> artistItem) {
        mArtistItemList.clear();
        mArtistItemList.addAll(artistItem);
        notifyDataSetChanged();
    }

    private View getFooterView(int position, View convertView, ViewGroup parent) {
        if (position >= mTotalListSize && mTotalListSize > 0) {
            TextView tvLastRow = new TextView(mActivity);
            tvLastRow.setHint("Reached the last row.");
            tvLastRow.setGravity(Gravity.CENTER);
            return tvLastRow;
        }
        View row = convertView;
        if (row == null) {
            row = mLayoutInflater.inflate(R.layout.list_footer, parent, false);
        }
        return row;
    }

    private static class ViewHolder {
        TextView nameText;
        TextView listenersText;
        ImageView imageList;
    }
}
