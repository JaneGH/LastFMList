package com.itclimb.lastfmlist.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ArtistItem implements Parcelable {
    @SerializedName("Name")
    @Expose
    public String name;
    @SerializedName("Listeners")
    @Expose
    public String listeners;
    @SerializedName("ImageUrl")
    @Expose
    public String imageUrl;
    @SerializedName("Country")
    @Expose
    public String country;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.listeners);
        dest.writeString(this.imageUrl);
        dest.writeString(this.country);
    }

    public ArtistItem() {
    }

    protected ArtistItem(Parcel in) {
        this.name      = in.readString();
        this.listeners = in.readString();
        this.imageUrl  = in.readString();
        this.country   = in.readString();
    }

    public static final Creator<ArtistItem> CREATOR = new Creator<ArtistItem>() {
        @Override
        public ArtistItem createFromParcel(Parcel source) {
            return new ArtistItem(source);
        }

        @Override
        public ArtistItem[] newArray(int size) {
            return new ArtistItem[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() == this.getClass()) {
            ArtistItem store = (ArtistItem) obj;
            if (store.name.equals(this.name)||store.country.equals(this.country))
                return true;
        }
        return false;
    }
}

