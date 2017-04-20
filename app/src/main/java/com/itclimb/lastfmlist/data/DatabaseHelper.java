package com.itclimb.lastfmlist.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.itclimb.lastfmlist.data.model.AlbumItem;
import com.itclimb.lastfmlist.data.model.ArtistItem;
import com.itclimb.lastfmlist.data.tables.AlbumTable;
import com.itclimb.lastfmlist.data.tables.ArtistTable;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;


/**
 * Created by MyComp on 19.04.2017.
 */

@Singleton
public class DatabaseHelper {

    private final BriteDatabase mDb;

//    @Inject
//    public DatabaseHelper(DbOpenHelper dbOpenHelper) {
//        mDb = SqlBrite.create().wrapDatabaseHelper(dbOpenHelper);
//    }

    public DatabaseHelper(Context context) {
        mDb = SqlBrite.create().wrapDatabaseHelper(new DbOpenHelper(context));
    }

    public void setArtists(List<ArtistItem> artists, String country) {
        BriteDatabase.Transaction transaction = mDb.newTransaction();
        try {
            mDb.delete(ArtistTable.TABLE_NAME, ArtistTable.Column.COUNTRY + " = '" + country + "'");
            for (ArtistItem artist : artists) {
                mDb.insert(ArtistTable.TABLE_NAME,
                        ArtistTable.toContentValues(artist),
                        SQLiteDatabase.CONFLICT_REPLACE);
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    public List<ArtistItem> getArtists(String country) {
        Cursor cursor = mDb.query("SELECT * FROM " + ArtistTable.TABLE_NAME
                + " WHERE " + ArtistTable.Column.COUNTRY + "='" + country
                + "' ORDER BY " + ArtistTable.Column.NAME);
        ArrayList<ArtistItem> artists = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                artists.add(ArtistTable.parseCursor(cursor));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return artists;
    }

    public void setAlbums(List<AlbumItem> albums, String nameArtist) {
        BriteDatabase.Transaction transaction = mDb.newTransaction();
        try {
            mDb.delete(AlbumTable.TABLE_NAME, AlbumTable.Column.ARTIST + " = '" + nameArtist + "'");
            for (AlbumItem album : albums) {
                mDb.insert(AlbumTable.TABLE_NAME,
                        AlbumTable.toContentValues(album),
                        SQLiteDatabase.CONFLICT_REPLACE);
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    public List<AlbumItem> getAlbums(String nameArtist) {
        Cursor cursor = mDb.query("SELECT * FROM " + AlbumTable.TABLE_NAME
                + " WHERE " + AlbumTable.Column.ARTIST + "='" + nameArtist
                + "' ORDER BY " + AlbumTable.Column.TITLE);
        ArrayList<AlbumItem> albums = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                albums.add(AlbumTable.parseCursor(cursor));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return albums;
    }
}