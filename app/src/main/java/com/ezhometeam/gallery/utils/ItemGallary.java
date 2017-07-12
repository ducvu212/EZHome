package com.ezhometeam.gallery.utils;

import java.io.Serializable;

/**
 * Created by n on 06/07/2017.
 */

public class ItemGallary implements Serializable {
    private String mPathFile;
    protected String mThumnail;
    private long mId;

    public ItemGallary(String pathFile, long id) {
        mId = id;
        mPathFile = pathFile;
    }

    public ItemGallary(String pathFile, String thumnail, long id) {
        mPathFile = pathFile;
        mThumnail = thumnail;
        mId = id;
    }

    public String getPathFile() {
        return mPathFile;
    }


    public void setThumnail(String thumnail) {
        mThumnail = thumnail;
    }

    public String getThumbnail() {
        return mThumnail;
    }

    public long getId() {
        return mId;
    }
}
