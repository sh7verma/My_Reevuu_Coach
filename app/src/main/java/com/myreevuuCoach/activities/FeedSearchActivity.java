package com.myreevuuCoach.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.myreevuuCoach.R;
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
 * Created by dev on 12/12/18.
 */

public class FeedSearchActivity extends BaseActivity implements VideoAdapterItemClick {


    @BindView(R.id.rvVideoReviews)
    Container rvVideoReviews;

    @BindView(R.id.txtNoResult)
    TextView txtNoResult;
    @BindView(R.id.pbReviews)
    AVLoadingIndicatorView pbReviews;
    @BindView(R.id.edSearch)
    EditText edSearch;
    @BindView(R.id.imgBack)
    ImageView imgBack;
    VideoAdapter mVideoAdapter;
    ArrayList<FeedModel.Response> mData = new ArrayList<>();
    int mSelectedPosition = -1;
    Call<BaseSuccessModel> call;

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(InterConst.FEED_SEARCH_VIDEO_POSITION)) {
                Log.d("BROADCAST_FEED_DELETE", "SEARCH_FEED_POSITION" + String.valueOf(intent.getIntExtra(InterConst.FEED_SEARCH_VIDEO_POSITION, -1)));
                mData.remove(intent.getIntExtra(InterConst.FEED_SEARCH_VIDEO_POSITION, -1));
                mVideoAdapter.notifyAdapter(mData);
                setProgressVisibility();
            } else {
                Log.d("BROADCAST_FEED_DELETE", "SEARCH_FEED_POSITION HIT_API");
            }
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_feed_search;
    }

    @Override
    protected void onCreateStuff() {
        registerReceiver(broadcastReceiver, new IntentFilter(InterConst.BROADCAST_MY_FEED_VIDEO_DELETE));
        EventBus.getDefault().register(this);

        rvVideoReviews.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mVideoAdapter = new VideoAdapter(mContext, mData);
        rvVideoReviews.setAdapter(mVideoAdapter);
        mVideoAdapter.onAdapterItemClick(this);
    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initListener() {
        imgBack.setOnClickListener(this);

        edSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) ||
                        (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_SEARCH)))
                        || (actionId == EditorInfo.IME_ACTION_DONE)) {

                    hideKeyboard(FeedSearchActivity.this);

                    mData = new ArrayList<>();
                    populateData(mData);
                    if (edSearch.getText().toString().trim().length() > 0) {
                        txtNoResult.setVisibility(View.GONE);
                        pbReviews.setVisibility(View.VISIBLE);
                        hitFeedsApi(edSearch.getText().toString().trim());
                    } else {
                        txtNoResult.setVisibility(View.VISIBLE);
                        pbReviews.setVisibility(View.GONE);
                    }

                }
                return false;
            }
        });

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    mData = new ArrayList<>();
                    populateData(mData);

                    txtNoResult.setVisibility(View.VISIBLE);
                    pbReviews.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    void hitFeedsApi(String s) {
        Call<FeedModel> call = RetrofitClient.getInstance().getSearchFeeds(mUtils.getString(InterConst.ACCESS_TOKEN, ""), s.trim());
        call.enqueue(new Callback<FeedModel>() {
            @Override
            public void onResponse(Call<FeedModel> call, Response<FeedModel> response) {
                pbReviews.setVisibility(View.GONE);
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
                pbReviews.setVisibility(View.GONE);
                toast(t.getMessage());
                setProgressVisibility();
            }
        });
    }


    void populateData(List<FeedModel.Response> response) {
        mData = new ArrayList<>();
        mData.addAll(response);
        mVideoAdapter.notifyAdapter(mData);
    }

    void setProgressVisibility() {
        if (mData.size() > 0) {
            txtNoResult.setVisibility(View.GONE);
        } else {
            txtNoResult.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected Context getContext() {
        return this;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(int position) {
        mSelectedPosition = position;

        if (mUtils.getInt(InterConst.ID, 0) != mData.get(position).getUser_id()) {
            Intent intent = new Intent(mContext, FeedDetailActivity.class);
            intent.putExtra("feedData", mData.get(position));
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        } else {
            Intent intent = new Intent(mContext, MyFeedDetailActivity.class);
            intent.putExtra("feedData", mData.get(position));
            intent.putExtra("video_id", mData.get(position).getId());

            intent.putExtra(InterConst.FEED_SEARCH_VIDEO_POSITION, position);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
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
        Intent intent = new Intent(mContext, CommentActivity.class);
        intent.putExtra("post_id", mData.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onItemShareClick(int position) {
        mSelectedPosition = position;
        shareTextUrl(mData.get(position).getUrl());
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
            mVideoAdapter.notifyItemChanged(position);
            call = RetrofitClient.getInstance().setFavArticles(
                    mUtils.getString(InterConst.ACCESS_TOKEN, ""),
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
                            showSnackBar(rvVideoReviews, response.body().getError().getMessage());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<BaseSuccessModel> call, @NonNull Throwable t) {
                    hideProgress();
                    showSnackBar(rvVideoReviews, String.valueOf(t));
                    t.printStackTrace();
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void doThis(CommentIntentServiceResult intentServiceResult) {
        Log.e("FeedDetail", "eventbus");
        if (mSelectedPosition != -1) {
            switch (intentServiceResult.getStatus()) {
                case 1:
                    mData.get(mSelectedPosition).setLiked(intentServiceResult.getItem().getLiked());
                    mData.get(mSelectedPosition).setLikes_count(intentServiceResult.getItem().getLikes_count());
                    mVideoAdapter.notifyItemChanged(mSelectedPosition);

                    break;
                case 2:
                    mData.get(mSelectedPosition).setComments_count(intentServiceResult.getItem().getComments_count());
                    mVideoAdapter.notifyItemChanged(mSelectedPosition);
                    break;
                case 3:
                    mVideoAdapter.notifyItemChanged(mSelectedPosition);
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
                    mVideoAdapter.notifyItemChanged(mSelectedPosition);

            }
        }
    }


    void itemDeleted(int position) {
        mData.remove(position);
        mVideoAdapter.notifyItemRemoved(position);
        setProgressVisibility();
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

}
