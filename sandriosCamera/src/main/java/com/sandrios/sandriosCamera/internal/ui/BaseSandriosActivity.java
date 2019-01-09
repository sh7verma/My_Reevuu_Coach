package com.sandrios.sandriosCamera.internal.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.RestrictTo;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.sandrios.sandriosCamera.R;
import com.sandrios.sandriosCamera.internal.SandriosCamera;
import com.sandrios.sandriosCamera.internal.configuration.CameraConfiguration;
import com.sandrios.sandriosCamera.internal.gallery.ActivityVideoGallery;
import com.sandrios.sandriosCamera.internal.gallery.VideoGalleryModelLib;
import com.sandrios.sandriosCamera.internal.manager.CameraOutputModel;
import com.sandrios.sandriosCamera.internal.ui.model.GalleryModel;
import com.sandrios.sandriosCamera.internal.ui.model.PhotoQualityOption;
import com.sandrios.sandriosCamera.internal.ui.model.VideoQualityOption;
import com.sandrios.sandriosCamera.internal.ui.preview.PreviewActivity;
import com.sandrios.sandriosCamera.internal.ui.view.CameraControlPanel;
import com.sandrios.sandriosCamera.internal.ui.view.CameraSwitchView;
import com.sandrios.sandriosCamera.internal.ui.view.FlashSwitchView;
import com.sandrios.sandriosCamera.internal.ui.view.ImageGalleryAdapter;
import com.sandrios.sandriosCamera.internal.ui.view.MediaActionSwitchView;
import com.sandrios.sandriosCamera.internal.ui.view.RecordButton;
import com.sandrios.sandriosCamera.internal.utils.ImageFilePath;
import com.sandrios.sandriosCamera.internal.utils.SandriosBus;
import com.sandrios.sandriosCamera.internal.utils.Size;
import com.sandrios.sandriosCamera.internal.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Arpit Gandhi on 12/1/16.
 */

@RestrictTo(RestrictTo.Scope.LIBRARY)
public abstract class BaseSandriosActivity<CameraId> extends SandriosCameraActivity<CameraId>
        implements
        RecordButton.RecordButtonListener,
        FlashSwitchView.FlashModeSwitchListener,
        MediaActionSwitchView.OnMediaActionStateChangeListener,
        CameraSwitchView.OnCameraTypeChangeListener, CameraControlPanel.SettingsClickListener, CameraControlPanel.PickerItemClickListener {

    public static final int ACTION_CONFIRM = 900;
    public static final int ACTION_RETAKE = 901;
    public static final int ACTION_CANCEL = 902;
    public static final int ACTION_DELETE = 903;
    public static final int PHOTO = 103;
    public static final int VIDEO = 104;
    protected static final int REQUEST_PREVIEW_CODE = 1001;
    @CameraConfiguration.MediaAction
    protected int mediaAction = CameraConfiguration.MEDIA_ACTION_BOTH;
    @CameraConfiguration.MediaQuality
    protected int mediaQuality = CameraConfiguration.MEDIA_QUALITY_HIGHEST;
    @CameraConfiguration.MediaQuality
    protected int passedMediaQuality = CameraConfiguration.MEDIA_QUALITY_HIGHEST;
    protected CharSequence[] videoQualities;
    protected CharSequence[] photoQualities;
    protected boolean enableImageCrop = false;
    protected int videoDuration = -1;
    protected long videoFileSize = -1;
    protected boolean autoRecord = false;
    protected int minimumVideoDuration = -1;
    protected boolean showPicker = true;
    protected int type;
    @MediaActionSwitchView.MediaActionState
    protected int currentMediaActionState;
    @CameraSwitchView.CameraType
    protected int currentCameraType = CameraSwitchView.CAMERA_TYPE_REAR;
    @SuppressLint("WrongConstant")
    @CameraConfiguration.MediaQuality
    protected int newQuality = -1;
    @CameraConfiguration.FlashMode
    protected int flashMode = CameraConfiguration.FLASH_MODE_AUTO;
    private CameraControlPanel cameraControlPanel;
    private AlertDialog settingsDialog;
    private int SELECT_IMAGE = 12;
    private int SELECT_VIDEO = 13;
    private int VIDEO_GALLERY = 14;

    public static ArrayList<GalleryModel> galleryPicsList = new ArrayList<>();
    public static ArrayList<ImageGalleryAdapter.PickerTile> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onProcessBundle(Bundle savedInstanceState) {
        super.onProcessBundle(savedInstanceState);

        extractConfiguration(getIntent().getExtras());
       /* currentMediaActionState = mediaAction == CameraConfiguration.MEDIA_ACTION_VIDEO ?
                MediaActionSwitchView.ACTION_VIDEO : MediaActionSwitchView.ACTION_PHOTO;*/

        currentMediaActionState = CameraConfiguration.MEDIA_ACTION_VIDEO;
    }

    @Override
    protected void onCameraControllerReady() {
        super.onCameraControllerReady();

        videoQualities = getVideoQualityOptions();
        photoQualities = getPhotoQualityOptions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //cameraControlPanel.postInit(type, galleryPicsList);//onResume
        cameraControlPanel.lockControls();
        cameraControlPanel.allowRecord(false);
//        cameraControlPanel.showPicker(showPicker);
        galleryPicsList = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraControlPanel.lockControls();
        cameraControlPanel.allowRecord(false);
    }

    private void extractConfiguration(Bundle bundle) {
        if (bundle != null) {
            if (bundle.containsKey(CameraConfiguration.Arguments.MEDIA_ACTION)) {
                switch (bundle.getInt(CameraConfiguration.Arguments.MEDIA_ACTION)) {
                    case CameraConfiguration.MEDIA_ACTION_PHOTO:
                        mediaAction = CameraConfiguration.MEDIA_ACTION_PHOTO;
                        break;
                    case CameraConfiguration.MEDIA_ACTION_VIDEO:
                        mediaAction = CameraConfiguration.MEDIA_ACTION_VIDEO;
                        break;
                    default:
                        mediaAction = CameraConfiguration.MEDIA_ACTION_BOTH;
                        break;
                }
            }

            if (bundle.containsKey(CameraConfiguration.Arguments.MEDIA_QUALITY)) {
                switch (bundle.getInt(CameraConfiguration.Arguments.MEDIA_QUALITY)) {
                    case CameraConfiguration.MEDIA_QUALITY_AUTO:
                        mediaQuality = CameraConfiguration.MEDIA_QUALITY_AUTO;
                        break;
                    case CameraConfiguration.MEDIA_QUALITY_HIGHEST:
                        mediaQuality = CameraConfiguration.MEDIA_QUALITY_HIGHEST;
                        break;
                    case CameraConfiguration.MEDIA_QUALITY_HIGH:
                        mediaQuality = CameraConfiguration.MEDIA_QUALITY_HIGH;
                        break;
                    case CameraConfiguration.MEDIA_QUALITY_MEDIUM:
                        mediaQuality = CameraConfiguration.MEDIA_QUALITY_MEDIUM;
                        break;
                    case CameraConfiguration.MEDIA_QUALITY_LOW:
                        mediaQuality = CameraConfiguration.MEDIA_QUALITY_LOW;
                        break;
                    case CameraConfiguration.MEDIA_QUALITY_LOWEST:
                        mediaQuality = CameraConfiguration.MEDIA_QUALITY_LOWEST;
                        break;
                    default:
                        mediaQuality = CameraConfiguration.MEDIA_QUALITY_MEDIUM;
                        break;
                }
                passedMediaQuality = mediaQuality;
            }

            if (bundle.containsKey(CameraConfiguration.Arguments.VIDEO_DURATION))
                videoDuration = bundle.getInt(CameraConfiguration.Arguments.VIDEO_DURATION);

            if (bundle.containsKey(CameraConfiguration.Arguments.VIDEO_FILE_SIZE))
                videoFileSize = bundle.getLong(CameraConfiguration.Arguments.VIDEO_FILE_SIZE);

            if (bundle.containsKey(CameraConfiguration.Arguments.MINIMUM_VIDEO_DURATION))
                minimumVideoDuration = bundle.getInt(CameraConfiguration.Arguments.MINIMUM_VIDEO_DURATION);

            if (bundle.containsKey(CameraConfiguration.Arguments.SHOW_PICKER))
                showPicker = bundle.getBoolean(CameraConfiguration.Arguments.SHOW_PICKER);

            if (bundle.containsKey(CameraConfiguration.Arguments.PICKER_TYPE))
                type = bundle.getInt(CameraConfiguration.Arguments.PICKER_TYPE);

            if (bundle.containsKey(CameraConfiguration.Arguments.ENABLE_CROP))
                enableImageCrop = bundle.getBoolean(CameraConfiguration.Arguments.ENABLE_CROP);

            if (bundle.containsKey(CameraConfiguration.Arguments.FLASH_MODE))
                switch (bundle.getInt(CameraConfiguration.Arguments.FLASH_MODE)) {
                    case CameraConfiguration.FLASH_MODE_AUTO:
                        flashMode = CameraConfiguration.FLASH_MODE_AUTO;
                        break;
                    case CameraConfiguration.FLASH_MODE_ON:
                        flashMode = CameraConfiguration.FLASH_MODE_ON;
                        break;
                    case CameraConfiguration.FLASH_MODE_OFF:
                        flashMode = CameraConfiguration.FLASH_MODE_OFF;
                        break;
                    default:
                        flashMode = CameraConfiguration.FLASH_MODE_AUTO;
                        break;
                }
            if (bundle.containsKey(CameraConfiguration.Arguments.AUTO_RECORD)) {
                if (mediaAction == CameraConfiguration.MEDIA_ACTION_VIDEO) {
                    autoRecord = bundle.getBoolean(CameraConfiguration.Arguments.AUTO_RECORD);
                }
            }
        }
    }

    @Override
    View getUserContentView(LayoutInflater layoutInflater, ViewGroup parent) {
        cameraControlPanel = (CameraControlPanel) layoutInflater.inflate(R.layout.user_control_layout, parent, false);
        // cameraControlPanel.postInit(type, null);

        if (cameraControlPanel != null) {
            cameraControlPanel.setup(getMediaAction());

            switch (flashMode) {
                case CameraConfiguration.FLASH_MODE_AUTO:
                    cameraControlPanel.setFlasMode(FlashSwitchView.FLASH_AUTO);
                    break;
                case CameraConfiguration.FLASH_MODE_ON:
                    cameraControlPanel.setFlasMode(FlashSwitchView.FLASH_ON);
                    break;
                case CameraConfiguration.FLASH_MODE_OFF:
                    cameraControlPanel.setFlasMode(FlashSwitchView.FLASH_OFF);
                    break;
            }

            cameraControlPanel.setRecordButtonListener(this);
            cameraControlPanel.setFlashModeSwitchListener(this);
            cameraControlPanel.setOnMediaActionStateChangeListener(this);
            cameraControlPanel.setOnCameraTypeChangeListener(this);
            cameraControlPanel.setMaxVideoDuration(getVideoDuration());
            cameraControlPanel.setMaxVideoFileSize(getVideoFileSize());
            cameraControlPanel.setSettingsClickListener(this);
            cameraControlPanel.setPickerItemClickListener(this);
            cameraControlPanel.shouldShowCrop(enableImageCrop);

            if (autoRecord) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cameraControlPanel.startRecording();
                    }
                }, 1500);
            }
        }
        return cameraControlPanel;
    }

    @Override
    public void onSettingsClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (currentMediaActionState == MediaActionSwitchView.ACTION_VIDEO) {
            builder.setSingleChoiceItems(videoQualities, getVideoOptionCheckedIndex(), getVideoOptionSelectedListener());
            if (getVideoFileSize() > 0)
                builder.setTitle(String.format(getString(R.string.settings_video_quality_title),
                        "(Max " + String.valueOf(getVideoFileSize() / (1024 * 1024) + " MB)")));
            else
                builder.setTitle(String.format(getString(R.string.settings_video_quality_title), ""));
        } else {
            builder.setSingleChoiceItems(photoQualities, getPhotoOptionCheckedIndex(), getPhotoOptionSelectedListener());
            builder.setTitle(R.string.settings_photo_quality_title);
        }

        builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (newQuality > 0 && newQuality != mediaQuality) {
                    mediaQuality = newQuality;
                    dialogInterface.dismiss();
                    cameraControlPanel.lockControls();
                    getCameraController().switchQuality();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        settingsDialog = builder.create();
        settingsDialog.show();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(settingsDialog.getWindow().getAttributes());
        layoutParams.width = Utils.convertDipToPixels(this, 350);
        layoutParams.height = Utils.convertDipToPixels(this, 350);
        settingsDialog.getWindow().setAttributes(layoutParams);
    }

    @Override
    public void onItemClick(Uri filePath) {
        int mimeType = getMimeType(getActivity(), filePath.toString());
        SandriosBus.getBus().send(new CameraOutputModel(mimeType, filePath.toString()));
        this.finish();
    }

    @Override
    public void onCameraTypeChanged(@CameraSwitchView.CameraType int cameraType) {
        if (currentCameraType == cameraType) return;
        currentCameraType = cameraType;

        cameraControlPanel.lockControls();
        cameraControlPanel.allowRecord(false);

        int cameraFace = cameraType == CameraSwitchView.CAMERA_TYPE_FRONT
                ? CameraConfiguration.CAMERA_FACE_FRONT : CameraConfiguration.CAMERA_FACE_REAR;

        getCameraController().switchCamera(cameraFace);
    }


    @Override
    public void onFlashModeChanged(@FlashSwitchView.FlashMode int mode) {
        switch (mode) {
            case FlashSwitchView.FLASH_AUTO:
                flashMode = CameraConfiguration.FLASH_MODE_AUTO;
                getCameraController().setFlashMode(CameraConfiguration.FLASH_MODE_AUTO);
                break;
            case FlashSwitchView.FLASH_ON:
                flashMode = CameraConfiguration.FLASH_MODE_ON;
                getCameraController().setFlashMode(CameraConfiguration.FLASH_MODE_ON);
                break;
            case FlashSwitchView.FLASH_OFF:
                flashMode = CameraConfiguration.FLASH_MODE_OFF;
                getCameraController().setFlashMode(CameraConfiguration.FLASH_MODE_OFF);
                break;
        }
    }

    @Override
    public void onMediaActionChanged(int mediaActionState) {
        if (currentMediaActionState == mediaActionState) return;
        currentMediaActionState = mediaActionState;
    }

    @Override
    public void onTakePhotoButtonPressed() {
        cameraControlPanel.lockGallery();
        getCameraController().takePhoto();
    }

    @Override
    public void onStartRecordingButtonPressed() {
        cameraControlPanel.hideGalleryVies();
        getCameraController().startVideoRecord();
    }

    @Override
    public void onStopRecordingButtonPressed() {
        cameraControlPanel.showGalleryVies();
        getCameraController().stopVideoRecord();
    }

    @Override
    protected void onScreenRotation(int degrees) {
        cameraControlPanel.rotateControls(degrees);
        rotateSettingsDialog(degrees);
    }

    @Override
    public int getMediaAction() {
        return mediaAction;
    }

    @Override
    public int getMediaQuality() {
        return mediaQuality;
    }

    @Override
    public int getVideoDuration() {
        return videoDuration;
    }

    @Override
    public long getVideoFileSize() {
        return videoFileSize;
    }

    @Override
    public int getFlashMode() {
        return flashMode;
    }

    @Override
    public int getMinimumVideoDuration() {
        return minimumVideoDuration / 1000;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void updateCameraPreview(Size size, View cameraPreview) {
        cameraControlPanel.unLockControls();
        cameraControlPanel.allowRecord(true);

        setCameraPreview(cameraPreview, size);
    }

    @Override
    public void updateUiForMediaAction(@CameraConfiguration.MediaAction int mediaAction) {

    }

    @Override
    public void updateCameraSwitcher(int numberOfCameras) {
        cameraControlPanel.allowCameraSwitching(numberOfCameras > 1);
    }

    @Override
    public void onPhotoTaken() {
//        startPreviewActivity();//onPhotoTaken
        savePhotoVideo(PHOTO);//onPhotoTaken
    }


    @Override
    public void onVideoRecordStop() {
        cameraControlPanel.allowRecord(false);
        cameraControlPanel.onStopVideoRecord();
//        startPreviewActivity();//onVideoRecordStop
        savePhotoVideo(VIDEO);//onVideoRecordStop

    }

    private void savePhotoVideo(int type) {
        if (type == VIDEO) {
            cameraControlPanel.allowRecord(true);
            cameraControlPanel.setup(CameraConfiguration.MEDIA_ACTION_VIDEO);
            cameraControlPanel.showViews();


            new SaveImage().execute(getCameraController().getOutputFile().toString());

//            setIntent(getCameraController().getOutputFile().toString());
//                int cameraFace = currentCameraType == CameraSwitchView.CAMERA_TYPE_FRONT
//                        ? CameraConfiguration.CAMERA_FACE_FRONT : CameraConfiguration.CAMERA_FACE_REAR;
//                int cameraFace = CameraConfiguration.CAMERA_FACE_REAR;
//                getCameraController().switchCamera(CameraSwitchView.getCurrentCameraType());
        } else {

            new SaveImage().execute(getCameraController().getOutputFile().toString());
//                new SaveImage.execute();
//                rotateImageIfRequired(getCameraController().getOutputFile().toString());
        }
    }

    @Override
    public void onVideoRecordStart(int width, int height) {
        cameraControlPanel.onStartVideoRecord(getCameraController().getOutputFile());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void releaseCameraPreview() {
        clearCameraPreview();
    }

    private void startPreviewActivity() {
        Intent intent = PreviewActivity.newIntent(this,
                getMediaAction(), getCameraController().getOutputFile().toString(), cameraControlPanel.showCrop());
        startActivityForResult(intent, REQUEST_PREVIEW_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PREVIEW_CODE) {
                if (PreviewActivity.isResultConfirm(data)) {
                    String path = PreviewActivity.getMediaFilePatch(data);
                    int mimeType = getMimeType(getActivity(), path);
                    SandriosBus.getBus().send(new CameraOutputModel(mimeType, path));
                    //  cameraControlPanel.postInit(type, null);
                    // this.finish();
                } else if (PreviewActivity.isResultCancel(data)) {
                    this.finish();
                } else if (PreviewActivity.isResultRetake(data)) {
                    //ignore, just proceed the camera
                }
            }
            if (requestCode == SELECT_IMAGE) {
                if (data != null) {
                    galleryPicsList = new ArrayList<>();
//                        String selectedImagePath = ImageFilePath.getPath(getApplicationContext(), selectedImage);
                    galleryPicsList = data.getParcelableArrayListExtra("galleryList");
                    Uri selectedImage = data.getData();
//                        cameraControlPanel.postInit(type, galleryPicsList);

                }
            } else if (requestCode == SELECT_VIDEO) {
                if (data != null) {
                    galleryPicsList = new ArrayList<>();
                    Uri selectedImage = data.getData();
                    String fileRealPath = ImageFilePath.getPath(this, data.getData());
                    File file = new File(fileRealPath);
                    final long fileSize = file.length() / (1024 * 1024);
                    if (fileSize < 150) {

                        GalleryModel galleryModel = new GalleryModel();
                        galleryModel.setPath(String.valueOf(ImageFilePath.getPath(this, selectedImage)));
                        galleryPicsList.add(galleryModel);
                    }
//                        cameraControlPanel.postInit(type, galleryPicsList);

                }
            } else if (requestCode == VIDEO_GALLERY) {
                String status = data.getExtras().getString("close");
                if (status.equalsIgnoreCase("yes")) {
                    cameraControlPanel.sendResultBack();
                }

            } else if (resultCode == Activity.RESULT_CANCELED) {

            }

        }
    }

    String save_video_thumbnail(String pathFile) {
        String saved_path = null;
        if (TextUtils.isEmpty(saved_path)) {
            try {
//				final Uri uri = Uri.parse(pathFile);
//				final String path = FileUtils.getPath(this, uri);
                File file = new File(pathFile);
                long length = file.length();
                if ((length / 1024) > (1024 * 110)) {//more than 100MB
//					Toast.makeText(getApplicationContext(), getResources().getString(R.string.file_size_exceeds), Toast.LENGTH_SHORT).show();
//					return;
                }
                File ff = new File(pathFile);
                Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(ff.getAbsolutePath(), MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
                saved_path = savePicture(bitmap, "ve");
            } catch (Exception e) {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
        return saved_path;
    }

    private String savePicture(Bitmap bm, String imgName) {
        OutputStream fOut = null;
        String pathd = null;
        String strDirectory = Environment.getExternalStorageDirectory().toString();

        File f = new File(strDirectory, imgName);
        try {
            fOut = new FileOutputStream(f);

            /**Compress image**/
            bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();

            /**Update image to gallery**/
            pathd = MediaStore.Images.Media.insertImage(getContentResolver(), f.getAbsolutePath(), f.getName(), f.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pathd;
    }

    public static int getMimeType(Context context, String path) {
        Uri uri = Uri.fromFile(new File(path));
        String extension;
        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(path);
        }
        String mimeTypeString
                = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        int mimeType = SandriosCamera.MediaType.PHOTO;
        if (mimeTypeString.toLowerCase().contains("video")) {
            mimeType = SandriosCamera.MediaType.VIDEO;
        }
        return mimeType;
    }

    private void rotateSettingsDialog(int degrees) {
        if (settingsDialog != null && settingsDialog.isShowing()) {
            ViewGroup dialogView = (ViewGroup) settingsDialog.getWindow().getDecorView();
            for (int i = 0; i < dialogView.getChildCount(); i++) {
                dialogView.getChildAt(i).setRotation(degrees);
            }
        }
    }

    protected abstract CharSequence[] getVideoQualityOptions();

    protected abstract CharSequence[] getPhotoQualityOptions();

    protected int getVideoOptionCheckedIndex() {
        int checkedIndex = -1;
        if (mediaQuality == CameraConfiguration.MEDIA_QUALITY_AUTO) checkedIndex = 0;
        else if (mediaQuality == CameraConfiguration.MEDIA_QUALITY_HIGH) checkedIndex = 1;
        else if (mediaQuality == CameraConfiguration.MEDIA_QUALITY_MEDIUM) checkedIndex = 2;
        else if (mediaQuality == CameraConfiguration.MEDIA_QUALITY_LOW) checkedIndex = 3;

        if (passedMediaQuality != CameraConfiguration.MEDIA_QUALITY_AUTO) checkedIndex--;

        return checkedIndex;
    }

    protected int getPhotoOptionCheckedIndex() {
        int checkedIndex = -1;
        if (mediaQuality == CameraConfiguration.MEDIA_QUALITY_HIGHEST) checkedIndex = 0;
        else if (mediaQuality == CameraConfiguration.MEDIA_QUALITY_HIGH) checkedIndex = 1;
        else if (mediaQuality == CameraConfiguration.MEDIA_QUALITY_MEDIUM) checkedIndex = 2;
        else if (mediaQuality == CameraConfiguration.MEDIA_QUALITY_LOWEST) checkedIndex = 3;
        return checkedIndex;
    }

    protected DialogInterface.OnClickListener getVideoOptionSelectedListener() {
        return new DialogInterface.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(DialogInterface dialogInterface, int index) {
                newQuality = ((VideoQualityOption) videoQualities[index]).getMediaQuality();
            }
        };
    }

    protected DialogInterface.OnClickListener getPhotoOptionSelectedListener() {
        return new DialogInterface.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(DialogInterface dialogInterface, int index) {
                newQuality = ((PhotoQualityOption) photoQualities[index]).getMediaQuality();
            }
        };
    }

    public class SaveImage extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            String path = (String) params[0];
            try {
                rotateImageIfRequired(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return path;
        }

        @Override
        protected void onPostExecute(Object object) {
            super.onPostExecute(object);
            String path = (String) object;
            if (path.endsWith("mp4") && cameraControlPanel.getVideoMinimumDuration()) {
                Toast.makeText(BaseSandriosActivity.this, "Video is too small. It should be more than 2 seconds", Toast.LENGTH_SHORT).show();

                File file = new File(getCameraController().getOutputFile().toString());
                if (file.exists()) {
                    file.delete();
                }
                // cameraControlPanel.postInit(type, null);
            } else {
                setIntent(path);
            }
            getCameraController().stopVideoRecord();
            getCameraController().releaseCamera();
            getCameraController().initialise();
            getCameraController().onResume();
            cameraControlPanel.unLockGallery();
        }
    }

    private Bitmap rotateImageIfRequired(String selectedImage) throws IOException {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(selectedImage, bmOptions);

        InputStream input = getContentResolver().openInputStream(Uri.fromFile(new File(selectedImage)));
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(selectedImage);

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(selectedImage, bitmap, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(selectedImage, bitmap, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(selectedImage, bitmap, 270);
            default:
                return bitmap;
        }

    }

    private Bitmap rotateImage(String selectedImage, Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
        return bitmap;
    }

    protected void setIntent(String filePath) {

        Intent resultIntent = new Intent();
        resultIntent.putExtra(PreviewActivity.RESPONSE_CODE_ARG, BaseSandriosActivity.ACTION_CONFIRM);
        resultIntent.putExtra(PreviewActivity.FILE_PATH_ARG, filePath);
        if (PreviewActivity.isResultConfirm(resultIntent)) {
            String path = PreviewActivity.getMediaFilePatch(resultIntent);
            int mimeType = getMimeType(getActivity(), path);
            SandriosBus.getBus().send(new CameraOutputModel(mimeType, path));
            Bitmap thumb = Utils.getVideoThumb(path);
            String videoTime = Utils.convertMilliSecToTime(Long.parseLong(Utils.getVideoTime(this, path)));
            VideoGalleryModelLib.setSelectedVideoInstance(new VideoGalleryModelLib(path, thumb, videoTime, false));
            SandriosCamera.getInterfacesDone().onDone(new VideoGalleryModelLib(path, thumb, videoTime, false));
            // cameraControlPanel.postInit(type, null);
            // this.finish();
        } else if (PreviewActivity.isResultCancel(resultIntent)) {
            this.finish();
        } else if (PreviewActivity.isResultRetake(resultIntent)) {
            //ignore, just proceed the camera
        }
    }


    @Override
    public void onGalleryClick() {
        Intent intent = new Intent(BaseSandriosActivity.this, ActivityVideoGallery.class);
        //intent.putParcelableArrayListExtra("list", imageGalleryAdapter.getAllItems());
        startActivityForResult(intent, VIDEO_GALLERY);
        /*AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setTitle("Select Option:-");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        arrayAdapter.add("Video");
        arrayAdapter.add("Photo");

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 1) {

                    list = imageGalleryAdapter.getAllItems();
                    int count = 0;
                    for (int i = 0; i < imageGalleryAdapter.getAllItems().size(); i++) {
                        if (imageGalleryAdapter.getAllItems().get(i).getImageUri().toString().contains(".jpg")) {
                            count++;
                        }
                    }
                    if (count >= 5) {
                        Toast.makeText(BaseSandriosActivity.this, R.string.photo_video_limit, Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(BaseSandriosActivity.this, GallleryActivity.class);
                        intent.putParcelableArrayListExtra("list", imageGalleryAdapter.getAllItems());
                        startActivityForResult(intent, SELECT_IMAGE);
                    }
                } else {
                    *//*for (int i = 0; i < imageGalleryAdapter.getAllItems().size(); i++) {
                        if (imageGalleryAdapter.getAllItems().get(i).getImageUri().toString().contains(".mp4")) {
                            Toast.makeText(BaseSandriosActivity.this, R.string.photo_video_limit, Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }*//*

                    File file = new File(com.sandrios.sandriosCamera.internal.ui.viewpager.Utils.path);
                    Intent intent = new Intent();
                    intent.setType("video/*");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, file); // set the image file name
                    intent.setAction(Intent.ACTION_GET_CONTENT);//
                    startActivityForResult(Intent.createChooser(intent, "Select Option"), SELECT_VIDEO);

                }
            }
        });
        builderSingle.show();*/

    }
}
