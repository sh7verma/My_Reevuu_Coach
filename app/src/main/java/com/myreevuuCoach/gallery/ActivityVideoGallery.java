package com.myreevuuCoach.gallery;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.myreevuuCoach.R;
import com.myreevuuCoach.activities.AddVideoDetailActivity;
import com.myreevuuCoach.interfaces.AdapterClickInterface;

import java.util.ArrayList;
import java.util.HashSet;

public class ActivityVideoGallery extends AppCompatActivity implements AdapterClickInterface {
    private static final int UPLOAD_VIDEO = 101;

    ProgressBar videoLoader;
    View btnAction;
    View img_back;
    RecyclerView rvGallery;
    Context mContext;
    ArrayList<VideoGalleryModel> galleryVideoList = new ArrayList<>();
    int currentSelectedPosition = -1;
    Cursor cursor;
    private VideoGalleryAdapter videoGalleryAdaptor;
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_gallery);
        VideoGalleryModel.setSelectedVideoInstance(null);
        rvGallery = (RecyclerView) findViewById(R.id.rvGallery);
        rvGallery.setLayoutManager(new GridLayoutManager(ActivityVideoGallery.this, 3) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        btnAction = findViewById(R.id.btnAction);
        btnAction.setVisibility(View.GONE);
        img_back = findViewById(R.id.img_back);
        videoLoader = (ProgressBar) findViewById(R.id.videoLoader);
        mContext = this;
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSelectedPosition != -1) {
                    VideoGalleryModel videoGalleryModel = galleryVideoList.get(currentSelectedPosition);
                    /*VideoGalleryModelLib.setSelectedVideoInstance(galleryVideoList.get(currentSelectedPosition));
                    Intent i = new Intent(ActivityVideoGallery.this, ActivityAddVideoDetails.class);
                    startActivityForResult(i, UPLOAD_VIDEO);
*/

                    String realVideoTime = VideoUtils.getVideoTime(ActivityVideoGallery.this, videoGalleryModel.getVideoPath());
                    if (VideoUtils.checkVideoMinTimeValid((Long.parseLong(realVideoTime)))) {
                        if (VideoUtils.checkVideoMaxTimeValid((Long.parseLong(realVideoTime)))) {
                            VideoGalleryModel.setSelectedVideoInstance(videoGalleryModel);

                            Intent i = new Intent(ActivityVideoGallery.this, AddVideoDetailActivity.class);
                            startActivityForResult(i, UPLOAD_VIDEO);
                        } else {
                            Toast.makeText(ActivityVideoGallery.this, "Video is greater than 15 min", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ActivityVideoGallery.this, "Video is less than 5 sec", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(ActivityVideoGallery.this, "Please select video", Toast.LENGTH_SHORT).show();
                }


            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoGalleryModel.setSelectedVideoInstance(null);
                closeActivity();
            }
        });
        videoGalleryAdaptor = new VideoGalleryAdapter(mContext, galleryVideoList, this);
        rvGallery.setAdapter(videoGalleryAdaptor);
        new Thread() {
            public void run() {
                rvGallery.setLayoutManager(new GridLayoutManager(ActivityVideoGallery.this, 3));
                getAllMedia();
            }
        }.start();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null) {
            cursor.close();
        }
    }

    private void closeActivity() {
        Intent in = new Intent();
        setResult(RESULT_OK, in);
        finish();
    }

    //
    //GETVIDEO LISTING
    public void getAllMedia() {
        ColumnIndexCache cache = new ColumnIndexCache();
        HashSet<VideoGalleryModel> videoItemHashSet = new HashSet<>();
        // String[] projection = {MediaStore.Video.VideoColumns.DATA, MediaStore.Video.Media.DISPLAY_NAME};
        cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Video.Media.TITLE);
        try {
            cursor.moveToFirst();
            do {
                try {
                    String path = (cursor.getString(cache.getColumnIndex(cursor, MediaStore.Video.Media.DATA)));
                    Bitmap thumb = VideoUtils.getVideoThumb(path);
                    String videoTime = VideoUtils.convertMilliSecToTime(Long.parseLong(VideoUtils.getVideoTime(this, path)));
                    videoItemHashSet.add(new VideoGalleryModel(path, thumb, videoTime, false));
                    galleryVideoList.add(new VideoGalleryModel(path, thumb, videoTime, false));
                    notifyItem(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        notifyItem(true);
    }


    @Override
    public void onBackPressed() {
        closeActivity();
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

    @Override
    public void onItemClick(int position) {
        if (currentSelectedPosition != -1) {
            galleryVideoList.get(currentSelectedPosition).setSelected(false);
            videoGalleryAdaptor.notifyItemChanged(currentSelectedPosition);
        }

        currentSelectedPosition = position;
        btnAction.setVisibility(View.VISIBLE);
        galleryVideoList.get(position).setSelected(true);

        videoGalleryAdaptor.notifyItemChanged(currentSelectedPosition);


    }

    private void notifyItem(final boolean loaderDissmiss) {
        runOnUiThread(new Runnable() {
            public void run() {
                if (loaderDissmiss) {
                    videoLoader.setVisibility(View.GONE);
                }
                rvGallery.setVisibility(View.VISIBLE);
                if (galleryVideoList.size() > 0) {
                    videoGalleryAdaptor.notifyItemInserted(galleryVideoList.size());
                } else {
                    Toast.makeText(mContext, "No Video Found.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
