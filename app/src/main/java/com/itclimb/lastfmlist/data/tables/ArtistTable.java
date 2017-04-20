package com.itclimb.lastfmlist.data.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import com.itclimb.lastfmlist.data.model.ArtistItem;

public class ArtistTable extends Table {

    public static final String TABLE_NAME = "lastFmList_artist";

    public static final String CREATE = CREATE_TABLE + TABLE_NAME + " (" +
            Column._ID + INTEGER + PRIMARY_KEY + AUTOINCREMENT + COMMA +
            Column.NAME + TEXT + COMMA +
            Column.COUNTRY + TEXT + COMMA +
            Column.LISTENERS + TEXT + COMMA +
            Column.IMAGE_URL + TEXT +
            " );";

    public abstract static class Column implements BaseColumns {
        public static final String NAME = "Name";
        public static final String LISTENERS = "Listeners";
        public static final String IMAGE_URL = "ImageUrl";
        public static final String COUNTRY = "country";
    }

    public static ContentValues toContentValues(ArtistItem artist) {
        ContentValues values = new ContentValues();
        values.put(Column.NAME, artist.name);
        values.put(Column.LISTENERS, artist.listeners);
        values.put(Column.COUNTRY, artist.country);
        values.put(Column.IMAGE_URL, artist.imageUrl);
        return values;
    }

    public static ArtistItem parseCursor(Cursor cursor) {
        ArtistItem artist = new ArtistItem();
        artist.name = cursor.getString(cursor.getColumnIndexOrThrow(Column.NAME));
        artist.listeners = cursor.getString(cursor.getColumnIndexOrThrow(Column.LISTENERS));
        artist.imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(Column.IMAGE_URL));
        artist.country = cursor.getString(cursor.getColumnIndexOrThrow(Column.COUNTRY));

        return artist;
    }

}
