package com.myreevuuCoach.activities

import android.support.v4.view.ViewPager
import android.view.View
import com.myreevuuCoach.R
import com.myreevuuCoach.adapters.FullImageAdapter
import kotlinx.android.synthetic.main.activity_fullviewimage.*

class FullViewImageActivity : BaseKotlinActivity() {

    var mImagesArrayList = ArrayList<String>()

    override fun getContentView() = R.layout.activity_fullviewimage

    override fun initUI() {

    }

    override fun onCreateStuff() {
        mImagesArrayList.addAll(intent.getStringArrayListExtra("images"))
        vpImages.adapter = FullImageAdapter(mContext, mImagesArrayList)
        vpImages.currentItem = intent.getIntExtra("imagePosition", 0)

        vpImages.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {

            }
        })

    }

    override fun initListener() {
        txtDone.setOnClickListener(this)
    }

    override fun getContext() = this

    override fun onClick(view: View?) {
        when (view) {
            txtDone -> {
                finish()
            }
        }
    }
}