package com.myreevuuCoach.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myreevuuCoach.R;
import com.myreevuuCoach.activities.CommentActivity;
import com.myreevuuCoach.activities.FeedDetailActivity;
import com.myreevuuCoach.activities.FeedSearchActivity;
import com.myreevuuCoach.activities.LandingActivity;
import com.myreevuuCoach.activities.MyFeedDetailActivity;
import com.myreevuuCoach.adapters.VideoAdapter;
import com.myreevuuCoach.interfaces.InterConst;
import com.myreevuuCoach.interfaces.VideoAdapterItemClick;
import com.myreevuuCoach.models.BaseSuccessModel;
import com.myreevuuCoach.models.FeedModel;
import com.myreevuuCoach.network.RetrofitClient;
import com.myreevuuCoach.services.CommentIntentServiceResult;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

public class HomeFragment extends BaseFragment implements View.OnClickListener, VideoAdapterItemClick {

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


    @BindView(R.id.llVideoContainer)
    RelativeLayout llVideoContainer;
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
    int mSelectedPosition = -1;
    Call<BaseSuccessModel> call;
    private boolean loading = true;
    private int offSet = 1;
    BroadcastReceiver videoAddedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("videoAddedReceiver", "videoAddedReceiver HIT_API");
            if (intent.hasExtra(InterConst.INTEND_EXTRA)) {
                onCallResume();
            } else {
                setLocalNewRequestCount();
            }
        }
    };
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

        final ViewTreeObserver observer = llVideoContainer.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int height = llVideoContainer.getHeight();
                        Log.e("DIVIDE", height+"");
                        utils.setInt(InterConst.VIDEO_CONTAINER_HEIGHT, height);// (int) utils.pxToDp(getActivity(), height));
                    }
                });




        EventBus.getDefault().register(this);
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(InterConst.BROADCAST_MY_FEED_VIDEO_DELETE));
        getActivity().registerReceiver(videoStopReceiver, new IntentFilter(InterConst.BROADCAST_VIDEO_STOP_RECIVER));
        getActivity().registerReceiver(videoAddedReceiver, new IntentFilter(InterConst.BROADCAST_VIDEO_ADDED_RECIVER));

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
        getActivity().unregisterReceiver(videoAddedReceiver);
        EventBus.getDefault().unregister(this);
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
        try {
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
        } catch (Exception e) {

        }
    }

    public void setLocalNewRequestCount() {
        if (utils != null) {
            final int count = utils.getInt(InterConst.NEW_REQUEST_COUNT, 0);
            try {
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
            } catch (Exception e) {

            }
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
        mSelectedPosition = position;
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

    @Override
    public void onItemLikeClick(int position) {
        mSelectedPosition = position;

        setLikedStatus(position);
    }

    @Override
    public void onItemCommentClick(int position) {
        mSelectedPosition = position;
        Intent intent = new Intent(getActivity(), CommentActivity.class);
        intent.putExtra("post_id", mData.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onItemShareClick(int position) {
        mSelectedPosition = position;

        shareTextUrl(mData.get(position).getUrl());
    }

    void hideUnreadNotificationDot() {
        utils.setInt(InterConst.UNREAD_NOTIFICATION_DOT, 0);
        img_unread_dot.setVisibility(View.GONE);
    }

    void showUnreadNotificationDot() {
        img_unread_dot.setVisibility(View.VISIBLE);
    }

    public void checkUnreadNotification() {
        try {
            if (utils.getInt(InterConst.UNREAD_NOTIFICATION_DOT, 0) == 0) {
                hideUnreadNotificationDot();
            } else {
                showUnreadNotificationDot();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void shareTextUrl(String url) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_TEXT, url);
        startActivity(Intent.createChooser(share, "Share link!"));
    }


    private void setLikedStatus(final int position) {
        if (connectedToInternet()) {
            switch (mData.get(position).getLiked()) {
                case 0:
                    mData.get(position).setLiked(1);
                    mData.get(position).setLikes_count(mData.get(position).getLikes_count() + 1);
                    break;
                case 1:
                    mData.get(position).setLiked(0);
                    mData.get(position).setLikes_count(mData.get(position).getLikes_count() - 1);
                    break;
            }
            mAdapter.notifyItemChanged(position);
            call = RetrofitClient.getInstance().setFavArticles(
                    utils.getString(InterConst.ACCESS_TOKEN, ""),
                    mData.get(position).getId()
            );
            call.enqueue(new retrofit2.Callback<BaseSuccessModel>() {
                @Override
                public void onResponse(@NonNull Call<BaseSuccessModel> call, @NonNull Response<BaseSuccessModel> response) {
                    hideProgress();
                    if (response.body() != null) {
                        if (response.body().getResponse().getMessage().toLowerCase().contains("un")) {
                            // articleFavDialog(response.body().getResponse().getMessage());
                            //EventBus.getDefault().post(new CommentIntentServiceResult(1, 0));
                            //removeArticleFav();
                        } else {
                            //EventBus.getDefault().post(new CommentIntentServiceResult(1, 1));
                            // articleFavDialog(response.body().getResponse().getMessage());
                            //markArticleFav();
                        }
                    } else {
                        if (response.body().getError().getCode() == 506) {
                            itemDeleted(position);
                            videoDeleted("This post has been deleted");
                        } else if (response.body().getError().getCode() == InterConst.INVALID_ACCESS)
                            moveToSplash();
                        else
                            showSnackBar(rvFeeds, response.body().getError().getMessage());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<BaseSuccessModel> call, @NonNull Throwable t) {
                    hideProgress();
                    showSnackBar(rvFeeds, String.valueOf(t));
                    t.printStackTrace();
                }
            });
        }
    }

    private void videoDeleted(String Message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        //  alertDialogBuilder.setTitle(getString(R.string.logout));
        alertDialogBuilder.setMessage(Message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // moveBack();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    void itemDeleted(int position) {
        mData.remove(position);
        mAdapter.notifyItemRemoved(position);
        setProgressVisibility();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void doThis(CommentIntentServiceResult intentServiceResult) {
        Log.e("FeedDetail", "eventbus");
        if (mSelectedPosition != -1) {
            switch (intentServiceResult.getStatus()) {
                case 1:
                    mData.get(mSelectedPosition).setLiked(intentServiceResult.getItem().getLiked());
                    mData.get(mSelectedPosition).setLikes_count(intentServiceResult.getItem().getLikes_count());
//                    mAdapter.notifyItemChanged(mSelectedPosition);

//                    setData();
                    mAdapter.notifyItemChanged(mSelectedPosition);

                    break;
                case 2:
                    mData.get(mSelectedPosition).setComments_count(intentServiceResult.getItem().getComments_count());
                    mAdapter.notifyItemChanged(mSelectedPosition);
                    break;
                case 3:
                    mAdapter.notifyItemChanged(mSelectedPosition);
                    break;
                case 4:
                    itemDeleted(mSelectedPosition);
                    break;
                case 5:
                    try {
                        mData.get(mSelectedPosition).setComments_count(intentServiceResult.getItem().getComments_count());
                        mData.get(mSelectedPosition).setLiked(intentServiceResult.getItem().getLiked());
                        mData.get(mSelectedPosition).setLikes_count(intentServiceResult.getItem().getLikes_count());
                    } catch (Exception e) {

                    }
                    mAdapter.notifyItemChanged(mSelectedPosition);

            }
        }
    }
}
