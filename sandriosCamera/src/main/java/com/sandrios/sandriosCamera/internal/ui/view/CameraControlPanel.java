package com.sandrios.sandriosCamera.internal.ui.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaActionSound;
import android.net.Uri;
import android.os.Build;
import android.os.FileObserver;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sandrios.sandriosCamera.R;
import com.sandrios.sandriosCamera.internal.SandriosCamera;
import com.sandrios.sandriosCamera.internal.configuration.CameraConfiguration;
import com.sandrios.sandriosCamera.internal.gallery.VideoGalleryModelLib;
import com.sandrios.sandriosCamera.internal.manager.listener.InterfacesDone;
import com.sandrios.sandriosCamera.internal.ui.BaseSandriosActivity;
import com.sandrios.sandriosCamera.internal.ui.model.GalleryModel;
import com.sandrios.sandriosCamera.internal.utils.DateTimeUtils;
import com.sandrios.sandriosCamera.internal.utils.Utils;
import com.sandrios.sandriosCamera.internal.utils.VideoRequestHandler;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Arpit Gandhi on 7/6/16.
 */
public class CameraControlPanel extends RelativeLayout
        implements RecordButton.RecordButtonListener,
        MediaActionSwitchView.OnMediaActionStateChangeListener {

    private static Context context;

    private CameraSwitchView cameraSwitchView;
    private RecordButton recordButton;
    private MediaActionSwitchView mediaActionSwitchView;
    private FlashSwitchView flashSwitchView;
    private TextView recordDurationText;
    private TextView recordSizeText;
    private TextView done;
    private ImageButton settingsButton;
    private ImageButton imgClose;
    private ImageView imgGallery;
    private RecyclerView recyclerView;

    public static ImageGalleryAdapter imageGalleryAdapter;
    private RecordButton.RecordButtonListener recordButtonListener;
    private MediaActionSwitchView.OnMediaActionStateChangeListener onMediaActionStateChangeListener;
    private CameraSwitchView.OnCameraTypeChangeListener onCameraTypeChangeListener;
    private FlashSwitchView.FlashModeSwitchListener flashModeSwitchListener;
    private SettingsClickListener settingsClickListener;
    private PickerItemClickListener pickerItemClickListener;

    private TimerTaskBase countDownTimer;
    private long maxVideoFileSize = 0;
    private String mediaFilePath;
    private boolean hasFlash = false;
    private
    @MediaActionSwitchView.MediaActionState
    int mediaActionState;
    private int mediaAction;
    private boolean showImageCrop = false;
    private FileObserver fileObserver;
    private int mWidth;
    InterfacesDone interfacesDone;
    private int cameraSwitch;
    private String GALLERY_IMAGE_PATH = "";
    MediaActionSound sound;
    private long duration = 0;


    VideoRequestHandler videoRequestHandler;
    Picasso picassoInstance;


    public CameraControlPanel(Context context) {
        this(context, null);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public CameraControlPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        sound = new MediaActionSound();
        init();
    }

    private void init() {
        DisplayMetrics display = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(display);
        mWidth = display.widthPixels;
        hasFlash = context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        LayoutInflater.from(context).inflate(R.layout.camera_control_panel_layout, this);
        setBackgroundColor(Color.TRANSPARENT);
        settingsButton = (ImageButton) findViewById(R.id.settings_view);
        imgClose = (ImageButton) findViewById(R.id.imgClose);
        imgGallery = (ImageView) findViewById(R.id.img_galery);
        cameraSwitchView = (CameraSwitchView) findViewById(R.id.front_back_camera_switcher);
        mediaActionSwitchView = (MediaActionSwitchView) findViewById(R.id.photo_video_camera_switcher);
        mediaActionSwitchView.setVisibility(GONE);
        recordButton = (RecordButton) findViewById(R.id.record_button);
        flashSwitchView = (FlashSwitchView) findViewById(R.id.flash_switch_view);
        recordDurationText = (TextView) findViewById(R.id.record_duration_text);
        recordSizeText = (TextView) findViewById(R.id.record_size_mb_text);

        done = (TextView) findViewById(R.id.done);
        done.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (mWidth * 0.04));
        done.setPadding(mWidth / 32, mWidth / 32, mWidth / 32, mWidth / 32);
        done.setVisibility(GONE);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        // recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        cameraSwitchView.setOnCameraTypeChangeListener(onCameraTypeChangeListener);
        cameraSwitchView.setVisibility(GONE);
        mediaActionSwitchView.setOnMediaActionStateChangeListener(this);

        setOnCameraTypeChangeListener(onCameraTypeChangeListener);
        setOnMediaActionStateChangeListener(onMediaActionStateChangeListener);
        setFlashModeSwitchListener(flashModeSwitchListener);
        setRecordButtonListener(recordButtonListener);

        done.setVisibility(GONE);
        imgGallery.setVisibility(GONE);
        //recyclerView.setVisibility(GONE);

        settingsButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_settings_white_24dp));
        settingsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (settingsClickListener != null) settingsClickListener.onSettingsClick();
            }
        });
        imgClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BaseSandriosActivity) context).onBackPressed();
            }
        });
        imgGallery.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsClickListener.onGalleryClick();
                //  SandriosCamera.getInterfacesDone().onOpenGallery();

            }
        });

        if (hasFlash)
            flashSwitchView.setVisibility(VISIBLE);
        else flashSwitchView.setVisibility(GONE);

        countDownTimer = new TimerTask(recordDurationText);
    }

    public void sendResultBack() {
        SandriosCamera.getInterfacesDone().onOpenGallery(VideoGalleryModelLib.getSelectedVideoInstance());
        ((BaseSandriosActivity) context).finish();
    }

    public void postInit(int mediatype, ArrayList<GalleryModel> path) {
        boolean videoFlag = false;
        if (mediatype != 0 && mediatype == CameraConfiguration.VIDEO) {
            if (imageGalleryAdapter != null) {
                imageGalleryAdapter = new ImageGalleryAdapter(context, CameraConfiguration.VIDEO, path);

            } else {
                imageGalleryAdapter = new ImageGalleryAdapter(context, -1, path);
            }
            recyclerView.setAdapter(imageGalleryAdapter);
            imageGalleryAdapter.setOnItemClickListener(new ImageGalleryAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    pickerItemClickListener.onItemClick(imageGalleryAdapter.getItem(position).getImageUri());
                }
            });
            if (imageGalleryAdapter.getAllItems() != null && imageGalleryAdapter.getAllItems().size() > 0) {

                try {
                    /*Glide.with(context)
                            .load(imageGalleryAdapter.getAllItems().get(0).getImageUri().getPath()) // Uri of the picture
                            .placeholder(R.drawable.ic_gallery)
                            .thumbnail(0.1f)
                            .into(imgGallery);*/
                    String pathhhh = imageGalleryAdapter.getAllItems().get(0).getImageUri().getPath();
                    videoRequestHandler = new VideoRequestHandler();
                    picassoInstance = new Picasso.Builder(context.getApplicationContext())
                            .addRequestHandler(videoRequestHandler)
                            .build();
                    picassoInstance.load(VideoRequestHandler.SCHEME_VIDEO + ":" + pathhhh).into(imgGallery);
                    /*Picasso.with(context)
                            .load(imageGalleryAdapter.getAllItems().get(0).getImageUri().getPath())
                            .into(imgGallery);*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                /*Picasso.with(context)
                        .load(R.drawable.ic_gallery_2)
                        .into(imgGallery);*/

            }
        }
    }

    public boolean getVideoMinimumDuration() {
        if (duration < 3) {
            return true;
        }
        return false;
    }

    public void hideGalleryVies() {
        done.setVisibility(GONE);
        imgGallery.setVisibility(GONE);
        //  recyclerView.setVisibility(GONE);
    }

    public void showGalleryVies() {
        //  done.setVisibility(VISIBLE);
        imgGallery.setVisibility(VISIBLE);
        //  recyclerView.setVisibility(VISIBLE);

    }

    public void lockGallery() {
        imgGallery.setEnabled(false);
        recordButton.setEnabled(false);

    }

    public void unLockGallery() {

        imgGallery.setEnabled(true);
        recordButton.setEnabled(true);
    }

    public void lockControls() {
        cameraSwitchView.setEnabled(false);
        recordButton.setEnabled(false);
        settingsButton.setEnabled(false);
        flashSwitchView.setEnabled(false);
        done.setVisibility(GONE);
        imgGallery.setVisibility(GONE);
        // recyclerView.setVisibility(GONE);
    }

    public void unLockControls() {
        cameraSwitchView.setEnabled(true);
        recordButton.setEnabled(true);
        settingsButton.setEnabled(true);
        flashSwitchView.setEnabled(true);
        // done.setVisibility(VISIBLE);
        imgGallery.setVisibility(VISIBLE);
        //  recyclerView.setVisibility(GONE);
    }

    public void showViews() {
        // cameraSwitchView.setVisibility(VISIBLE);
        recordButton.setVisibility(VISIBLE);
        flashSwitchView.setVisibility(VISIBLE);
        settingsButton.setVisibility(VISIBLE);
        //  mediaActionSwitchView.setVisibility(VISIBLE);
        // done.setVisibility(VISIBLE);
        imgGallery.setVisibility(VISIBLE);
        //recyclerView.setVisibility(GONE);
    }

    public void setup(int mediaAction) {
        this.mediaAction = mediaAction;
        if (CameraConfiguration.MEDIA_ACTION_VIDEO == mediaAction) {
            recordButton.setup(mediaAction, this);
            flashSwitchView.setVisibility(GONE);
        } else {
            recordButton.setup(CameraConfiguration.MEDIA_ACTION_VIDEO, this);
        }

        /*if (CameraConfiguration.MEDIA_ACTION_BOTH != mediaAction) {
            mediaActionSwitchView.setVisibility(GONE);
        } else mediaActionSwitchView.setVisibility(VISIBLE);*/
    }

    public void setMediaFilePath(final File mediaFile) {
        this.mediaFilePath = mediaFile.toString();
    }

    public void setMaxVideoFileSize(long maxVideoFileSize) {
        this.maxVideoFileSize = maxVideoFileSize;
    }

    public void setMaxVideoDuration(int maxVideoDurationInMillis) {
        if (maxVideoDurationInMillis > 0)
            countDownTimer = new CountdownTask(recordDurationText, maxVideoDurationInMillis);
        else countDownTimer = new TimerTask(recordDurationText);
    }

    public void setFlasMode(@FlashSwitchView.FlashMode int flashMode) {
        flashSwitchView.setFlashMode(flashMode);
    }

    public void setMediaActionState(@MediaActionSwitchView.MediaActionState int actionState) {
        if (mediaActionState == actionState) return;
        if (MediaActionSwitchView.ACTION_PHOTO == actionState) {
            recordButton.setMediaAction(CameraConfiguration.MEDIA_ACTION_PHOTO);
            if (hasFlash)
                flashSwitchView.setVisibility(VISIBLE);
        } else {
            recordButton.setMediaAction(CameraConfiguration.MEDIA_ACTION_VIDEO);
            flashSwitchView.setVisibility(GONE);
        }
        mediaActionState = actionState;
        mediaActionSwitchView.setMediaActionState(actionState);
    }

    public void setRecordButtonListener(RecordButton.RecordButtonListener recordButtonListener) {
        this.recordButtonListener = recordButtonListener;
    }

    public void rotateControls(int rotation) {
        cameraSwitchView.setRotation(rotation);
        mediaActionSwitchView.setRotation(rotation);
        flashSwitchView.setRotation(rotation);
        imgGallery.setRotation(rotation);
        recordDurationText.setRotation(rotation);
        recordSizeText.setRotation(rotation);
    }

    public void setOnMediaActionStateChangeListener(MediaActionSwitchView.OnMediaActionStateChangeListener onMediaActionStateChangeListener) {
        this.onMediaActionStateChangeListener = onMediaActionStateChangeListener;
    }

    public void setOnCameraTypeChangeListener(CameraSwitchView.OnCameraTypeChangeListener onCameraTypeChangeListener) {
        this.onCameraTypeChangeListener = onCameraTypeChangeListener;
        if (cameraSwitchView != null)
            cameraSwitchView.setOnCameraTypeChangeListener(this.onCameraTypeChangeListener);
    }

    public void setFlashModeSwitchListener(FlashSwitchView.FlashModeSwitchListener flashModeSwitchListener) {
        this.flashModeSwitchListener = flashModeSwitchListener;
        if (flashSwitchView != null)
            flashSwitchView.setFlashSwitchListener(this.flashModeSwitchListener);
    }

    public void setSettingsClickListener(SettingsClickListener settingsClickListener) {
        this.settingsClickListener = settingsClickListener;
    }

    public void setPickerItemClickListener(PickerItemClickListener pickerItemClickListener) {
        this.pickerItemClickListener = pickerItemClickListener;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onTakePhotoButtonPressed() {
        /*int count = 0;
        for (int i = 0; i < imageGalleryAdapter.getAllItems().size(); i++) {
            if (imageGalleryAdapter.getAllItems().get(i).getImageUri().toString().contains(".jpg")) {
                count++;
            }
        }
        if (count >= 5) {
            Toast.makeText(context, R.string.photo_video_limit, Toast.LENGTH_SHORT).show();
        } else if (recordButtonListener != null) {

            sound.play(MediaActionSound.SHUTTER_CLICK);
            recordButtonListener.onTakePhotoButtonPressed();
        }*/

        sound.play(MediaActionSound.SHUTTER_CLICK);
        recordButtonListener.onTakePhotoButtonPressed();

       /* File myDirectory = new File(Environment.getExternalStorageDirectory(), "MyDirectory");

        try {
            if(!myDirectory.exists()) {
                myDirectory.mkdirs();
            }else{

            }
        } catch (Exception e) {
            System.out.println("Exception iss: " +e.toString());
        }*/
    }

    public void onStartVideoRecord(final File mediaFile) {
        done.setVisibility(GONE);
        imgGallery.setVisibility(GONE);
        // recyclerView.setVisibility(GONE);
        setMediaFilePath(mediaFile);
        if (maxVideoFileSize > 0) {
            recordSizeText.setText("1Mb" + " / " + maxVideoFileSize / (1024 * 1024) + "Mb");
            recordSizeText.setVisibility(GONE);
            try {
                fileObserver = new FileObserver(this.mediaFilePath) {
                    private long lastUpdateSize = 0;

                    @Override
                    public void onEvent(int event, String path) {
                        final long fileSize = mediaFile.length() / (1024 * 1024);
                        if ((fileSize - lastUpdateSize) >= 1) {
                            lastUpdateSize = fileSize;
                            recordSizeText.post(new Runnable() {
                                @Override
                                public void run() {
                                    recordSizeText.setText(fileSize + "Mb" + " / " + maxVideoFileSize / (1024 * 1024) + "Mb");
                                }
                            });
                        }
                    }
                };
                fileObserver.startWatching();
            } catch (Exception e) {
                Log.e("FileObserver", "setMediaFilePath: ", e);
            }
        }
        countDownTimer.start();
    }

    public void allowRecord(boolean isAllowed) {
        recordButton.setEnabled(isAllowed);
    }

    public void showPicker(boolean isShown) {
        recyclerView.setVisibility(isShown ? VISIBLE : GONE);
    }

    public boolean showCrop() {
        return showImageCrop;
    }

    public void shouldShowCrop(boolean showImageCrop) {
        this.showImageCrop = showImageCrop;
    }

    public void allowCameraSwitching(boolean isAllowed) {
        //cameraSwitchView.setVisibility(isAllowed ? VISIBLE : GONE);
    }

    public void onStopVideoRecord() {
        if (fileObserver != null) {
            fileObserver.stopWatching();
        }
        countDownTimer.stop();
        recordSizeText.setVisibility(GONE);
        // cameraSwitchView.setVisibility(View.VISIBLE);
        settingsButton.setVisibility(VISIBLE);
        //  done.setVisibility(VISIBLE);
        // recyclerView.setVisibility(GONE);
        imgGallery.setVisibility(VISIBLE);

       /* if (CameraConfiguration.MEDIA_ACTION_BOTH != mediaAction) {
            mediaActionSwitchView.setVisibility(GONE);
        } else mediaActionSwitchView.setVisibility(VISIBLE);*/
        recordButton.setRecordState(RecordButton.READY_FOR_RECORD_STATE);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onStartRecordingButtonPressed() {
        /*for (int i = 0; i < imageGalleryAdapter.getAllItems().size(); i++) {
            if (imageGalleryAdapter.getAllItems().get(i).getImageUri().toString().contains(".mp4")) {
                Toast.makeText(context, R.string.photo_video_limit, Toast.LENGTH_SHORT).show();
                recordButton.setRecordState(RecordButton.READY_FOR_RECORD_STATE);
                return;
            }
        }*/
        cameraSwitchView.setVisibility(View.GONE);
        mediaActionSwitchView.setVisibility(GONE);
        settingsButton.setVisibility(GONE);
        //    recyclerView.setVisibility(GONE);
        done.setVisibility(GONE);
        imgGallery.setVisibility(GONE);
        //   recyclerView.setVisibility(GONE);

        if (recordButtonListener != null)
            sound.play(MediaActionSound.START_VIDEO_RECORDING);
        recordButtonListener.onStartRecordingButtonPressed();
    }

    @Override
    public void onStopRecordingButtonPressed() {
        onStopVideoRecord();
        if (recordButtonListener != null)
            recordButtonListener.onStopRecordingButtonPressed();
       /* if (duration < 2){
            Toast.makeText(context, "Video is too small. It should be more than 2 seconds", Toast.LENGTH_SHORT).show();
            return;
        }*/
        ((Activity) context).finish();

    }

    @Override
    public void onMediaActionChanged(int mediaActionState) {
        setMediaActionState(mediaActionState);
        if (onMediaActionStateChangeListener != null)
            onMediaActionStateChangeListener.onMediaActionChanged(this.mediaActionState);
    }

    public void setMediaType(int type) {
    }

    public void startRecording() {

        recordButton.performClick();
    }

    public int getCameraSwitch() {
        return cameraSwitch;
    }

    public void setCameraSwitch(int cameraSwitch) {
        this.cameraSwitch = cameraSwitch;
    }

    public interface SettingsClickListener {
        void onSettingsClick();

        void onGalleryClick();
    }

    public interface PickerItemClickListener {
        void onItemClick(Uri filePath);
    }

    abstract class TimerTaskBase {
        Handler handler = new Handler(Looper.getMainLooper());
        TextView timerView;
        boolean alive = false;
        long recordingTimeSeconds = 0;
        long recordingTimeMinutes = 0;

        TimerTaskBase(TextView timerView) {
            this.timerView = timerView;
        }

        abstract void stop();

        abstract void start();
    }

    private class CountdownTask extends TimerTaskBase implements Runnable {

        private int maxDurationMilliseconds = 0;

        CountdownTask(TextView timerView, int maxDurationMilliseconds) {
            super(timerView);
            this.maxDurationMilliseconds = maxDurationMilliseconds;
        }

        @Override
        public void run() {

            recordingTimeSeconds--;

            int millis = (int) recordingTimeSeconds * 1000;

            timerView.setText(
                    String.format(Locale.ENGLISH, "%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(millis),
                            TimeUnit.MILLISECONDS.toSeconds(millis) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
                    ));

            if (recordingTimeSeconds < 10) {
                timerView.setTextColor(Color.RED);
            }

            if (alive && recordingTimeSeconds > 0) handler.postDelayed(this, DateTimeUtils.SECOND);
        }

        @Override
        void stop() {
            timerView.setVisibility(View.INVISIBLE);
            alive = false;
        }

        @Override
        void start() {
            alive = true;
            recordingTimeSeconds = maxDurationMilliseconds / 1000;
            timerView.setTextColor(Color.WHITE);
            timerView.setText(
                    String.format(Locale.ENGLISH, "%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(maxDurationMilliseconds),
                            TimeUnit.MILLISECONDS.toSeconds(maxDurationMilliseconds) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(maxDurationMilliseconds))
                    ));
            timerView.setVisibility(View.VISIBLE);
            handler.postDelayed(this, DateTimeUtils.SECOND);
        }
    }

    private class TimerTask extends TimerTaskBase implements Runnable {

        TimerTask(TextView timerView) {
            super(timerView);
        }

        @Override
        public void run() {
            recordingTimeSeconds++;
            duration = recordingTimeSeconds;
            done.setVisibility(GONE);
            imgGallery.setVisibility(GONE);
            //  recyclerView.setVisibility(GONE);
            if (recordingTimeSeconds == Utils.VIDEO_DURATION) {
                recordingTimeSeconds = 0;
                recordingTimeMinutes++;
                onStopRecordingButtonPressed();//timer stop video after 1 minutes
            }
            timerView.setText(
                    String.format(Locale.ENGLISH, "%02d:%02d", recordingTimeMinutes, recordingTimeSeconds));
            if (alive) handler.postDelayed(this, DateTimeUtils.SECOND);
        }

        public void start() {
            alive = true;
            recordingTimeMinutes = 0;
            recordingTimeSeconds = 0;
            timerView.setText(
                    String.format(Locale.ENGLISH, "%02d:%02d", recordingTimeMinutes, recordingTimeSeconds));
            timerView.setVisibility(View.VISIBLE);
            handler.postDelayed(this, DateTimeUtils.SECOND);
        }

        public void stop() {
            timerView.setVisibility(View.INVISIBLE);
            alive = false;
        }
    }


}
