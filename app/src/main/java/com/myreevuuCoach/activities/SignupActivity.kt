package com.myreevuuCoach.activities

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.graphics.Typeface
import android.util.Patterns
import android.view.View
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.myreevuuCoach.R
import com.myreevuuCoach.interfaces.InterConst
import com.myreevuuCoach.models.SignUpModel
import com.myreevuuCoach.network.RetrofitClient
import com.myreevuuCoach.utils.Constants
import kotlinx.android.synthetic.main.activity_signup.*
import me.leolin.shortcutbadger.ShortcutBadger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignupActivity : BaseKotlinActivity() {

    override fun getContentView(): Int {
        val decorView = window.decorView
        decorView.setBackgroundResource(R.mipmap.bg_login)
        return R.layout.activity_signup
    }

    override fun initUI() {

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()

        val typeface = Typeface.createFromAsset(assets, "regular.ttf")

        edName.typeface = typeface
        edEmail.typeface = typeface
        edPassword.typeface = typeface


        svSignup.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            svSignup.getWindowVisibleDisplayFrame(r)
            val heightDiff = svSignup.rootView.height - (r.bottom - r.top)

            if (heightDiff > Constants.dpToPx(100)) { // if more than 100 pixels, its probably a keyboard...
                //ok now we know the keyboard is up...
//                llCheckbox.setPadding(0, 0, 0, resources.getDimension(R.dimen._140sdp).toInt())
                llBottom.visibility = View.INVISIBLE
            } else {
                //ok now we know the keyboard is down...
//                llCheckbox.setPadding(0, 0, 0, 0)
                llBottom.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateStuff() {
        getFirebaseToken()
        if (mUtils.getInt(InterConst.APP_ICON_BADGE_COUNT, 0) == 0) {
            mUtils.setInt(InterConst.APP_ICON_BADGE_COUNT, 0)
            ShortcutBadger.removeCount(mContext)
        }
    }

    override fun initListener() {
        txtSIGNUP.setOnClickListener(this)
        txtSignin.setOnClickListener(this)
        tv_privacy.setOnClickListener(this)
    }

    override fun getContext() = this

    override fun onClick(view: View?) {
        val signupIntent: Intent
        when (view) {
            txtSIGNUP -> {
                verifyDetails()
            }
            txtSignin -> {
                signupIntent = Intent(mContext, SigninActivity::class.java)
                startActivity(signupIntent)
                finish()
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
            }
            tv_privacy -> {
                showAlert(txtSIGNUP, getString(R.string.work_progress))
            }
        }
    }

    private fun verifyDetails() {
        if (edName.text.trim().toString().length < 2)
            showAlert(txtSIGNUP, getString(R.string.error_name))
        else if (edEmail.text.toString().trim { it <= ' ' }.isEmpty())
            showAlert(txtSIGNUP, resources.getString(R.string.enter_email))
        else if (!validateEmail(edEmail.text.toString().trim()))
            showAlert(txtSIGNUP, resources.getString(R.string.enter_valid_email))
        else if (edEmail.text.toString().trim().startsWith("."))
            showAlert(txtSIGNUP, resources.getString(R.string.enter_valid_email))
        else if (edPassword.text.toString().trim { it <= ' ' }.isEmpty())
            showAlert(txtSIGNUP, resources.getString(R.string.enter_password))
        else if (edPassword.text.toString().trim { it <= ' ' }.length < 8)
            showAlert(txtSIGNUP, resources.getString(R.string.error_password))
        else if (!cbAgreeTerms.isChecked)
            showAlert(txtSIGNUP, resources.getString(R.string.error_terms))
        else {
            if (connectedToInternet()) {
                Constants.closeKeyboard(this, txtSIGNUP)
                hitAPI()
            } else
                showInternetAlert(txtSIGNUP)
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


    private fun hitAPI() {
        showLoader()
        val call = RetrofitClient.getInstance().userSignup(edName.text.toString().trim(),
                edEmail.text.toString().trim(),
                edPassword.text.toString().trim(),
                mUtils.getString("deviceToken", ""),
                platformType)
        call.enqueue(object : Callback<SignUpModel> {
            override fun onFailure(call: Call<SignUpModel>?, t: Throwable) {
                dismissLoader()
                showAlert(txtSIGNUP, t.localizedMessage)
            }

            override fun onResponse(call: Call<SignUpModel>?, response: Response<SignUpModel>) {
                dismissLoader()
                if (response.body().error != null) {
                    showAlert(txtSIGNUP, response.body().error.message)
                } else {
                    setUserData(response.body())
                    moveToVerifyEmail()
                }
            }
        })
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