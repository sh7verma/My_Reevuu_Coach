package com.myreevuuCoach.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.myreevuuCoach.R;
import com.myreevuuCoach.fragments.ReevuuFragment;
import com.myreevuuCoach.interfaces.InterConst;
import com.myreevuuCoach.network.RetrofitClient;

import butterknife.BindView;

/**
 * Created by dev on 12/12/18.
 */

public class StartVideoReviewActivity extends BaseActivity {

    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    String url;

    @Override
    protected int getContentView() {
        return R.layout.activity_review_activity;
    }

    @Override
    protected void onCreateStuff() {

        Log.d("URL_VIDEO", getIntent().getStringExtra(InterConst.VIDEO_URL));
        if (getIntent().hasExtra(InterConst.REVIEW_REQUEST_ID)) {
            url = RetrofitClient.URL_REVIEW_VIDEO + getIntent().getStringExtra(InterConst.REVIEW_REQUEST_ID) + "/video";
            Log.d("URL", url);
        } else {
            finish();
        }
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
            }
        });

        webView.loadUrl(url);
    }


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }


    @Override
    protected void initUI() {

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
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onFormResubmission(WebView view, Message dontResend, Message resend) {
            super.onFormResubmission(view, dontResend, resend);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.e("webview", "URL = " + url);
            progressBar.setVisibility(View.VISIBLE);
            if (url.contains("type=1")) {
                try {
                    ReevuuFragment.getInstance().onCallResume();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            } else if (url.contains("type=2")) {
                finish();
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
    }


}
