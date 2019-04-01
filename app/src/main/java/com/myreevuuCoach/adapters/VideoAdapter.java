package com.myreevuuCoach.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.myreevuuCoach.R;
import com.myreevuuCoach.firebase.FirebaseChatConstants;
import com.myreevuuCoach.interfaces.InterConst;
import com.myreevuuCoach.interfaces.VideoAdapterItemClick;
import com.myreevuuCoach.models.FeedModel;
import com.myreevuuCoach.utils.Constants;
import com.myreevuuCoach.utils.Utils;
import com.myreevuuCoach.video.VideoConst;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import im.ene.toro.ToroPlayer;
import im.ene.toro.ToroUtil;
import im.ene.toro.exoplayer.SimpleExoPlayerViewHelper;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.widget.Container;

/**
 * Created by dev on 22/11/18.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    VideoAdapterItemClick itemClick;
    Context mContext;
    ArrayList<FeedModel.Response> mData;
    Utils utils;
    int mWidth, mHeight;


    String TAG = "VIDEO_HEIGHT";
    int SCREEN_HEIGHT = 0;
    int SCREEN_WIDTH = 0;
    int DEC_HEIGHT = 0;
    int CONTAINER_HEIGHT = 0;
    int MAX_HEIGHT = 0; //CONTAINER + DEC+HEIGHT
    int MIN_HEIGHT = 0;
    int LANDSPACE_MIN_HEIGHT = 0;


    public VideoAdapter(Context context, ArrayList<FeedModel.Response> arrayList) {
        mContext = context;
        mData = arrayList;
        utils = new Utils(mContext);

        mWidth = utils.getInt("width", 0);
        mHeight = utils.getInt("height", 0);


        SCREEN_HEIGHT = mHeight;
        SCREEN_WIDTH = mWidth;

        DEC_HEIGHT = utils.dpToPx(mContext, 100);
        CONTAINER_HEIGHT = utils.getInt(InterConst.VIDEO_CONTAINER_HEIGHT, 0);
        MAX_HEIGHT = CONTAINER_HEIGHT - DEC_HEIGHT;
        MIN_HEIGHT = utils.dpToPx(mContext, 200);
        LANDSPACE_MIN_HEIGHT = 200;//utils.dpToPx(mContext, 250);


    }

    public void onAdapterItemClick(VideoAdapterItemClick click) {
        itemClick = click;
    }

    public void notifyAdapter(ArrayList<FeedModel.Response> list) {
        mData = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.item_feed, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int pos) {


        final int position = pos;
        int heightinPixel = 0;
        int videoOrientation = InterConst.VIDEO_ORIENTATION_VERTICLE;
        if (MIN_HEIGHT > 300) {
            MIN_HEIGHT = 300;
        }

        switch (mData.get(position).getPost_type()) {
            case 1:
                holder.imgSound.setVisibility(View.GONE);

                RelativeLayout.LayoutParams paramsImg = new
                        RelativeLayout.LayoutParams(SCREEN_WIDTH,
                        MIN_HEIGHT);
                paramsImg.height = utils.dpToPx(mContext, MIN_HEIGHT);
                holder.ll_view.setLayoutParams(paramsImg);


                Picasso.get()
                        .load(mData.get(position).getThumbnail())
                        .placeholder(R.mipmap.ic_ph)
                        .error(R.mipmap.ic_ph).into(holder.thumb);
                holder.imgSound.setVisibility(View.INVISIBLE);
                holder.thumb.setVisibility(View.VISIBLE);
                holder.playerView.setVisibility(View.GONE);
                holder.pgLoading.setVisibility(View.GONE);
                //holder.txtSportName.setVisibility(View.VISIBLE);
                break;
            case 2:
                holder.thumb.setVisibility(View.GONE);
                holder.playerView.setVisibility(View.VISIBLE);
                holder.pgLoading.setVisibility(View.VISIBLE);
                if (mData.get(position).getVideo_height() > mData.get(position).getVideo_width()) {
                    videoOrientation = InterConst.VIDEO_ORIENTATION_VERTICLE;
                } else {
                    videoOrientation = InterConst.VIDEO_ORIENTATION_LANDSCAPE;
                }
                switch (videoOrientation) {
                    case InterConst.VIDEO_ORIENTATION_VERTICLE:
                        heightinPixel = utils.dpToPx(mContext, mData.get(position).getVideo_height());
                        break;
                    case InterConst.VIDEO_ORIENTATION_LANDSCAPE:
                        heightinPixel = mData.get(position).getVideo_height();
                        break;
                }

                int weightinPixel = utils.dpToPx(mContext, mData.get(position).getVideo_width());
                if (mData.get(position).getUser_type() != FirebaseChatConstants.TYPE_ADMIN) {
                    int videoHeight = 0;
                    if (heightinPixel >= CONTAINER_HEIGHT) {
                        videoHeight = MAX_HEIGHT;
                    } else if (heightinPixel >= MAX_HEIGHT) {
                        videoHeight = MAX_HEIGHT;
                    } else {
                        videoHeight = heightinPixel;// - DEC_HEIGHT;
                    }

                    RelativeLayout.LayoutParams params;
                    if (videoHeight >= MIN_HEIGHT) {
                        params = new
                                RelativeLayout.LayoutParams(SCREEN_WIDTH,
                                videoHeight);
                        params.height = videoHeight;
                    } else {
                        if (videoOrientation == InterConst.VIDEO_ORIENTATION_VERTICLE) {
                            params = new
                                    RelativeLayout.LayoutParams(SCREEN_WIDTH,
                                    MIN_HEIGHT);
                            params.height = utils.dpToPx(mContext, MIN_HEIGHT);
                        } else {
                            params = new
                                    RelativeLayout.LayoutParams(SCREEN_WIDTH,
                                    LANDSPACE_MIN_HEIGHT);
                            params.height = LANDSPACE_MIN_HEIGHT;
                        }

                    }

                    holder.ll_view.setLayoutParams(params);
                } else {
                    switch (videoOrientation) {
                        case InterConst.VIDEO_ORIENTATION_VERTICLE:
                            heightinPixel = utils.dpToPx(mContext, mData.get(position).getVideo_height());
                            break;
                        case InterConst.VIDEO_ORIENTATION_LANDSCAPE:
                            heightinPixel = mData.get(position).getVideo_height();
                            break;
                    }
                    int videoHeight = 0;
                    if (heightinPixel == 0) {
                        videoHeight = LANDSPACE_MIN_HEIGHT;
                    } else if (heightinPixel >= CONTAINER_HEIGHT) {
                        videoHeight = MAX_HEIGHT;
                    } else if (heightinPixel >= MAX_HEIGHT) {
                        videoHeight = MAX_HEIGHT;
                    } else {
                        videoHeight = heightinPixel;// - DEC_HEIGHT;
                    }
                    RelativeLayout.LayoutParams params = new
                            RelativeLayout.LayoutParams(SCREEN_WIDTH,
                            videoHeight);
                    params.height = utils.dpToPx(mContext, videoHeight);
                    holder.ll_view.setLayoutParams(params);
                }

                break;
        }
























        holder.txtSportTitle.setText(mData.get(position).getTitle());
        holder.txtSportOwner.setText("By " + mData.get(position).getFullname());
        holder.txtSportName.setText(mData.get(position).getSport());
        if (mData.get(position).getLiked() == 1) {
            holder.imgLike.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_like_selected));
        } else {
            holder.imgLike.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_like));
        }

        if (!mData.get(position).getThumbnail().equalsIgnoreCase("")) {
            Picasso.get()
                    .load(mData.get(position).getThumbnail())
                    .placeholder(R.mipmap.ic_ph)
                    .error(R.mipmap.ic_ph).into(holder.thumb);
        } else {
            Picasso.get()
                    .load(R.mipmap.ic_ph)
                    .placeholder(R.mipmap.ic_ph)
                    .error(R.mipmap.ic_ph).into(holder.thumb);
        }

        if (mData.get(position).getPost_type() == InterConst.POST_TYPE_VIDEO) {
            holder.imgSound.setVisibility(View.VISIBLE);
            holder.bind(Uri.parse(mData.get(position).getUrl()));
//            if (mData.get(position).getVideo_height() > 400) {
//                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
//                        , 400);
//                holder.playerView.setLayoutParams(lp);
//            } else {
//                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
//                        , ViewGroup.LayoutParams.WRAP_CONTENT);
//                holder.playerView.setLayoutParams(lp);
//            }

            if (!TextUtils.isEmpty(mData.get(position).getThumbnail())) {
                new DownLoadImageTask(holder.playerView).execute(mData.get(position).getThumbnail());
            }

            if (!holder.isPlaying()) {
                holder.thumb.animate().alpha(1).setDuration(500);
            } else {
                holder.thumb.animate().alpha(0).setDuration(500);
            }

            if (VideoConst.IS_MUSIC_ENABLE) {
                holder.imgSound.setImageResource(R.mipmap.ic_unmute);
            } else {
                holder.imgSound.setImageResource(R.mipmap.ic_mute);
            }

            holder.imgSound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.playerView.getPlayer().getVolume() == 1) {
                        VideoConst.IS_MUSIC_ENABLE = false;
                        holder.playerView.getPlayer().setVolume(0);
                        holder.imgSound.setImageResource(R.mipmap.ic_mute);
                    } else {
                        VideoConst.IS_MUSIC_ENABLE = true;
                        holder.playerView.getPlayer().setVolume(1);
                        holder.imgSound.setImageResource(R.mipmap.ic_unmute);
                    }
                }
            });
        } else {
            holder.imgSound.setVisibility(View.GONE);
        }


        switch (mData.get(position).getUser_type()) {
            case FirebaseChatConstants.TYPE_COACH:
                holder.llImage.setVisibility(View.GONE);
                holder.txtPostType.setText(R.string.coach_video);
                holder.txtSportName.setText(mData.get(position).getSport());
                holder.txtSportOwner.setText("By " + mData.get(position).getFullname());
                break;
            case FirebaseChatConstants.TYPE_ATHLETE:
                holder.llImage.setVisibility(View.GONE);
                holder.txtPostType.setText(R.string.athlete_video);
                holder.txtSportName.setText(mData.get(position).getSport());
                holder.txtSportOwner.setText("By " + mData.get(position).getFullname());

                break;
            case FirebaseChatConstants.TYPE_ADMIN:
                holder.llImage.setVisibility(View.VISIBLE);
                holder.imgSound.setVisibility(View.GONE);
                holder.llImage.setVisibility(View.VISIBLE);
                holder.txtLikeCount.setText(String.valueOf(mData.get(position).getLikes_count()));
                holder.txtComment.setText(String.valueOf(mData.get(position).getComments_count()));

                holder.txtSportName.setVisibility(View.GONE);

                try {
                    holder.txtSportOwner.setText(Constants.Companion.displayDateTime(mData.get(position).getCreated_at()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                switch (mData.get(position).getPost_type()) {
                    case 1:
                        holder.txtPostType.setText(mData.get(position).getSport());
                        break;
                    case 2:
                        holder.txtPostType.setText(R.string.admin_video);
                        holder.txtSportName.setText(mData.get(position).getSport());
                        break;
                }
                break;
        }
        holder.cvFeeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.onItemClick(position);
            }
        });

        holder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.onItemShareClick(position);
            }
        });
        holder.imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.onItemLikeClick(position);
            }
        });

        holder.imgComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.onItemCommentClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private Bitmap blur(Bitmap image) {
        if (null == image) return null;
        Bitmap outputBitmap = Bitmap.createBitmap(image);
        final RenderScript renderScript = RenderScript.create(mContext);
        Allocation tmpIn = Allocation.createFromBitmap(renderScript, image);
        Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        theIntrinsic.setRadius(25f);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements ToroPlayer, ToroPlayer.EventListener {

        @BindView(R.id.cvFeed)
        CardView cvFeeds;
        @BindView(R.id.player)
        SimpleExoPlayerView playerView;
        @BindView(R.id.imgSound)
        ImageView imgSound;
        @BindView(R.id.img_thumb)
        ImageView thumb;
        @BindView(R.id.pgLoading)
        ProgressBar pgLoading;

        @BindView(R.id.txtSportTitle)
        TextView txtSportTitle;
        @BindView(R.id.txtSportOwner)
        TextView txtSportOwner;
        @BindView(R.id.txtSportName)
        TextView txtSportName;

        @BindView(R.id.ll_view)
        RelativeLayout ll_view;


        //Image
        @BindView(R.id.llImage)
        LinearLayout llImage;

        @BindView(R.id.imgLike)
        ImageView imgLike;
        @BindView(R.id.txtLikeCount)
        TextView txtLikeCount;
        @BindView(R.id.imgComment)
        ImageView imgComment;
        @BindView(R.id.txtComment)
        TextView txtComment;
        @BindView(R.id.imgShare)
        ImageView imgShare;
        @BindView(R.id.txtPostType)
        TextView txtPostType;


        SimpleExoPlayerViewHelper helper;
        Uri mediaUri;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @NonNull
        @Override
        public View getPlayerView() {
            return playerView;
        }

        @NonNull
        @Override
        public PlaybackInfo getCurrentPlaybackInfo() {
            return helper != null ? helper.getLatestPlaybackInfo() : new PlaybackInfo();
        }

        @Override
        public void initialize(@NonNull Container container, PlaybackInfo playbackInfo) {
            if (mediaUri != null) {
                if (helper == null) {
                    helper = new SimpleExoPlayerViewHelper(container, this, mediaUri);
                }
                helper.initialize(playbackInfo);
                helper.addPlayerEventListener(this);

            }
        }

        @Override
        public void play() {
            if (helper != null) {
                helper.play();
                thumb.animate().alpha(0).setDuration(500);

                if (VideoConst.IS_MUSIC_ENABLE) {
                    playerView.getPlayer().setVolume(1);
                    imgSound.setImageResource(R.mipmap.ic_unmute);
                } else {
                    playerView.getPlayer().setVolume(0);
                    imgSound.setImageResource(R.mipmap.ic_mute);
                }

            }
        }

        @Override
        public void pause() {
            if (helper != null) helper.pause();
            if (helper != null) {
                thumb.animate().alpha(1).setDuration(500);
            }
        }

        @Override
        public boolean isPlaying() {
            return helper != null && helper.isPlaying();
        }

        @Override
        public void release() {
            if (helper != null) {
                helper.release();
                helper = null;
            }
        }

        @Override
        public boolean wantsToPlay() {
            return ToroUtil.visibleAreaOffset(this, itemView.getParent()) >= 0.85;
        }

        @Override
        public int getPlayerOrder() {
            return getAdapterPosition();
        }

        void bind(Uri media) {
            this.mediaUri = media;
        }

        @Override
        public void onBuffering() {
            pgLoading.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPlaying() {
            pgLoading.setVisibility(View.GONE);
        }

        @Override
        public void onPaused() {
            pgLoading.setVisibility(View.GONE);
        }

        @Override
        public void onCompleted(Container container, ToroPlayer player) {
            player.play();
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {
        SimpleExoPlayerView imageView;

        public DownLoadImageTask(SimpleExoPlayerView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try {
                InputStream is = new URL(urlOfImage).openStream();

                logo = BitmapFactory.decodeStream(is);
            } catch (Exception e) { // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        protected void onPostExecute(Bitmap result) {
            Bitmap blurResult = blur(result);
            BitmapDrawable background = null;
            background = new BitmapDrawable(mContext.getResources(), blurResult);
            imageView.setBackground(background);
        }
    }

}