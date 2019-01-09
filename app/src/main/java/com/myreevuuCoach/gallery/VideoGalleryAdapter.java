package com.myreevuuCoach.gallery;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.myreevuuCoach.R;
import com.myreevuuCoach.interfaces.AdapterClickInterface;

import java.util.ArrayList;

public class VideoGalleryAdapter extends RecyclerView.Adapter<VideoGalleryAdapter.ViewHolder> {

    public static AdapterClickInterface interfaces;
    Context mContext;
    ArrayList<VideoGalleryModel> mdata;


    public VideoGalleryAdapter(Context context, ArrayList<VideoGalleryModel> mdata,
                               AdapterClickInterface interfaces) {
        mContext = context;
        this.mdata = mdata;
        this.interfaces = interfaces;
    }


    @NonNull
    @Override
    public VideoGalleryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.row_video_gallery_new, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoGalleryAdapter.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaces.onItemClick(position);
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

    @Override
    public long getItemId(int position) {
        return position;
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


}