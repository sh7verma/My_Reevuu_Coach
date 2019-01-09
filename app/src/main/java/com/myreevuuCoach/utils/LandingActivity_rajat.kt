//package com.myreevuuCoach.utils
//
//import android.support.v4.app.Fragment
//import android.view.View
//import android.view.animation.AlphaAnimation
//import android.view.animation.Animation
//import android.view.animation.AnimationSet
//import android.view.animation.ScaleAnimation
//import com.myreevuuCoach.R
//import com.myreevuuCoach.activities.BaseKotlinActivity
//import com.myreevuuCoach.fragments.HomeKotlinFragment
//import kotlinx.android.synthetic.main.activity_landing.*
//import kotlinx.android.synthetic.main.anim_overlay.*
//
//class LandingActivity_rajat : BaseKotlinActivity() {
//
//    override fun getContentView() = R.layout.activity_landing
//
//    override fun initUI() {
//
//    }
//
//    override fun onCreateStuff() {
//        if (intent.hasExtra("displayAnimation"))
//            displaySplashAnimation()
//
//        replaceFragment(HomeKotlinFragment())
//
//        if (mUtils.getInt("displayTip", 0) == 0)
//            rlTips.visibility = View.VISIBLE
//        else
//            rlTips.visibility = View.GONE
//    }
//
//    override fun initListener() {
//        txtGOTIT.setOnClickListener(this)
//        rlTips.setOnClickListener(this)
//        llProfile.setOnClickListener(this)
//        llReview.setOnClickListener(this)
//        llChat.setOnClickListener(this)
//        llHome.setOnClickListener(this)
//        imgUploadVideo.setOnClickListener(this)
//    }
//
//    override fun getContext() = this
//
//    override fun onClick(view: View?) {
//        when (view) {
//            txtGOTIT -> {
//                mUtils.setInt("displayTip", 1)
//                rlTips.visibility = View.GONE
//            }
//            llProfile -> {
//                showAlert(llBottomNavigation, getString(R.string.work_progress))
//            }
//            llChat -> {
//                showAlert(llBottomNavigation, getString(R.string.work_progress))
//            }
//            llHome -> {
//            }
//            llReview -> {
//                showAlert(llBottomNavigation, getString(R.string.work_progress))
//            }
//            imgUploadVideo -> {
//                showAlert(llBottomNavigation, getString(R.string.work_progress))
//            }
//        }
//    }
//
//    private fun replaceFragment(fragment: Fragment) {
//        supportFragmentManager.beginTransaction().replace(R.id.llContainer, fragment, null).commit()
//    }
//
//    private fun displaySplashAnimation() {
//        rlOverlay.alpha = 1f
//        val scaleAnimation = ScaleAnimation(1f, 3f, 1f, 3f, Animation.RELATIVE_TO_SELF, 0.5f,
//                Animation.RELATIVE_TO_SELF, 0.5f)
//        scaleAnimation.duration = 300
//
//        val alphaAnimation = AlphaAnimation(1f, 0f)
//        alphaAnimation.duration = 300
//
//        val animatorSet = AnimationSet(true)
//        animatorSet.addAnimation(scaleAnimation)
//        animatorSet.addAnimation(alphaAnimation)
//        rlOverlay.startAnimation(animatorSet)
//
//        animatorSet.setAnimationListener(object : Animation.AnimationListener {
//            override fun onAnimationRepeat(p0: Animation?) {
//            }
//
//            override fun onAnimationEnd(p0: Animation?) {
//                rlOverlay.alpha = 0f
//            }
//
//            override fun onAnimationStart(p0: Animation?) {
//            }
//        })
//    }
//
//}
