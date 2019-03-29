package com.myreevuuCoach.customrecorder.views;

/*
 * TimeLapseRecordingSample
 * Sample project to capture audio and video periodically from internal mic/camera
 * and save as time lapsed MPEG4 file.
 *
 * Copyright (c) 2015 saki t_saki@customrecorder.com
 *
 * File name: CameraFragment.java
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
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.myreevuuCoach.R;
import com.myreevuuCoach.customrecorder.media.TLMediaAudioEncoder;
import com.myreevuuCoach.customrecorder.media.TLMediaEncoder;
import com.myreevuuCoach.customrecorder.media.TLMediaMovieBuilder;
import com.myreevuuCoach.customrecorder.media.TLMediaVideoEncoder;
import com.myreevuuCoach.gallery.ActivityVideoGallery;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class CameraFragment extends Fragment {


    private static final boolean DEBUG = true;    // TODO set false on releasing
    private static final String TAG = "CameraFragment";

    /**
     * for camera preview display
     */
    private CameraGLView mCameraView;
    /**
     * button for start/stop recording
     */
    private ImageButton mRecordButton;
    private TLMediaVideoEncoder mVideoEncoder;
    private TLMediaAudioEncoder mAudioEncoder;
    private TLMediaMovieBuilder mMuxer;
    private boolean mIsRecording;
    private String mMovieName;

    private ImageView iv_recordButton;
    private ImageView iv_pauseButton;
    private ImageView iv_galleryButton;
    private ImageView iv_back;
    private ImageView iv_flash;
    private ImageView ivSwitchCamera;
    private TextView tv_flashMode;
    private Chronometer cm_recording_time;
    private View ll_flash;
    private boolean mIsRecordingPause;

    ActivityCustomRecorder parentActivity;

    public CameraFragment() {
        // need default constructor
    }

    Camera camera;
    private boolean mIsFlashOn;
    boolean hasCameraFlash;
    private int CURRENT_CAMERA_VIEW = 0; //0 back & 1 front
    private String CURRENT_FLASH_MODE = Camera.Parameters.FLASH_MODE_AUTO; //0 back & 1 front
    long timeWhenStopped = 0;
    String filePAth = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_camera, container, false);

        parentActivity = (ActivityCustomRecorder) getActivity();

        hasCameraFlash = getActivity().getPackageManager().
                hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        mCameraView = rootView.findViewById(R.id.cameraView);
        mCameraView.setVideoSize(1280, 720);
        // mCameraView.setOnTouchListener(mOnTouchListener);
        mRecordButton = rootView.findViewById(R.id.record_button);
        mRecordButton.setOnClickListener(mOnClickListener);

        /**/
        iv_recordButton = rootView.findViewById(R.id.iv_recordButton);
        iv_pauseButton = rootView.findViewById(R.id.iv_pauseButton);
        iv_pauseButton.setVisibility(View.INVISIBLE);
        iv_galleryButton = rootView.findViewById(R.id.iv_galleryButton);
        iv_back = rootView.findViewById(R.id.iv_back);
        iv_flash = rootView.findViewById(R.id.iv_flash);
        ivSwitchCamera = rootView.findViewById(R.id.ivSwitchCamera);
        tv_flashMode = rootView.findViewById(R.id.tv_flashMode);
        ll_flash = rootView.findViewById(R.id.ll_flash);
        cm_recording_time = rootView.findViewById(R.id.cm_recording_time);
        cm_recording_time.setVisibility(View.INVISIBLE);
        cm_recording_time.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            public void onChronometerTick(Chronometer cArg) {
                long ticker = SystemClock.elapsedRealtime() - timeWhenStopped;
                String timer = DateFormat.format("kk:mm", ticker).toString();
                if (DEBUG) Log.v(TAG, "Chronometer : " + timer);

                /*cm_recording_time = cArg;*/
                /*long t = SystemClock.elapsedRealtime() - cArg.getBase();
                cArg.setText(DateFormat.format("kk:mm:ss", t));
                cArg.setText(cArg.getBase()+"");*/
            }
        });
        iv_recordButton.setOnClickListener(mOnClickListener);
        iv_pauseButton.setOnClickListener(mOnClickListener);
        iv_galleryButton.setOnClickListener(mOnClickListener);
        iv_back.setOnClickListener(mOnClickListener);
        ivSwitchCamera.setOnClickListener(mOnClickListener);
        ll_flash.setOnClickListener(mOnClickListener);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (DEBUG) Log.v(TAG, "onResume:");
        mCameraView.onResume();
    }

    @Override
    public void onPause() {
        if (DEBUG) Log.v(TAG, "onPause:");
        stopRecording();
        mCameraView.onPause();
        super.onPause();
    }

    void toggelCamera() {
        if (CURRENT_CAMERA_VIEW == 0) {
            CURRENT_CAMERA_VIEW = 1;
            mCameraView.setCameraId(CURRENT_CAMERA_VIEW);
            ll_flash.setVisibility(View.GONE);
        } else {
            CURRENT_CAMERA_VIEW = 0;
            mCameraView.setCameraId(CURRENT_CAMERA_VIEW);
            ll_flash.setVisibility(View.VISIBLE);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mCameraView.onResume();

            }

        }, 200);
        mCameraView.onPause();
    }

    void toggelFlash() {
        if (CURRENT_FLASH_MODE.equalsIgnoreCase(Camera.Parameters.FLASH_MODE_AUTO)) {
            CURRENT_FLASH_MODE = Camera.Parameters.FLASH_MODE_OFF;
            mCameraView.setCAMERA_FLASH_MODE(Camera.Parameters.FLASH_MODE_OFF);
            iv_flash.setImageResource(R.drawable.ic_flash_off);
        } else if (CURRENT_FLASH_MODE.equalsIgnoreCase(Camera.Parameters.FLASH_MODE_ON)) {
            CURRENT_FLASH_MODE = Camera.Parameters.FLASH_MODE_AUTO;
            mCameraView.setCAMERA_FLASH_MODE(Camera.Parameters.FLASH_MODE_AUTO);
            iv_flash.setImageResource(R.drawable.ic_flash);
        } else if (CURRENT_FLASH_MODE.equalsIgnoreCase(Camera.Parameters.FLASH_MODE_OFF)) {
            CURRENT_FLASH_MODE = Camera.Parameters.FLASH_MODE_ON;
            mCameraView.setCAMERA_FLASH_MODE(Camera.Parameters.FLASH_MODE_ON);
            iv_flash.setImageResource(R.drawable.ic_flash);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mCameraView.onResume();
            }

        }, 200);

        tv_flashMode.setText(StringUtils.capitalize(CURRENT_FLASH_MODE));
        mCameraView.onPause();
    }

    /*
     *
     */
    public final void fixedScreenOrientation(final boolean fixed) {
        getActivity().setRequestedOrientation(
                fixed ? ActivityInfo.SCREEN_ORIENTATION_LOCKED : ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    /**
     * method when touch record button
     */
    private final OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.record_button:
                    recordingButton();
                    break;
                case R.id.iv_recordButton:
                    recordingButton();
                    break;
                case R.id.iv_pauseButton:
                    pauseButton();
                    break;
                case R.id.iv_galleryButton:
                    Intent intent = new Intent(getActivity(), ActivityVideoGallery.class);
                    startActivity(intent);
                    break;
                case R.id.iv_back:
                    parentActivity.onBackPressed();
                    break;
                case R.id.ll_flash:
                    toggelFlash();
                    /*if (hasCameraFlash) {
                        if (mIsFlashOn)
                            flashLightOff();
                        else
                            flashLightOn();
                    } else {
                        Toast.makeText(getActivity(), "No flash available on your device",
                                Toast.LENGTH_SHORT).show();
                    }*/

                    /*if (mIsFlashOn)
                        mIsFlashOn = false;
                    else
                        mIsFlashOn = true;
*/
                    break;
                case R.id.ivSwitchCamera:
                    toggelCamera();
                    break;
            }
        }
    };


    void recordingButton() {
        if (DEBUG) Log.v(TAG, "recordingButton");

        if (!mIsRecording) {
            startRecording();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (DEBUG) Log.v(TAG, "resumeRecording");
                    resumeRecording();
                    cm_recording_time.setVisibility(View.VISIBLE);

                }

            }, 500);

        } else {
            pauseRecording();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopRecording();
                }

            }, 500);

        }
    }

    void pauseButton() {
        if (mIsRecordingPause) {
            resumeRecording();
        } else {
            pauseRecording();
        }
    }

    private final View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (mIsRecording) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        resumeRecording();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        pauseRecording();
                        break;
                }
                return true;
            } else
                return false;
        }
    };

    /**
     * start recording
     * This is a sample project and call this on UI thread to avoid being complicated
     * but basically this should be called on private thread because preparing
     * of encoder may be heavy work on some devices
     */
    private void startRecording() {
        if (mIsRecording) return;
        if (DEBUG) Log.v(TAG, "start:");
        try {
            mRecordButton.setColorFilter(0xffffff00);
            timeWhenStopped = 0;
            // turn yellow
            mMovieName = TAG; // + System.nanoTime();
            if (true) {
                // for video capturing
                mVideoEncoder = new TLMediaVideoEncoder(getActivity(), mMovieName, mMediaEncoderListener);
                try {
                    mVideoEncoder.setFormat(mCameraView.getVideoWidth(), mCameraView.getVideoHeight());
                    mVideoEncoder.prepare();
                } catch (Exception e) {
                    Log.e(TAG, "startRecording:", e);
                    mVideoEncoder.release();
                    mVideoEncoder = null;
                    throw e;
                }
            }
            if (true) {
                // for audio capturing
                mAudioEncoder = new TLMediaAudioEncoder(getActivity(), mMovieName, mMediaEncoderListener);
                try {
                    mAudioEncoder.prepare();
                } catch (Exception e) {
                    Log.e(TAG, "startRecording:", e);
                    mAudioEncoder.release();
                    mAudioEncoder = null;
                    throw e;
                }
            }
            if (mVideoEncoder != null) {
                mVideoEncoder.start(true);
            }
            if (mAudioEncoder != null) {
                mAudioEncoder.start(true);
            }
            mIsRecording = true;
        } catch (Exception e) {
            mRecordButton.setColorFilter(0);
            Log.e(TAG, "startCapture:", e);
        }
        fixedScreenOrientation(mIsRecording);
    }

    private void afterStartRecording() {
        iv_recordButton.setImageResource(R.drawable.ic_video_stop);
        iv_pauseButton.setVisibility(View.VISIBLE);
        iv_back.setVisibility(View.INVISIBLE);
        iv_galleryButton.setVisibility(View.INVISIBLE);
        ll_flash.setVisibility(View.INVISIBLE);
        ivSwitchCamera.setVisibility(View.INVISIBLE);
    }

    private void afterStopRecording() {
        cm_recording_time.setVisibility(View.INVISIBLE);
        iv_recordButton.setImageResource(R.drawable.ic_cam_btn);
        iv_pauseButton.setVisibility(View.INVISIBLE);
        iv_pauseButton.setImageResource(R.drawable.ic_pause_video);
        iv_back.setVisibility(View.VISIBLE);
        iv_galleryButton.setVisibility(View.VISIBLE);
        ll_flash.setVisibility(View.VISIBLE);
        ivSwitchCamera.setVisibility(View.VISIBLE);
        parentActivity.setResult(filePAth);
    }


    /**
     * request stop recording
     */
    private void stopRecording() {
        if (!mIsRecording) return;
        if (DEBUG) Log.v(TAG, "stop");

        mIsRecording = false;
        mRecordButton.setColorFilter(0);    // return to default color
        if (mVideoEncoder != null) {
            mVideoEncoder.stop();
            mVideoEncoder.release();
        }
        if (mAudioEncoder != null) {
            mAudioEncoder.stop();
            mAudioEncoder.release();
        }
        fixedScreenOrientation(mIsRecording);
        try {
            mMuxer = new TLMediaMovieBuilder(getActivity(), mMovieName);
            filePAth = mMuxer.getOutputPath();
            mMuxer.build(mTLMediaMovieBuilderCallback);
            if (DEBUG) Log.v(TAG, "filePAth " + filePAth);
        } catch (Exception e) {
            e.printStackTrace();
        }
        afterStopRecording();

    }

    /**
     * resume recording
     */
    private void resumeRecording() {
        mIsRecordingPause = false;

        iv_pauseButton.setImageResource(R.drawable.ic_pause_video);
        cm_recording_time.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
        cm_recording_time.start();

        afterStartRecording();
        if (!mIsRecording) return;
        mRecordButton.setColorFilter(0xffff0000);    // turn red
        try {
            if (mVideoEncoder != null) {
                if (mVideoEncoder.isPaused())
                    mVideoEncoder.resume();
            }
            if (mAudioEncoder != null) {
                if (mAudioEncoder.isPaused())
                    mAudioEncoder.resume();
            }
        } catch (IOException e) {
            stopRecording();
        }
    }

    /**
     * pause recording
     */
    private void pauseRecording() {
        mIsRecordingPause = true;
        iv_pauseButton.setImageResource(R.drawable.ic_play_video);

        timeWhenStopped = cm_recording_time.getBase() - SystemClock.elapsedRealtime();
        cm_recording_time.stop();

        if (!mIsRecording) return;
        mRecordButton.setColorFilter(0xffffff00);    // turn yellow
        if ((mVideoEncoder != null) && !mVideoEncoder.isPaused())
            try {
                mVideoEncoder.pause();
            } catch (Exception e) {
                Log.e(TAG, "pauseRecording:", e);
                mVideoEncoder.release();
                mVideoEncoder = null;
            }
        if ((mAudioEncoder != null) && !mAudioEncoder.isPaused())
            try {
                mAudioEncoder.pause();
            } catch (Exception e) {
                Log.e(TAG, "pauseRecording:", e);
                mAudioEncoder.release();
                mAudioEncoder = null;
            }
    }

    /**
     * callback methods from encoder
     */
    private final TLMediaEncoder.MediaEncoderListener mMediaEncoderListener
            = new TLMediaEncoder.MediaEncoderListener() {

        @Override
        public void onPrepared(TLMediaEncoder encoder) {
            if (DEBUG) Log.v(TAG, "onPrepared:encoder=" + encoder);
        }

        @Override
        public void onStopped(TLMediaEncoder encoder) {
            if (DEBUG) Log.v(TAG, "onStopped:encoder=" + encoder);
        }

        @Override
        public void onResume(TLMediaEncoder encoder) {
            if (DEBUG) Log.v(TAG, "onResume:encoder=" + encoder);
            if (encoder instanceof TLMediaVideoEncoder)
                mCameraView.setVideoEncoder((TLMediaVideoEncoder) encoder);
        }

        @Override
        public void onPause(TLMediaEncoder encoder) {
            if (DEBUG) Log.v(TAG, "onPause:encoder=" + encoder);
            if (encoder instanceof TLMediaVideoEncoder)
                mCameraView.setVideoEncoder(null);
        }
    };

    /**
     * callback methods from TLMediaMovieBuilder
     */
    private TLMediaMovieBuilder.TLMediaMovieBuilderCallback mTLMediaMovieBuilderCallback
            = new TLMediaMovieBuilder.TLMediaMovieBuilderCallback() {

        @Override
        public void onFinished(String output_path) {
            if (DEBUG) Log.v(TAG, "onFinished:");
            mMuxer = null;
            if (!TextUtils.isEmpty(output_path)) {
                final Activity activity = CameraFragment.this.getActivity();
                if ((activity == null) || activity.isFinishing()) return;
                // add movie to gallery
                MediaScannerConnection.scanFile(activity, new String[]{output_path}, null, null);
            }
        }

        @Override
        public void onError(Exception e) {
            if (DEBUG) Log.v(TAG, "onError:" + e.getMessage());
        }
    };


    /*
     *
     * */
    Handler timeHandle = new Handler();
    int videoTimer = 0;

    Runnable runnableTimer = new Runnable() {
        @Override
        public void run() {
            videoTimer++;
            startTimer();
        }
    };

    void startTimer() {
        videoTimer = 0;
        timeHandle.postDelayed(runnableTimer, 1000);
    }

    void resumeTimer() {
        timeHandle.removeCallbacks(runnableTimer);
    }

    void stopeTimer() {
        timeHandle.removeCallbacks(runnableTimer);
    }
}
