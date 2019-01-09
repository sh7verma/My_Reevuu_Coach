package com.sandrios.sandriosCamera.internal.gallery;

import android.graphics.Bitmap;

public class VideoGalleryModelLib {
    String videoPath;
    Bitmap videothumbnail;
    String videoTime;
    boolean selected;

    static VideoGalleryModelLib mSelectedModel;

    public static void setSelectedVideoInstance(VideoGalleryModelLib model) {
        mSelectedModel = model;
    }

    public static VideoGalleryModelLib getSelectedVideoInstance() {
        return mSelectedModel;
    }


    public VideoGalleryModelLib(String videoPath, Bitmap videothumbnail, String videoTime, boolean selected) {
        this.videoPath = videoPath;
        this.videothumbnail = videothumbnail;
        this.videoTime = videoTime;
        this.selected = selected;
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
