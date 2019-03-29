package com.myreevuuCoach.adapters

import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.allattentionhere.autoplayvideos.AAH_CustomViewHolder
import com.allattentionhere.autoplayvideos.AAH_VideosAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.myreevuuCoach.R
import com.myreevuuCoach.interfaces.AdapterClickInterface
import com.myreevuuCoach.models.FeedModel
import kotlinx.android.synthetic.main.item_feed.view.*


class FeedAdapter(var feedArray: List<FeedModel.Response>, var activity: FragmentActivity) : AAH_VideosAdapter() {

    var isMuted: Boolean = false
    private var itemClick: AdapterClickInterface? = null

    fun onAdapterItemClick(open: AdapterClickInterface) {
        itemClick = open
    }

    fun notifyAdapter(feed: List<FeedModel.Response>) {
        feedArray = feed
        notifyDataSetChanged()
    }

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val vhItem: MyViewHolder
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_feed, parent,
                false)
        vhItem = MyViewHolder(v)
        return vhItem
    }

    override fun onBindViewHolder(holder: AAH_CustomViewHolder, position: Int) {
        holder as MyViewHolder
        holder.txtSportTitle.text = feedArray[position].title
        holder.txtSportOwner.text = "By ${feedArray[position].fullname}"
        holder.txtSportName.text = feedArray[position].sport
        holder.setImageUrl(feedArray[position].thumbnail)
        holder.setVideoUrl(feedArray[position].url)

        holder.imgSound.setOnClickListener {
            if (isMuted) {
                holder.unmuteVideo()
                holder.imgSound.setImageResource(R.mipmap.ic_unmute)
            } else {
                holder.muteVideo()
                holder.imgSound.setImageResource(R.mipmap.ic_mute)
            }
            isMuted = !isMuted
        }

        holder.cvFeed.setOnClickListener {
            itemClick!!.onItemClick(position)
        }

        Glide.with(activity).load(feedArray[position].thumbnail).apply(RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)).into(holder.getAAH_ImageView())
    }

    override fun getItemCount(): Int {
        return feedArray.size
    }

    inner class MyViewHolder(itemView: View) : AAH_CustomViewHolder(itemView) {
        val vvFeedListing = itemView.vvFeedListing!!
        val txtSportTitle = itemView.txtSportTitle!!
        val txtSportOwner = itemView.txtSportOwner!!
        val txtSportName = itemView.txtSportName!!
        val imgSound = itemView.imgSound!!
        val cvFeed = itemView.cvFeed!!



        override fun videoStarted() {
            super.videoStarted()
            if (isMuted) {
                muteVideo()
                imgSound.setImageResource(R.mipmap.ic_mute)
            } else {
                unmuteVideo()
                imgSound.setImageResource(R.mipmap.ic_unmute)
            }
        }
    }
}