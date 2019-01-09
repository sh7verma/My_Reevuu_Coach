package com.myreevuuCoach.fragments;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.myreevuuCoach.R;
import com.myreevuuCoach.activities.FeedDetailActivity;
import com.myreevuuCoach.activities.MyFeedDetailActivity;
import com.myreevuuCoach.adapters.VideoAdapter;
import com.myreevuuCoach.interfaces.AdapterClickInterface;
import com.myreevuuCoach.interfaces.InterConst;
import com.myreevuuCoach.models.FeedModel;
import com.myreevuuCoach.network.RetrofitClient;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import im.ene.toro.widget.Container;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dev on 20/11/18.
 */

public class ProfileVideoFragment extends BaseFragment implements AdapterClickInterface {

    @SuppressLint("StaticFieldLeak")
    static ProfileVideoFragment fragment;
    @SuppressLint("StaticFieldLeak")
    static Context mContext;

    @BindView(R.id.player_container)
    Container rvFeeds;

    @BindView(R.id.pbFeed)
    AVLoadingIndicatorView pbFeed;
    @BindView(R.id.txtNoVideos)
    TextView txtNoVideos;

    ArrayList<FeedModel.Response> mData = new ArrayList<>();
    VideoAdapter mAdapter;
    LinearLayoutManager layoutManager;


    BroadcastReceiver videoStopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            rvFeeds.swapAdapter(mAdapter, false);
        }
    };


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(InterConst.PROFILE_VIDEO_POSITION)) {

                Log.d("BROADCAST_FEED_DELETE", "PROFILE_VIDEO_POSITION" + String.valueOf(intent.getIntExtra(InterConst.PROFILE_VIDEO_POSITION, -1)));

                mData.remove(intent.getIntExtra(InterConst.PROFILE_VIDEO_POSITION, -1));
                mAdapter.notifyAdapter(mData);
                setProgressVisibility();
            } else {
                Log.d("BROADCAST_FEED_DELETE", "PROFILE_VIDEO_POSITION HIT_API");
                setData();
            }

        }
    };

    public static ProfileVideoFragment newInstance(Context context) {
        fragment = new ProfileVideoFragment();
        mContext = context;
        return fragment;
    }

    public static ProfileVideoFragment getInstance() {
        return fragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_profile_video;
    }

    @Override
    protected void onCreateStuff() {
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(InterConst.BROADCAST_MY_FEED_VIDEO_DELETE));

        getActivity().registerReceiver(videoStopReceiver, new IntentFilter(InterConst.BROADCAST_VIDEO_STOP_RECIVER));

        layoutManager = new LinearLayoutManager(mContext);
        rvFeeds.setLayoutManager(layoutManager);

        mAdapter = new VideoAdapter(mContext, mData);
        rvFeeds.setAdapter(mAdapter);
        pbFeed.setVisibility(View.VISIBLE);
        setData();
    }

    public void setData() {
        if (connectedToInternet())
            hitFeedsApi();
        else {
            pbFeed.setVisibility(View.GONE);
            setProgressVisibility();
            showInternetAlert(rvFeeds);
        }
    }

    void hitFeedsApi() {
        Call<FeedModel> call = RetrofitClient.getInstance().getVideos(
                utils.getString(InterConst.ACCESS_TOKEN, ""),
                utils.getInt(InterConst.ID, 0),
                1);
        call.enqueue(new Callback<FeedModel>() {
            @Override
            public void onResponse(Call<FeedModel> call, Response<FeedModel> response) {
                pbFeed.setVisibility(View.GONE);
                Log.d("response", String.valueOf(response.body()));
                if (response.body().getResponse() != null) {
                    populateData(response.body().getResponse());
                } else {
                    toast(response.body().getError().getMessage());
                    if (response.body().getError().getCode() == InterConst.INVALID_ACCESS) {
                        moveToSplash();
                    }
                }
                setProgressVisibility();
            }

            @Override
            public void onFailure(Call<FeedModel> call, Throwable t) {
                pbFeed.setVisibility(View.GONE);
                toast(t.getMessage());
                setProgressVisibility();
            }
        });
    }

    void populateData(List<FeedModel.Response> response) {
        mData = new ArrayList<>();
        mData.addAll(response);
        mAdapter.notifyAdapter(mData);
    }

    void setProgressVisibility() {
        if (mData.size() > 0) {
            txtNoVideos.setVisibility(View.GONE);
        } else {
            txtNoVideos.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initListeners() {
        mAdapter.onAdapterItemClick(this);
    }

    public void onCallPause() {
        if (rvFeeds != null)
            rvFeeds.swapAdapter(mAdapter, false);
    }

    public void onCallResume() {
        if (rvFeeds != null)
            rvFeeds.swapAdapter(mAdapter, false);
    }


    public void removeAdapter() {
        if (rvFeeds != null)
            rvFeeds.setAdapter(null);
    }


    public void onVideoAdded() {
        setData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
        getActivity().unregisterReceiver(videoStopReceiver);
    }

    @Override
    public void onItemClick(int position) {
        if (utils.getInt(InterConst.ID, 0) != mData.get(position).getUser_id()) {
            Intent intent = new Intent(getActivity(), FeedDetailActivity.class);
            intent.putExtra("feedData", mData.get(position));
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        } else {
            Intent intent = new Intent(getActivity(), MyFeedDetailActivity.class);
            intent.putExtra("feedData", mData.get(position));
            intent.putExtra("video_id", mData.get(position).getId());

            intent.putExtra(InterConst.PROFILE_VIDEO_POSITION, position);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        }
    }
}
