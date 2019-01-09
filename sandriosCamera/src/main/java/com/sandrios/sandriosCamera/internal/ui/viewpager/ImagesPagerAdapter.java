package com.sandrios.sandriosCamera.internal.ui.viewpager;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.sandrios.sandriosCamera.R;
import com.sandrios.sandriosCamera.internal.ui.view.ImageGalleryAdapter;

import java.util.ArrayList;
import java.util.List;


public class ImagesPagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    VideoView videoview;
    MediaController mediaController;
    private List<ImageGalleryAdapter.PickerTile> mResources = new ArrayList<>();

    public ImagesPagerAdapter(Context context, List<ImageGalleryAdapter.PickerTile> mResources) {
        mContext = context;
        this.mResources = mResources;
        mediaController = new MediaController(mContext);
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mResources.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_image_pager, container, false);

        ImageView image = (ImageView) itemView.findViewById(R.id.img_tutorial);
        videoview = (VideoView) itemView.findViewById(R.id.videoview);
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
         FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) videoview.getLayoutParams();
        params.width =  metrics.widthPixels;
        params.height = metrics.heightPixels;
        params.leftMargin = 0;
        videoview.setLayoutParams(params);
        if (mResources.get(position).getImageUri().toString().endsWith(".mp4")) {
            videoview.setVisibility(View.VISIBLE);
            image.setVisibility(View.GONE);
            videoview.setVideoURI(mResources.get(position).getImageUri());
            videoview.setMediaController(mediaController);


            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.BOTTOM;
            mediaController.setLayoutParams(lp);
            mediaController.setAnchorView(videoview);
//
//            ((ViewGroup) mediaController.getParent()).removeView(mediaController);

//            ( (itemView.findViewById(R.id.videoview))).addView(mediaController);

            /*if (videoview.isPlaying()) {
                videoview.stopPlayback();
            } else {
                videoview.start();
            }*/
        } else {
            mediaController.hide();
            videoview.setVisibility(View.GONE);
            image.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(mResources.get(position).getImageUri().toString())
                    .thumbnail(0.1f)
                    .dontAnimate()
                    .centerCrop()
                    .placeholder(ContextCompat.getDrawable(mContext, R.drawable.ic_gallery))
                    .error(ContextCompat.getDrawable(mContext, R.drawable.ic_error))
                    .into(image);

        }

        // imgTutorial.setImageResource(mResources[position]);
        container.addView(itemView);
        return itemView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}