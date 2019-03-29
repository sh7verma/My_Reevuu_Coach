package com.myreevuuCoach.activities

import android.content.Intent
import android.graphics.Typeface
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.myreevuuCoach.R
import com.myreevuuCoach.firebase.MessageHistoryModel
import com.myreevuuCoach.interfaces.InterConst
import com.myreevuuCoach.models.SignUpModel
import com.myreevuuCoach.network.RetrofitClient
import com.myreevuuCoach.utils.Constants
import kotlinx.android.synthetic.main.activity_signin.*
import me.leolin.shortcutbadger.ShortcutBadger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SigninActivity : BaseKotlinActivity() {

    override fun getContentView() = R.layout.activity_signin

    override fun initUI() {
        val typeface = Typeface.createFromAsset(assets, "regular.ttf")

        edEmail.typeface = typeface
        edPassword.typeface = typeface

    }

    override fun onCreateStuff() {
        getFirebaseToken()
        if (mUtils.getInt(InterConst.APP_ICON_BADGE_COUNT, 0) == 0) {
            mUtils.setInt(InterConst.APP_ICON_BADGE_COUNT, 0)
            ShortcutBadger.removeCount(mContext)
        }
    }

    override fun initListener() {
        txtSIGNIN.setOnClickListener(this)
        txtSignup.setOnClickListener(this)
        txtForgotPassword.setOnClickListener(this)
    }

    override fun getContext() = this

    override fun onClick(view: View?) {
        val signinIntent: Intent
        when (view) {
            txtForgotPassword -> {
                signinIntent = Intent(mContext, ForgotPassword::class.java)
                startActivity(signinIntent)
                overridePendingTransition(0, 0)
            }
            txtSIGNIN -> {
                verifyDetails()
            }
            txtSignup -> {
                signinIntent = Intent(mContext, SignupActivity::class.java)
                startActivity(signinIntent)
                finish()
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
            }
        }
    }

    private fun verifyDetails() {
        if (edEmail.text.toString().trim { it <= ' ' }.isEmpty())
            showAlert(txtSIGNIN, resources.getString(R.string.enter_email))
        else if (!validateEmail(edEmail.text.toString().trim()))
            showAlert(txtSIGNIN, resources.getString(R.string.enter_valid_email))
        else if (edEmail.text.toString().trim().startsWith("."))
            showAlert(txtSIGNIN, resources.getString(R.string.enter_valid_email))
        else if (edPassword.text.toString().trim { it <= ' ' }.isEmpty())
            showAlert(txtSIGNIN, resources.getString(R.string.enter_password))
        else if (edPassword.text.toString().trim { it <= ' ' }.length < 8)
            showAlert(txtSIGNIN, resources.getString(R.string.error_password))
        else {
            if (connectedToInternet()) {
                Constants.closeKeyboard(this, txtSIGNIN)
                hitLoginAPI()
            } else
                showInternetAlert(txtSIGNIN)
        }
    }

    private fun getFirebaseToken() {
        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        return@OnCompleteListener
                    }
                    val token = task.result!!.token
                    mUtils.setString("deviceToken", token)
                })
    }


    private fun hitLoginAPI() {
        showLoader()
        val call = RetrofitClient.getInstance().userLogin(edEmail.text.toString().trim(),
                edPassword.text.toString().trim(),
                mUtils.getString("deviceToken", ""),
                mPlatformStatus)
        call.enqueue(object : Callback<SignUpModel> {
            override fun onFailure(call: Call<SignUpModel>?, t: Throwable?) {
                dismissLoader()
                showAlert(txtSIGNIN, t!!.localizedMessage)
            }

            override fun onResponse(call: Call<SignUpModel>?, response: Response<SignUpModel>) {
                dismissLoader()
                if (response.body().error != null) {
                    showAlert(txtSIGNIN, response.body().error.message)
                } else {
                    setUserData(response.body())
                    if (response.body().response.email_verified == 0) {
                        moveToVerifyEmail()
                    } else {
                        if (response.body().response.profile_status == 0) {
                            moveToRegisterCoach()
                        } else if (response.body().response.profile_status == 1) {
                            moveToCreateProfile()
                        } else if (response.body().response.profile_status == 2) {
                            hitHistoryApi()
                        }
                    }
                }
            }
        })
    }

    internal fun hitHistoryApi() {
        showLoader()
        val call = RetrofitClient.getInstance().getChatHistory(mUtils!!.getString(InterConst.ACCESS_TOKEN, ""))
        call.enqueue(object : Callback<MessageHistoryModel> {
            override fun onResponse(call: Call<MessageHistoryModel>, response: Response<MessageHistoryModel>) {
                if (response.body().response != null) {
                    dismissLoader()
                    mDb!!.addMessagesHistory(response.body().response, mUtils.getInt(InterConst.ID, -1).toString())
                    moveToLanding()
                } else {
                    dismissLoader()
                    if (response.body().error!!.code == InterConst.INVALID_ACCESS) {
                        Toast.makeText(mContext!!, response.body().error!!.message, Toast.LENGTH_SHORT).show()
                        moveToSplash()
                    } else
                        showAlert(txtSIGNIN, response.body().error!!.message!!)
                }
            }

            override fun onFailure(call: Call<MessageHistoryModel>, t: Throwable) {
                dismissLoader()
            }
        })
    }

    private fun moveToLanding() {
        registerOnInterCom()
        subscribeOnFirebase()
        if (mUtils.getInt(InterConst.PROFILE_APPROVED, 0) == 0) {
            hitCoachProfileApi()
        } else {
            val verifyIntent = Intent(mContext, LandingActivity::class.java)
            startActivity(verifyIntent)
            finish()
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
        }
    }

    fun hitCoachProfileApi() {
        if (connectedToInternet()) {
            showLoader()
            val call = RetrofitClient.getInstance().coach_profile(
                    mUtils.getString(InterConst.ACCESS_TOKEN, ""),
                    mUtils.getInt(InterConst.ID, -1).toString())
            call.enqueue(object : Callback<SignUpModel> {
                override fun onResponse(call: Call<SignUpModel>, response: Response<SignUpModel>) {
                    dismissLoader()
                    if (response.body().response != null) {
                        mUtils.setInt(InterConst.PROFILE_APPROVED, response.body().response.is_approved)
                        if (mUtils.getInt(InterConst.PROFILE_APPROVED, 0) == 0) {
                            val intent = Intent(mContext, ProfileNotApprovedActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        showAlert(response.body().error.message)
                        if (response.body().error.code == InterConst.INVALID_ACCESS) {
                            moveToSplash()
                        }
                    }
                }

                override fun onFailure(call: Call<SignUpModel>, t: Throwable) {
                    dismissLoader()
                    showAlert(t.message!!)
                }
            })
        }
    }


    private fun moveToRegisterCoach() {
        val verifyIntent = Intent(mContext, RegisterCoachActivity::class.java)
        startActivity(verifyIntent)
        finish()
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
    }

    private fun moveToCreateProfile() {
        val verifyIntent = Intent(mContext, CreateProfileActivity::class.java)
        startActivity(verifyIntent)
        finish()
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
    }

    private fun moveToVerifyEmail() {
        val signupIntent = Intent(mContext, VerifyEmailActivity::class.java)
        startActivity(signupIntent)
        overridePendingTransition(0, 0)
    }

    internal fun validateEmail(text: CharSequence): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        val matcher = pattern.matcher(text)
        return matcher.matches()
    }

}