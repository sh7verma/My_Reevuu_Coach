package com.myreevuuCoach.activities

import android.app.Activity
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.view.View
import com.myreevuuCoach.R
import com.myreevuuCoach.interfaces.InterConst
import com.myreevuuCoach.models.SkipModel
import com.myreevuuCoach.network.RetrofitClient
import com.myreevuuCoach.utils.Utils
import kotlinx.android.synthetic.main.activity_broadcast.*
import retrofit2.Call
import retrofit2.Response

/**
 * Created by dev on 12/3/19.
 */
class BroadcastActivity : BaseActivity() {

    override fun getContentView(): Int {
        this.window.setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.light_white_transparent))
        return R.layout.activity_broadcast
    }

    override fun initUI() {

    }
    override fun onCreateStuff() {
        txtTitle.text = intent.getStringExtra("broadcastTitle")
        txtMessage.text = intent.getStringExtra("broadcastMessage")

        if (intent.hasExtra(InterConst.NotificationID))
            notificationUpdate("2", intent.getStringExtra(InterConst.NotificationID))
    }

    private fun notificationUpdate(read_type: String, broadCastID: String) {
        if (connectedToInternet(root_View)) {
            val call = RetrofitClient.getInstance().setNotification(
                    mUtils.getString(InterConst.ACCESS_TOKEN, ""),
                    read_type,
                    "",
                    broadCastID)
            call.enqueue(object : retrofit2.Callback<SkipModel> {
                override fun onResponse(call: Call<SkipModel>, response: Response<SkipModel>) {
                    if (response.body()!!.response != null) {

                    } else {
                        if (response.body()!!.error.code == InterConst.INVALID_ACCESS)
                            moveToSplash()
                        else
                            showAlertSnackBar(root_View, response.body()!!.error.message)
                    }
                }

                override fun onFailure(call: Call<SkipModel>, t: Throwable) {
                    showAlertSnackBar(root_View, t.message)
                    t.printStackTrace()
                }
            })
        }
    }

    override fun initListener() {
        txtOK.setOnClickListener {
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
            overridePendingTransition(0, 0)
        }
    }

    override fun getContext() = this

    override fun onClick(p0: View?) {

    }
}
