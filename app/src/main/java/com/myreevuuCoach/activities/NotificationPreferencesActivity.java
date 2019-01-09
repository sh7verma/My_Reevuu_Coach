package com.myreevuuCoach.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.myreevuuCoach.R;
import com.myreevuuCoach.interfaces.InterConst;
import com.myreevuuCoach.models.BaseSuccessModel;
import com.myreevuuCoach.network.RetrofitClient;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dev on 4/12/18.
 */

public class NotificationPreferencesActivity extends BaseActivity {

    @BindView(R.id.imgBack)
    ImageView imgBack;
    @BindView(R.id.llPush)
    LinearLayout llPush;
    @BindView(R.id.llEmailPush)
    LinearLayout llEmailPush;
    @BindView(R.id.imgPush)
    ImageView imgPush;
    @BindView(R.id.imgEmailPush)
    ImageView imgEmailPush;

    @Override
    protected int getContentView() {
        return R.layout.activity_notification_preferences;
    }

    @Override
    protected void onCreateStuff() {

    }

    @Override
    protected void initUI() {
        llPush.setOnClickListener(this);
        llEmailPush.setOnClickListener(this);
        imgBack.setOnClickListener(this);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected Context getContext() {
        return this;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llPush:
                goToNotificationSettings(InterConst.NOTIFICATION_CHANNEL);
                break;
            case R.id.imgBack:
                finish();
                break;
            case R.id.llEmailPush:
                if (mUtils.getInt(InterConst.EMAIL_PUSH_STATUS, 1) == 1) {
                    changeEmailPushPrefrences(0);
                } else {
                    changeEmailPushPrefrences(1);
                }
                break;
        }
    }

    private void changeEmailPushPrefrences(final int updatedStatus) {
        if (connectedToInternet(llEmailPush)) {
            showProgress();
            Call<BaseSuccessModel> call = RetrofitClient.getInstance().notification_preferences(
                    mUtils.getString(InterConst.ACCESS_TOKEN, ""),
                    String.valueOf(updatedStatus));
            call.enqueue(new retrofit2.Callback<BaseSuccessModel>() {
                @Override
                public void onResponse(@NonNull Call<BaseSuccessModel> call, @NonNull Response<BaseSuccessModel> response) {
                    hideProgress();
                    if (response.body().getResponse() != null) {
                        mUtils.setInt(InterConst.EMAIL_PUSH_STATUS, updatedStatus);
                        changeEmailPushNotification();
                    } else {
                        if (response.body().getError().getCode() == InterConst.INVALID_ACCESS)
                            moveToSplash();
                        else
                            showAlertSnackBar(llEmailPush, response.body().getError().getMessage());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<BaseSuccessModel> call, @NonNull Throwable t) {
                    hideProgress();
                    toast(t.getMessage());
                    t.printStackTrace();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NotificationManagerCompat.from(mContext).areNotificationsEnabled()) {
            imgPush.setImageResource(R.mipmap.ic_on);
        } else {
            imgPush.setImageResource(R.mipmap.ic_off);
        }
        changeEmailPushNotification();
    }

    private void changeEmailPushNotification() {
        if (mUtils.getInt(InterConst.EMAIL_PUSH_STATUS, 1) == 1) {
            imgEmailPush.setImageResource(R.mipmap.ic_on);
        } else {
            imgEmailPush.setImageResource(R.mipmap.ic_off);
        }
    }


    public void goToNotificationSettings(String channel) {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (channel != null) {
                intent.setAction(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel);
            } else {
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            }
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, mContext.getPackageName());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, mContext.getPackageName());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra("app_package", mContext.getPackageName());
            intent.putExtra("app_uid", mContext.getApplicationInfo().uid);
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + mContext.getPackageName()));
        }
        mContext.startActivity(intent);
    }
}
