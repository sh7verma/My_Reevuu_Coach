package com.myreevuuCoach.fragments;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.myreevuuCoach.R;
import com.myreevuuCoach.activities.ReviewedVideoActivity;
import com.myreevuuCoach.adapters.AcceptedVideoAdapter;
import com.myreevuuCoach.interfaces.AdapterClickInterface;
import com.myreevuuCoach.interfaces.InterConst;
import com.myreevuuCoach.models.RequestListModel;
import com.myreevuuCoach.models.RequestsModel;
import com.myreevuuCoach.network.RetrofitClient;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import butterknife.BindView;
import im.ene.toro.widget.Container;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dev on 4/12/18.
 */

public class ReevuuReviewedFragment extends BaseFragment implements AdapterClickInterface {

    @SuppressLint("StaticFieldLeak")
    static ReevuuReviewedFragment fragment;
    @SuppressLint("StaticFieldLeak")
    static Context mContext;

    @BindView(R.id.rvRequestsAccepted)
    Container rvRequests;
    @BindView(R.id.txtNoReviews)
    TextView txtNoReviews;
    @BindView(R.id.pbReviews)
    AVLoadingIndicatorView pbReviews;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    AcceptedVideoAdapter mAdapter;

    ArrayList<RequestsModel.ResponseBean> mData = new ArrayList<>();
    BroadcastReceiver videoStopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            rvRequests.swapAdapter(mAdapter, false);
        }
    };

    public static ReevuuReviewedFragment newInstance(Context context) {
        fragment = new ReevuuReviewedFragment();
        mContext = context;
        return fragment;
    }

    public static ReevuuReviewedFragment getInstance() {
        return fragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_reevuu_reviewed;
    }

    @Override
    protected void onCreateStuff() {
        getActivity().registerReceiver(videoStopReceiver, new IntentFilter(InterConst.BROADCAST_VIDEO_STOP_RECIVER));

        rvRequests.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new AcceptedVideoAdapter(mContext, mData);
        rvRequests.setAdapter(mAdapter);
        hitApi();
        populateData(mData);

        swipeRefresh.setColorSchemeColors(mContext.getResources().getColor(R.color.colorPrimary));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                hitApi();
            }
        });
    }

    @Override
    protected void initListeners() {
        mAdapter.onAdapterItemClick(this);
    }

    public void onCallPause() {
        if (rvRequests != null)
            rvRequests.swapAdapter(mAdapter, false);
    }

    public void removeAdapter() {
        if (rvRequests != null)
            rvRequests.setAdapter(null);
    }

    public void onCallResume() {
        if (rvRequests != null)
            rvRequests.swapAdapter(mAdapter, false);
        hitApi();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(videoStopReceiver);
    }

    void hitApi() {
        if (connectedToInternet(txtNoReviews)) {
            if (mData.size() == 0) {
                pbReviews.setVisibility(View.VISIBLE);
                txtNoReviews.setVisibility(View.GONE);
            }
            Call<RequestListModel> call = RetrofitClient.getInstance().coach_requests(utils.getString(InterConst.ACCESS_TOKEN, ""), ""
                    , InterConst.REEVUU_REQUESTS_REVIEWED);
            call.enqueue(new Callback<RequestListModel>() {
                @Override
                public void onResponse(Call<RequestListModel> call, Response<RequestListModel> response) {
                    pbReviews.setVisibility(View.GONE);
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
                public void onFailure(Call<RequestListModel> call, Throwable t) {
                    pbReviews.setVisibility(View.GONE);
                    toast(t.getMessage());
                    setProgressVisibility();
                }
            });
        } else {
            pbReviews.setVisibility(View.GONE);
            setProgressVisibility();
        }
    }

    void populateData(ArrayList<RequestsModel.ResponseBean> response) {
        mData = new ArrayList<>();
        mData.addAll(response);
        mAdapter.notifyAdapter(mData);
    }

    void setProgressVisibility() {
        swipeRefresh.setRefreshing(false);
        if (mData.size() > 0) {
            txtNoReviews.setVisibility(View.GONE);
        } else {
            txtNoReviews.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(mContext, ReviewedVideoActivity.class);
        intent.putExtra(InterConst.REVIEW_REQUEST_ID, String.valueOf(mData.get(position).getId()));
        intent.putExtra(InterConst.VIDEO_URL, mData.get(position).getVideo().getUrl());
        startActivity(intent);
//        toast(getString(R.string.work_progress));
    }
}
