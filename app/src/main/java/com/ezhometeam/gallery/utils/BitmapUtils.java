package com.ezhometeam.gallery.utils;

import android.graphics.BitmapFactory;

import java.io.File;

/**
 * Created by n on 06/07/2017.
 */

public class BitmapUtils {
    public static PairInt calculateResizeImage(String pathFile, final int minSize) {
        File file = new File(pathFile);
        if (!file.exists()) return null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathFile, options);
        int widthImage = minSize;
        int heightImage = (int) ((float) widthImage * options.outHeight / options.outWidth);
        if (heightImage < minSize) {
            heightImage = minSize;
            widthImage = (int) ((float) heightImage * options.outWidth / options.outHeight);
        }
        return new PairInt(widthImage, heightImage);

    }
}
