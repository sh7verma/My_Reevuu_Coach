package com.myreevuuCoach.fragments;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.myreevuuCoach.R;
import com.myreevuuCoach.adapters.RequestsAdapter;
import com.myreevuuCoach.dialog.RequestAcceptedDialog;
import com.myreevuuCoach.interfaces.InterConst;
import com.myreevuuCoach.interfaces.ItemClickInterface;
import com.myreevuuCoach.models.RequestListModel;
import com.myreevuuCoach.models.RequestsModel;
import com.myreevuuCoach.network.RetrofitClient;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dev on 4/12/18.
 */

public class ReevuuRequestsFragment extends BaseFragment {

    @SuppressLint("StaticFieldLeak")
    static ReevuuRequestsFragment fragment;
    @SuppressLint("StaticFieldLeak")
    static Context mContext;

    @BindView(R.id.rvRequests)
    RecyclerView rvRequests;
    @BindView(R.id.txtTotalRequests)
    TextView txtTotalRequests;
    @BindView(R.id.txtNoReviews)
    TextView txtNoReviews;
    @BindView(R.id.pbReviews)
    AVLoadingIndicatorView pbReviews;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    RequestsAdapter mAdapter;

    ArrayList<RequestsModel.ResponseBean> mData = new ArrayList<>();

    BroadcastReceiver videoAddedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("videoAddedReceiver", "videoAddedReceiver HIT_API");
            onCallResume();
        }
    };

    public static ReevuuRequestsFragment newInstance(Context context) {
        fragment = new ReevuuRequestsFragment();
        mContext = context;
        return fragment;
    }

    public static ReevuuRequestsFragment getInstance() {
        return fragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_reevuu_requests;
    }

    @Override
    protected void onCreateStuff() {
        getActivity().registerReceiver(videoAddedReceiver,
                new IntentFilter(InterConst.BROADCAST_VIDEO_ADDED_RECIVER));

        rvRequests.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new RequestsAdapter(mContext, mData, mHeight, new ItemClickInterface() {
            @Override
            public void onItemClick(int p, String type) {
                hitRequestApi(p, type);
            }
        });

        swipeRefresh.setColorSchemeColors(mContext.getResources().getColor(R.color.colorPrimary));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                hitApi();
            }
        });
        rvRequests.setAdapter(mAdapter);
        hitApi();
        populateData(mData);
    }

    @Override
    protected void initListeners() {

    }

    public void onCallResume() {
        hitApi();
    }


    void hitApi() {
        if (connectedToInternet(txtTotalRequests)) {
            if (mData.size() == 0) {
                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pbReviews.setVisibility(View.VISIBLE);
                            txtNoReviews.setVisibility(View.GONE);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    pbReviews.setVisibility(View.VISIBLE);
                    txtNoReviews.setVisibility(View.GONE);
                }
            }
            Call<RequestListModel> call = RetrofitClient.getInstance().coach_requests(utils.getString(InterConst.ACCESS_TOKEN, ""), ""
                    , InterConst.REEVUU_REQUESTS_REQUESTED);
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
        setNewRequestCount();
    }

    void setProgressVisibility() {
        swipeRefresh.setRefreshing(false);
        if (mData.size() > 0) {
            txtNoReviews.setVisibility(View.GONE);
        } else {
            txtNoReviews.setVisibility(View.VISIBLE);
        }
    }

    private void hitRequestApi(final int position, String type) {
        if (connectedToInternet(txtNoReviews)) {
            showProgress();
            Call<RequestsModel> call = RetrofitClient.getInstance().response_a_request(utils.getString(InterConst.ACCESS_TOKEN, ""),
                    String.valueOf(mData.get(position).getId()), "2", type);
            call.enqueue(new Callback<RequestsModel>() {
                @Override
                public void onResponse(Call<RequestsModel> call, Response<RequestsModel> response) {
                    hideProgress();
                    if (response.body().getResponse() != null) {
                        if (response.body().getResponse().getReview_status() == Integer.parseInt(InterConst.REEVUU_REQUESTS_ACCEPTED)) {
                            new RequestAcceptedDialog(mContext,
                                    mContext.getString(R.string.accepted_request),
                                    new DialogInterface() {
                                @Override
                                public void cancel() {

                                }

                                @Override
                                public void dismiss() {

                                }

                            }).showDialog();
                        }
                        mData.remove(position);
                        mAdapter.notifyAdapter(mData);
                        setNewRequestCount();
                        setProgressVisibility();
                    } else {
                        if (response.body().getError().getCode() == InterConst.REQUEST_NOT_EXIST) {
                            toast(getString(R.string.video_deleted));

                            mData.remove(position);
                            mAdapter.notifyAdapter(mData);
                            setNewRequestCount();
                            setProgressVisibility();
                        } else {
                            toast(response.body().getError().getMessage());
                            if (response.body().getError().getCode() == InterConst.INVALID_ACCESS) {
                                moveToSplash();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RequestsModel> call, Throwable t) {
                    hideProgress();
                    toast(t.getMessage());
                }
            });
        }
    }

    private void setNewRequestCount() {
        if (mData.size() == 0) {
            txtTotalRequests.setVisibility(View.GONE);
        } else {
            txtTotalRequests.setVisibility(View.VISIBLE);
        }
        txtTotalRequests.setText(mData.size() + " new requests");

        utils.setInt(InterConst.NEW_REQUEST_COUNT, mData.size());

        HomeFragment.getInstance().setNewRequestCount(mData.size());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(videoAddedReceiver);
    }
}
