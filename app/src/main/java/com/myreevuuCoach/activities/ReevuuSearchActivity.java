package com.myreevuuCoach.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.myreevuuCoach.R;
import com.myreevuuCoach.adapters.AcceptedVideoAdapter;
import com.myreevuuCoach.adapters.RequestsAdapter;
import com.myreevuuCoach.dialog.RequestAcceptedDialog;
import com.myreevuuCoach.interfaces.AdapterClickInterface;
import com.myreevuuCoach.interfaces.InterConst;
import com.myreevuuCoach.interfaces.ItemClickInterface;
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
 * Created by dev on 5/12/18.
 */

public class ReevuuSearchActivity extends BaseActivity implements AdapterClickInterface {


    @BindView(R.id.rvReviews)
    RecyclerView rvReviews;

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

    RequestsAdapter mAdapter;
    AcceptedVideoAdapter mVideoAdapter;

    ArrayList<RequestsModel.ResponseBean> mData = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_reevuu_search;
    }

    @Override
    protected void onCreateStuff() {

        if (getIntent().getStringExtra(InterConst.INTEND_EXTRA).equals(InterConst.REEVUU_REQUESTS_REQUESTED)) {
            rvReviews.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            mAdapter = new RequestsAdapter(mContext, mData, mHeight, new ItemClickInterface() {
                @Override
                public void onItemClick(int p, String type) {
                    hitRequestApi(p, type);
                }
            });
            rvReviews.setAdapter(mAdapter);
        } else {
            rvVideoReviews.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            mVideoAdapter = new AcceptedVideoAdapter(mContext, mData);
            rvVideoReviews.setAdapter(mVideoAdapter);
            mVideoAdapter.onAdapterItemClick(this);
        }
    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void initListener() {
        imgBack.setOnClickListener(this);

        edSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) || (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_SEARCH)))
                        || (actionId == EditorInfo.IME_ACTION_DONE)) {

                    hideKeyboard(ReevuuSearchActivity.this);

                    mData = new ArrayList<>();
                    populateData(mData);
                    if (edSearch.getText().toString().trim().length() > 0) {
                        txtNoResult.setVisibility(View.GONE);
                        pbReviews.setVisibility(View.VISIBLE);
                        hitSearchApi(edSearch.getText().toString().trim());
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
        Intent intent = new Intent(mContext, RequestDetailActivity.class);
        intent.putExtra(InterConst.INTEND_EXTRA, mData.get(position));
        startActivity(intent);
    }


    private void hitRequestApi(final int position, String type) {
        if (connectedToInternet(txtNoResult)) {
            showProgress();
            Call<RequestsModel> call = RetrofitClient.getInstance().response_a_request(
                    mUtils.getString(InterConst.ACCESS_TOKEN, ""),
                    String.valueOf(mData.get(position).getId()),
                    type);

            call.enqueue(new Callback<RequestsModel>() {
                @Override
                public void onResponse(Call<RequestsModel> call, Response<RequestsModel> response) {
                    hideProgress();
                    if (response.body().getResponse() != null) {
                        if (response.body().getResponse().getReview_status() == Integer.parseInt(InterConst.REEVUU_REQUESTS_ACCEPTED)) {
                            new RequestAcceptedDialog(mContext, new DialogInterface() {
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
                        setProgressVisibility();
                    } else {
                        toast(response.body().getError().getMessage());
                        if (response.body().getError().getCode() == InterConst.INVALID_ACCESS) {
                            moveToSplash();
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

    void hitSearchApi(String search) {
        if (connectedToInternet(txtNoResult)) {

            Call<RequestListModel> call = RetrofitClient.getInstance().coach_requests(
                    mUtils.getString(InterConst.ACCESS_TOKEN, ""),
                    search.trim(),
                    getIntent().getStringExtra(InterConst.INTEND_EXTRA));
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
        }
    }

    void populateData(ArrayList<RequestsModel.ResponseBean> response) {
        mData = new ArrayList<>();
        mData.addAll(response);
        if (getIntent().getStringExtra(InterConst.INTEND_EXTRA).equals(InterConst.REEVUU_REQUESTS_REQUESTED)) {
            mAdapter.notifyAdapter(mData);
        } else {
            mVideoAdapter.notifyAdapter(mData);
        }
    }


    void setProgressVisibility() {
        edSearch.setFocusable(true);
        if (mData.size() > 0) {
            txtNoResult.setVisibility(View.GONE);
        } else {
            txtNoResult.setVisibility(View.VISIBLE);
        }
    }


}
