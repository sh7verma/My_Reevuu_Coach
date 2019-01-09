package com.myreevuuCoach.filePicker;

import java.util.List;
import java.util.Map;

/**
 * Created by gabriel on 10/2/17.
 */

public interface FileMapResultCallback {
    void onResultCallback(Map<FileType, List<Document>> files);
}

