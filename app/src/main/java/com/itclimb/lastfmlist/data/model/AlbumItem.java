package com.itclimb.lastfmlist.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AlbumItem implements Parcelable {
    @SerializedName("Title")
    @Expose
    public String title;
    @SerializedName("PlayCount")
    @Expose
    public String playCount;
    @SerializedName("ImageUrl")
    @Expose
    public String imageUrl;
    @SerializedName("Artist")
    @Expose
    public String artist;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.playCount);
        dest.writeString(this.imageUrl);
        dest.writeString(this.artist);
    }

    public AlbumItem() {
    }

    private AlbumItem(Parcel in) {
        this.title      = in.readString();
        this.playCount = in.readString();
        this.imageUrl  = in.readString();
        this.artist   = in.readString();
    }

    public static final Creator<AlbumItem> CREATOR = new Creator<AlbumItem>() {
        @Override
        public AlbumItem createFromParcel(Parcel source) {
            return new AlbumItem(source);
        }

        @Override
        public AlbumItem[] newArray(int size) {
            return new AlbumItem[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() == this.getClass()) {
            AlbumItem store = (AlbumItem) obj;
            if (store.title.equals(this.title)||store.artist.equals(this.artist))
                return true;
        }
        return false;
    }
}

