package com.sandrios.sandriosCamera.internal.ui.viewpager;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.sandrios.sandriosCamera.R;
import com.sandrios.sandriosCamera.internal.ui.view.ImageGalleryAdapter;

import java.io.File;
import java.util.List;

/**
 * Created by dev on 30/11/17.
 */

public class ActivityImagesViewpager extends AppCompatActivity {

    protected int type;
    ViewPager viewPager;
    List<ImageGalleryAdapter.PickerTile> mResources;
    ImagesPagerAdapter pagerAdapter;
    int pos;
    private ImageView imgDelete;
    private int mWidth;
    String FOLDERNAME = "MyReevuu/Athlete";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_viewpager);
        DisplayMetrics display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
        mWidth = display.widthPixels;
        initUI();
        pos = getIntent().getIntExtra("position", 0);
        if (getIntent().hasExtra("type")){
            int type = getIntent().getIntExtra("type",0);
            if (type == 32){
                imgDelete.setVisibility(View.INVISIBLE);
            }
        }
        mResources = getIntent().getParcelableArrayListExtra("ImagesArr");
        pagerAdapter = new ImagesPagerAdapter(this, mResources);

        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(0);
        viewPager.setCurrentItem(pos);

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mResources.size()>0){
                    String path = mResources.get(viewPager.getCurrentItem()).getImageUri().getPath().replace("file:///storage/emulated/0/"+FOLDERNAME, "");
                    File file = new File(Utils.path + path);
                    deleteFiles(mResources.get(viewPager.getCurrentItem()).getImageUri().getPath());
                   /* if (file.exists()) {
                        if (file.delete()) {
                            Log.e("-->", "file Deleted :");
                            callBroadCast();
                        } else {
                            Log.e("-->", "file not Deleted :");
                        }
                    }*/
                    file.delete();
                    mResources.remove(viewPager.getCurrentItem());
                    pagerAdapter = new ImagesPagerAdapter(ActivityImagesViewpager.this, mResources);
                    viewPager.setAdapter(pagerAdapter);
                    checkItems();
                }

            }
        });


    }
    private void deleteFiles(String pathStr) {

        String path = Environment.getExternalStorageDirectory() + FOLDERNAME;
        File dir = new File(path);
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null){
                for (int i = 0; i < children.length; i++) {
                    File file  = new File(dir,children[i]);
                    if (pathStr.contains(file.getAbsolutePath())){
                        file.delete();
                    }
                }
            }
        }
    }
    private void checkItems() {
        if (mResources.size()<1){
            finish();
        }
    }

    private void initUI() {

        imgDelete = (ImageView) findViewById(R.id.img_delete);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
    }

    public void callBroadCast() {
        if (Build.VERSION.SDK_INT >= 14) {
            Log.e("-->", " >= 14");
            MediaScannerConnection.scanFile(this, new String[]{"/data/user/0/com.sandrios.sandriosCamera.sample/files/"}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                    Log.e("ExternalStorage", "Scanned " + path + ":");
                    Log.e("ExternalStorage", "-> uri=" + uri);
                }
            });
        } else {
            Log.e("-->", " < 14");
            LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" + "/data/user/0/com.sandrios.sandriosCamera.sample/files/")));
        }
    }

    @Override
    public void onBackPressed() {

        finish();
        /*SandriosCamera
                .with(this)
                .setShowPicker(true)
                .setShowPickerType(CameraConfiguration.VIDEO)
                .setVideoFileSize(20)
                .setMediaAction(CameraConfiguration.MEDIA_ACTION_BOTH)
                .enableImageCropping(true)
                .launchCamera(new SandriosCamera.CameraCallback() {
                    @Override
                    public void onComplete(CameraOutputModel model) {
                        Log.e("File", "" + model.getPath());
                        Log.e("Type", "" + model.getType());
                       // Toast.makeText(getApplicationContext(), "Media captured.", Toast.LENGTH_SHORT).show();
                    }
                });*/
    }
}
