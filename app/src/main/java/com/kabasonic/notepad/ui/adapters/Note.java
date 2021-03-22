package com.kabasonic.notepad.ui.adapters;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Note {

    ArrayList<Bitmap> mImageList = new ArrayList<>();
    String mTitle;
    String mBody;
    int mBadge;

    public Note(){
        //empty constructor
    }

    public Note(String mTitle, String mBody, ArrayList<Bitmap> mImageList,int  mBadge){
        this.mTitle = mTitle;
        this.mBody = mBody;
        this.mImageList = mImageList;
        this.mBadge = mBadge;
    }

    public Note(String mTitle, String mBody){
        this.mTitle = mTitle;
        this.mBody = mBody;
    }


    public ArrayList<Bitmap> getmImageList() {
        return mImageList;
    }

    public void setmImageList(ArrayList<Bitmap> mImageList) {
        this.mImageList = mImageList;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmBody() {
        return mBody;
    }

    public void setmBody(String mBody) {
        this.mBody = mBody;
    }

    public int getmBadge() {
        return mBadge;
    }

    public void setmBadge(int mBadge) {
        this.mBadge = mBadge;
    }
}
