package com.kabasonic.notepad.ui.adapters;

import android.graphics.Bitmap;

public class ImageItem {
    private Bitmap mImage;

    public ImageItem() {
    }

    public ImageItem(Bitmap mImage) {
        this.mImage = mImage;
    }

    public Bitmap getmImage() {
        return mImage;
    }

    public void setmImage(Bitmap mImage) {
        this.mImage = mImage;
    }
}
