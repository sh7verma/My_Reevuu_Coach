package com.myreevuuCoach.activities

import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.myreevuuCoach.R


class DeepLinkingActivity : BaseActivity() {
    override fun getContentView() = R.layout.activity_deeplinking

    override fun initUI() {
    }

    override fun onCreateStuff() {
        val data = intent.dataString
        if (!TextUtils.isEmpty(data)) {
            val uri = Uri.parse(data)
            val args = uri.queryParameterNames

            Log.e("Arguments = ", args.toString() + " " + uri.path)

            if (mUtils!!.getString("access_token", "").isNotEmpty()) {
                var intent: Intent? = null
                when {
//                    uri.path == "/posts" -> {
//                        intent = if (uri.getQueryParameter("post_type").toInt() == Constants.EVENT)
//                            Intent(mContext!!, EventsDetailActivity::class.java)
//                        else
//                            Intent(mContext!!, CommunityDetailActivity::class.java)
//
//                        intent.putExtra("postId", uri.getQueryParameter("id"))
//                    }
//                    uri.path == "/notes" -> {
//                        intent = Intent(mContext!!, NotesActivity::class.java)
//                        intent.putExtra("noteId", uri.getQueryParameter("id"))
//                        intent.putExtra("noteFileName", uri.getQueryParameter("filename"))
//                    }
//                    uri.path == "/share_user" -> {
//                        intent = Intent(mContext!!, OtherProfileActivity::class.java)
//                        intent.putExtra("otherUserId", uri.getQueryParameter("id"))
//                    }
                }
                startActivity(intent)
                finish()
                overridePendingTransition(0, 0)
            } else {
                val intent = Intent(mContext!!, SplashActivity::class.java)
                startActivity(intent)
                finish()
                overridePendingTransition(0, 0)
            }
        }
    }

    override fun initListener() {
    }

    override fun getContext() = this

    override fun onClick(p0: View?) {
    }
}