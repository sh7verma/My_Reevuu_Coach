package com.myreevuuCoach.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
 * Created by dev on 12/12/18.
 */

public class FeedSearchActivity extends BaseActivity implements AdapterClickInterface {


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
//            Log.d("SEARCH_FEED_POSITION", String.valueOf(intent.getIntExtra(InterConst.FEED_SEARCH_VIDEO_POSITION, -1)));
//            mData.remove(intent.getIntExtra(InterConst.FEED_SEARCH_VIDEO_POSITION, -1));
//            mVideoAdapter.notifyAdapter(mData);
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_feed_search;
    }

    @Override
    protected void onCreateStuff() {
        registerReceiver(broadcastReceiver, new IntentFilter(InterConst.BROADCAST_MY_FEED_VIDEO_DELETE));

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
}
