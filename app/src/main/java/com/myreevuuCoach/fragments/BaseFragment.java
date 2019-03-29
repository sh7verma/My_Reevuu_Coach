package com.myreevuuCoach.fragments;

import android.app.Activity;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.gson.Gson;
import com.myreevuuCoach.R;
import com.myreevuuCoach.activities.SignupActivity;
import com.myreevuuCoach.firebase.Database;
import com.myreevuuCoach.firebase.FirebaseChatConstants;
import com.myreevuuCoach.firebase.FirebaseListeners;
import com.myreevuuCoach.interfaces.InterConst;
import com.myreevuuCoach.models.SignUpModel;
import com.myreevuuCoach.models.DefaultArrayModel;
import com.myreevuuCoach.services.ListenerService;
import com.myreevuuCoach.utils.ConnectionDetector;
import com.myreevuuCoach.utils.CustomLoadingDialog;
import com.myreevuuCoach.utils.MarshMallowPermission;
import com.myreevuuCoach.utils.Utils;

import butterknife.ButterKnife;

/**
 * Created by dev on 12/11/18.
 */

public abstract class BaseFragment extends Fragment {

    public MarshMallowPermission mPermission;
    protected int mWidth, mHeight;
    protected Context mContext;
    protected Context mContextActivity;
    protected String errorInternet;
    protected String platformStatus = "0";
    protected String errorAPI;
    protected String errorAccessToken;
    protected String terminateAccount;
    Utils utils;
    Gson mGson = new Gson();
    SignUpModel mSigUpModel;
    DefaultArrayModel mArrayModel;
    Database mDb;
    private Snackbar mSnackbar;

    public static void hideKeyboard(Activity mContext) {
        // Check if no views has focus:
        View view = mContext.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showKeyboard(Activity mContext) {
        // Check if no views has focus:
        View view = mContext.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        utils = new Utils(getActivity());
        mDb = new Database(getActivity());
        getDefaults();
        mPermission = new MarshMallowPermission(getActivity());
        errorInternet = getResources().getString(R.string.internet);
        errorAPI = getResources().getString(R.string.error);
        errorAccessToken = getResources().getString(R.string.invalid_access_token);
        mSigUpModel = mGson.fromJson(utils.getString(InterConst.RESPONSE, ""), SignUpModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        mSigUpModel = mGson.fromJson(utils.getString(InterConst.RESPONSE, ""), SignUpModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getContentView(), container, false);
        ButterKnife.bind(BaseFragment.this, view);
        mContext = getContext();
        mContextActivity = getActivity();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onCreateStuff();
        initListeners();
    }

    protected abstract int getContentView();

    //onCreate
    protected abstract void onCreateStuff();

    protected abstract void initListeners();

    protected void getDefaults() {
        DisplayMetrics display = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(display);
        mWidth = display.widthPixels;
        mHeight = display.heightPixels;
        Log.e("Height = ", mHeight + " width " + mWidth);
        utils.setInt("width", mWidth);
        utils.setInt("height", mHeight);
    }

    protected void showProgress() {
        CustomLoadingDialog.getLoader().showLoader(getActivity());
    }

    protected void hideProgress() {
        CustomLoadingDialog.getLoader().dismissLoader();
    }

    protected void showSnakbarAlert(View view, String message) {
        mSnackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        mSnackbar.getView().setBackgroundColor(Color.RED);
        mSnackbar.show();
    }

    protected void showSnackBar(View view, String message) {
        mSnackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        mSnackbar.show();
    }

    public boolean connectedToInternet() {
        if ((new ConnectionDetector(mContext)).isConnectingToInternet()) {
            return true;
        } else {
            return false;
        }
    }

    protected void showAlertSnackBar(View view, String message) {
        mSnackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        mSnackbar.getView().setBackgroundColor(Color.RED);
        mSnackbar.show();
    }


    public boolean connectedToInternet(View view) {
        if ((new ConnectionDetector(mContext)).isConnectingToInternet()) {
            return true;
        } else {
            showInternetAlert(view);
            return false;
        }
    }

    protected void showInternetAlert(View view) {
        mSnackbar = Snackbar.make(view, errorInternet, Snackbar.LENGTH_SHORT);
        mSnackbar.show();
    }

    public void toast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    protected void moveToSplash() {

        DatabaseReference mFirebaseConfig = FirebaseDatabase.getInstance().getReference().child(FirebaseChatConstants.USERS);
        mFirebaseConfig.child("id_" + String.valueOf(utils.getInt(FirebaseChatConstants.user_id, -1)))
                .child("online_status").setValue(ServerValue.TIMESTAMP);
        FirebaseListeners.getListenerClass(mContext).RemoveAllListeners();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            JobScheduler jobScheduler = (JobScheduler) mContext.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            jobScheduler.cancelAll();
        }


        mContext.stopService(new Intent(mContext.getApplicationContext(), ListenerService.class));

        utils.clear_shf();
        mDb.deleteAllTables();
        Intent inSplash = new Intent(mContextActivity, SignupActivity.class);
        inSplash.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        inSplash.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mContext.startActivity(inSplash);
    }


}
