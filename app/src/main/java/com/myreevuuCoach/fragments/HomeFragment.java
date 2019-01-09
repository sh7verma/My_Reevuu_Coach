package com.myreevuuCoach.fragments;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myreevuuCoach.R;
import com.myreevuuCoach.activities.FeedDetailActivity;
import com.myreevuuCoach.activities.FeedSearchActivity;
import com.myreevuuCoach.activities.LandingActivity;
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
 * Created by dev on 19/11/18.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener, AdapterClickInterface {

    @SuppressLint("StaticFieldLeak")
    static HomeFragment fragment;
    @SuppressLint("StaticFieldLeak")
    static Context mContext;

    @BindView(R.id.llRequests)
    LinearLayout llRequests;
    @BindView(R.id.imgNoti)
    ImageView imgNoti;
    @BindView(R.id.imgSearch)
    ImageView imgSearch;
    @BindView(R.id.player_container)
    Container rvFeeds;
    @BindView(R.id.txtNoVideos)
    TextView txtNoVideos;
    @BindView(R.id.txtNewRequests)
    TextView txtNewRequests;

    @BindView(R.id.pbFeed)
    AVLoadingIndicatorView pbFeed;

    ArrayList<FeedModel.Response> mData = new ArrayList<>();

    VideoAdapter mAdapter;
    LinearLayoutManager mLayoutManager;
    BroadcastReceiver videoStopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("videoStopReceiver", "OFF");
            rvFeeds.swapAdapter(mAdapter, true);
        }
    };
    @BindView(R.id.img_unread_dot)
    ImageView img_unread_dot;
    private boolean loading = true;
    private int offSet = 1;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(InterConst.HOME_VIDEO_POSITION)) {
                Log.d("BROADCAST_FEED_DELETE", "HOME_VIDEO_POSITION" + String.valueOf(intent.getIntExtra(InterConst.HOME_VIDEO_POSITION, -1)));
                mData.remove(intent.getIntExtra(InterConst.HOME_VIDEO_POSITION, -1));
                mAdapter.notifyAdapter(mData);
                setProgressVisibility();
            } else {
                Log.d("BROADCAST_FEED_DELETE", "HOME_VIDEO_POSITION HIT_API");
                onCallResume();
            }
        }
    };

    public static HomeFragment newInstance(Context context) {
        fragment = new HomeFragment();
        mContext = context;
        return fragment;
    }

    public static HomeFragment getInstance() {
        return fragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_home;
    }

    @Override
    protected void onCreateStuff() {
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(InterConst.BROADCAST_MY_FEED_VIDEO_DELETE));
        getActivity().registerReceiver(videoStopReceiver, new IntentFilter(InterConst.BROADCAST_VIDEO_STOP_RECIVER));

        mLayoutManager = new LinearLayoutManager(mContext);
        rvFeeds.setLayoutManager(mLayoutManager);

        mAdapter = new VideoAdapter(mContext, mData);
        rvFeeds.setAdapter(mAdapter);
        pbFeed.setVisibility(View.VISIBLE);
        setData();
        populateData(mData);
    }

    void setData() {
        if (connectedToInternet()) {
            hitFeedsApi();
        } else {
            showInternetAlert(imgSearch);
            pbFeed.setVisibility(View.GONE);
            setProgressVisibility();
        }
    }

    @Override
    protected void initListeners() {
        imgNoti.setOnClickListener(this);
        imgSearch.setOnClickListener(this);
        mAdapter.onAdapterItemClick(this);
        llRequests.setOnClickListener(this);

        rvFeeds.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        hitFeedsApi();
                    }
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
        getActivity().unregisterReceiver(videoStopReceiver);
    }

    void hitFeedsApi() {
        loading = false;
        Call<FeedModel> call = RetrofitClient.getInstance().getFeeds(utils.getString(InterConst.ACCESS_TOKEN, ""),
                String.valueOf(offSet));
        call.enqueue(new Callback<FeedModel>() {
            @Override
            public void onResponse(Call<FeedModel> call, Response<FeedModel> response) {
                loading = true;
                pbFeed.setVisibility(View.GONE);
                utils.setInt(InterConst.UNREAD_NOTIFICATION_DOT, response.body().getUnread_notification());
                checkUnreadNotification();
                Log.d("response", String.valueOf(response.body()));
                if (response.body().getResponse() != null) {
                    offSet++;
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
                loading = true;
                pbFeed.setVisibility(View.GONE);
                toast(t.getMessage());
                setProgressVisibility();
            }
        });
    }

    public void setNewRequestCount(final int count) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtNewRequests.setText(count + "");
                if (count == 0) {
                    llRequests.setVisibility(View.GONE);
                } else {
                    llRequests.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void setLocalNewRequestCount() {
        if (utils != null) {
            final int count = utils.getInt(InterConst.NEW_REQUEST_COUNT, 0);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtNewRequests.setText(count + "");
                    if (count == 0) {
                        llRequests.setVisibility(View.GONE);
                    } else {
                        llRequests.setVisibility(View.VISIBLE);
                    }
                }
            });

        }
    }

    void resetPaging() {
        mData = new ArrayList<>();
        offSet = 1;
    }

    void populateData(List<FeedModel.Response> response) {
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgSearch:
                Intent intent = new Intent(mContext, FeedSearchActivity.class);
                startActivity(intent);
                break;
            case R.id.imgNoti:
                hideUnreadNotificationDot();
                assert ((LandingActivity) getActivity()) != null;
                ((LandingActivity) getActivity()).openNotificationCenterPage();
                break;

            case R.id.llRequests:
                assert ((LandingActivity) getActivity()) != null;
                ((LandingActivity) getActivity()).setPagerItem(InterConst.FRAG_REEVUU);
                break;
        }
    }

    public void onCallPause() {
        if (rvFeeds != null)
            rvFeeds.swapAdapter(mAdapter, false);
    }

    public void removeAdapter() {
        if (rvFeeds != null)
            rvFeeds.setAdapter(null);
    }

    public void onCallResume() {
        if (rvFeeds != null)
            rvFeeds.swapAdapter(mAdapter, false);
        resetPaging();
        setData();
        checkUnreadNotification();

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
            intent.putExtra(InterConst.HOME_VIDEO_POSITION, position);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        }
    }

    void hideUnreadNotificationDot() {
        utils.setInt(InterConst.UNREAD_NOTIFICATION_DOT, 0);
        img_unread_dot.setVisibility(View.GONE);
    }

    void showUnreadNotificationDot() {
        img_unread_dot.setVisibility(View.VISIBLE);

    }

    public void checkUnreadNotification() {
        if (utils.getInt(InterConst.UNREAD_NOTIFICATION_DOT, 0) == 0) {
            hideUnreadNotificationDot();
        } else {
            showUnreadNotificationDot();
        }
    }

}
