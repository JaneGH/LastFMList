package com.itclimb.lastfmlist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.itclimb.lastfmlist.data.tables.AlbumTable;
import com.itclimb.lastfmlist.data.tables.ArtistTable;
import com.itclimb.lastfmlist.data.tables.Table;
import com.itclimb.lastfmlist.injection.ApplicationContext;
import javax.inject.Inject;

public class DbOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "lastFmList.db";
    public static final int DATABASE_VERSION = 1;

    @Inject
    public DbOpenHelper(@ApplicationContext Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            db.execSQL(ArtistTable.CREATE);
            db.execSQL(AlbumTable.CREATE);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Table.DROP_TABLE_IF_EXISTS + ArtistTable.TABLE_NAME);
        onCreate(db);
    }

}