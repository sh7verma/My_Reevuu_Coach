package com.myreevuuCoach.activities

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.CoordinatorLayout
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.myreevuuCoach.R
import com.myreevuuCoach.customViews.FlowLayout
import com.myreevuuCoach.interfaces.InterConst
import com.myreevuuCoach.models.*
import com.myreevuuCoach.network.RetrofitClient
import com.myreevuuCoach.utils.Constants
import com.myreevuuCoach.utils.ErrorUtils
import com.myreevuuCoach.utils.OnViewGlobalLayoutListener
import com.universalvideoview.UniversalVideoView
import kotlinx.android.synthetic.main.activity_my_feed_detail.*
import kotlinx.android.synthetic.main.dialog_delete.*
import kotlinx.android.synthetic.main.layout_expertise.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by dev on 22/11/18.
 */

class MyFeedDetailActivity : BaseKotlinActivity(), UniversalVideoView.VideoViewCallback {

    private val SEEK_POSITION_KEY = "SEEK_POSITION_KEY"
    private var mSeekPosition: Int = 1
    private var cachedHeight: Int = 0
    private var isFullscreen: Boolean = false

    lateinit var mFeedResponse: FeedModel.Response

    override fun getContentView() = R.layout.activity_my_feed_detail

    override fun initUI() {
        llMediaController.setOnLoadingView(R.layout.activity_video_loading)
        videoView.setMediaController(llMediaController)
        /*if (getIntent().hasExtra("video_id")) {
            getVideoDetails()
        }*/

    }

    fun populateData() {
        setVideoAreaSize()
        videoView.setVideoViewCallback(this)
        videoView.start()
        populateBottomData()
    }

    override fun onCreateStuff() {
        /*if (getIntent().hasExtra("video_id")) {
            if (getIntent().hasExtra(InterConst.NotificationID)) {
                notificationUpdate()
            }
            getVideoDetails()
        } else {
           // mFeedResponse = intent.getParcelableExtra("feedData")
            populateData()
        }*/

        mFeedResponse = intent.getParcelableExtra("feedData")
        populateData()

    }

    override fun initListener() {
        imgClose.setOnClickListener(this)
        imgDelete.setOnClickListener(this)
    }

    override fun getContext() = this

    override fun onClick(view: View?) {
        when (view) {
            imgClose -> {
                moveBack()
            }
            imgDelete -> {
                deleteDialog()
            }
        }
    }

    private fun deleteDialog() {
        val optionDialog = BottomSheetDialog(this)
        optionDialog.setContentView(R.layout.dialog_delete)

        val dialogParms = CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT,
                CoordinatorLayout.LayoutParams.WRAP_CONTENT)
        dialogParms.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL

        val bottomSheet = optionDialog.window.findViewById(android.support.design.R.id.design_bottom_sheet) as FrameLayout
        bottomSheet.setBackgroundResource(R.drawable.white_default)
        bottomSheet.layoutParams = dialogParms
        bottomSheet.viewTreeObserver
                .addOnGlobalLayoutListener(OnViewGlobalLayoutListener(bottomSheet, Constants.dpToPx(400)))

        val txtDelete = optionDialog.txtDelete
        val txtCancel = optionDialog.txtCancel

        txtDelete.setOnClickListener {
            if (connectedToInternet()) {
                hitDeleteAPI(optionDialog)
            } else
                showToast(mContext, mErrorInternet)
        }

        txtCancel.setOnClickListener {
            optionDialog.cancel()
        }

        optionDialog.show()
    }

    override fun onPause() {
        super.onPause()
        if (videoView != null) {
            mSeekPosition = videoView.currentPosition
            videoView.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        if (videoView != null) {
            videoView.seekTo(mSeekPosition)
        }
    }

    private fun setVideoAreaSize() {
        flVideo.post(Runnable {
            val width = flVideo.width
            cachedHeight = resources.getDimension(R.dimen._220sdp).toInt()

            //                cachedHeight = (int) (width * 3f / 4f);
            //                cachedHeight = (int) (width * 9f / 16f);
            val videoLayoutParams = flVideo.layoutParams
            videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            videoLayoutParams.height = cachedHeight
            flVideo.layoutParams = videoLayoutParams
            videoView.setVideoPath(mFeedResponse.url)
            videoView.requestFocus()
            videoView.seekTo(1)
            videoView.pause()
        })
    }

    private fun populateBottomData() {
        /* Glide.with(this).load(mResponse.thumbnail).apply(RequestOptions()
                 .diskCacheStrategy(DiskCacheStrategy.ALL)).into(imgThumbnail)*/

        val options = RequestOptions().centerCrop().placeholder(R.mipmap.ic_profile_avatar)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)

        Glide.with(this).load(mFeedResponse.profile_pic).apply(options).into(imgProfilePic)
        txtPlayerName.text = mFeedResponse.fullname
        txtSportTitle.text = mFeedResponse.title
        txtSportName.text = mFeedResponse.sport
        if (mFeedResponse.user_type == 1)
            txtRole.text = "${mFeedResponse.sport} Coach"
        else
            txtRole.text = "Athlete"

        txtSportDesc.text = mFeedResponse.description

//        txtViews.text = "${mResponse.views} Views"

        txtDateTime.text = Constants.displayDateTime(mFeedResponse.created_at)
        for (improvement in mFeedResponse.improvement)
            flArea.addView(inflateExpertiseView(improvement))
    }

    private fun hitDeleteAPI(optionDialog: BottomSheetDialog) {
        showLoader()
        val call = RetrofitClient.getInstance().deleteVideo(mFeedResponse.id,
                mUtils.getString(InterConst.ACCESS_TOKEN, ""))
        call.enqueue(object : Callback<BaseSuccessModel> {
            override fun onFailure(call: Call<BaseSuccessModel>?, t: Throwable?) {
                dismissLoader()
                showToast(mContext, t!!.localizedMessage)
            }

            override fun onResponse(call: Call<BaseSuccessModel>?, response: Response<BaseSuccessModel>) {
                dismissLoader()
                if (response.isSuccessful) {
                    val intent = Intent(InterConst.BROADCAST_MY_FEED_VIDEO_DELETE)

                    if (getIntent().hasExtra(InterConst.HOME_VIDEO_POSITION)) {
                        intent.putExtra(InterConst.HOME_VIDEO_POSITION,
                                getIntent().getIntExtra(InterConst.HOME_VIDEO_POSITION, -1))
//                        ProfileVideoFragment.getInstance().setData()
                    } else if (getIntent().hasExtra(InterConst.PROFILE_VIDEO_POSITION)) {
                        intent.putExtra(InterConst.PROFILE_VIDEO_POSITION, getIntent().getIntExtra(InterConst.PROFILE_VIDEO_POSITION, -1))
//                        sendBroadcast(intent)
//                        HomeFragment.getInstance().onCallResume()
                    } else if (getIntent().hasExtra(InterConst.FEED_SEARCH_VIDEO_POSITION)) {
                        intent.putExtra(InterConst.FEED_SEARCH_VIDEO_POSITION, getIntent().getIntExtra(InterConst.FEED_SEARCH_VIDEO_POSITION, -1))
//                        sendBroadcast(intent)
//                        HomeFragment.getInstance().onCallResume()
//                        ProfileVideoFragment.getInstance().setData()
                    }
                    sendBroadcast(intent)

                    optionDialog.dismiss()
                    showToast(mContext, response.body().response.message)
                    finish()

                } else {
                    if (response.body().error.code == InterConst.INVALID_ACCESS) {
                        moveToSplash()
                    } else {
                        showAlert(response.body().error.message)
                    }
                }
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SEEK_POSITION_KEY, mSeekPosition)
    }

    override fun onRestoreInstanceState(outState: Bundle) {
        super.onRestoreInstanceState(outState)
        mSeekPosition = outState.getInt(SEEK_POSITION_KEY)
    }

    private fun switchTitleBar(show: Boolean) {
        if (show) {
            llToolbar.visibility = View.VISIBLE
        } else {
            llToolbar.visibility = View.GONE
        }
    }

    override fun onPause(mediaPlayer: MediaPlayer?) {
    }

    override fun onStart(mediaPlayer: MediaPlayer?) {

    }

    override fun onBufferingStart(mediaPlayer: MediaPlayer?) {

    }

    override fun onBufferingEnd(mediaPlayer: MediaPlayer?) {

    }

    override fun onScaleChange(isFullscreen: Boolean) {
        this.isFullscreen = isFullscreen
        if (isFullscreen) {
            val layoutParams = flVideo.layoutParams
            layoutParams.width = mHeight
            layoutParams.height = mWidth
            flVideo.layoutParams = layoutParams
            llBottom.visibility = View.GONE

        } else {
            val layoutParams = flVideo.layoutParams
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams.height = this.cachedHeight
            flVideo.layoutParams = layoutParams
            llBottom.visibility = View.VISIBLE
        }

        switchTitleBar(!isFullscreen)
    }

    private fun inflateExpertiseView(optionsModel: OptionsModel): View {
        val interestChip = LayoutInflater.from(this).inflate(R.layout.layout_expertise,
                null, false)

        val innerParms = FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        interestChip.llExpertise.layoutParams = innerParms

        interestChip.txtExpertise.text = optionsModel.name
        interestChip.txtExpertise.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
        when (optionsModel.color) {
            1 -> interestChip.txtExpertise.setBackgroundResource(R.drawable.gradient_first)
            2 -> interestChip.txtExpertise.setBackgroundResource(R.drawable.gradient_second)
            3 -> interestChip.txtExpertise.setBackgroundResource(R.drawable.gradient_third)
            4 -> interestChip.txtExpertise.setBackgroundResource(R.drawable.gradient_fourth)
            5 -> interestChip.txtExpertise.setBackgroundResource(R.drawable.gradient_fifth)
            6 -> interestChip.txtExpertise.setBackgroundResource(R.drawable.gradient_sixth)
        }

        return interestChip
    }

    override fun onBackPressed() {
        if (this.isFullscreen) {
            videoView.setFullscreen(false)
        } else {
            super.onBackPressed()
        }
    }

    private fun moveBack() {

        finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
    }



}