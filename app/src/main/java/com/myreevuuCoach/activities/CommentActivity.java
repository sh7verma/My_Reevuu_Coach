package com.myreevuuCoach.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.myreevuuAthlete.adapters.CommentsAdapter;
import com.myreevuuCoach.R;
import com.myreevuuCoach.interfaces.InterConst;
import com.myreevuuCoach.models.BaseSuccessModel;
import com.myreevuuCoach.models.CommentModel;
import com.myreevuuCoach.models.CommentModelSingle;
import com.myreevuuCoach.network.RetrofitClient;
import com.myreevuuCoach.services.CommentIntentServiceResult;
import com.myreevuuCoach.utils.Constants;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends BaseActivity {

    ArrayList<Integer> mCommentIdArray = new ArrayList<Integer>();
    @BindView(R.id.rvComments)
    RecyclerView rvComments;
    @BindView(R.id.srlComments)
    SwipeRefreshLayout srlComments;
    @BindView(R.id.imgBackCustom)
    ImageView imgBackCustom;
    @BindView(R.id.imgSendComments)
    ImageView imgSendComments;
    @BindView(R.id.llToolbar)
    View llToolbar;
    @BindView(R.id.llOptionActionbar)
    View llOptionActionbar;
    @BindView(R.id.imgBackOption)
    ImageView imgBackOption;
    @BindView(R.id.imgDelete)
    ImageView imgDelete;
    @BindView(R.id.root_View)
    LinearLayout root_View;
    @BindView(R.id.edComments)
    EditText edComments;
    Call<CommentModel> backgroundCommentsAPICall;
    int deletePos = -1;
    int commentID = -1;
    private int postId = 1;
    private int mOffset = 1;
    private ArrayList<CommentModel.ResponseBean> mCommentsArray = new ArrayList<CommentModel.ResponseBean>();
    private CommentsAdapter mCommentAdapter = null;
    private LinearLayoutManager mLayoutManager = null;
    private CommentActivity mCommentsInstance = null;
    private Handler mHandler = null;
    private Runnable mRunnable = null;
    private int mCommentCount = 0;

    @Override
    protected int getContentView() {
        return R.layout.activity_comments;
    }

    @Override
    protected void onCreateStuff() {
        postId =getIntent().getIntExtra("post_id",0);
        mCommentsInstance = this;
        rvComments.setLayoutManager(new LinearLayoutManager(mContext));
        mCommentAdapter = new CommentsAdapter(mContext, mCommentsArray, this);
        rvComments.setAdapter(mCommentAdapter);
        srlComments.setColorSchemeResources(R.color.colorPrimary);
        srlComments.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (connectedToInternet()) {
                    mOffset++;
                    hitAPI();
                } else
                    showInternetAlert(srlComments);
            }
        });
        if (connectedToInternet())
            hitAPI();
        else
            showInternetAlert(srlComments);
    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void initListener() {
        imgBackCustom.setOnClickListener(this);
        imgSendComments.setOnClickListener(this);
        llOptionActionbar.setOnClickListener(this);
        imgBackOption.setOnClickListener(this);
        imgDelete.setOnClickListener(this);
    }

    @Override
    protected Context getContext() {
        return this;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgSendComments:
                if (connectedToInternet()) {
                    if (!edComments.getText().toString().trim().isEmpty()) {
                        postComment(edComments.getText().toString().trim());
                        edComments.setText(Constants.Companion.getEMPTY());
                    } else
                        showAlertSnackBar(imgSendComments, getString(R.string.field_not_empty));
                } else {
                    showInternetAlert(imgSendComments);
                }
                break;
            case R.id.imgBackCustom:
                moveBack();
                break;
            case R.id.imgBackOption:
                hideDeleteToolbar();
                break;
            case R.id.imgDelete:
                hitDeleteCommentAPI();
                hideDeleteToolbar();
                break;
        }
    }

    private void hideDeleteToolbar() {
        mCommentsArray.get(deletePos - 1).setSelected(false);
        mCommentAdapter.notifyItemChanged(deletePos);
        llOptionActionbar.setVisibility(View.GONE);
        llToolbar.setVisibility(View.VISIBLE);
    }

    private void showDeleteToolbar() {
        llOptionActionbar.setVisibility(View.VISIBLE);
        llToolbar.setVisibility(View.GONE);
    }

    private void postComment(String commentText) {
        if (connectedToInternet(root_View)) {
            //showLoader();
            Call<CommentModelSingle> call = RetrofitClient.getInstance().postComments(
                    mUtils.getString(InterConst.ACCESS_TOKEN, ""),
                    postId,
                    commentText);
            call.enqueue(new Callback<CommentModelSingle>() {
                @Override
                public void onResponse(@NonNull Call<CommentModelSingle> call, @NonNull Response<CommentModelSingle> response) {
                    // dismissLoader();
                    if (response.body() != null && response.body().getResponse() != null) {
                        addOwnComment(response.body().getResponse());
                    } else {
                        if (response.body().getError().getCode() == 506) {
                            onBackPressed();
                        } else if (response.body().getError().getCode() == InterConst.INVALID_ACCESS)
                            moveToSplash();
                        else
                            showAlertSnackBar(root_View, response.body().getError().getMessage());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CommentModelSingle> call, @NonNull Throwable t) {
                    // dismissLoader();
                    showAlertSnackBar(root_View, String.valueOf(t));
                    t.printStackTrace();
                }
            });
        }
    }

    private void addOwnComment(CommentModelSingle.ResponseBean response) {
        CommentModel.ResponseBean commentModel = new CommentModel.ResponseBean();
        commentModel.setUser_id(response.getUser_id());
        commentModel.setArticle_id(response.getArticle_id());
        commentModel.setName(response.getName());
        commentModel.setProfile_pic(response.getProfile_pic());
        commentModel.setComment(response.getComment());
        commentModel.setCreated_at(response.getCreated_at());
//        commentModel.date_time = getDate()
        mCommentsArray.add(commentModel);
        mCommentAdapter.notifyItemInserted(mCommentsArray.size());
        rvComments.smoothScrollToPosition(mCommentAdapter.getItemCount() - 1);
        mCommentCount++;
        updateCommentCountByBroadcast();
    }

    private void getRealTimeComments() {
        mRunnable = new Runnable() {
            @Override
            public void run() {
                if (mCommentsArray.size() > 0)
                    hitBackgroundCommentsAPI();
            }
        };
        mHandler.postDelayed(mRunnable, 3000);
    }

    private void hitAPI() {
        if (mOffset == 1)
            showProgress();

        Call<CommentModel> call = RetrofitClient.getInstance().getComments(
                mUtils.getString(InterConst.ACCESS_TOKEN, ""),
                postId,
                mOffset);
        call.enqueue(new Callback<CommentModel>() {
            @Override
            public void onResponse(@NonNull Call<CommentModel> call, @NonNull Response<CommentModel> response) {
                hideProgress();
                if (response.body().getResponse() != null) {
                    if (mOffset == 1)
                        hideProgress();

                    mCommentCount = response.body().getComments_count();
                    populateData(response.body().getResponse());
                    updateCommentCountByBroadcast();
                } else {
                    toast(response.body().getError().getMessage());
                    if (response.body().getError().getCode() == InterConst.INVALID_ACCESS) {
                        moveToSplash();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommentModel> call, @NonNull Throwable t) {
                hideProgress();
                showSnackBar(root_View, t.getLocalizedMessage());
                t.printStackTrace();
            }
        });
    }

    private void hitBackgroundCommentsAPI() {
        backgroundCommentsAPICall = RetrofitClient.getInstance().getLatestCommnets(
                mUtils.getString(InterConst.ACCESS_TOKEN, ""),
                postId);
        //mCommentsArray.get(mCommentsArray.size() - 1).getId());
        backgroundCommentsAPICall.enqueue(new Callback<CommentModel>() {
            @Override
            public void onResponse(@NonNull Call<CommentModel> call, @NonNull Response<CommentModel> response) {
                hideProgress();
                if (response.body().getResponse() != null) {
                    for (int i = 0; i < response.body().getResponse().size(); i++) {
                        CommentModel.ResponseBean commnentData = response.body().getResponse().get(i);
                        if (!mCommentIdArray.contains(commnentData.getId())) {
                            mCommentCount++;
                            mCommentIdArray.add(commnentData.getId());
                            mCommentsArray.add(commnentData);
                        }
                    }
                    mCommentAdapter.notifyDataSetChanged();
                    updateCommentCountByBroadcast();
                    getRealTimeComments();
                } else {
                    toast(response.body().getError().getMessage());
                    if (response.body().getError().getCode() == InterConst.INVALID_ACCESS) {
                        moveToSplash();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommentModel> call, @NonNull Throwable t) {
                hideProgress();
                showSnackBar(root_View, t.getLocalizedMessage());
                t.printStackTrace();
            }
        });
    }

    private void hitDeleteCommentAPI() {
        showProgress();
        Call<BaseSuccessModel> backgroundCommentsAPICall = RetrofitClient.getInstance().deleteComment(
                mUtils.getString(InterConst.ACCESS_TOKEN, ""),
                commentID);

        // mCommentsArray.remove(deletePos-1);

        backgroundCommentsAPICall.enqueue(new Callback<BaseSuccessModel>() {
            @Override
            public void onResponse(@NonNull Call<BaseSuccessModel> call, @NonNull Response<BaseSuccessModel> response) {
               hideProgress();
                mCommentAdapter.removeItem(deletePos);
                mCommentCount--;
                updateCommentCountByBroadcast();
                if (response.body().getResponse() != null && response.body().getResponse().getCode() == 111) {

                } else {
                    toast(response.body().getError().getMessage());
                    if (response.body().getError().getCode() == InterConst.INVALID_ACCESS) {
                        moveToSplash();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseSuccessModel> call, @NonNull Throwable t) {
               hideProgress();
                showSnackBar(root_View, t.getLocalizedMessage());
                t.printStackTrace();
            }
        });

    }

    private void populateData(List<CommentModel.ResponseBean> response) {
        if (mOffset == 1) {
            for (int i = 0; i < response.size(); i++) {
                mCommentIdArray.add(response.get(i).getId());
            }
            mCommentsArray.addAll(response);
            Collections.reverse(mCommentsArray);
            mCommentAdapter = new CommentsAdapter(mContext, mCommentsArray, mCommentsInstance);
            rvComments.setAdapter(mCommentAdapter);
            if (mCommentAdapter.getItemCount() > 0) {
                rvComments.smoothScrollToPosition(mCommentAdapter.getItemCount() - 1);
            }
            /// fetching comments in background
            mHandler = new Handler();
            getRealTimeComments();
        } else {
            srlComments.setRefreshing(false);
            for (int i = 0; i < response.size(); i++) {
                CommentModel.ResponseBean commnentData = response.get(i);
                if (!mCommentIdArray.contains(commnentData.getId())) {
                    mCommentIdArray.add(commnentData.getId());
                    mCommentsArray.add(0, commnentData);
                }
            }
            updateCommentCountByBroadcast();
            mCommentAdapter.notifyDataSetChanged();
        }

    }

    private void updateCommentCountByBroadcast() {
        EventBus.getDefault().post(new CommentIntentServiceResult(2, mCommentCount, null));
    }

    @Override
    public void onBackPressed() {
        moveBack();
    }

    private void moveBack() {
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        }
        if (backgroundCommentsAPICall != null) {
            backgroundCommentsAPICall.cancel();
        }
        //Constants.closeKeyboard(mContext!!, imgBackCustom)
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    public void deleteComment(final int adapterPosition, final int commentID) {
        deletePos = adapterPosition;
        this.commentID = commentID;
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Delete Comment")
                .setMessage("Are you sure you want to delete this comment?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        hitDeleteCommentAPI();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

        // showDeleteToolbar();
    }
}
