package com.myreevuuCoach.activities

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.myreevuuCoach.R
import com.myreevuuCoach.customViews.FlowLayout
import com.myreevuuCoach.dialog.RequestAcceptedDialog
import com.myreevuuCoach.fragments.ReevuuFragment
import com.myreevuuCoach.fragments.ReevuuRequestsFragment
import com.myreevuuCoach.interfaces.InterConst
import com.myreevuuCoach.models.OptionsModel
import com.myreevuuCoach.models.RequestsModel
import com.myreevuuCoach.models.SkipModel
import com.myreevuuCoach.network.RetrofitClient
import com.myreevuuCoach.utils.AlertDialogs
import com.myreevuuCoach.utils.Constants
import com.myreevuuCoach.utils.ErrorUtils
import com.universalvideoview.UniversalVideoView
import kotlinx.android.synthetic.main.activity_request_detail.*
import kotlinx.android.synthetic.main.layout_expertise.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by dev on 5/12/18.
 */

class RequestDetailActivity : BaseKotlinActivity(), UniversalVideoView.VideoViewCallback {

    private val SEEK_POSITION_KEY = "SEEK_POSITION_KEY"
    private var mSeekPosition: Int = 1
    private var cachedHeight: Int = 0
    private var isFullscreen: Boolean = false
    private val RESULT_VIDEO_REVIEWED: Int = 101
    private lateinit var mResponse: RequestsModel.ResponseBean

    override fun getContentView() = R.layout.activity_request_detail

    override fun initUI() {
        llMediaController.setOnLoadingView(R.layout.activity_video_loading)
        videoView.setMediaController(llMediaController)

    }

    fun populateData() {
        setVideoAreaSize()
        videoView.setVideoViewCallback(this)
        if (mResponse.review_status == Integer.parseInt(InterConst.REEVUU_REQUESTS_ACCEPTED)) {
            llAccept.visibility = View.GONE
            llRemainingTime.visibility = View.VISIBLE
            txtViews.visibility = View.VISIBLE
            setRemainingTime()
        } else if (mResponse.review_status == Integer.parseInt(InterConst.REEVUU_REQUESTS_DECLINE)) {
            llAccept.visibility = View.GONE
            llRemainingTime.visibility = View.VISIBLE
            txtViews.visibility = View.VISIBLE
            showDeclineAlert()
        } else if (mResponse.review_status == Integer.parseInt(InterConst.REEVUU_REQUESTS_REVIEWED)) {
            llAccept.visibility = View.GONE
            llRemainingTime.visibility = View.GONE
            txtViews.visibility = View.VISIBLE
        } else {
            llAccept.visibility = View.VISIBLE
            llRemainingTime.visibility = View.GONE
            txtViews.visibility = View.GONE
            setRemainingTime()
        }

        videoView.start()
        populateBottomData()
    }

    var review_request_id = ""
    override fun onCreateStuff() {
        if (getIntent().hasExtra(InterConst.NotificationID)) {
            notificationUpdate()
        }
        if (getIntent().hasExtra("review_request_id")) {
            review_request_id = getIntent().getStringExtra("review_request_id").toString()
        } else {
            mResponse = intent.getParcelableExtra(InterConst.INTEND_EXTRA)
            review_request_id = mResponse.id.toString()
        }

        getRequestDetails()
    }

    private fun setRemainingTime() {
        val s = mResponse.remaining_time.split(":".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        val hrs = Integer.parseInt(s[0].trim({ it <= ' ' }))
        val min = Integer.parseInt(s[1].trim({ it <= ' ' }))
        val sec = Integer.parseInt(s[2].trim({ it <= ' ' }))
        val count = hrs * 60 * 60 * 1000 + min * 60 * 1000 + sec * 1000
        object : CountDownTimer(count.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                var hours: String? = null
                var minutes: String? = null
                var seconds: String? = null

                hours = (millisUntilFinished / 1000 / 60 / 60).toString()

                if (millisUntilFinished / 1000 / 60 / 60 < 10 && (millisUntilFinished / 1000 / 60 / 60) > 1) {
                    hours = "0$hours"
                }
                minutes = (millisUntilFinished / 1000 / 60 % 60).toString()

                if (millisUntilFinished / 1000 / 60 % 60 < 10 && (millisUntilFinished / 1000 / 60 % 60) > 1) {
                    minutes = "0$minutes"
                }
                seconds = (millisUntilFinished / 1000 % 60).toString()

                if ((millisUntilFinished / 1000 % 60) < 10 && (millisUntilFinished / 1000 % 60) > 1) {
                    seconds = "0$seconds"
                }
                txtRemainingTime.text = """${hours}h ${minutes}m ${seconds}s"""
            }

            override fun onFinish() {
                showAlert()
            }

        }.start()
    }

    internal fun showDeclineAlert() {
        AlertDialogs.tryAgainDialog(mContext, getString(R.string.ok), getString(R.string.declined_message)) { finish() }
    }

    internal fun showAlert() {
        AlertDialogs.tryAgainDialog(mContext, getString(R.string.ok), getString(R.string.time_finished)) { finish() }
    }

    override fun initListener() {
        imgClose.setOnClickListener(this)
        txtDecline.setOnClickListener(this)
        txtAccept.setOnClickListener(this)
        txtStartReviewing.setOnClickListener(this)
        llProfile.setOnClickListener(this)
    }

    override fun getContext() = this

    override fun onClick(view: View?) {
        when (view) {
            imgClose -> {
                moveBack()
            }
            txtAccept -> {
                hitApi(InterConst.REEVUU_REQUESTS_ACCEPTED)
            }
            txtDecline -> {
                hitApi(InterConst.REEVUU_REQUESTS_DECLINE)
            }
            txtStartReviewing -> {
                val intent = Intent(mContext, StartVideoReviewActivity::class.java)
                intent.putExtra(InterConst.REVIEW_REQUEST_ID, mResponse.id.toString())
                intent.putExtra(InterConst.VIDEO_URL, mResponse.video.url)
                startActivityForResult(intent, RESULT_VIDEO_REVIEWED)
                //  showToast(mContext,getString(R.string.work_progress))
            }
            llProfile -> {
                val intent = Intent(mContext, OthersProfileActivity::class.java)
                intent.putExtra(InterConst.PROFILE_PIC, mResponse.profile_pic)
                intent.putExtra(InterConst.NAME, mResponse.name)
                intent.putExtra(InterConst.ID, mResponse.user_id)
                startActivity(intent)
            }
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
            videoView.setVideoPath(mResponse.video.url)
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

        Glide.with(this).load(mResponse.profile_pic).apply(options).into(imgProfilePic)
        txtPlayerName.text = mResponse.video.fullname
        txtSportTitle.text = mResponse.video.title
        txtSportName.text = mResponse.video.sport
        txtRemainingTime.text = mResponse.remaining_time

        if (mResponse.video.user_type == 1)
            txtRole.text = "${mResponse.video.sport} Coach"
        else
            txtRole.text = "Athlete"

        txtSportDesc.text = mResponse.video.description
        txtViews.text = "${mResponse.video.views} Views"
        txtDateTime.text = Constants.displayDateTime(mResponse.created_at)
        for (improvement in mResponse.video.improvement)
            flImprovements.addView(inflateExpertiseView(improvement))
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


    private fun hitApi(type: String) {
        if (connectedToInternet()) {
            showLoader()
            val call = RetrofitClient.getInstance().response_a_request(mUtils.getString(InterConst.ACCESS_TOKEN, ""),
                    mResponse.id.toString(),
                    type)
            call.enqueue(object : Callback<RequestsModel> {
                override fun onResponse(call: Call<RequestsModel>, response: Response<RequestsModel>) {
                    dismissLoader()
                    if (response.body().response != null) {
                        if (response.body().response.review_status == Integer.parseInt(InterConst.REEVUU_REQUESTS_ACCEPTED)) {
                            RequestAcceptedDialog(mContext, object : DialogInterface {
                                override fun cancel() {

                                }

                                override fun dismiss() {
                                    finish()
                                }
                            }).showDialog()
                        } else {
                            finish()
                        }
                        if (ReevuuRequestsFragment.getInstance() != null)
                            ReevuuRequestsFragment.getInstance().onCallResume()
                    } else {
                        if (response.body().error.code == InterConst.REQUEST_NOT_EXIST) {
                            showAlert(txtSportDesc, getString(R.string.video_deleted))

                            if (ReevuuRequestsFragment.getInstance() != null)
                                ReevuuRequestsFragment.getInstance().onCallResume()
                            finish()
                        } else {
                            showAlert(txtSportDesc, response.body().error.message)
                            if (response.body().error.code == InterConst.INVALID_ACCESS) {
                                moveToSplash()
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<RequestsModel>, t: Throwable) {
                    dismissLoader()
                    showToast(mContext, t.message.toString())
                }
            })
        }
    }

    override fun onBackPressed() {
        if (this.isFullscreen) {
            videoView.setFullscreen(false)
        } else {
            super.onBackPressed()
        }
    }

    private fun moveBack() {
        if (getIntent().hasExtra(InterConst.NotificationID)) {
            val intent = Intent(mContext, LandingActivity::class.java)
            startActivity(intent)
        } else {
            finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RESULT_VIDEO_REVIEWED) {
                ReevuuFragment.getInstance().setPagerItem(InterConst.FRAG_REEVUU_REVIEWED)
                finish()
            }
        }
    }

    internal var notificationReadType = "2"//1 outer and 2 notification Read

    private fun notificationUpdate() {
        val call = RetrofitClient.getInstance().setNotification(
                mUtils.getString(InterConst.ACCESS_TOKEN, ""),
                notificationReadType,
                getIntent().getStringExtra(InterConst.NotificationID))
        call.enqueue(object : Callback<SkipModel> {
            override fun onFailure(call: Call<SkipModel>?, t: Throwable?) {
                showToast(mContext, t!!.localizedMessage)
            }

            override fun onResponse(call: Call<SkipModel>?, response: Response<SkipModel>) {
                dismissLoader()
                if (response.isSuccessful) {
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

    private fun getRequestDetails() {
        showLoader()
        val call = RetrofitClient.getInstance().getRequestDetailByID(
                mUtils.getString(InterConst.ACCESS_TOKEN, ""), review_request_id)
        call.enqueue(object : Callback<RequestsModel> {
            override fun onFailure(call: Call<RequestsModel>?, t: Throwable?) {
                dismissLoader()
                showAlert(t!!.localizedMessage)
                // showToast(mContext, t!!.localizedMessage)
            }

            override fun onResponse(call: Call<RequestsModel>?, response: Response<RequestsModel>) {
                dismissLoader()
                if (response.body().response != null) {
                    mResponse = response.body().response
                    populateData()
                } else {
                    if (response.body().error.code == InterConst.INVALID_ACCESS) {
                        moveToSplash()
                    } else {
                        showAlert(response.body().error.message)
                        // showAlert(llMediaController, errorModel.error.message)
                    }
                }
            }
        })
    }

}
