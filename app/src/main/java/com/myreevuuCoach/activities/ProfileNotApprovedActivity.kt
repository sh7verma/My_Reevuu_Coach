package com.myreevuuCoach.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v4.content.LocalBroadcastManager
import android.view.Gravity
import android.view.View
import com.myreevuuCoach.R
import com.myreevuuCoach.interfaces.InterConst
import com.myreevuuCoach.utils.RoundedTransformation
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile_not_approved.*

/**
 * Created by dev on 16/3/19.
 */

class ProfileNotApprovedActivity : BaseActivity() {

    override fun getContentView(): Int {
        return R.layout.activity_profile_not_approved
    }

    override fun onCreateStuff() {
        setData()
    }

     var receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val inStarted = Intent(mContext, LandingActivity::class.java)
            inStarted.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            inStarted.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(inStarted)
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }
    }

    fun setData() {
        if (!mUtils.getString(InterConst.PROFILE_PIC, "").equals("", ignoreCase = true)) {
            Picasso.get()
                    .load(mUtils.getString(InterConst.PROFILE_PIC, ""))
                    .transform(RoundedTransformation(mHeight * 10, 0))
                    .resize((mHeight * 0.20).toInt(), (mHeight * 0.20).toInt())
                    .placeholder(R.mipmap.ic_profile)
                    .centerCrop(Gravity.TOP)
                    .error(R.mipmap.ic_profile).into(imgUser)
        } else {
            Picasso.get()
                    .load(R.mipmap.ic_profile)
                    .transform(RoundedTransformation(mHeight * 10, 0))
                    .resize((mHeight * 0.20).toInt(), (mHeight * 0.20).toInt())
                    .placeholder(R.mipmap.ic_profile)
                    .centerCrop(Gravity.TOP)
                    .error(R.mipmap.ic_profile).into(imgUser)
        }
    }

    override fun initUI() {

    }

    override fun initListener() {
        txtLogout.setOnClickListener(this)
    }

    override fun getContext(): Context? {
        return this
    }

    override fun onClick(view: View) {
        when (view) {
            txtLogout -> {
                moveToSplash()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,
                IntentFilter(InterConst.BROADCAST_PROFILE_APPROVED))
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }

}
