package com.myreevuuCoach.customrecorder.views;

/*
 * TimeLapseRecordingSample
 * Sample project to capture audio and video periodically from internal mic/camera
 * and save as time lapsed MPEG4 file.
 *
 * Copyright (c) 2015 saki t_saki@customrecorder.com
 *
 * File name: MainActivity.java
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 * All files in the folder are under this Apache License, Version 2.0.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Toast;

import com.myreevuuCoach.R;
import com.myreevuuCoach.activities.AddVideoDetailActivity;
import com.myreevuuCoach.activities.BaseActivity;
import com.myreevuuCoach.gallery.VideoGalleryModel;
import com.myreevuuCoach.gallery.VideoUtils;

public class ActivityCustomRecorder extends BaseActivity {


    @Override
    protected int getContentView() {
        return R.layout.activity_custom_camera;
    }

    @Override
    protected void onCreateStuff() {

    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected Context getContext() {
        return this;
    }


    @Override
    protected void onPause() {
        super.onPause();
        getFragmentManager().popBackStack();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFragmentManager().beginTransaction().replace(R.id.container, new CameraFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, resultIntent);
        finish();
    }

    private static final int UPLOAD_VIDEO = 101;

    public void setResult(String filePath) {
        /*Intent resultIntent = new Intent();
        resultIntent.putExtra("file_path", filePath);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();*/
        try {
            selectedVideoValidation(filePath, UPLOAD_VIDEO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

    }

    protected void selectedVideoValidation(String path, int UPLOAD_VIDEO) {
        try {
            Bitmap thumb = VideoUtils.getVideoThumb(path);
            String videoTime = VideoUtils.convertMilliSecToTime(Long.parseLong(VideoUtils.getVideoTime(this, path)));
            VideoGalleryModel galleryVideoList = new VideoGalleryModel(path, thumb, videoTime, false);
            String realVideoTime = VideoUtils.getVideoTime(this, path);
            if (VideoUtils.checkVideoMinTimeValid((Long.parseLong(realVideoTime)))) {
                if (VideoUtils.checkVideoMaxTimeValid((Long.parseLong(realVideoTime)))) {
                    VideoGalleryModel.setSelectedVideoInstance(galleryVideoList);
//                    Intent intent = new Intent(this, AddVideoDetailActivity.class);
//                    startActivityForResult(intent, UPLOAD_VIDEO);
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(this, "Video is greater than 15 min", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Video is less than 5 sec", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case UPLOAD_VIDEO:
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
            }

        }
    }
}
