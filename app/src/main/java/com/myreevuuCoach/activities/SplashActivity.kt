package com.myreevuuCoach.activities

import android.content.Intent
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.myreevuuCoach.R
import com.myreevuuCoach.interfaces.InterConst
import com.myreevuuCoach.models.SignUpModel
import com.myreevuuCoach.network.RetrofitClient
import me.leolin.shortcutbadger.ShortcutBadger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SplashActivity : BaseKotlinActivity() {

    override fun getContentView() = R.layout.activity_splash

    override fun initUI() {
        getFirebaseToken()
        if (mUtils.getInt(InterConst.APP_ICON_BADGE_COUNT, 0) == 0) {
            mUtils.setInt(InterConst.APP_ICON_BADGE_COUNT, 0)
            ShortcutBadger.removeCount(mContext)
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

    override fun onCreateStuff() {

        Handler().postDelayed({
            if (mUtils.getInt("emailVerified", 0) == 1) {
                when {
                    mUtils.getInt("profileStatus", 0) == 0 -> navigateToNext(RegisterCoachActivity())
                    mUtils.getInt("profileStatus", 0) == 1 -> navigateToNext(CreateProfileActivity())
                    mUtils.getInt("profileStatus", 0) == 2 -> navigateToNext(LandingActivity())
                }
            } else {
                if (!TextUtils.isEmpty(mUtils.getString(InterConst.ACCESS_TOKEN, "")) ||
                        mUtils.getInt(InterConst.ID, -1) != -1) {

                    val signupIntent = Intent(mContext, VerifyEmailActivity::class.java)
                    startActivity(signupIntent)
                    overridePendingTransition(0, 0)
                } else {
                    navigateToNext(WalkthroughActivity())
                }
            }
        }, 2000)
    }

    private fun navigateToNext(activity: AppCompatActivity) {
        if (mUtils.getInt(InterConst.PROFILE_APPROVED, 0) == 0 &&  mUtils.getInt("profileStatus", 0) == 2) {
            hitCoachProfileApi()
        } else {
            val nextIntent = Intent(this@SplashActivity, activity::class.java)
            nextIntent.putExtra("displayAnimation", true)
            startActivity(nextIntent)
            finish()
            overridePendingTransition(0, 0)
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


    override fun initListener() {

    }

    override fun onClick(p0: View?) {
        // no op
    }

    override fun getContext() = this

}
