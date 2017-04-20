package com.itclimb.lastfmlist.data.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import com.itclimb.lastfmlist.data.model.AlbumItem;
import com.itclimb.lastfmlist.data.model.ArtistItem;

public class AlbumTable extends Table {

    public static final String TABLE_NAME = "lastFmList_album";

    public static final String CREATE = CREATE_TABLE + TABLE_NAME + " (" +
            Column._ID + INTEGER + PRIMARY_KEY + AUTOINCREMENT + COMMA +
            Column.TITLE + TEXT + COMMA +
            Column.PLAY_COUNT + TEXT + COMMA +
            Column.IMAGE_URL + TEXT + COMMA +
            Column.ARTIST + TEXT +
            " );";

    public abstract static class Column implements BaseColumns {
        public static final String TITLE = "Title";
        public static final String PLAY_COUNT = "PlayCount";
        public static final String IMAGE_URL = "ImageUrl";
        public static final String ARTIST = "artist";
    }

    public static ContentValues toContentValues(AlbumItem album) {
        ContentValues values = new ContentValues();
        values.put(Column.TITLE, album.title);
        values.put(Column.PLAY_COUNT, album.playCount);
        values.put(Column.ARTIST, album.artist);
        values.put(Column.IMAGE_URL, album.imageUrl);
        return values;
    }

    public static AlbumItem parseCursor(Cursor cursor) {
        AlbumItem artist = new AlbumItem();
        artist.title = cursor.getString(cursor.getColumnIndexOrThrow(Column.TITLE));
        artist.playCount = cursor.getString(cursor.getColumnIndexOrThrow(Column.PLAY_COUNT));
        artist.imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(Column.IMAGE_URL));
        artist.artist = cursor.getString(cursor.getColumnIndexOrThrow(Column.ARTIST));

        return artist;
    }

}
