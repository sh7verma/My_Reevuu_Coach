package com.myreevuuCoach.activities

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v4.content.LocalBroadcastManager
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import com.myreevuuCoach.R
import com.myreevuuCoach.interfaces.InterConst
import com.myreevuuCoach.models.BaseSuccessModel
import com.myreevuuCoach.network.RetrofitClient
import com.myreevuuCoach.utils.Constants
import kotlinx.android.synthetic.main.activity_verify_email.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerifyEmailActivity : BaseKotlinActivity() {
    override fun getContentView(): Int {
        this.window.setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.colorTransperent));
        return R.layout.activity_verify_email
    }

    override fun initUI() {
        translateView(mHeight.toFloat(), 0f, false)
    }

    override fun onCreateStuff() {

        val firstText = "A verification email has been sent to your registered email "
        val email: String?

        if (intent.hasExtra(InterConst.INTEND_EXTRA)) {
            email = mUtils.getString(InterConst.NEW_EMAIL, "")
        } else {
            email = mUtils.getString("email", "")
        }
        val secondString = ". Please click the link to contine the signup process."
        val text = SpannableString("$firstText$email$secondString")
//        text.setSpan(RelativeSizeSpan(1.5f), text.length - "stackOverflow".length, text.length,
//                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(ForegroundColorSpan(Color.BLACK), firstText.length, firstText.length + email.length + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtEmailMessage.setText(text);

    }

    override fun initListener() {
        llOuterVerifyEmail.setOnClickListener(this)
        llVerifyEmail.setOnClickListener(this)
        txtRESENDEMAIL.setOnClickListener(this)
    }

    override fun getContext() = this

    override fun onClick(view: View?) {
        when (view) {
            txtRESENDEMAIL -> {
                if (connectedToInternet())
                    hitResendEmailAPI()
                else
                    showInternetAlert(txtRESENDEMAIL)
            }
            llOuterVerifyEmail -> {
//                translateView(0f, mHeight.toFloat(), true)
            }
        }
    }

    private fun translateView(fromY: Float, toY: Float, finishActivity: Boolean) {
        val translateAnimation = TranslateAnimation(0f, 0f, fromY, toY)
        translateAnimation.fillAfter = true
        translateAnimation.duration = 300
        llVerifyEmail.startAnimation(translateAnimation)

        translateAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                if (finishActivity) {
                    moveBack()
                }
            }

            override fun onAnimationStart(p0: Animation?) {

            }
        })
    }

    private fun hitResendEmailAPI() {
        showLoader()
        val call = RetrofitClient.getInstance().resendEmail(mUtils.getString("accessToken", ""))
        call.enqueue(object : Callback<BaseSuccessModel> {
            override fun onFailure(call: Call<BaseSuccessModel>?, t: Throwable?) {
                dismissLoader()
                showAlert(txtRESENDEMAIL, t!!.localizedMessage)
            }

            override fun onResponse(call: Call<BaseSuccessModel>?, response: Response<BaseSuccessModel>) {
                dismissLoader()
                if (response.body().error != null) {
                    showAlert(txtRESENDEMAIL, response.body().error.message)
                } else {
                    showToast(mContext, response.body().response.message)
                }
            }
        })
    }


    internal var receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            mUtils.setBoolean("addEmailFragment", false)
            mUtils.setInt("emailVerified", 1)
            if (getIntent().hasExtra(InterConst.INTEND_EXTRA)) {
                moveToSplash()
            } else {
                val inStarted = Intent(mContext, RegisterCoachActivity::class.java)
                inStarted.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                inStarted.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(inStarted)
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mUtils.setInt("inside_verify", 1)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,
                IntentFilter(Constants.EMAIL_VERIFY))
    }

    override fun onStop() {
        mUtils.setInt("inside_verify", 0)
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }

    override fun onBackPressed() {
        translateView(0f, mHeight.toFloat(), true)
    }

    private fun moveBack() {
        finish()
        overridePendingTransition(0, 0)
    }

}