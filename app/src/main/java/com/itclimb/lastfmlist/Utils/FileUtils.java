package com.itclimb.lastfmlist.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import timber.log.Timber;

public class FileUtils {
    public static String saveFile(Context context, Bitmap b, String picName) {
        String fileName = picName.replace('/', '_');
        FileOutputStream fos;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            return fileName;
        } catch (FileNotFoundException e) {
            Timber.d("file not found");
            e.printStackTrace();
        } catch (IOException e) {
            Timber.d("io exception");
            e.printStackTrace();
        }

        return null;
    }

    public static Bitmap loadBitmap(Context context, String picName) {
        Bitmap b = null;
        FileInputStream fis;
        try {
            fis = context.openFileInput(picName);
            b = BitmapFactory.decodeStream(fis);
            fis.close();
        } catch (FileNotFoundException e) {
            Timber.d("file not found");
            e.printStackTrace();
        } catch (IOException e) {
            Timber.d("io exception");
            e.printStackTrace();
        }
        return b;
    }

    public static String getFileName(String picName) {
        return picName.replace('/', '_').replace(' ', '_');
    }
}
