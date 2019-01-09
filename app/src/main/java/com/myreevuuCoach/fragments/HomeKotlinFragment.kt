package com.myreevuuCoach.fragments

import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.myreevuuCoach.R
import com.myreevuuCoach.activities.LandingActivity
import com.myreevuuCoach.adapters.FeedAdapter
import com.myreevuuCoach.models.FeedModel
import com.myreevuuCoach.network.RetrofitClient
import com.myreevuuCoach.utils.Constants
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//
//class HomeKotlinFragment : BaseKotlinFragment() {
//
//    lateinit var landingActivity: LandingActivity
//
//    override fun setContentView() = R.layout.fragment_home
//
//    override fun initUI() {
//
//    }
//
//    override fun onActivityCreated() {
//
//        landingActivity = activity as LandingActivity
//
////        if (landingActivity.connectedToInternet())
////            hitFeedAPI()
////        else
////            showInternetAlert(rvFeeds)
//
//        rvFeeds.layoutManager = LinearLayoutManager(activity)
//    }
//
//
//    override fun initListener() {
//        imgNoti.setOnClickListener(this)
//        imgSearch.setOnClickListener(this)
//    }
//
//    override fun onClick(view: View?) {
//        when (view) {
//            imgNoti -> {
//                showAlertSnackBar(rvFeeds, getString(R.string.work_progress))
//            }
//        }
//    }
//
////    private fun hitFeedAPI() {
////        val call = RetrofitClient.getInstance().getFeeds()
////        call.enqueue(object : Callback<FeedModel> {
////            override fun onFailure(call: Call<FeedModel>?, t: Throwable?) {
////                pbFeed.visibility = View.GONE
////                showAlertSnackBar(rvFeeds, t!!.localizedMessage)
////            }
////
////            override fun onResponse(call: Call<FeedModel>?, response: Response<FeedModel>) {
////                pbFeed.visibility = View.GONE
////                if (response.body().error != null) {
////                        showAlertSnackBar(rvFeeds, response.body().error.message)
////                } else {
////                    populateData(response.body().response)
////                }
////            }
////        })
////    }
//
//    private fun populateData(response: List<FeedModel.Response>) {
//        val urlList = ArrayList<String>()
//        for (feed in response) {
//            urlList.add(feed.url)
//        }
//        rvFeeds.setActivity(activity)
//        rvFeeds.mAdapter = FeedAdapter(response, activity!!)
//        rvFeeds.setPlayOnlyFirstVideo(true)
//        rvFeeds.setVisiblePercent(50f)
////        rvFeeds.preDownload(urlList)
//        rvFeeds.smoothScrollBy(0, 1);
//        rvFeeds.smoothScrollBy(0, -1);
//    }
//
//    override fun onOptionSelected(selectedOption: String, id: Int) {
//        // no op
//    }
//
//
//    override fun onStop() {
//        super.onStop()
//        //add this code to pause videos (when app is minimised or paused)
//        rvFeeds.stopVideos()
//    }
//
//
//}