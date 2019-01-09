package com.myreevuuCoach.adapters


import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter


class FragmentAdapter(fragmentManager: FragmentManager, mFragmentArray: ArrayList<Fragment>)
    : FragmentPagerAdapter(fragmentManager) {

    var mFragmentArray = ArrayList<Fragment>()

    init {
        this.mFragmentArray = mFragmentArray
    }

    override fun getItem(position: Int): Fragment {
        return mFragmentArray[position]
    }

    override fun getCount() = mFragmentArray.size
}