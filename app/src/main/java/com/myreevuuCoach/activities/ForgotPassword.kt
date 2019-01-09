package com.myreevuuCoach.activities

import android.support.v4.content.ContextCompat
import android.util.Patterns
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import com.myreevuuCoach.R
import com.myreevuuCoach.models.BaseSuccessModel
import com.myreevuuCoach.network.RetrofitClient
import com.myreevuuCoach.utils.Constants
import kotlinx.android.synthetic.main.activity_forgot_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPassword : BaseKotlinActivity() {
    override fun getContentView(): Int {
        this.window.setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.colorTransperent));
        return R.layout.activity_forgot_password
    }

    override fun initUI() {
        translateView(mHeight.toFloat(), 0f, false)
    }

    override fun onCreateStuff() {

    }

    override fun initListener() {
        llOuterForgot.setOnClickListener(this)
        llForgotPassword.setOnClickListener(this)
        txtRESET.setOnClickListener(this)
    }

    override fun getContext() = this

    override fun onClick(view: View?) {
        when (view) {
            txtRESET -> {
                verifyDetails()
            }
            llOuterForgot -> {
                Constants.closeKeyboard(mContext, llOuterForgot)
                translateView(0f, mHeight.toFloat(), true)
            }
        }
    }

    private fun verifyDetails() {
        if (edEmail.text.toString().trim { it <= ' ' }.isEmpty())
            showAlert(txtRESET, resources.getString(R.string.enter_email))
        else if (!validateEmail(edEmail.text.toString().trim()))
            showAlert(txtRESET, resources.getString(R.string.enter_valid_email))
        else if (edEmail.text.toString().trim().startsWith("."))
            showAlert(txtRESET, resources.getString(R.string.enter_valid_email))
        else {
            hitForgotPasswordAPI()
        }
    }

    private fun hitForgotPasswordAPI() {
        showLoader()
        val call = RetrofitClient.getInstance().forgotPassword(edEmail.text.toString().trim(), userType)
        call.enqueue(object : Callback<BaseSuccessModel> {
            override fun onFailure(call: Call<BaseSuccessModel>?, t: Throwable?) {
                dismissLoader()
                showAlert(txtRESET, t!!.localizedMessage)
            }

            override fun onResponse(call: Call<BaseSuccessModel>?, response: Response<BaseSuccessModel>) {
                dismissLoader()
                if (response.body().error != null) {
                    showAlert(txtRESET, response.body().error.message)
                } else {
                    showToast(mContext, response.body().response.message)
                    moveBack()
                }
            }
        })
    }

    private fun translateView(fromY: Float, toY: Float, finishActivity: Boolean) {
        val translateAnimation = TranslateAnimation(0f, 0f, fromY, toY)
        translateAnimation.fillAfter = true
        translateAnimation.duration = 300
        llForgotPassword.startAnimation(translateAnimation)

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

    override fun onBackPressed() {
        Constants.closeKeyboard(mContext, llOuterForgot)
        translateView(0f, mHeight.toFloat(), true)
    }

    private fun moveBack() {
        finish()
        overridePendingTransition(0, 0)
    }

    internal fun validateEmail(text: CharSequence): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        val matcher = pattern.matcher(text)
        return matcher.matches()
    }

}