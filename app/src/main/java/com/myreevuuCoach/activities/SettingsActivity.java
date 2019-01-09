package com.myreevuuCoach.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.myreevuuCoach.R;
import com.myreevuuCoach.firebase.FirebaseChatConstants;
import com.myreevuuCoach.interfaces.InterConst;
import com.myreevuuCoach.network.RetrofitClient;
import com.myreevuuCoach.utils.AlertDialogs;
import com.myreevuuCoach.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;

/**
 * Created by dev on 4/12/18.
 */

public class SettingsActivity extends BaseActivity {

    public static final String GOOGLE_GMAIL_ID = "support@myreevuu.com";

    @BindView(R.id.imgBack)
    ImageView imgBack;
    @BindView(R.id.txtChangePassword)
    TextView txtChangePassword;
    @BindView(R.id.txtChangeEmail)
    TextView txtChangeEmail;
    @BindView(R.id.txtverification)
    TextView txtVerification;
    @BindView(R.id.txtLogOut)
    TextView txtLogOut;
    @BindView(R.id.txtNotificationPreferences)
    TextView txtNotificationPreferences;
    @BindView(R.id.txtTerms)
    TextView txtTerms;
    @BindView(R.id.txtFaq)
    TextView txtFaq;
    @BindView(R.id.txtAboutUs)
    TextView txtAboutUs;
    @BindView(R.id.txtPrivacy)
    TextView txtPrivacy;
    @BindView(R.id.txtEmailUs)
    TextView txtEmailUs;
    @BindView(R.id.txtUnreadBatchCount)
    TextView txtUnreadBatchCount;

    @BindView(R.id.txtContactUs)
    TextView txtContactUs;

    /*BroadCast PUSH REACIVED*/
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(Constants.Companion.getNEW_MESSAGE_FROM_ADMIN())) {
                setUnreadCount();
            }
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_settings;
    }

    @Override
    protected void onCreateStuff() {
        txtChangePassword.setOnClickListener(this);
        txtChangeEmail.setOnClickListener(this);

        if (!TextUtils.isEmpty(mUtils.getString(InterConst.NEW_EMAIL, ""))) {
            txtVerification.setVisibility(View.VISIBLE);
        } else {
            txtVerification.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void initListener() {
        imgBack.setOnClickListener(this);
        txtLogOut.setOnClickListener(this);
        txtNotificationPreferences.setOnClickListener(this);
        txtTerms.setOnClickListener(this);
        txtFaq.setOnClickListener(this);
        txtAboutUs.setOnClickListener(this);
        txtPrivacy.setOnClickListener(this);
        txtEmailUs.setOnClickListener(this);
        txtContactUs.setOnClickListener(this);
    }

    @Override
    protected Context getContext() {
        return this;
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.txtContactUs:
                intent = new Intent(this, ConversationActivity.class);

                intent.putExtra(InterConst.PROFILE_PIC, "");
                intent.putExtra(InterConst.NAME, InterConst.APP_ADMIN_NAME);

                ArrayList<String> mParticpantIDSList = new ArrayList<String>();

                mParticpantIDSList.add(InterConst.APP_ADMIN_ID + "_" + FirebaseChatConstants.TYPE_ADMIN);
                mParticpantIDSList.add(mUtils.getInt(FirebaseChatConstants.user_id, -1) + "_" + FirebaseChatConstants.TYPE_COACH);
                Collections.sort(mParticpantIDSList);

                String mParticpantIDS = mParticpantIDSList.toString();
                mParticpantIDS = mParticpantIDS.replace(" ", "");
                String participants = mParticpantIDS.substring(1, mParticpantIDS.length() - 1);
                intent.putExtra("participantIDs", participants);
                startActivity(intent);
                break;
            case R.id.txtChangePassword:
                intent = new Intent(mContext, ChangePasswordActivity.class);
                startActivity(intent);
                break;

            case R.id.txtNotificationPreferences:
                intent = new Intent(mContext, NotificationPreferencesActivity.class);
                startActivity(intent);
                break;

            case R.id.txtChangeEmail:
                intent = new Intent(mContext, ChangeEmailActivity.class);
                startActivity(intent);
                break;

            case R.id.imgBack:
                finish();
                break;

            case R.id.txtTerms:
                openWebview(InterConst.Webview.TERM_CONDITION, RetrofitClient.URL_TERMS_CONDITIONS);
                break;

            case R.id.txtFaq:
                openWebview(InterConst.Webview.FAQs, RetrofitClient.URL_FAQs);
                break;

            case R.id.txtAboutUs:
                openWebview(InterConst.Webview.ABOUT, RetrofitClient.URL_ABOUT_US);
                break;

            case R.id.txtPrivacy:
                openWebview(InterConst.Webview.PRIVACY, RetrofitClient.URL_PRIVACY_POLICY);
                break;

            case R.id.txtEmailUs:
                openEmail();
                break;
            case R.id.txtLogOut:
                showAlert();
                break;

        }
    }

    void openEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", GOOGLE_GMAIL_ID, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    public void openWebview(InterConst.Webview type, String urlToOpen) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(InterConst.WEBVIEW_TYPE, type);
        intent.putExtra(InterConst.WEBVIEW_URL, urlToOpen);
        startActivity(intent);
    }

    void showAlert() {
        AlertDialogs.confirmYesNoDialog(mContext, "Are you sure you want to log out?", new AlertDialogs.DialogClick() {
            @Override
            public void yes(DialogInterface dialog) {
                moveToSplash();
            }

            @Override
            public void no(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,
                new IntentFilter(Constants.Companion.getNEW_MESSAGE_FROM_ADMIN()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }


    private void setUnreadCount() {
        int count = mUtils.getInt(InterConst.APP_ADMIN_UNREAD_COUNT, 0);
        if (count > 0) {
            txtUnreadBatchCount.setVisibility(View.VISIBLE);
            txtUnreadBatchCount.setText(count + "");
        } else {
            txtUnreadBatchCount.setVisibility(View.GONE);
        }
    }

}
