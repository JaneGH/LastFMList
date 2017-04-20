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
import android.graphics.Bitmap;
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
import com.itclimb.lastfmlist.data.model.AlbumItem;
import com.itclimb.lastfmlist.views.AlbumFragment;

import java.util.List;

public class AlbumListAdapter extends BaseAdapter {

    private List<AlbumItem> mAlbumItemList;
    private LayoutInflater mLayoutInflater;
    private Activity mActivity;
    private int totalListSize;

    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_ACTIVITY = 1;


    public AlbumListAdapter(AlbumFragment albumFragment, List<AlbumItem> mAlbumItemList, int totalListSize) {
        this.mAlbumItemList = mAlbumItemList;
        this.mActivity = albumFragment.getActivity();
        this.mLayoutInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.totalListSize = totalListSize;
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
        return mAlbumItemList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return (position >= mAlbumItemList.size()) ? VIEW_TYPE_LOADING : VIEW_TYPE_ACTIVITY;
    }

    @Override
    public AlbumItem getItem(int position) {
        return (getItemViewType(position) == VIEW_TYPE_ACTIVITY) ? mAlbumItemList.get(position) : null;
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
            view = mLayoutInflater.inflate(R.layout.item_album_list, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.nameText = (TextView) view.findViewById(R.id.tv_name_album);
            holder.listenersText = (TextView) view.findViewById(R.id.tv_listeners_album);
            holder.imageList = (ImageView) view.findViewById(R.id.iv_album);
            view.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        AlbumItem albumItem = getItem(position);
        holder.nameText.setText(albumItem.title);
        holder.listenersText.setText(String.format("%s", albumItem.playCount));
        String nameFile = FileUtils.getFileName(albumItem.artist + "_" + albumItem.title);
        Bitmap imageAlbum = FileUtils.loadBitmap(LastFmListApp.get(), nameFile);
        if (imageAlbum!=null) {
            holder.imageList.setImageBitmap(imageAlbum);
        }
        return view;
    }

    public void add(List<AlbumItem> albumItem) {
        mAlbumItemList.clear();
        mAlbumItemList.addAll(albumItem);
        notifyDataSetChanged();
    }

    private View getFooterView(int position, View convertView, ViewGroup parent) {
        if (position >= totalListSize && totalListSize > 0) {
            // the ListView has reached the last row
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
