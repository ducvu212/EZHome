package com.ezhometeam.gallery.utils;

/**
 * Created by n on 06/07/2017.
 */

public class StringUtils {
    public static boolean isEmpty(String content) {
        if ( content == null || "".equals(content)) {
            return true;
        }
        return false;
    }
}
