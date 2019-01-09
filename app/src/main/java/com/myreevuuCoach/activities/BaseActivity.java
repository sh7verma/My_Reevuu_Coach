package com.myreevuuCoach.activities;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.gson.Gson;
import com.myreevuuCoach.R;
import com.myreevuuCoach.firebase.Database;
import com.myreevuuCoach.firebase.FirebaseChatConstants;
import com.myreevuuCoach.firebase.FirebaseListeners;
import com.myreevuuCoach.interfaces.InterConst;
import com.myreevuuCoach.models.SignUpModel;
import com.myreevuuCoach.utils.ConnectionDetector;
import com.myreevuuCoach.utils.CustomLoadingDialog;
import com.myreevuuCoach.services.ListenerService;
import com.myreevuuCoach.utils.MarshMallowPermission;
import com.myreevuuCoach.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by dev on 12/11/18.
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    public MarshMallowPermission mPermission;
    protected int mWidth, mHeight;
    protected Context mContext;
    protected String errorInternet;
    protected String platformStatus = "0";
    protected String deviceToken = "0";
    protected String errorAPI;
    protected String errorAccessToken;
    protected String terminateAccount;
    Utils mUtils;
    Gson mGson = new Gson();
    Typeface typefaceLight, typefaceRegular, typefaceBold;
    String TAG;
    SignUpModel mSigUpModel;
    private Snackbar mSnackbar;
    Database db;

    public static void hideKeyboard(Activity mContext) {
        // Check if no views has focus:
        View view = mContext.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void hideKeyboardDialog(Activity mContext) {
        // Check if no views has focus:
        View view = mContext.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FacebookSdk.sdkInitialize(this);
        setContentView(getContentView());
        mUtils = new Utils(BaseActivity.this);
        mContext = getContext();
        ButterKnife.bind(this);

        TAG = mContext.getClass().getName();
        db = new Database(this);
        getDefaults();
        mPermission = new MarshMallowPermission(this);
        typefaceLight = Typeface.createFromAsset(getAssets(), "light.ttf");
        typefaceRegular = Typeface.createFromAsset(getAssets(), "regular.ttf");
        typefaceBold = Typeface.createFromAsset(getAssets(), "bold.ttf");

        errorInternet = getResources().getString(R.string.internet);
        errorAPI = getResources().getString(R.string.error);
        errorAccessToken = getResources().getString(R.string.invalid_access_token);

        mSigUpModel = mGson.fromJson(mUtils.getString("response", ""), SignUpModel.class);

        initUI();
        onCreateStuff();
        initListener();
    }


    @Override
    protected void onStart() {
        super.onStart();
        onPosted();
        onStarted();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    protected abstract int getContentView();

    protected void showProgress() {
        CustomLoadingDialog.getLoader().showLoader(BaseActivity.this);
    }

    protected void hideProgress() {
        CustomLoadingDialog.getLoader().dismissLoader();
    }

    //onCreate
    protected abstract void onCreateStuff();

    //onStart
    protected void onStarted() {

    }

    //onPostCreate
    protected void onPosted() {

    }

    protected abstract void initUI();

    protected abstract void initListener();

    protected void getDefaults() {
        DisplayMetrics display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
        mWidth = display.widthPixels;
        mHeight = display.heightPixels;
        Log.e("Height = ", mHeight + " width " + mWidth);
        mUtils.setInt("width", mWidth);
        mUtils.setInt("height", mHeight);
    }

    protected void moveToSplash() {
        DatabaseReference mFirebaseConfig = FirebaseDatabase.getInstance()
                .getReference().child(FirebaseChatConstants.USERS);
        mFirebaseConfig.child("id_" + String.valueOf(mUtils.getInt(FirebaseChatConstants.user_id, -1)))
                .child("online_status").setValue(ServerValue.TIMESTAMP);

        FirebaseListeners.getListenerClass(mContext).RemoveAllListeners();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            JobScheduler jobScheduler = (JobScheduler) mContext.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            jobScheduler.cancelAll();
        }

        mContext.stopService(new Intent(mContext.getApplicationContext(), ListenerService.class));
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.cancelAll();
        stopService(new Intent(getApplicationContext(), ListenerService.class));

        mUtils.clear_shf();
        db.deleteAllTables();
        Intent inSplash = new Intent(mContext, SignupActivity.class);
        inSplash.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        inSplash.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(inSplash);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        finish();
    }

    protected void showSnackBar(View view, String message) {
        mSnackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        mSnackbar.show();
    }

    protected abstract Context getContext();

    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void showAlertSnackBar(View view, String message) {
        mSnackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        mSnackbar.getView().setBackgroundColor(Color.RED);
        mSnackbar.show();
    }

    public boolean connectedToInternet() {
        if ((new ConnectionDetector(mContext)).isConnectingToInternet()) {
            return true;
        } else {
            return false;
        }
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

    protected RequestBody createPartFromString(String data) {
        return RequestBody.create(MediaType.parse("text/plain"), data);
    }

    protected MultipartBody.Part createImageFilePart(File mFile, String name) {
        RequestBody reqFile;
        if (mFile != null) {
            reqFile = RequestBody.create(MediaType.parse("image/*"), mFile);
            return MultipartBody.Part.createFormData(name, mFile.getName(), reqFile);
        } else {
            reqFile = RequestBody.create(MediaType.parse("image/*"), "");
            return MultipartBody.Part.createFormData("", "", reqFile);
        }
    }

    protected MultipartBody.Part createVideoFilePart(File mFile, String name) {
        RequestBody reqFile;
        if (mFile != null) {
            reqFile = RequestBody.create(MediaType.parse("video/*"), mFile);
            return MultipartBody.Part.createFormData(name, mFile.getName(), reqFile);
        } else {
            reqFile = RequestBody.create(MediaType.parse("video/*"), "");
            return MultipartBody.Part.createFormData("", "", reqFile);
        }
    }

    void setUserData(SignUpModel response) {
        mUtils.setString(InterConst.RESPONSE, mGson.toJson(response));
        mUtils.setString(InterConst.EMAIL, response.getResponse().getEmail());
        mUtils.setString(InterConst.USER_NAME, response.getResponse().getUsername());
        mUtils.setInt(InterConst.USER_TYPE, response.getResponse().getUser_type());

        mUtils.setString(InterConst.PHONE_NUMBER, response.getResponse().getPhone_number());
        mUtils.setInt(InterConst.GENDER_STATUS, response.getResponse().getGender());
        mUtils.setString(InterConst.NAME, response.getResponse().getName());
        mUtils.setString(InterConst.PROFILE_PIC, response.getResponse().getProfile_pic());
        mUtils.setInt(InterConst.ID, response.getResponse().getId());

        mUtils.setString(InterConst.EMAIL, response.getResponse().getEmail());
        mUtils.setString(InterConst.ACCESS_TOKEN, response.getResponse().getAccess_token());
        mUtils.setInt(InterConst.EMAIL_VERIFIED, response.getResponse().getEmail_verified());
        mUtils.setInt(InterConst.PROFILE_STATUS, response.getResponse().getProfile_status());

        mUtils.setString(InterConst.NEW_EMAIL, response.getResponse().getNew_email());

        mUtils.setInt(InterConst.PROFILE_APPROVED, response.getResponse().is_approved());

        mUtils.setInt(InterConst.EMAIL_PUSH_STATUS, response.getResponse().getEmail_notification());


        if (mUtils.getInt(InterConst.GENDER_STATUS, -1) == 0) {
            mUtils.setString(InterConst.GENDER, InterConst.FEMALE);
        } else if (mUtils.getInt(InterConst.GENDER_STATUS, -1) == 1) {
            mUtils.setString(InterConst.GENDER, InterConst.MALE);
        } else if (mUtils.getInt(InterConst.GENDER_STATUS, -1) == 2) {
            mUtils.setString(InterConst.GENDER, InterConst.OTHER);
        }
    }

    private String getAge(String dobString) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            date = sdf.parse(dobString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date == null) return "0";

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.setTime(date);

        int year = dob.get(Calendar.YEAR);
        int month = dob.get(Calendar.MONTH);
        int day = dob.get(Calendar.DAY_OF_MONTH);

        dob.set(year, month + 1, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return age + "";
    }

    File imageToFile(Bitmap bitmap, String name) {
        File filesDir = mContext.getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        return imageFile;
    }
}
