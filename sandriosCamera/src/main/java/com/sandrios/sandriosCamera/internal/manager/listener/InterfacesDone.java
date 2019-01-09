package com.sandrios.sandriosCamera.internal.manager.listener;

import com.sandrios.sandriosCamera.internal.gallery.VideoGalleryModelLib;

/**
 * Created by app on 18-Dec-17.
 */

public interface InterfacesDone {
    void onDone(VideoGalleryModelLib cameraOutputModel);

    void onOpenGallery(VideoGalleryModelLib selectedVideoInstance);

}
