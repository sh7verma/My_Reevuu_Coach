package com.myreevuuCoach.activities

import android.content.*
import android.net.Uri
import android.support.v4.content.LocalBroadcastManager
import android.text.TextUtils
import android.view.View
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.myreevuuCoach.R
import com.myreevuuCoach.interfaces.InterConst
import com.myreevuuCoach.network.RetrofitClient
import com.myreevuuCoach.utils.AlertDialogs
import com.myreevuuCoach.utils.Constants
import io.intercom.android.sdk.Intercom
import kotlinx.android.synthetic.main.activity_settings.*

/**
 * Created by dev on 4/12/18.
 */

class SettingsActivity : BaseActivity() {

    /*BroadCast PUSH REACIVED*/
    var receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.hasExtra(Constants.NEW_MESSAGE_FROM_ADMIN)) {
                setUnreadCount()
            }
        }
    }

    private fun createLink() {
        val referCode = mUtils.getString(InterConst.REFERRAL_CODE, "")
        val link = "https://myreevuu.com/?invitedby=$referCode"
        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(link))
                .setDomainUriPrefix("https://myrevvuucoach.page.link")
                .setAndroidParameters(DynamicLink.AndroidParameters.Builder().build())
                .setIosParameters(
                        DynamicLink.IosParameters.Builder("com.ReeVuu-Coach")
                                .setAppStoreId("1337671978")
                                .setMinimumVersion("1.0")
                                .build())
                .buildShortDynamicLink()
                .addOnSuccessListener { shortDynamicLink ->
                    shortDynamicLink.shortLink
                    share(shortDynamicLink.shortLink)
                }
    }

    override fun getContentView(): Int {
        return R.layout.activity_settings
    }

    override fun onCreateStuff() {
        txtChangePassword!!.setOnClickListener(this)
        txtChangeEmail!!.setOnClickListener(this)
        if (!TextUtils.isEmpty(mUtils.getString(InterConst.NEW_EMAIL, ""))) {
            txtverification!!.visibility = View.VISIBLE
        } else {
            txtverification!!.visibility = View.GONE
        }

    }

    override fun initUI() {

    }

    override fun initListener() {
        imgBack!!.setOnClickListener(this)
        txtLogOut!!.setOnClickListener(this)
        txtNotificationPreferences!!.setOnClickListener(this)
        txtTerms!!.setOnClickListener(this)
        txtFaq!!.setOnClickListener(this)
        txtAboutUs!!.setOnClickListener(this)
        txtPrivacy!!.setOnClickListener(this)
        txtEmailUs!!.setOnClickListener(this)
        flContactUs!!.setOnClickListener(this)
        txtInviteReferal!!.setOnClickListener(this)
    }

    override fun getContext(): Context {
        return this
    }

    override fun onClick(view: View) {
        val intent: Intent
        when (view.id) {
            R.id.flContactUs ->
                //                intent = new Intent(this, ConversationActivity.class);
                //
                //                intent.putExtra(InterConst.PROFILE_PIC, "");
                //                intent.putExtra(InterConst.NAME, InterConst.APP_ADMIN_NAME);
                //
                //                ArrayList<String> mParticpantIDSList = new ArrayList<String>();
                //
                //                mParticpantIDSList.add(InterConst.APP_ADMIN_ID + "_" + FirebaseChatConstants.TYPE_ADMIN);
                //                mParticpantIDSList.add(mUtils.getInt(FirebaseChatConstants.user_id, -1) + "_" + FirebaseChatConstants.TYPE_COACH);
                //                Collections.sort(mParticpantIDSList);
                //
                //                String mParticpantIDS = mParticpantIDSList.toString();
                //                mParticpantIDS = mParticpantIDS.replace(" ", "");
                //                String participants = mParticpantIDS.substring(1, mParticpantIDS.length() - 1);
                //                intent.putExtra("participantIDs", participants);
                //                startActivity(intent);

                Intercom.client().displayMessenger()
            R.id.txtChangePassword -> {
                intent = Intent(mContext, ChangePasswordActivity::class.java)
                startActivity(intent)
            }

            R.id.txtNotificationPreferences -> {
                intent = Intent(mContext, NotificationPreferencesActivity::class.java)
                startActivity(intent)
            }

            R.id.txtChangeEmail -> {
                intent = Intent(mContext, ChangeEmailActivity::class.java)
                startActivity(intent)
            }

            R.id.imgBack -> finish()

            R.id.txtTerms -> openWebview(InterConst.Webview.TERM_CONDITION, RetrofitClient.URL_TERMS_CONDITIONS)

            R.id.txtFaq -> openWebview(InterConst.Webview.FAQs, RetrofitClient.URL_FAQs)

            R.id.txtAboutUs -> openWebview(InterConst.Webview.ABOUT, RetrofitClient.URL_ABOUT_US)

            R.id.txtPrivacy -> openWebview(InterConst.Webview.PRIVACY, RetrofitClient.URL_PRIVACY_POLICY)

            R.id.txtEmailUs -> openEmail()
            R.id.txtInviteReferal -> createLink()
            R.id.txtLogOut -> showAlert()
        }
    }

    internal fun openEmail() {
        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", GOOGLE_GMAIL_ID, null))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "")
        startActivity(Intent.createChooser(emailIntent, "Send email..."))
    }

    fun openWebview(type: InterConst.Webview, urlToOpen: String) {
        val intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra(InterConst.WEBVIEW_TYPE, type)
        intent.putExtra(InterConst.WEBVIEW_URL, urlToOpen)
        startActivity(intent)
    }

    internal fun showAlert() {
        AlertDialogs.confirmYesNoDialog(mContext, "Are you sure you want to log out?", object : AlertDialogs.DialogClick {
            override fun yes(dialog: DialogInterface) {
                moveToSplash()
            }

            override fun no(dialog: DialogInterface) {
                dialog.dismiss()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,
                IntentFilter(Constants.NEW_MESSAGE_FROM_ADMIN))
    }

    public override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }

    private fun setUnreadCount() {
        val count = mUtils.getInt(InterConst.APP_ADMIN_UNREAD_COUNT, 0)
        if (count > 0) {
            txtUnreadBatchCount!!.visibility = View.VISIBLE
            txtUnreadBatchCount!!.text = count.toString() + ""
        } else {
            txtUnreadBatchCount!!.visibility = View.GONE
        }
    }


    fun share(link: Uri) {
        val referrerName = mUtils.getString(InterConst.USER_NAME, "")
        val subject = String.format("%s Download MyReevuu and start earning by reviewing videos,", referrerName)

        val invitationLink = link
        val msg = "Download MyReevuu and start earning by reviewing videos, click on the link to register : $invitationLink or you can directly enter my referral code : ${mUtils.getString(InterConst.REFERRAL_CODE, "")}"
        val msgHtml = String.format("<p>Download MyReevuu and start earning by reviewing videos! Use my " + "<a href=\"%s\">referrer link</a>!</p>", invitationLink)

//        val sendIntent = Intent()
//        sendIntent.action = Intent.ACTION_SEND
//        sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
//        sendIntent.putExtra(Intent.EXTRA_TEXT, msg)
//        sendIntent.putExtra(Intent.EXTRA_HTML_TEXT, msgHtml)
//        sendIntent.type = "text/plain"
//        startActivity(sendIntent)

        val share = Intent(android.content.Intent.ACTION_SEND)
        share.type = "text/plain"
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
        share.putExtra(Intent.EXTRA_TEXT, msg)
        startActivity(Intent.createChooser(share, "Share link!"))

    }

    companion object {
        val GOOGLE_GMAIL_ID = "support@myreevuu.com"
    }

}
