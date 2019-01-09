package com.myreevuuCoach.adapters

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.myreevuuCoach.R
import kotlinx.android.synthetic.main.item_walkthrough.view.*

class WalkthroughAdapter(private var mWalkArray: ArrayList<Int>, private var mContext: Context) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as View
    }

    override fun getCount() = mWalkArray.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.item_walkthrough, container, false)
        val imgWalkthrough = view.imgWalkthrough
        imgWalkthrough.setImageResource(mWalkArray[position])
        container.addView(view)
        return view
    }

    override fun destroyItem(parent: ViewGroup, position: Int, `object`: Any) {
        parent.removeView(`object` as View)
    }
}