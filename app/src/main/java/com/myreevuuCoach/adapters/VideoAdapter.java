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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.myreevuuCoach.R;
import com.myreevuuCoach.interfaces.AdapterClickInterface;
import com.myreevuuCoach.models.FeedModel;
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

    AdapterClickInterface itemClick;
    Context mContext;
    ArrayList<FeedModel.Response> mData;

    public VideoAdapter(Context context, ArrayList<FeedModel.Response> arrayList) {
        mContext = context;
        mData = arrayList;
    }

    public void onAdapterItemClick(AdapterClickInterface click) {
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
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.txtSportTitle.setText(mData.get(position).getTitle());
        holder.txtSportOwner.setText("By " + mData.get(position).getFullname());
        holder.txtSportName.setText(mData.get(position).getSport());

        holder.bind(Uri.parse(mData.get(position).getUrl()));

        if (!TextUtils.isEmpty(mData.get(position).getThumbnail())) {
            new DownLoadImageTask(holder.playerView).execute(mData.get(position).getThumbnail());
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


        if (!holder.isPlaying()) {
            holder.thumb.animate().alpha(1).setDuration(500);
        } else {
            holder.thumb.animate().alpha(0).setDuration(500);
        }

        holder.cvFeeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.onItemClick(position);
            }
        });

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
//                notifyDataSetChanged();
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
            if (helper == null) {
                helper = new SimpleExoPlayerViewHelper(container, this, mediaUri);
            }
            helper.initialize(playbackInfo);
            helper.addPlayerEventListener(this);
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