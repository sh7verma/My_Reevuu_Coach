package com.myreevuuCoach.activities

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.CoordinatorLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.myreevuuCoach.R
import com.myreevuuCoach.adapters.ReportAdapter
import com.myreevuuCoach.customViews.FlowLayout
import com.myreevuuCoach.interfaces.InterConst
import com.myreevuuCoach.models.BaseSuccessModel
import com.myreevuuCoach.models.FeedModel
import com.myreevuuCoach.models.OptionsModel
import com.myreevuuCoach.network.RetrofitClient
import com.myreevuuCoach.utils.Constants
import com.myreevuuCoach.utils.ErrorUtils
import com.myreevuuCoach.utils.OnViewGlobalLayoutListener
import com.universalvideoview.UniversalVideoView
import kotlinx.android.synthetic.main.activity_feed_detail.*
import kotlinx.android.synthetic.main.dialog_options.*
import kotlinx.android.synthetic.main.layout_expertise.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedDetailActivity : BaseKotlinActivity(), UniversalVideoView.VideoViewCallback {

    private val SEEK_POSITION_KEY = "SEEK_POSITION_KEY"
    private var mSeekPosition: Int = 1
    private var cachedHeight: Int = 0
    private var isFullscreen: Boolean = false

    lateinit var mFeedResponse: FeedModel.Response
    var reportReason: String = Constants.EMPTY

    override fun getContentView() = R.layout.activity_feed_detail

    override fun initUI() {
        llMediaController.setOnLoadingView(R.layout.activity_video_loading)
        videoView.setMediaController(llMediaController)
        setVideoAreaSize()
        videoView.setVideoViewCallback(this)
    }

    override fun onCreateStuff() {
        mFeedResponse = intent.getParcelableExtra("feedData")
        videoView.start()
        populateBottomData()
    }

    override fun initListener() {
        imgReport.setOnClickListener(this)
        imgClose.setOnClickListener(this)
        llProfile.setOnClickListener(this)
    }

    override fun getContext() = this

    override fun onClick(view: View?) {
        when (view) {
            imgClose -> {
                moveBack()
            }
            imgReport -> {
                reportDialog(Constants.reportData(), getString(R.string.report_video))
            }
            llProfile -> {
                if (mFeedResponse.user_type != 1) {
                    val intent = Intent(mContext, OthersProfileActivity::class.java)
                    intent.putExtra(InterConst.PROFILE_PIC, mFeedResponse.profile_pic)
                    intent.putExtra(InterConst.NAME, mFeedResponse.fullname)
                    intent.putExtra(InterConst.ID, mFeedResponse.user_id)
                    startActivity(intent)
                }
            }
        }
    }

    private fun reportDialog(optionsArray: ArrayList<OptionsModel>, title: String) {
        val optionDialog = BottomSheetDialog(this)
        optionDialog.setContentView(R.layout.dialog_options)

        val dialogParms = CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT,
                CoordinatorLayout.LayoutParams.WRAP_CONTENT)
        dialogParms.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL

        val bottomSheet = optionDialog.window.findViewById(android.support.design.R.id.design_bottom_sheet) as FrameLayout
        bottomSheet.setBackgroundResource(R.drawable.white_default)
        bottomSheet.layoutParams = dialogParms
        bottomSheet.viewTreeObserver
                .addOnGlobalLayoutListener(OnViewGlobalLayoutListener(bottomSheet, Constants.dpToPx(400)))

        val txtOptionTitle = optionDialog.txtOptionTitle
        val rvOptions = optionDialog.rvOptions
        val txtDone = optionDialog.txtDone
        txtDone.visibility = View.VISIBLE

        txtOptionTitle.text = title
        txtDone.text = getString(R.string.report_video)

        rvOptions.layoutManager = LinearLayoutManager(this)
        rvOptions.adapter = ReportAdapter(optionsArray, this)

        txtDone.setOnClickListener {
            if (!TextUtils.isEmpty(reportReason)) {
                if (connectedToInternet()) {
                    hitReportAPI(optionDialog)
                } else
                    showToast(mContext, mErrorInternet)
            } else {
                showToast(mContext, getString(R.string.pick_reason))
            }
        }
        optionDialog.show()
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
        txtViews.text = "${mFeedResponse.views} Views"
        txtDateTime.text = Constants.displayDateTime(mFeedResponse.created_at)
        for (improvement in mFeedResponse.improvement)
            flImprovements.addView(inflateExpertiseView(improvement))
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


    private fun hitReportAPI(optionDialog: BottomSheetDialog) {
        showLoader()
        val call = RetrofitClient.getInstance().reportFeed(mFeedResponse.id, reportReason,
                mUtils.getString("accessToken", ""))
        call.enqueue(object : Callback<BaseSuccessModel> {
            override fun onFailure(call: Call<BaseSuccessModel>?, t: Throwable?) {
                dismissLoader()


                showToast(mContext, t!!.localizedMessage)
            }

            override fun onResponse(call: Call<BaseSuccessModel>?, response: Response<BaseSuccessModel>) {
                dismissLoader()
                if (response.isSuccessful) {
                    reportReason = Constants.EMPTY
                    optionDialog.dismiss()
                    showToast(mContext, response.body().response.message)
                } else {
                    val errorModel = ErrorUtils.parseError(response)
                    if (errorModel.error.code == Constants.ERROR_CODE) {
                        moveToSplash()
                    } else {
                        showAlert(llMediaController, errorModel.error.message)
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