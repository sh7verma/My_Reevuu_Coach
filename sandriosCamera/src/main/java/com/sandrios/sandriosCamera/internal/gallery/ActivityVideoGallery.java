package com.sandrios.sandriosCamera.internal.gallery;

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

import com.sandrios.sandriosCamera.R;
import com.sandrios.sandriosCamera.internal.manager.listener.OnGalleryClick;
import com.sandrios.sandriosCamera.internal.utils.Utils;

import java.util.ArrayList;
import java.util.HashSet;

public class ActivityVideoGallery extends AppCompatActivity implements OnGalleryClick {

    ProgressBar videoLoader;
    View btnAction;
    View img_back;
    RecyclerView rvGallery;
    ArrayList<VideoGalleryModelLib> galleryVideoList = new ArrayList<>();
    private VideoGalleryAdaptor videoGalleryAdaptor;

    int currentSelectedPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_gallery);
        VideoGalleryModelLib.setSelectedVideoInstance(null);
        rvGallery = (RecyclerView) findViewById(R.id.rvGallery);
        btnAction = findViewById(R.id.btnAction);
        img_back = findViewById(R.id.img_back);
        videoLoader = (ProgressBar) findViewById(R.id.videoLoader);

        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoGalleryModelLib.setSelectedVideoInstance(galleryVideoList.get(currentSelectedPosition));
                closeActivity("yes");

            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoGalleryModelLib.setSelectedVideoInstance(null);
                closeActivity("no");
            }
        });
        GalleryAdapter.setInterface(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rvGallery.setLayoutManager(new GridLayoutManager(ActivityVideoGallery.this, 3));
                getAllMedia();
            }

        }, 1000);

    }


    private void closeActivity(String closeCamera) {
        Intent in = new Intent();
        in.putExtra("close", closeCamera);
        setResult(RESULT_OK, in);
        finish();
    }

    //GETVIDEO LISTING
    public void getAllMedia() {
        HashSet<VideoGalleryModelLib> videoItemHashSet = new HashSet<>();
        //String[] projection = {MediaStore.Video.VideoColumns.DATA, MediaStore.Video.Media.DISPLAY_NAME};
        Cursor cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Video.Media.TITLE);
        try {
            cursor.moveToFirst();
            do {
                String path = (cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
                Bitmap thumb = Utils.getVideoThumb(path);
                String videoTime = Utils.convertMilliSecToTime(Long.parseLong(Utils.getVideoTime(this, path)));
                videoItemHashSet.add(new VideoGalleryModelLib(path, thumb, videoTime, false));

            } while (cursor.moveToNext());

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        galleryVideoList = new ArrayList<>(videoItemHashSet);
        videoGalleryAdaptor = new VideoGalleryAdaptor(ActivityVideoGallery.this, galleryVideoList, this);
        rvGallery.setAdapter(videoGalleryAdaptor);
        videoLoader.setVisibility(View.GONE);
        rvGallery.setVisibility(View.VISIBLE);
    }
    //GETVIDEO LISTING


    @Override
    public void onBackPressed() {
        closeActivity("no");
    }

    @Override
    public void onclick(int rowPosition) {
        currentSelectedPosition = rowPosition;
        for (int i = 0; i < galleryVideoList.size(); i++) {
            galleryVideoList.get(i).setSelected(false);
        }
        galleryVideoList.get(rowPosition).setSelected(true);
        videoGalleryAdaptor.notifyDataSetChanged();
    }


}
