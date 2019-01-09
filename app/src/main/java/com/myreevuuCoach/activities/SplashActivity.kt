package com.myreevuuCoach.activities

import android.content.Intent
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.myreevuuCoach.R
import com.myreevuuCoach.interfaces.InterConst
import me.leolin.shortcutbadger.ShortcutBadger


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
            } else
                navigateToNext(WalkthroughActivity())
        }, 2000)
    }

    private fun navigateToNext(activity: AppCompatActivity) {
        val nextIntent = Intent(this@SplashActivity, activity::class.java)
        nextIntent.putExtra("displayAnimation", true)
        startActivity(nextIntent)
        finish()
        overridePendingTransition(0, 0)
    }


    override fun initListener() {

    }

    override fun onClick(p0: View?) {
        // no op
    }

    override fun getContext() = this

}
