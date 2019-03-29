package com.myreevuuCoach.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.myreevuuCoach.R;
import com.myreevuuCoach.adapters.NotificationCenterAdapter;
import com.myreevuuCoach.interfaces.AdapterClickInterface;
import com.myreevuuCoach.interfaces.InterConst;
import com.myreevuuCoach.models.NotificationCenterModel;
import com.myreevuuCoach.models.SkipModel;
import com.myreevuuCoach.network.RetrofitClient;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationCenterActivity extends BaseActivity implements AdapterClickInterface {
    NotificationCenterAdapter notificationCenterAdapter;
    @BindView(R.id.tv_no_notifcation)
    TextView tv_no_notifcation;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    @BindView(R.id.pbFeed)
    AVLoadingIndicatorView pbFeed;
    @BindView(R.id.img_back)
    ImageView img_back;
    int notificationReadType = 1;//1 outer and 2 notification Read
    int notificationID = -1;
    private ArrayList<NotificationCenterModel.NotificationResponse> listNotifications = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_notification_center;
    }

    @Override
    protected void onCreateStuff() {
        tv_no_notifcation.setText("No Notifications");
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        notificationCenterAdapter = new NotificationCenterAdapter(this, listNotifications, this);
        recyclerView.setAdapter(notificationCenterAdapter);
        notificationReadType = 1;
        notificationUpdate();
        getNotificationList();

    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void initListener() {
        img_back.setOnClickListener(this);
    }

    @Override
    protected Context getContext() {
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }

    void showLoader() {
        pbFeed.setVisibility(View.VISIBLE);
    }

    void dismissLoader() {
        pbFeed.setVisibility(View.GONE);
    }

    private void getNotificationList() {
        if (connectedToInternet()) {
            showLoader();
            listNotifications.clear();
            notificationCenterAdapter.notifyDataSetChanged();
            Call<NotificationCenterModel> call = RetrofitClient.getInstance().getNotification(
                    mUtils.getString(InterConst.ACCESS_TOKEN, ""));
            call.enqueue(new Callback<NotificationCenterModel>() {
                @Override
                public void onResponse(@NonNull Call<NotificationCenterModel> call, @NonNull Response<NotificationCenterModel> response) {
                    dismissLoader();
                    if (response.body().getResponse() != null) {
                        listNotifications.addAll(response.body().getResponse());
                        if (listNotifications.size() > 0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            tv_no_notifcation.setVisibility(View.GONE);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            tv_no_notifcation.setVisibility(View.VISIBLE);
                        }
                        notificationCenterAdapter.notifyDataSetChanged();
                    } else {
                        if (response.body().getError().getCode() == InterConst.INVALID_ACCESS)
                            moveToSplash();
                        else {
                            showSnackBar(tv_no_notifcation, response.body().getError().getMessage());
                            recyclerView.setVisibility(View.GONE);
                            tv_no_notifcation.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<NotificationCenterModel> call, @NonNull Throwable t) {
                    dismissLoader();
                    showSnackBar(tv_no_notifcation, t.getMessage());
                    recyclerView.setVisibility(View.GONE);
                    tv_no_notifcation.setVisibility(View.VISIBLE);
                    t.printStackTrace();
                }
            });
        }
    }

    private void notificationUpdate() {
        if (connectedToInternet(tv_no_notifcation)) {
            Call<SkipModel> call = RetrofitClient.getInstance().setNotification(
                    mUtils.getString(InterConst.ACCESS_TOKEN, ""),
                    notificationReadType + "",
                    notificationID + "","");
            call.enqueue(new Callback<SkipModel>() {
                @Override
                public void onResponse(@NonNull Call<SkipModel> call, @NonNull Response<SkipModel> response) {
                    if (response.body().getResponse() != null) {

                    } else {
                        if (response.body().getError().getCode() == InterConst.INVALID_ACCESS)
                            moveToSplash();
                        else
                            showAlertSnackBar(tv_no_notifcation, response.body().getError().getMessage());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<SkipModel> call, @NonNull Throwable t) {
                    showAlertSnackBar(tv_no_notifcation, t.getMessage());
                    t.printStackTrace();
                }
            });
        }
    }

    @Override
    public void onItemClick(int rowPosition) {
        notificationID = listNotifications.get(rowPosition).getNotificationId();
        notificationReadType = 2;
        notificationUpdate();
        String push_type = listNotifications.get(rowPosition).getPush_type();
        listNotifications.get(rowPosition).setRead_status(1);
        Intent intent = null;
        if (push_type.equals("4") || push_type.equals("12")) {
            intent = new Intent(this, RequestDetailActivity.class);
            intent.putExtra("reviewRequestId", listNotifications.get(rowPosition).getReview_request_id() + "");
            startActivity(intent);
        }
        notificationCenterAdapter.notifyItemChanged(rowPosition);
    }
}
