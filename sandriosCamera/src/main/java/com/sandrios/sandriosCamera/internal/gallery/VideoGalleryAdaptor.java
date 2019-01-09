package com.sandrios.sandriosCamera.internal.gallery;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sandrios.sandriosCamera.R;
import com.sandrios.sandriosCamera.internal.manager.listener.OnGalleryClick;
import com.sandrios.sandriosCamera.internal.utils.VideoRequestHandler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VideoGalleryAdaptor extends RecyclerView.Adapter<VideoGalleryAdaptor.ViewHolder> {

    Context mContext;
    ArrayList<VideoGalleryModelLib> mdata;
    public static OnGalleryClick interfaces;

    VideoRequestHandler videoRequestHandler;
    Picasso picassoInstance;

    public VideoGalleryAdaptor(Context context, ArrayList<VideoGalleryModelLib> mdata, OnGalleryClick interfaces) {
        mContext = context;
        this.mdata = mdata;
        this.interfaces = interfaces;
    }

    @NonNull
    @Override
    public VideoGalleryAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.row_video_gallery, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoGalleryAdaptor.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaces.onclick(position);
            }
        });
        holder.tvVideoLength.setText(mdata.get(position).getVideoTime());
        holder.imgVideo.setImageBitmap(mdata.get(position).getVideothumbnail());
        if (mdata.get(position).isSelected()) {
            holder.laySelected.setVisibility(View.VISIBLE);
        } else {
            holder.laySelected.setVisibility(View.GONE);
        }

        /*String pathhhh = mdata.get(position);
        videoRequestHandler = new VideoRequestHandler();
        picassoInstance = new Picasso.Builder(mContext.getApplicationContext())
                .addRequestHandler(videoRequestHandler)
                .build();
        picassoInstance.load(VideoRequestHandler.SCHEME_VIDEO + ":" + pathhhh).into(holder.imgVideo);
*/
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgVideo;
        TextView tvVideoLength;
        View laySelected;

        public ViewHolder(View itemView) {
            super(itemView);
            imgVideo = (ImageView) itemView.findViewById(R.id.imgVideo);
            tvVideoLength = (TextView) itemView.findViewById(R.id.tvVideoLength);
            laySelected = itemView.findViewById(R.id.laySelected);

        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}