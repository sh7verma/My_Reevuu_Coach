package com.myreevuuCoach.activities;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.myreevuuCoach.R;
import com.myreevuuCoach.fragments.HomeFragment;
import com.myreevuuCoach.fragments.ProfileVideoFragment;
import com.myreevuuCoach.gallery.VideoGalleryModel;
import com.myreevuuCoach.gallery.VideoUtils;
import com.myreevuuCoach.interfaces.InterConst;
import com.myreevuuCoach.models.VideoModel;
import com.myreevuuCoach.network.RetrofitClient;
import com.myreevuuCoach.utils.ProgressRequestBody;
import com.vincent.videocompressor.Util;
import com.vincent.videocompressor.VideoCompress;

import java.io.File;
import java.util.Calendar;

import az.plainpie.PieView;
import az.plainpie.animation.PieAngleAnimation;
import butterknife.BindView;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dev on 21/11/18.
 */

public class UploadVideoActivity extends BaseActivity implements ProgressRequestBody.UploadCallbacks {

    @BindView(R.id.pieView)
    PieView pieView;
    @BindView(R.id.txt_progress)
    TextView txtProgress;
    @BindView(R.id.txtDone)
    TextView txtDone;
    @BindView(R.id.imgUploaded)
    ImageView imgUploaded;

    private long startTime, endTime;


    @Override
    protected int getContentView() {
        return R.layout.activity_upload_video;
    }

    @Override
    protected void onCreateStuff() {

        PieAngleAnimation animation = new PieAngleAnimation(pieView);
        pieView.startAnimation(animation);
        pieView.setPieInnerPadding(40);

        pieView.setMainBackgroundColor(getResources().getColor(R.color.progressGrey));
        pieView.setPercentageBackgroundColor(getResources().getColor(R.color.colorPrimary));
        pieView.setInnerBackgroundColor(getResources().getColor(R.color.black));

        compressVideo(VideoGalleryModel.getSelectedVideoInstance().getVideoPath());
    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void initListener() {
        txtDone.setOnClickListener(this);
    }

    @Override
    protected Context getContext() {
        return this;
    }

    @Override
    public void onBackPressed() {
        if (pieView.getPercentage() == 100) {
            ProfileVideoFragment.getInstance().onVideoAdded();
            HomeFragment.getInstance().onCallResume();

            ProfileVideoFragment.getInstance().onCallPause();
            HomeFragment.getInstance().onCallPause();

            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
        } else {
            super.onBackPressed();
        }
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtDone:
                if (txtDone.getText().equals(getString(R.string.retry))) {
                    finish();
                } else {
                    ProfileVideoFragment.getInstance().onVideoAdded();
                    HomeFragment.getInstance().onCallResume();

                    ProfileVideoFragment.getInstance().onCallPause();
                    HomeFragment.getInstance().onCallPause();

                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
        }
    }


    void uploadVideoApi(String filePath, int originalWidth, int originalHeight) {

        pieView.setPercentage(0);
        txtProgress.setText(getString(R.string.please_wait_your_video_is_still_uploading));

        File file = new File(filePath);

        ProgressRequestBody fileBody = new ProgressRequestBody(file, "video", this);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("video", file.getName(), fileBody);

        Call<VideoModel> call = RetrofitClient.getInstance().uploadVideo(
                createPartFromString(mUtils.getString(InterConst.ACCESS_TOKEN, "")),
                filePart,
                createImageFilePart(imageToFile(VideoGalleryModel.getSelectedVideoInstance().getVideothumbnail(), Calendar.getInstance().getTime().toString()), "thumbnail"),
                createPartFromString(getIntent().getStringExtra("title")),
                createPartFromString(getIntent().getStringExtra("description")),
                createPartFromString(InterConst.PRIVACY_PUBLIC),
                createPartFromString(String.valueOf(mSigUpModel.getResponse().getSport_info().getSport().getId())),
                createPartFromString(String.valueOf(originalWidth)),
                createPartFromString(String.valueOf(originalHeight)),
                createPartFromString(""),
                createPartFromString(getIntent().getStringExtra("expertise")));

        call.enqueue(new Callback<VideoModel>() {
            @Override
            public void onResponse(Call<VideoModel> call, Response<VideoModel> response) {
                Log.e("onResponse", String.valueOf(response));
                if (response.body().getResponse() != null) {
                    pieView.setPercentage(100);
                    imgUploaded.setVisibility(View.VISIBLE);
                    pieView.setInnerTextVisibility(View.GONE);
                    txtProgress.setText(getString(R.string.video_is_uploaded_successfully));
                    txtDone.setVisibility(View.VISIBLE);
                } else {
                    if (response.body().getError().getCode() == InterConst.INVALID_ACCESS) {
                        moveToSplash();
                    } else {
                        showAlertSnackBar(pieView, response.body().getError().getMessage());
                        retry();
                    }
                }
            }

            @Override
            public void onFailure(Call<VideoModel> call, Throwable t) {
                Log.e("onFailure", String.valueOf(t));
                showAlertSnackBar(pieView, getString(R.string.wrong));
                retry();
            }

        });

    }

    private void retry() {
        txtDone.setVisibility(View.VISIBLE);
        txtProgress.setText(R.string.we_could_not_connect_to_the_server);
        txtDone.setText(getString(R.string.retry));
    }

    @Override
    public void onProgressUpdate(int percentage) {
        pieView.setPercentage(percentage);
        Log.e("percentage", String.valueOf(percentage));
    }

    @Override
    public void onError() {
        Log.e("percentage", String.valueOf("onError"));
        finish();
    }

    @Override
    public void onFinish() {
        Log.e("percentage", String.valueOf("onFinish"));
    }


    void compressVideo(String videoPath) {
        txtProgress.setText(R.string.compressing_video);

        VideoUtils.isfolderExists();
        File f = new File(Environment.getExternalStorageDirectory() +
                File.separator + InterConst.APP_NAME + File.separator + "VID_" + Calendar.getInstance().getTimeInMillis() + ".mp4");
        final String destPath = f.getAbsolutePath();

        VideoCompress.compressVideoMedium(videoPath, destPath, new VideoCompress.CompressListener() {
            @Override
            public void onStart() {
                pieView.setPercentage(0);
                startTime = System.currentTimeMillis();
                Util.writeFile(mContext, "Start at: " + Calendar.getInstance().getTimeInMillis() + "\n");
            }

            @Override
            public void onSuccess() {
                pieView.setPercentage(100);

                Util.writeFile(mContext, "End at: " + Calendar.getInstance().getTimeInMillis() + "\n");
                Util.writeFile(mContext, "Total: " + ((endTime - startTime) / 1000) + "s" + "\n");
                Util.writeFile(mContext);

                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(destPath);

                String width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
                String height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
                String rotation = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
                long duration = Long.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION))
                        * 1000;

                int rotationValue = Integer.valueOf(rotation);
                int originalWidth = Integer.valueOf(width);
                int originalHeight = Integer.valueOf(height);

                if (connectedToInternet(txtDone)) {
                    uploadVideoApi(destPath, originalWidth, originalHeight);
                }

            }

            @Override
            public void onFail() {
                endTime = System.currentTimeMillis();
                Util.writeFile(mContext, "Failed Compress!!!" + Calendar.getInstance().getTimeInMillis());

                retry();
            }

            @Override
            public void onProgress(float percent) {
                pieView.setPercentage(percent);
            }
        });

    }


//    private void compressVideo(String path) {
//
//        txtProgress.setText(R.string.compressing_video);
//        pieView.setPercentage(0);
//
//        VideoCompressor.compress(this, path, new VideoCompressListener() {
//            @Override
//            public void onSuccess(final String outputFile, String filename, long duration) {
//                Worker.postMain(new Runnable() {
//                    @Override
//                    public void run() {
//                        SGLog.e("video compress success:" + outputFile);
//                        pieView.setPercentage(100);
//                        if (connectedToInternet(txtDone)) {
//                            uploadVideoApi(outputFile);
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onFail(final String reason) {
//                Worker.postMain(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(mContext, "video compress failed:" + reason, Toast.LENGTH_SHORT).show();
//                        Log.e("compress failed:", reason);
//                        retry();
//                    }
//                });
//            }
//
//            @Override
//            public void onProgress(final int progress) {
//                Worker.postMain(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.e("compres: progress", String.valueOf(progress));
//                        pieView.setPercentage(progress);
//                    }
//                });
//            }
//        });
//    }
}
