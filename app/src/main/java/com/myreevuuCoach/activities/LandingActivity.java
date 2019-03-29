package com.myreevuuCoach.activities;

import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.myreevuuCoach.BuildConfig;
import com.myreevuuCoach.R;
import com.myreevuuCoach.adapters.ChatsAdapter;
import com.myreevuuCoach.adapters.FragmentPagerAdapter;
import com.myreevuuCoach.customViews.CustomViewPager;
import com.myreevuuCoach.customrecorder.views.ActivityCustomRecorder;
import com.myreevuuCoach.dialog.DialogVideoOption;
import com.myreevuuCoach.dialog.RecordVideoDialog;
import com.myreevuuCoach.firebase.FirebaseChatConstants;
import com.myreevuuCoach.firebase.FirebaseListeners;
import com.myreevuuCoach.fragments.ChatFragment;
import com.myreevuuCoach.fragments.HomeFragment;
import com.myreevuuCoach.fragments.ProfileFragment;
import com.myreevuuCoach.fragments.ProfileInfoFragment;
import com.myreevuuCoach.fragments.ProfileVideoFragment;
import com.myreevuuCoach.fragments.ReevuuAcceptedFragment;
import com.myreevuuCoach.fragments.ReevuuFragment;
import com.myreevuuCoach.fragments.ReevuuReviewedFragment;
import com.myreevuuCoach.gallery.ActivityVideoGallery;
import com.myreevuuCoach.gallery.VideoGalleryModel;
import com.myreevuuCoach.gallery.VideoUtils;
import com.myreevuuCoach.interfaces.DialogRecordVideoCallBack;
import com.myreevuuCoach.interfaces.InterConst;
import com.myreevuuCoach.interfaces.UnreadBatchInterface;
import com.myreevuuCoach.models.DefaultArrayModel;
import com.myreevuuCoach.models.NotificationCenterModel;
import com.myreevuuCoach.models.SignUpModel;
import com.myreevuuCoach.models.SkipModel;
import com.myreevuuCoach.network.RetrofitClient;
import com.myreevuuCoach.services.JobSchedulerService;
import com.myreevuuCoach.services.ListenerService;
import com.myreevuuCoach.utils.Constants;
import com.myreevuuCoach.utils.MarshMallowPermission;
import com.myreevuuCoach.utils.RealFilePathUtil;
import com.myreevuuCoach.utils.ScreenOffReceiver;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import me.leolin.shortcutbadger.ShortcutBadger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dev on 12/11/18.
 */

public class LandingActivity extends BaseActivity implements DialogRecordVideoCallBack, FirebaseListeners.ProfileListenerInterface, UnreadBatchInterface {

    private static final int UPLOAD_VIDEO = 101;
    private static final int RESULT_CAMERA = 110;
    private static final int RESULT_GALLERY = 120;
    boolean isAdapterNull = false;

    @BindView(R.id.vp_landing)
    CustomViewPager vpLanding;

    @BindView(R.id.imgUploadVideo)
    ImageView imgUploadVideo;
    @BindView(R.id.imgTipHint)
    ImageView imgTipHint;
    @BindView(R.id.rlTips)
    RelativeLayout rlTips;
    @BindView(R.id.txtGOTIT)
    TextView txtGOTIT;

    // bottom bar
    @BindView(R.id.llBottomNavigation)
    LinearLayout llBottomNavigation;

    @BindView(R.id.llHome)
    LinearLayout llHome;
    @BindView(R.id.llChat)
    LinearLayout llChat;
    @BindView(R.id.llReview)
    LinearLayout llReview;
    @BindView(R.id.llProfile)
    LinearLayout llProfile;

    @BindView(R.id.imgHome)
    ImageView imgHome;
    @BindView(R.id.txtHome)
    TextView txtHome;

    @BindView(R.id.imgChat)
    ImageView imgChat;
    @BindView(R.id.txtChat)
    TextView txtChat;

    @BindView(R.id.imgReevuu)
    ImageView imgReevuu;
    @BindView(R.id.txtReevuu)
    TextView txtReevuu;

    @BindView(R.id.imgProfile)
    ImageView imgProfile;
    @BindView(R.id.txtProfile)
    TextView txtProfile;
    @BindView(R.id.txtUnreadBatchCount)
    TextView txtUnreadBatchCount;

    FragmentPagerAdapter mAdapter;
    int mSelected = InterConst.FRAG_NULL;
    ScreenOffReceiver screenOffBroadcast = new ScreenOffReceiver();
    String recordVideoPath = "" + "temp.mp4";
    /*BroadCast PUSH REACIVED*/
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // if (intent.hasExtra(Constants.Companion.getNEW_MESSAGE())) {
            updateNotificationBell();
            //  }
        }
    };
    private ArrayList<NotificationCenterModel.NotificationResponse> listBroadcastPush = new ArrayList<>();

    private void updateNotificationBell() {
        ((HomeFragment) mAdapter.getFragment(InterConst.FRAG_HOME)).checkUnreadNotification();
        ((ChatFragment) mAdapter.getFragment(InterConst.FRAG_CHAT)).checkUnreadNotification();
        ((ReevuuFragment) mAdapter.getFragment(InterConst.FRAG_REEVUU)).checkUnreadNotification();


    }

    @Override
    public int getContentView()/**/ {
        return R.layout.activity_landing;
    }

    @Override
    public void initUI() {
        mUtils.setInt(InterConst.APP_ICON_BADGE_COUNT, 0);
        ShortcutBadger.removeCount(mContext);
        initIntercom();
        checkForBroadcastFromNotification();
        getBroadcastPush();

    }

    private void initIntercom() {
//      Register a user with Intercom
        updateOnInterCom();
    }

    @Override
    public void onCreateStuff() {
        registerReceiver(screenOffBroadcast, new IntentFilter(Intent.ACTION_SCREEN_ON));
        registerReceiver(screenOffBroadcast, new IntentFilter(Intent.ACTION_SCREEN_OFF));

        FirebaseListeners.setProfileDataListener(this);

        if (mUtils.getInt(InterConst.DISPLAY_TIP, 0) == 0) {
            if (mSigUpModel.getResponse().getSkip_tip() == 0) {
                rlTips.setVisibility(View.VISIBLE);
            } else {
                rlTips.setVisibility(View.GONE);
            }
        } else {
            rlTips.setVisibility(View.GONE);
        }
        hitDefaultArrayApi();

        if (mUtils.getInt(InterConst.PROFILE_APPROVED, 0) == 0) {
            hitCoachProfileApi();
        }

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager());
        mAdapter.addFragment(HomeFragment.newInstance(this));
        mAdapter.addFragment(ChatFragment.Companion.newInstance(this));
        mAdapter.addFragment(ReevuuFragment.newInstance(this));
        mAdapter.addFragment(ProfileFragment.newInstance(this));

        vpLanding.setPagingEnabled(false);
        vpLanding.setAdapter(mAdapter);
        vpLanding.setOffscreenPageLimit(4);

        vpLanding.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("onPageSelected", "LandingActivity" + String.valueOf(position));
                Log.d("onPageSelected", "mSelected" + String.valueOf(mSelected));

                mSelected = position;

                if (position == InterConst.FRAG_HOME) {
                    ((HomeFragment) mAdapter.getFragment(InterConst.FRAG_HOME)).onCallResume();
                } else {
                    ((HomeFragment) mAdapter.getFragment(InterConst.FRAG_HOME)).onCallPause();
                }

                if (position != InterConst.FRAG_PROFILE) {
                    ((ProfileFragment) mAdapter.getFragment(InterConst.FRAG_PROFILE)).onCallPause();
                }

                if (position == InterConst.FRAG_REEVUU) {
                    ((ReevuuFragment) mAdapter.getFragment(InterConst.FRAG_REEVUU)).onCallResume();
                } else {
                    ((ReevuuFragment) mAdapter.getFragment(InterConst.FRAG_REEVUU)).onCallPause();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        loadFragment(InterConst.FRAG_HOME);

    }

    public void setPagerItem(int position) {
        loadFragment(position);
        ReevuuFragment.getInstance().setPagerFirstItem();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,
                new IntentFilter(Constants.Companion.getNEW_MESSAGE()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(screenOffBroadcast);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);

        if (!TextUtils.isEmpty(mUtils.getString(InterConst.ACCESS_TOKEN, ""))
                && mUtils.getInt(InterConst.PROFILE_STATUS, 0) == 2) {
            FirebaseDatabase.getInstance().getReference().child(FirebaseChatConstants.USERS)
                    .child("id_" + String.valueOf(mUtils.getInt(FirebaseChatConstants.user_id, -1)))
                    .child("online_status")
                    .setValue(ServerValue.TIMESTAMP);
        }
    }

    @Override
    public void initListener() {
        llHome.setOnClickListener(this);
        llChat.setOnClickListener(this);
        llReview.setOnClickListener(this);
        llProfile.setOnClickListener(this);
        txtGOTIT.setOnClickListener(this);
        rlTips.setOnClickListener(this);
        imgUploadVideo.setOnClickListener(this);

        ChatsAdapter.setUnreadBatchListner(this);
        callService();
    }

    public void loadFragment(int selected) {
        hideKeyboard(this);

        imgHome.setImageResource(R.mipmap.ic_home);
        txtHome.setTextColor(getResources().getColor(R.color.colorGrey));

        imgChat.setImageResource(R.mipmap.ic_chat);
        txtChat.setTextColor(getResources().getColor(R.color.colorGrey));

        imgReevuu.setImageResource(R.mipmap.ic_review);
        txtReevuu.setTextColor(getResources().getColor(R.color.colorGrey));

        imgProfile.setImageResource(R.mipmap.ic_profile);
        txtProfile.setTextColor(getResources().getColor(R.color.colorGrey));

        if (selected == InterConst.FRAG_HOME) {
            imgHome.setImageResource(R.mipmap.ic_home_selected);
            txtHome.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else if (selected == InterConst.FRAG_CHAT) {
            imgChat.setImageResource(R.mipmap.ic_chats_selected);
            txtChat.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else if (selected == InterConst.FRAG_REEVUU) {
            imgReevuu.setImageResource(R.mipmap.ic_review_selected);
            txtReevuu.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else if (selected == InterConst.FRAG_PROFILE) {
            imgProfile.setImageResource(R.mipmap.ic_profile_selected);
            txtProfile.setTextColor(getResources().getColor(R.color.colorPrimary));
        }

        mSelected = selected;
        vpLanding.setCurrentItem(selected);
    }

    void callService() {
        // get job api service
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            ComponentName jobService = new ComponentName(getPackageName(), JobSchedulerService.class.getName());
            JobInfo jobInfo;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                jobInfo = new JobInfo.Builder(1, jobService).setMinimumLatency(5000).build();
            } else {
                jobInfo = new JobInfo.Builder(1, jobService).setPeriodic(5000).build();
            }
            jobScheduler.schedule(jobInfo);
        } else {
            if (checkServiceRunning()) {

            } else {
                startService(new Intent(getApplicationContext(), ListenerService.class));
            }
        }
    }

    public boolean checkServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.myreevuuCoach.services.ListenerService"
                    .equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // bottom bar
            case R.id.llHome:
                loadFragment(InterConst.FRAG_HOME);
                break;

            case R.id.llChat:
                loadFragment(InterConst.FRAG_CHAT);
                break;

            case R.id.llReview:
                loadFragment(InterConst.FRAG_REEVUU);
                break;

            case R.id.llProfile:
                loadFragment(InterConst.FRAG_PROFILE);
                break;

            case R.id.txtGOTIT:
                hitSkipApi();
                mUtils.setInt(InterConst.DISPLAY_TIP, 1);
                rlTips.setVisibility(View.GONE);
                break;

            case R.id.imgUploadVideo:
                if (!mPermission.checkPermissionForCamera() || !mPermission.checkPermissionForExternalStorage()) {
                    mPermission.requestPhoneStatePermission(mPermission.permissionList, InterConst.CAMERA_PERMISSION,
                            MarshMallowPermission.CAMERA_PERMISSION_REQUEST_CODE,
                            R.string.camera_permission_mess);
                } else {
                    openVideoOptionDialog();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MarshMallowPermission.CAMERA_PERMISSION_REQUEST_CODE:

                if (grantResults.length == 3) {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                            && grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                            grantResults.length > 0 && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                        // permissions granted.
                        openVideoOptionDialog();
                    }
                } else if (grantResults.length == 2) {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                            && grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        // permissions granted.
                        openVideoOptionDialog();
                    }
                } else if (grantResults.length == 1) {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // permissions granted.
                        openVideoOptionDialog();
                    }
                }
                break;
        }
    }

    private void openVideoOptionDialog() {
        new DialogVideoOption(LandingActivity.this, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            File f;
            String videoPath;


            switch (requestCode) {
                case InterConst.REQ_BROADCAST:
                    listBroadcastPush.remove(0);
                    checkForBroadCast();
                case UPLOAD_VIDEO:
                    ((HomeFragment) mAdapter.getFragment(InterConst.FRAG_HOME)).onCallResume();
                    loadFragment(InterConst.FRAG_PROFILE);
                    ProfileFragment.getInstance().setPagerItem(InterConst.PROFILE_VIDEO_FRAG);

                    break;
                case RESULT_CAMERA:
                    resetAdapters();
                    f = new File(Environment.getExternalStorageDirectory() +
                            File.separator + InterConst.APP_NAME, recordVideoPath);
                    videoPath = f.getAbsolutePath();
                    Log.d("SelectedVideoPathCamera", videoPath);
                    try {
                        Bitmap thumb = VideoUtils.getVideoThumb(videoPath);
                        if (thumb == null) {
                            thumb = VideoUtils.createThumbnailAtTime(videoPath);
                        }
                        String realVideoTime = VideoUtils.getVideoTime(mContext, videoPath);
                        String videoTime = VideoUtils.convertMilliSecToTime(Long.parseLong(realVideoTime));
                        if (VideoUtils.checkVideoMinTimeValid((Long.parseLong(realVideoTime)))) {
                            if (VideoUtils.checkVideoMaxTimeValid((Long.parseLong(realVideoTime)))) {
                                VideoGalleryModel.setSelectedVideoInstance(new VideoGalleryModel(videoPath, thumb, videoTime, false));

                                Intent intent = new Intent(this, AddVideoDetailActivity.class);
                                startActivityForResult(intent, UPLOAD_VIDEO);

                            } else {
                                toast(getString(R.string.video_is_greater_than_));
                            }
                        } else {
                            toast(getString(R.string.video_is_less));
                        }
                    } catch (Exception e) {
                        toast(getString(R.string.faled_to_capture_video));

                        e.printStackTrace();
                    }
//                    try {
//                        String path = data.getStringExtra("file_path");
//                        selectedVideoValidation(path, UPLOAD_VIDEO);
//                    } catch (Exception e) {
//                        toast("Failed to load");
//                        e.printStackTrace();
//                    }

                    break;

                case RESULT_GALLERY:
                    resetAdapters();
                    if (data.getData() != null) {
                        try {
                            Uri uri = data.getData();
                            String path = RealFilePathUtil.getPath(this, uri);
                            selectedVideoValidation(path, UPLOAD_VIDEO);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //USELESS CODE

                    /*Uri uri = null;
                    if (data != null) {
                        uri = data.getData();
                        Log.i(TAG, "Uri: " + uri.toString());
                    }

                    f = new File(FileUriUtils.getImageRealPath(getContentResolver(), uri, null));
                    videoPath = f.getAbsolutePath();
                    Log.d("SelectedVideoPathCamera", videoPath);
                    try {
                        Bitmap thumb = VideoUtils.getVideoThumb(videoPath);
                        if (thumb == null) {
                            thumb = VideoUtils.createThumbnailAtTime(videoPath);
                        }
                        String realVideoTime = VideoUtils.getVideoTime(mContext, videoPath);
                        String videoTime = VideoUtils.convertMilliSecToTime(Long.parseLong(realVideoTime));
                        if (VideoUtils.checkVideoMinTimeValid((Long.parseLong(realVideoTime)))) {
                            if (VideoUtils.checkVideoMaxTimeValid((Long.parseLong(realVideoTime)))) {
                                VideoGalleryModel.setSelectedVideoInstance(new VideoGalleryModel(videoPath, thumb, videoTime, false));

                                Intent intent = new Intent(this, AddVideoDetailActivity.class);
                                startActivityForResult(intent, UPLOAD_VIDEO);

                            } else {
                                toast(getString(R.string.video_is_greater_than_));
                            }
                        } else {
                            toast(getString(R.string.video_is_less));
                        }
                    } catch (Exception e) {
                        toast(getString(R.string.faled_to_capture_video));

                        e.printStackTrace();
                    }*/
                    break;
            }
        }
    }

    protected void selectedVideoValidation(String path, int UPLOAD_VIDEO) {
        try {
            Bitmap thumb = VideoUtils.getVideoThumb(path);
            String videoTime = VideoUtils.convertMilliSecToTime(Long.parseLong(VideoUtils.getVideoTime(this, path)));
            VideoGalleryModel galleryVideoList = new VideoGalleryModel(path, thumb, videoTime, false);
            String realVideoTime = VideoUtils.getVideoTime(this, path);
            if (VideoUtils.checkVideoMinTimeValid((Long.parseLong(realVideoTime)))) {
                if (VideoUtils.checkVideoMaxTimeValid((Long.parseLong(realVideoTime)))) {
                    VideoGalleryModel.setSelectedVideoInstance(galleryVideoList);
                    Intent intent = new Intent(this, AddVideoDetailActivity.class);
                    startActivityForResult(intent, UPLOAD_VIDEO);
                } else {
                    Toast.makeText(this, "Video is greater than 15 min", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Video is less than 5 sec", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void resetAdapters() {
        isAdapterNull = false;

        if (HomeFragment.getInstance() != null)
            HomeFragment.getInstance().onCallPause();

        if (ProfileVideoFragment.getInstance() != null)
            ProfileVideoFragment.getInstance().onCallPause();

        if (ReevuuAcceptedFragment.getInstance() != null)
            ReevuuAcceptedFragment.getInstance().onCallPause();

        if (ReevuuReviewedFragment.getInstance() != null)
            ReevuuReviewedFragment.getInstance().onCallPause();
    }

    void hitSkipApi() {
        Call<SkipModel> call = RetrofitClient.getInstance().skip_tip(mUtils.getString(InterConst.ACCESS_TOKEN, ""));
        call.enqueue(new Callback<SkipModel>() {
            @Override
            public void onResponse(Call<SkipModel> call, Response<SkipModel> response) {

            }

            @Override
            public void onFailure(Call<SkipModel> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (HomeFragment.getInstance() != null)
            HomeFragment.getInstance().setLocalNewRequestCount();

        if (isAdapterNull) {
            resetAdapters();
        }
    }

    @Override
    public void openCamera() {
        nullAdapter();
        Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        VideoUtils.isfolderExists();
        File f = new File(Environment.getExternalStorageDirectory() +
                File.separator + InterConst.APP_NAME, recordVideoPath);
        if (Build.VERSION.SDK_INT >= 24) {
            Uri photoURI = FileProvider.getUriForFile(mContext,
                    BuildConfig.APPLICATION_ID + ".provider", f);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            cameraIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, InterConst.CAMERA_DURATION_MAX_TIME);
        } else {
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            cameraIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, InterConst.CAMERA_DURATION_MAX_TIME);
        }
        startActivityForResult(cameraIntent, RESULT_CAMERA);
    }

    void openCameraToRecordVideo() {
        Intent intent = new Intent(mContext, ActivityCustomRecorder.class);
        startActivityForResult(intent, RESULT_CAMERA);
    }

    private void nullAdapter() {
        isAdapterNull = true;
        HomeFragment.getInstance().removeAdapter();
        ProfileVideoFragment.getInstance().removeAdapter();
        ReevuuAcceptedFragment.getInstance().removeAdapter();
        ReevuuReviewedFragment.getInstance().removeAdapter();
    }

    @Override
    public void openGallery() {
        Intent intent = new Intent(mContext, ActivityVideoGallery.class);
        startActivityForResult(intent, UPLOAD_VIDEO);
    }

    @Override
    public void openBrowse() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("video/*");
        startActivityForResult(intent, RESULT_GALLERY);
    }

    @Override
    public void openVideoOption() {
        new RecordVideoDialog(LandingActivity.this, this);
    }

    @Override
    public void onProfileChanged(String value) {
        if (String.valueOf(mUtils.getInt(InterConst.ID, -1)) == value)
            moveToSplash();
    }

    @Override
    public void onUnreadBatchCountChanged(int mUnReadBatchCount, int mUnAppBatchCount) {
        if (mUnReadBatchCount > 0) {
            txtUnreadBatchCount.setVisibility(View.VISIBLE);
            txtUnreadBatchCount.setText(mUnReadBatchCount + "");
        } else {
            txtUnreadBatchCount.setVisibility(View.GONE);
        }

        if (mUnAppBatchCount == 0) {
            mUtils.setInt(InterConst.APP_ICON_BADGE_COUNT, 0);
            ShortcutBadger.removeCount(mContext);
        }

        mUtils.setInt(InterConst.APP_ICON_BADGE_COUNT, mUnAppBatchCount);

        int APP_ICON_BADGE_COUNT = mUtils.getInt(InterConst.APP_ICON_BADGE_COUNT, 0);

        ShortcutBadger.applyCount(getApplicationContext(), APP_ICON_BADGE_COUNT);

    }

    public void openNotificationCenterPage() {
        updateNotificationBell();
        Intent intent = new Intent(this, NotificationCenterActivity.class);
        startActivity(intent);
    }

    void hitDefaultArrayApi() {
        if (TextUtils.isEmpty(mUtils.getString(InterConst.SPORTS_RESPONSE, ""))) {
            if (connectedToInternet(llChat)) {
                showProgress();
                Call<DefaultArrayModel> call = RetrofitClient.getInstance().profile_data(
                        mUtils.getString(InterConst.ACCESS_TOKEN, ""), String.valueOf(
                                mSigUpModel.getResponse().getSport_info().getSport().getId()));
                call.enqueue(new Callback<DefaultArrayModel>() {
                    @Override
                    public void onResponse(Call<DefaultArrayModel> call, Response<DefaultArrayModel> response) {
                        hideProgress();
                        if (response.body().getResponse() != null) {
                            mUtils.setString(InterConst.SPORTS_RESPONSE, mGson.toJson(response.body()));
                            ProfileInfoFragment.getInstance().setData();
                        } else {
                            toast(response.body().getError().getMessage());
                            if (response.body().getError().getCode() == InterConst.INVALID_ACCESS) {
                                moveToSplash();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DefaultArrayModel> call, Throwable t) {
                        hideProgress();
                        toast(t.getMessage());
                    }
                });
            }
        }
    }

    void hitCoachProfileApi() {
        if (connectedToInternet(llChat)) {
            showProgress();
            Call<SignUpModel> call = RetrofitClient.getInstance().coach_profile(
                    mUtils.getString(InterConst.ACCESS_TOKEN, ""),
                    String.valueOf(mUtils.getInt(InterConst.ID, -1)));
            call.enqueue(new Callback<SignUpModel>() {
                @Override
                public void onResponse(Call<SignUpModel> call, Response<SignUpModel> response) {
                    hideProgress();
                    if (response.body().getResponse() != null) {
                        mUtils.setInt(InterConst.PROFILE_APPROVED, response.body().getResponse().is_approved());
                        if (mUtils.getInt(InterConst.PROFILE_APPROVED, 0) == 0) {
                            Intent intent = new Intent(mContext, ProfileNotApprovedActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        toast(response.body().getError().getMessage());
                        if (response.body().getError().getCode() == InterConst.INVALID_ACCESS) {
                            moveToSplash();
                        }
                    }
                }

                @Override
                public void onFailure(Call<SignUpModel> call, Throwable t) {
                    hideProgress();
                    toast(t.getMessage());
                }
            });
        }
    }

    private void getBroadcastPush() {
        if (connectedToInternet()) {
            listBroadcastPush.clear();
            Call<NotificationCenterModel> call = RetrofitClient.getInstance().getBroadcastPush(
                    mUtils.getString(InterConst.ACCESS_TOKEN, ""));
            call.enqueue(new Callback<NotificationCenterModel>() {
                @Override
                public void onResponse(@NonNull Call<NotificationCenterModel> call, @NonNull Response<NotificationCenterModel> response) {
                    if (response.body().getResponse() != null) {
                        listBroadcastPush.addAll(response.body().getResponse());
                        if (listBroadcastPush.size() > 0) {
                            checkForBroadCast();
                        }
                    } else {
                        if (response.body().getError().getCode() == InterConst.INVALID_ACCESS)
                            moveToSplash();
                        else {
                            toast(response.body().getError().getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<NotificationCenterModel> call, @NonNull Throwable t) {

                    toast(t.getMessage());

                    t.printStackTrace();
                }
            });
        }
    }

    void checkForBroadCast() {
        if (listBroadcastPush.size() > 0) {
            //notificationUpdate("2", listBroadcastPush.get(0).getBroadcast_id() + "");
            String title = listBroadcastPush.get(0).getTitle();
            String message = listBroadcastPush.get(0).getMessage();
            Intent intent = new Intent(this, BroadcastActivity.class);
            intent.putExtra("broadcastTitle", title);
            intent.putExtra("broadcastMessage", message);
            intent.putExtra(InterConst.NotificationID, listBroadcastPush.get(0).getBroadcast_id());
            startActivityForResult(intent, InterConst.REQ_BROADCAST);
        }
    }

    private void checkForBroadcastFromNotification() {
        if (getIntent().hasExtra("BROADCAST_DATA_INTENT")) {
            //   notificationUpdate("1", getIntent().getStringExtra(Const.NotificationID));
            String title = getIntent().getStringExtra("broadcastTitle");
            String message = getIntent().getStringExtra("broadcastMessage");

            Intent intent = new Intent(this, BroadcastActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            intent.putExtra("broadcastTitle", title);
            intent.putExtra("broadcastMessage", message);
            intent.putExtra(InterConst.NotificationID, getIntent().getStringExtra(InterConst.NotificationID));
            startActivity(intent);
        }
    }
}
