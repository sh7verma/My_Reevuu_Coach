package com.myreevuuCoach.activities

import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.myreevuuCoach.R
import com.myreevuuCoach.interfaces.InterConst


class DeepLinkingActivity : BaseActivity() {
    override fun getContentView() = R.layout.activity_deeplinking

    override fun initUI() {
    }

    override fun onCreateStuff() {
        handleDeepLinking()


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

    fun handleDeepLinking() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(intent)
                .addOnSuccessListener(this) { pendingDynamicLinkData ->
                    // Get deep link from result (may be null if no link is found)
                    var deepLink: Uri? = null
                    if (pendingDynamicLinkData != null) {
                        deepLink = pendingDynamicLinkData.link
                    }

                    // Handle the deep link. For example, open the linked
                    // content, or apply promotional credit to the user's
                    // account.
                    // ...
                    if (deepLink != null
                            && deepLink.getBooleanQueryParameter("invitedby", false)) {
                        val referrerUid = deepLink.getQueryParameter("invitedby")

                        if (mUtils.getInt("profileStatus", 0) != 2)
                            mUtils.setString(InterConst.REFERRED_BY, referrerUid)
                    }
                    // ...
                }
                .addOnFailureListener(this) { e -> Log.w("SPLASH", "getDynamicLink:onFailure", e) }
    }


    override fun initListener() {
    }

    override fun getContext() = this

    override fun onClick(p0: View?) {
    }
}