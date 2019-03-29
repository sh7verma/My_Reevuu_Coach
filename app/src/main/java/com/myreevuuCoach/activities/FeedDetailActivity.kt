package com.myreevuuCoach.activities

import android.app.AlertDialog
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
import com.myreevuuCoach.firebase.FirebaseChatConstants
import com.myreevuuCoach.interfaces.InterConst
import com.myreevuuCoach.models.BaseSuccessModel
import com.myreevuuCoach.models.FeedModel
import com.myreevuuCoach.models.OptionsModel
import com.myreevuuCoach.models.VideoModel
import com.myreevuuCoach.network.RetrofitClient
import com.myreevuuCoach.services.CommentIntentServiceResult
import com.myreevuuCoach.utils.Constants
import com.myreevuuCoach.utils.ErrorUtils
import com.myreevuuCoach.utils.OnViewGlobalLayoutListener
import com.squareup.picasso.Picasso
import com.universalvideoview.UniversalVideoView
import kotlinx.android.synthetic.main.activity_feed_detail.*
import kotlinx.android.synthetic.main.dialog_options.*
import kotlinx.android.synthetic.main.layout_expertise.view.*
import org.greenrobot.eventbus.EventBus
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

    }

    override fun onCreateStuff() {
        if (intent.hasExtra("feedData")) {
            mFeedResponse = intent.getParcelableExtra("feedData")
            populateBottomData()
            setData()
        } else {
            hitVideoById()
        }

    }

    fun setData() {
        if (mFeedResponse.post_type == InterConst.POST_TYPE_VIDEO) {
            flVideo.visibility = View.VISIBLE
            imgThumbnail.visibility = View.GONE

            videoView.setMediaController(llMediaController)
            videoView.setVideoViewCallback(this)
            setVideoAreaSize()
            videoView.start()

        } else {
            flVideo.visibility = View.GONE
            videoView.visibility = View.GONE
            llMediaController.visibility = View.GONE
            imgThumbnail.visibility = View.VISIBLE

            if (mFeedResponse.liked == 1) {
                imgLike.setImageResource(R.mipmap.ic_like_selected)
            } else {
                imgLike.setImageResource(R.mipmap.ic_like)
            }
            txtComment.text = mFeedResponse.comments_count.toString()
            txtLikeCount.text = mFeedResponse.likes_count.toString()


            if (!mFeedResponse.thumbnail.equals("", ignoreCase = true)) {
                Picasso.get()
                        .load(mFeedResponse.thumbnail)
                        .placeholder(R.mipmap.ic_ph)
                        .error(R.mipmap.ic_ph).into(imgThumbnail)
            } else {
                Picasso.get()
                        .load(R.mipmap.ic_ph)
                        .placeholder(R.mipmap.ic_ph)
                        .error(R.mipmap.ic_ph).into(imgThumbnail)
            }
        }

        if (mFeedResponse.user_type == FirebaseChatConstants.TYPE_ADMIN) {

            llDate.visibility = View.GONE
            llInfo.visibility = View.GONE

            llAdminInfo.visibility = View.VISIBLE

            llAdmin.visibility = View.VISIBLE
            imgProfilePic.visibility = View.GONE
            txtViews.visibility = View.GONE

            txtAdminSportName.text = mFeedResponse.sport
            txtAdminDateTime.text = Constants.displayDateTime(mFeedResponse.created_at)

        } else {
            llAdmin.visibility = View.GONE
        }

    }

    override fun initListener() {
        imgReport.setOnClickListener(this)
        imgClose.setOnClickListener(this)
        llProfile.setOnClickListener(this)
        imgLike.setOnClickListener(this)
        txtLikeCount.setOnClickListener(this)
        imgComment.setOnClickListener(this)
        txtComment.setOnClickListener(this)
        imgShare.setOnClickListener(this)
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

            imgLike -> {
                setLikedStatus()
            }
            txtLikeCount -> {
                setLikedStatus()
            }
            imgComment -> {
                openCommentPage()
            }
            txtComment -> {
                openCommentPage()
            }
            imgShare -> {
                shareTextUrl()
            }
        }
    }

    private fun markArticleFav() {
        mFeedResponse.likes_count = mFeedResponse.likes_count + 1
        txtLikeCount.text = mFeedResponse.likes_count.toString()
        imgLike.setImageResource(R.mipmap.ic_like_selected)
    }


    private fun removeArticleFav() {
        mFeedResponse.likes_count = mFeedResponse.likes_count - 1
        txtLikeCount.text = mFeedResponse.likes_count.toString()
        imgLike.setImageResource(R.mipmap.ic_like)
    }

    private fun setLikedStatus() {
        if (connectedToInternet()) {

            var call: Call<BaseSuccessModel> = RetrofitClient.getInstance().setFavArticles(
                    mUtils.getString(InterConst.ACCESS_TOKEN, ""),
                    mFeedResponse.id
            )
            call.enqueue(object : retrofit2.Callback<BaseSuccessModel> {
                override fun onResponse(call: Call<BaseSuccessModel>, response: Response<BaseSuccessModel>) {
                    dismissLoader()
                    if (response.body() != null) {
                        var mode: VideoModel.ResponseBean = VideoModel.ResponseBean()

                        if (response.body().response.message.toLowerCase().contains("un")) {

                            mode.liked = 0
                            mode.likes_count = mFeedResponse.likes_count - 1

                            EventBus.getDefault().post(CommentIntentServiceResult(1, 0, mode))
                            removeArticleFav()
                        } else {

                            mode.liked = 1
                            mode.likes_count = mFeedResponse.likes_count + 1

                            EventBus.getDefault().post(CommentIntentServiceResult(1, 1, mode))
                            markArticleFav()
                        }
                    } else {
                        if (response.body().error.code == 506) {
                            EventBus.getDefault().post(CommentIntentServiceResult(4, 0, null))
                        } else if (response.body().error.code == InterConst.INVALID_ACCESS)
                            moveToSplash()
                        else
                            showAlert(llBottom, response.body().error.message)
                    }
                }

                override fun onFailure(call: Call<BaseSuccessModel>, t: Throwable) {
                    dismissLoader()
                    showAlert(llBottom, t.toString())
                    t.printStackTrace()
                }
            })
        }
    }

    fun shareTextUrl() {
        val share = Intent(android.content.Intent.ACTION_SEND)
        share.type = "text/plain"
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        //  share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
        share.putExtra(Intent.EXTRA_TEXT, mFeedResponse.url)
        startActivity(Intent.createChooser(share, "Share link!"))
    }

    private fun articleFavDialog(Message: String) {
        val alertDialogBuilder = AlertDialog.Builder(mContext)
        //  alertDialogBuilder.setTitle(getString(R.string.logout));
        alertDialogBuilder.setMessage(Message)
        alertDialogBuilder.setPositiveButton("Ok"
        ) { arg0, arg1 ->

        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun openCommentPage() {
        val intent = Intent(this, CommentActivity::class.java)
        intent.putExtra("post_id", mFeedResponse.id)
        startActivity(intent)
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
        flImprovements.removeAllViews()
        for (improvement in mFeedResponse.improvement)
            flImprovements.addView(inflateExpertiseView(improvement))

        if (mFeedResponse.improvement.isEmpty()) {
            txtAreasOfImprovements.visibility = View.GONE
        }


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

    override fun onRestart() {
        super.onRestart()
        hitVideoById()
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
            if (mFeedResponse.video_height > mFeedResponse.video_width) {
                videoView.setFullscreen(false)
                layoutParams.width = mWidth
                layoutParams.height = mHeight - resources.getDimension(R.dimen._20ssp).toInt()
            } else {
                layoutParams.width = mHeight
                layoutParams.height = mWidth
            }
            flVideo.layoutParams = layoutParams
            llBottom.visibility = View.GONE
            if (mFeedResponse.user_type == FirebaseChatConstants.TYPE_ADMIN) {
                llAdmin.visibility = View.GONE
            }

        } else {
            val layoutParams = flVideo.layoutParams
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams.height = this.cachedHeight
            flVideo.layoutParams = layoutParams
            llBottom.visibility = View.VISIBLE
            if (mFeedResponse.user_type == FirebaseChatConstants.TYPE_ADMIN) {
                llAdmin.visibility = View.VISIBLE
            }
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
            if (mFeedResponse.video_height > mFeedResponse.video_width) {
                val layoutParams = flVideo.layoutParams
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                layoutParams.height = this.cachedHeight
                flVideo.layoutParams = layoutParams
                llBottom.visibility = View.VISIBLE
                if (mFeedResponse.user_type == FirebaseChatConstants.TYPE_ADMIN) {
                    llAdmin.visibility = View.VISIBLE
                }
            } else {
                videoView.setFullscreen(false)
            }

        } else {
            super.onBackPressed()
        }
    }

    private fun moveBack() {
        finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
    }


    private fun hitVideoById() {
        if (connectedToInternet()) {
            var id: String = ""
            if (intent.hasExtra("id")) {
                id = intent.getStringExtra("id")
            } else {
                id = mFeedResponse.id.toString()
            }
            showLoader()
            val call = RetrofitClient.getInstance().getFeedSingleVideo(
                    mUtils.getString(InterConst.ACCESS_TOKEN, ""),
                    id)
            call.enqueue(object : retrofit2.Callback<VideoModel> {
                override fun onResponse(call: Call<VideoModel>, response: Response<VideoModel>) {
                    dismissLoader()
                    if (response.body().response != null) {
                        response.body().response
                        if (!intent.hasExtra("feedData")) {
                            mFeedResponse = FeedModel.Response(response.body().response.id,
                                    response.body().response.user_id
                                    , response.body().response.user_type
                                    , response.body().response.profile_pic
                                    , response.body().response.sport_id
                                    , response.body().response.sport
                                    , response.body().response.privacy
                                    , response.body().response.improvement
                                    , response.body().response.url
                                    , response.body().response.thumbnail
                                    , response.body().response.fullname
                                    , response.body().response.title
                                    , response.body().response.views
                                    , response.body().response.created_at
                                    , response.body().response.description
                                    , response.body().response.post_type,
                                    response.body().response.likes_count,
                                    response.body().response.comments_count,
                                    response.body().response.liked, 0, 0)

                            populateBottomData()
                        }
                        mFeedResponse.likes_count = response.body().response.likes_count
                        mFeedResponse.comments_count = response.body().response.comments_count
                        mFeedResponse.liked = response.body().response.liked

                        EventBus.getDefault().post(CommentIntentServiceResult(5, 0, response.body().response))
                        setData()
                    } else {
                        if (response.body().error.code == 506) {
                            EventBus.getDefault().post(CommentIntentServiceResult(4, 0, null))
                            videoDeleted("This post has been deleted")
                        } else if (response.body().error.code == InterConst.INVALID_ACCESS)
                            moveToSplash()
                        else
                            videoDeleted(response.body().error.message)
                    }
                }

                override fun onFailure(call: Call<VideoModel>, t: Throwable) {
                    dismissLoader()
                    showAlert(llBottom, t.message.toString())
                }
            })
        }
    }

    private fun videoDeleted(Message: String) {
        val alertDialogBuilder = AlertDialog.Builder(mContext)
        //  alertDialogBuilder.setTitle(getString(R.string.logout));
        alertDialogBuilder.setMessage(Message)
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.setPositiveButton("Ok"
        ) { arg0, arg1 -> moveBack() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}