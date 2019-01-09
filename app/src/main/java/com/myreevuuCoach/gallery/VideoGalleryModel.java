package com.myreevuuCoach.gallery;

import android.graphics.Bitmap;

/**
 * Created by dev on 30/11/18.
 */

public class VideoGalleryModel {
    static VideoGalleryModel mSelectedModel;
    String videoPath;
    Bitmap videothumbnail;
    String videoTime;
    boolean selected;

    public VideoGalleryModel(String videoPath, Bitmap videothumbnail, String videoTime, boolean selected) {
        this.videoPath = videoPath;
        this.videothumbnail = videothumbnail;
        this.videoTime = videoTime;
        this.selected = selected;
    }

    public static VideoGalleryModel getSelectedVideoInstance() {
        return mSelectedModel;
    }

    public static void setSelectedVideoInstance(VideoGalleryModel model) {
        mSelectedModel = model;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public Bitmap getVideothumbnail() {
        return videothumbnail;
    }

    public String getVideoTime() {
        return videoTime;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
