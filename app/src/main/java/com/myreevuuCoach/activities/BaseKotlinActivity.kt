package com.myreevuuCoach.activities

import android.app.Activity
import android.app.NotificationManager
import android.app.job.JobScheduler
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import butterknife.ButterKnife
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.myreevuuCoach.R
import com.myreevuuCoach.firebase.Database
import com.myreevuuCoach.firebase.FirebaseChatConstants
import com.myreevuuCoach.firebase.FirebaseListeners
import com.myreevuuCoach.firebase.ProfileModel
import com.myreevuuCoach.interfaces.InterConst
import com.myreevuuCoach.models.SignUpModel
import com.myreevuuCoach.services.ListenerService
import com.myreevuuCoach.utils.AlertDialogs
import com.myreevuuCoach.utils.ConnectionDetector
import com.myreevuuCoach.utils.CustomLoadingDialog
import com.myreevuuCoach.utils.Utils
import io.intercom.android.sdk.Intercom
import io.intercom.android.sdk.UserAttributes
import io.intercom.android.sdk.identity.Registration
import io.intercom.android.sdk.push.IntercomPushClient
import kotlinx.android.synthetic.main.anim_overlay.*
import org.apache.commons.lang3.text.WordUtils
import timber.log.Timber
import java.util.*

abstract class BaseKotlinActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var mContext: Context
    lateinit var mErrorInternet: String
    lateinit var mAPIError: String
    var mWidth: Int = 0
    var mHeight: Int = 0
    var mPlatformStatus: Int = 2
    lateinit var mUtils: Utils
    val platformType = "2"
    var userType = 1
    var mGson = Gson()
    var mDb: Database? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentView())

        mContext = getContext()
        mUtils = Utils(this)
        ButterKnife.bind(this)
        getDefaults()
        mDb = Database(mContext)
        initUI()
        onCreateStuff()
        initListener()
        mErrorInternet = getString(R.string.error_internet)
    }

    abstract fun getContentView(): Int
    abstract fun initUI() /// Alter UI here
    abstract fun onCreateStuff() /// Initalize Variables here
    abstract fun initListener() /// Initalize Click Listener Here
    abstract fun getContext(): Context /// Initalize Activity Context

    fun getDefaults() {
        val display = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(display)
        mWidth = display.widthPixels
        mHeight = display.heightPixels
        mUtils.setInt("width", mWidth)
        mUtils.setInt("height", mHeight)
        Timber.e(mWidth.toString())
        Timber.e(mHeight.toString())
    }

    fun subscribeOnFirebase() {
        val firebaseTopicList = ArrayList<String>()
        firebaseTopicList.add(InterConst.FIREBASE_TOPIC_1)
        firebaseTopicList.add(InterConst.FIREBASE_TOPIC_2)
        firebaseTopicList.add(InterConst.FIREBASE_TOPIC_3)

        for (i in firebaseTopicList.indices) {
            FirebaseMessaging.getInstance().subscribeToTopic(firebaseTopicList[i])
                    .addOnCompleteListener { Log.d("subscribeToTopic", "subscribeToTopic Success") }
        }

    }

    fun connectedToInternet() = (ConnectionDetector(mContext).isConnectingToInternet)

    fun showAlert(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

    internal fun showAlert(message: String) {
        AlertDialogs.tryAgainDialog(mContext, getString(R.string.ok), getString(R.string.time_finished)) { finish() }
    }


    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun showInternetAlert(view: View) {
        Snackbar.make(view, R.string.internet, Snackbar.LENGTH_SHORT).show()
    }

    fun showLoader() {
        CustomLoadingDialog.getLoader().showLoader(mContext)
    }

    fun dismissLoader() {
        CustomLoadingDialog.getLoader().dismissLoader()
    }

    fun moveToSplash() {
        val mFirebaseConfig = FirebaseDatabase.getInstance().reference.child(FirebaseChatConstants.USERS)
        mFirebaseConfig.child("id_" + mUtils!!.getInt(FirebaseChatConstants.user_id, -1))
                .child("online_status").setValue(ServerValue.TIMESTAMP)
        FirebaseListeners.getListenerClass(this).RemoveAllListeners()
        Intercom.client().displayMessenger();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            val jobScheduler = mContext.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            jobScheduler.cancelAll()
        }

        mContext.stopService(Intent(mContext.applicationContext, ListenerService::class.java))

        val notificationManager = mContext
                .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
        stopService(Intent(applicationContext, ListenerService::class.java))
        mUtils.clear_shf()
        mDb!!.deleteAllTables()
        val inSplash = Intent(mContext, SignupActivity::class.java)
        inSplash.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        inSplash.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        mContext.startActivity(inSplash)
    }

    fun hideKeyboard(mContext: Activity) {
        // Check if no views has focus:
        val view = mContext.currentFocus
        if (view != null) {
            val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private val intercomPushClient = IntercomPushClient()

    fun setUserData(response: SignUpModel) {

        mUtils.setString(InterConst.RESPONSE, mGson.toJson(response))

        mUtils.setString(InterConst.EMAIL, response.response.email)
        mUtils.setString(InterConst.USER_NAME, response.response.username)
        mUtils.setInt(InterConst.USER_TYPE, response.response.user_type)

        mUtils.setString(InterConst.PHONE_NUMBER, response.response.phone_number)
        mUtils.setInt(InterConst.GENDER_STATUS, response.response.gender)
        mUtils.setString(InterConst.NAME, response.response.name)
        mUtils.setString(InterConst.PROFILE_PIC, response.response.profile_pic)
        mUtils.setInt(InterConst.ID, response.response.id)

        mUtils.setString(InterConst.EMAIL, response.response.email)
        mUtils.setString(InterConst.ACCESS_TOKEN, response.response.access_token)
        mUtils.setInt(InterConst.EMAIL_VERIFIED, response.response.email_verified)
        mUtils.setInt(InterConst.PROFILE_STATUS, response.response.profile_status)

        mUtils.setString(InterConst.NEW_EMAIL, response.response.new_email)

        mUtils.setInt(InterConst.EMAIL_PUSH_STATUS, response.response.email_notification)
        intercomPushClient.sendTokenToIntercom(application, mUtils.getString("deviceToken", ""))

        mUtils.setString(InterConst.REFERRAL_CODE, response.response.referral_code)


        if (mUtils.getInt(InterConst.GENDER_STATUS, -1) == 0) {
            mUtils.setString(InterConst.GENDER, InterConst.FEMALE)
        } else if (mUtils.getInt(InterConst.GENDER_STATUS, -1) == 1) {
            mUtils.setString(InterConst.GENDER, InterConst.MALE)
        } else if (mUtils.getInt(InterConst.GENDER_STATUS, -1) == 2) {
            mUtils.setString(InterConst.GENDER, InterConst.OTHER)
        }
        mUtils.setInt(InterConst.PROFILE_APPROVED, response.response.is_approved)
        setFireBaseData()
    }

    fun setFireBaseData() {
        val values = HashMap<String, Any>()
        values.put("user_id", mUtils.getInt(FirebaseChatConstants.user_id, -1).toString())
        values.put("online_status", FirebaseChatConstants.ONLINE_LONG)
        values.put("access_token", mUtils.getString(InterConst.ACCESS_TOKEN, ""))
        values.put("user_name", mUtils.getString(InterConst.NAME, ""))
        values.put("user_pic", mUtils.getString(InterConst.PROFILE_PIC, ""))
        val mFirebaseConfigProfile = FirebaseDatabase.getInstance().getReference().child(FirebaseChatConstants.USERS)
        mFirebaseConfigProfile.child("id_" + mUtils.getInt(FirebaseChatConstants.user_id, -1)).updateChildren(values)

        val model = ProfileModel()
        model.access_token = mUtils.getString(InterConst.ACCESS_TOKEN, "")
        model.user_id = mUtils.getInt(FirebaseChatConstants.user_id, -1).toString()
        model.online_status = FirebaseChatConstants.ONLINE_LONG
        model.user_name = mUtils.getString(InterConst.NAME, "")
        model.user_pic = mUtils.getString(InterConst.PROFILE_PIC, "")
        mDb!!.addProfile(model)
    }


    fun displaySplashAnimation() {
        rlOverlay.alpha = 1f
        val scaleAnimation = ScaleAnimation(1f, 3f, 1f, 3f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f)
        scaleAnimation.duration = 300

        val alphaAnimation = AlphaAnimation(1f, 0f)
        alphaAnimation.duration = 300

        val animatorSet = AnimationSet(true)
        animatorSet.addAnimation(scaleAnimation)
        animatorSet.addAnimation(alphaAnimation)
        rlOverlay.startAnimation(animatorSet)

        animatorSet.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                rlOverlay.alpha = 0f
            }

            override fun onAnimationStart(p0: Animation?) {
            }
        })
    }

    protected fun registerOnInterCom() {
        //Register a user with Intercom
        val intercomClient = Intercom.client()
        intercomClient.registerIdentifiedUser(Registration.create().withUserId(mUtils.getInt(InterConst.ID, -1).toString() + ""))
//        intercomClient.hideMessenger()
//        intercomClient.setInAppMessageVisibility(Intercom.GONE)
        intercomPushClient.sendTokenToIntercom(application, mUtils.getString(InterConst.FCM_TOKEN, ""))
    }

    protected fun updateOnInterCom() {
        //Register a user with Intercom
        //Intercom.client().registerIdentifiedUser(Registration.create().withUserId(utils.getString(Const.EMAIL, "")));
        val userAttributes = UserAttributes.Builder()
                .withName(WordUtils.capitalize(mUtils.getString(InterConst.NAME, "")))
                .withEmail(mUtils.getString(InterConst.EMAIL, ""))
                .withUserId(mUtils.getInt(InterConst.ID, -1).toString() + "")
                .build()
        Intercom.client().updateUser(userAttributes)

    }
}