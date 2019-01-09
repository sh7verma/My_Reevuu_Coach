package com.sandrios.sandriosCamera.internal.gallery;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sandrios.sandriosCamera.R;
import com.sandrios.sandriosCamera.internal.manager.listener.OnGalleryClick;
import com.sandrios.sandriosCamera.internal.ui.model.GalleryModel;

import java.io.File;
import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.myViewHolder> {

	private boolean isActionMultiplePick;
	Context mContext;
	ArrayList<GalleryModel> list;
	public  static OnGalleryClick interfaces;
	int mWidth;

	public static void setInterface(OnGalleryClick inter){
		interfaces = inter;

	}


	public GalleryAdapter(Context mContext, ArrayList<GalleryModel> list, int mWid) {
		this.mContext = mContext;
		this.list = list;
		this.mWidth = mWid;
	}

	@Override
	public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(mContext).inflate(R.layout.gallery_item,parent,false);
		return new myViewHolder(view);
	}

	@Override
	public void onBindViewHolder(myViewHolder holder, final int position) {
		if (list.get(position).getCheck() == 1){
			holder.check.setVisibility(View.VISIBLE);
		}
		else {
			holder.check.setVisibility(View.INVISIBLE);
		}
		Uri uri = Uri.fromFile(new File(list.get(position).getPath()));
		Glide.with(mContext)
				.load(uri) // Uri of the picture
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.skipMemoryCache(false)
    			.into(holder.imageView);
		/*Picasso.with(mContext)
				.load(uri)
				.into(holder.imageView);*/
		holder.imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				interfaces.onclick(position);
			}
		});
	}

	@Override
	public int getItemCount() {
		return list.size();
	}


	public class myViewHolder extends RecyclerView.ViewHolder {
		ImageView imageView,check;
		RelativeLayout rlMain;

		public myViewHolder(View itemView) {
			super(itemView);
			imageView = (ImageView) itemView.findViewById(R.id.imgQueue);
			check = (ImageView) itemView.findViewById(R.id.imgQueueMultiSelected);
			rlMain = (RelativeLayout) itemView.findViewById(R.id.rl_main);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)(mWidth/3),(int)(mWidth/3));
			rlMain.setLayoutParams(params);

			RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams((int)(mWidth/15),(int)(mWidth/15));
			params1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM );
			params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			params1.setMargins(0,0,mWidth/24,mWidth/96);
			check.setLayoutParams(params1);
		}
	}


}
