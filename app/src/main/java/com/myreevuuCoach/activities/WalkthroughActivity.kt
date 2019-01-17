package com.myreevuuCoach.activities

import android.content.Intent
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.TextView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.myreevuuCoach.R
import com.myreevuuCoach.adapters.WalkthroughAdapter
import com.myreevuuCoach.utils.Constants
import kotlinx.android.synthetic.main.activity_walkthrough.*

class WalkthroughActivity : BaseKotlinActivity() {

    var mImagesArrayList = ArrayList<Int>()
    var mHeadingArray = ArrayList<String>()
    var mDescriptionArray = ArrayList<String>()

    override fun getContentView() = R.layout.activity_walkthrough

    override fun initUI() {
        displaySplashAnimation()
        cpIndicatorWalk.fillColor = ContextCompat.getColor(this, R.color.colorPrimary)
        cpIndicatorWalk.pageColor = ContextCompat.getColor(this, R.color.colorWhite)
        cpIndicatorWalk.strokeColor = ContextCompat.getColor(this, R.color.colorPrimary)
        cpIndicatorWalk.strokeWidth = Constants.dpToPx(1).toFloat()
    }

    override fun onCreateStuff() {
        getFirebaseToken()
        loadWalkthroughImages()
        loadHeadingAndDescription()

        txtWalkHeading.text = mHeadingArray[0]
        txtWalkDesc.text = mDescriptionArray[0]
    }

    override fun initListener() {
        txtNext.setOnClickListener(this)
        txtSkip.setOnClickListener(this)
    }

    override fun getContext() = this

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


    private val mHandler = Handler()
    var runnable: Runnable = object : Runnable {
        override fun run() {
            if (vpWalkthrough.currentItem == vpWalkthrough.adapter!!.count - 1) {
                vpWalkthrough.currentItem = 0
            } else {
                vpWalkthrough.currentItem = vpWalkthrough.currentItem + 1
            }
            mHandler.postDelayed(this, 6000)
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            txtNext -> {
                if (vpWalkthrough.currentItem == 4) {
                    /// lets get started
                    val signupIntent = Intent(this@WalkthroughActivity, SignupActivity::class.java)
                    startActivity(signupIntent)
                    finish()
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
                } else {
                    vpWalkthrough.currentItem = vpWalkthrough.currentItem + 1
                }
            }
            txtSkip -> {
                val signupIntent = Intent(this@WalkthroughActivity, SignupActivity::class.java)
                startActivity(signupIntent)
                finish()
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
            }
        }
    }

    private fun loadWalkthroughImages() {
        mImagesArrayList.add(R.mipmap.walk1)
        mImagesArrayList.add(R.mipmap.walk2)
        mImagesArrayList.add(R.mipmap.walk3)
        mImagesArrayList.add(R.mipmap.walk4)
        mImagesArrayList.add(R.mipmap.walk5)

        vpWalkthrough.adapter = WalkthroughAdapter(mImagesArrayList, this)
        vpWalkthrough.setPageTransformer(true, ZoomOutPageTransformer())
        cpIndicatorWalk.setViewPager(vpWalkthrough)
        mHandler.postDelayed(runnable, 6000)

        cpIndicatorWalk.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 4)
                    txtNext.text = getString(R.string.get_started)
                else
                    txtNext.text = getString(R.string.next)
                txtWalkHeading.text = mHeadingArray[position]
                txtWalkDesc.text = mDescriptionArray[position]
                animateView(txtWalkHeading)
                animateView(txtWalkDesc)
            }
        })
    }

    private fun loadHeadingAndDescription() {
        mHeadingArray.add("REVIEW VIDEOS & GET PAID!")
        mHeadingArray.add("EASY REGISTRATION PROCESS")
        mHeadingArray.add("RECEIVE REQUESTS FOR VIDEO REVIEW")
        mHeadingArray.add("PROVIDE YOUR REVIEW USING SMART TOOLS")
        mHeadingArray.add("GET PAID FOR EVERY REVIEW")

        mDescriptionArray.add("ReeVuu is an online platform that matches athletes to expert coaches who provide online video review and analysis of sporting event/training session")
        mDescriptionArray.add("Simply fill out our profile and let us know your areas of expertise and coaching experience")
        mDescriptionArray.add("Next we match a video submitted by a ReeVuu athlete to you based on your area of expertise")
        mDescriptionArray.add("Using our awesome online tools, simply watch the video, provide your review analysis and training recommendations")
        mDescriptionArray.add("That is right you are paid for each video you review. Start helping athletes and getting paid now!")
    }

    private fun animateView(txtView: TextView) {
        val alphaAnimation = AlphaAnimation(0f, 1f)
        alphaAnimation.duration = 500
        txtView.startAnimation(alphaAnimation)
    }

    class ZoomOutPageTransformer : ViewPager.PageTransformer {

        private val MIN_SCALE = 0.90f
        private val MIN_ALPHA = 0.5f

        override fun transformPage(view: View, position: Float) {
            view.apply {
                val pageWidth = width
                val pageHeight = height
                when {
                    position < -1 -> { // [-Infinity,-1)
                        // This page is way off-screen to the left.
                        alpha = 0f
                    }
                    position <= 1 -> { // [-1,1]
                        // Modify the default slide transition to shrink the page as well
                        val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                        val vertMargin = pageHeight * (1 - scaleFactor) / 2
                        val horzMargin = pageWidth * (1 - scaleFactor) / 2
                        translationX = if (position < 0) {
                            horzMargin - vertMargin / 2
                        } else {
                            horzMargin + vertMargin / 2
                        }

                        // Scale the page down (between MIN_SCALE and 1)
                        scaleX = scaleFactor
                        scaleY = scaleFactor

                        // Fade the page relative to its size.
                        alpha = (MIN_ALPHA +
                                (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                    }
                    else -> { // (1,+Infinity]
                        // This page is way off-screen to the right.
                        alpha = 0f
                    }
                }
            }
        }
    }




}