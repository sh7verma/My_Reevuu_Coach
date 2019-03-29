package com.myreevuuCoach.activities;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.myreevuuCoach.R;
import com.myreevuuCoach.adapters.SelectOptionAdapter;
import com.myreevuuCoach.customViews.FlowLayout;
import com.myreevuuCoach.gallery.VideoGalleryModel;
import com.myreevuuCoach.interfaces.AdapterClickInterface;
import com.myreevuuCoach.interfaces.InterConst;
import com.myreevuuCoach.models.DefaultArrayModel;
import com.myreevuuCoach.models.OptionsModel;
import com.myreevuuCoach.models.SignUpModel;
import com.myreevuuCoach.models.SportsArrayModel;
import com.myreevuuCoach.network.RetrofitClient;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;

import java.util.ArrayList;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dev on 21/11/18.
 */

public class AddVideoDetailActivity extends BaseActivity implements UniversalVideoView.VideoViewCallback {

    private static final int UPLOAD_VIDEO = 101;


    @BindView(R.id.flVideo)
    FrameLayout flVideo;
    @BindView(R.id.rlToolBar)
    RelativeLayout rlToolBar;

    @BindView(R.id.llMediaController)
    UniversalMediaController llMediaController;
    @BindView(R.id.videoView)
    UniversalVideoView videoView;

    @BindView(R.id.imgBack)
    ImageView imgBack;
    @BindView(R.id.edCategory)
    TextView edCategory;
    @BindView(R.id.edTitle)
    TextView edTitle;
    @BindView(R.id.edDescription)
    EditText edDescription;
    @BindView(R.id.flArea)
    FlowLayout flArea;
    @BindView(R.id.txtUploadVideo)
    TextView txtUploadVideo;
    @BindView(R.id.llBottom)
    LinearLayout llBottom;

    ArrayList<OptionsModel> mSportsArray = new ArrayList<>();

    BottomSheetDialog optionDialog;
    String SEEK_POSITION_KEY = "SEEK_POSITION_KEY";
    int mSeekPosition = 1;
    int cachedHeight = 0;
    ArrayList<DefaultArrayModel.ResponseBean.ExpertiesBean> mExpertiseArray;
    VideoGalleryModel videoGalleryModel;
    private Boolean isFullscreen = false;

    @Override
    protected int getContentView() {
        return R.layout.activity_add_video_detail;
    }

    @Override
    protected void onCreateStuff() {
        setData();
    }

    void setData() {
        mExpertiseArray = (ArrayList<DefaultArrayModel.ResponseBean.ExpertiesBean>)
                mGson.fromJson(mUtils.getString(InterConst.SPORTS_RESPONSE, ""), DefaultArrayModel.class)
                        .getResponse().getExperties();

        edCategory.setText(String.valueOf(mSigUpModel.getResponse().getSport_info().getSport().getName()));
        mSportsArray = mSigUpModel.getSports();

        for (int i = 0; i < mSportsArray.size(); i++) {
            if (mSportsArray.get(i).getName().equals(mSigUpModel.getResponse().getSport_info().getSport().getName())) {
                mSportsArray.get(i).setSelected(true);
            }
        }
        loadExpertiseData();
        setVideo();
    }


    void setVideo() {
        videoGalleryModel = VideoGalleryModel.getSelectedVideoInstance();
        videoView.setVideoPath(videoGalleryModel.getVideoPath());
        videoView.requestFocus();
//        videoView.start();
    }

    private void loadExpertiseData() {
        for (int i = 0; i < mExpertiseArray.size(); i++)
            flArea.addView(inflateExpertiseView(mExpertiseArray.get(i), i));
    }


    void hitSportArrayApi() {
        showProgress();
        Call<SportsArrayModel> call = RetrofitClient.getInstance().sports_array(mUtils.getString(InterConst.ACCESS_TOKEN, ""));
        call.enqueue(new Callback<SportsArrayModel>() {
            @Override
            public void onResponse(Call<SportsArrayModel> call, Response<SportsArrayModel> response) {
                hideProgress();
                if (response.body().getResponse() != null) {
                    mSigUpModel.getSports().clear();
                    for (int j = 0; j < response.body().getResponse().size(); j++) {
                        OptionsModel model = new OptionsModel(response.body().getResponse().get(j).getId(), response.body().getResponse().get(j).getName(), 0, false);
                        mSigUpModel.getSports().add(model);
                    }

                    mUtils.setString(InterConst.RESPONSE, mGson.toJson(mSigUpModel));
                    mSigUpModel = mGson.fromJson(mUtils.getString(InterConst.RESPONSE, ""),
                            SignUpModel.class);
                    mSportsArray.clear();
                    setData();
                    showOption();
                } else {
                    showAlertSnackBar(llBottom, response.body().getError().getMessage());
                }
            }

            @Override
            public void onFailure(Call<SportsArrayModel> call, Throwable t) {

            }
        });
    }


    @Override
    protected void initUI() {
        videoView.setMediaController(llMediaController);
        setVideoAreaSize();
        videoView.setVideoViewCallback(this);

    }

    private void setVideoAreaSize() {

        flVideo.post(new Runnable() {
            @Override
            public void run() {
                int width = flVideo.getWidth();
                cachedHeight = ((int) getResources().getDimension(R.dimen._220sdp));
                ViewGroup.LayoutParams videoLayoutParams = flVideo.getLayoutParams();
                videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                videoLayoutParams.height = cachedHeight;
                flVideo.setLayoutParams(videoLayoutParams);
                videoView.setVideoPath(videoGalleryModel.getVideoPath());
                videoView.requestFocus();
                videoView.seekTo(1);
            }
        });
    }


    @Override
    protected void initListener() {
        txtUploadVideo.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        edCategory.setOnClickListener(this);
    }

    @Override
    protected Context getContext() {
        return this;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtUploadVideo:
                if (videoView != null && videoView.isPlaying()) {
                    mSeekPosition = videoView.getCurrentPosition();
                    videoView.pause();
                }
                moveNext();
                break;
            case R.id.imgBack:
                if (this.isFullscreen) {
                    videoView.setFullscreen(false);
                } else {
                    super.onBackPressed();
                }
                break;

            case R.id.edCategory:
                hitSportArrayApi();
                break;
        }
    }

    void showOption() {
        optionDialog = new BottomSheetDialog(mContext);
        optionDialog.setContentView(R.layout.dialog_options);
        CoordinatorLayout.LayoutParams dialogParms = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT,
                CoordinatorLayout.LayoutParams.WRAP_CONTENT);

        dialogParms.gravity = Gravity.BOTTOM;
        FrameLayout bottomSheet = optionDialog.findViewById(android.support.design.R.id.design_bottom_sheet);
        bottomSheet.setBackgroundResource(R.drawable.white_default);
        bottomSheet.setLayoutParams(dialogParms);

        TextView txtOptionTitle = optionDialog.findViewById(R.id.txtOptionTitle);
        txtOptionTitle.setText(R.string.category);

        RecyclerView rvOption = optionDialog.findViewById(R.id.rvOptions);

        rvOption.setLayoutManager(new LinearLayoutManager(mContext));
        rvOption.setAdapter(new SelectOptionAdapter(mContext, mSportsArray, new AdapterClickInterface() {
            @Override
            public void onItemClick(int position) {
                hitExpertiesApi(String.valueOf(mSportsArray.get(position).getId()));
                edCategory.setText(mSportsArray.get(position).getName());
                optionDialog.dismiss();
            }
        }));
        optionDialog.show();
    }

    void hitExpertiesApi(String id) {
        showProgress();

        Call<DefaultArrayModel> call = RetrofitClient.getInstance().profile_data(
                mUtils.getString(InterConst.ACCESS_TOKEN, ""), id);
        call.enqueue(new Callback<DefaultArrayModel>() {
            @Override
            public void onResponse(Call<DefaultArrayModel> call, Response<DefaultArrayModel> response) {
                hideProgress();
                if (response.body().getResponse() != null) {
                    mExpertiseArray.clear();
                    flArea.removeAllViews();

                    mExpertiseArray = (ArrayList<DefaultArrayModel.ResponseBean.ExpertiesBean>) response.body().getResponse().getExperties();
                    loadExpertiseData();
                } else {
                    Toast.makeText(mContext, getString(R.string.error), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultArrayModel> call, Throwable t) {
                hideProgress();

            }
        });
    }

    void moveNext() {
        String expertise = "";
        StringBuilder expertiseBuilder = new StringBuilder();

        for (int i = 0; i < mExpertiseArray.size(); i++) {
            if (mExpertiseArray.get(i).isSelected()) {
                expertiseBuilder.append(mExpertiseArray.get(i).getId()).append(",");
            }
        }

        if (!TextUtils.isEmpty(expertiseBuilder)) {
            expertise = expertiseBuilder.toString().substring(0, expertiseBuilder.length() - 1);
        } else {
            showAlertSnackBar(edTitle, getString(R.string.select_at_least_one_experties));
            return;
        }

        if (TextUtils.isEmpty(edTitle.getText().toString())) {
            showAlertSnackBar(edTitle, getString(R.string.video_title_cannot_be_empty));
            return;
        } else if (TextUtils.isEmpty(edDescription.getText().toString())) {
            showAlertSnackBar(edTitle, getString(R.string.video_description_cannot_be_empty));
            return;
        } else if (TextUtils.isEmpty(expertise)) {
            showAlertSnackBar(edTitle, getString(R.string.select_at_least_one_experties));
            return;
        }

        Intent intent = new Intent(mContext, UploadVideoActivity.class);
        intent.putExtra("title", edTitle.getText().toString());
        intent.putExtra("description", edDescription.getText().toString());
        intent.putExtra("expertise", expertise);
        startActivityForResult(intent, UPLOAD_VIDEO);

    }

    View inflateExpertiseView(DefaultArrayModel.ResponseBean.ExpertiesBean model, final int position) {

        final View view = LayoutInflater.from(mContext).inflate(R.layout.layout_expertise, null, false);
        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout llExpertise = view.findViewById(R.id.llExpertise);
        final TextView txtExpertise = view.findViewById(R.id.txtExpertise);

        llExpertise.setLayoutParams(params);

        txtExpertise.setText(model.getName());
        txtExpertise.setTextColor(ContextCompat.getColor(mContext, R.color.white));

        txtExpertise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mExpertiseArray.get(position).isSelected()) {
                    mExpertiseArray.get(position).setSelected(false);
                    txtExpertise.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    txtExpertise.setBackgroundResource(R.drawable.unselected_expertise);
                } else {
                    switch (mExpertiseArray.get(position).getColor()) {
                        case 1:
                            txtExpertise.setBackgroundResource(R.drawable.gradient_first);
                            break;
                        case 2:
                            txtExpertise.setBackgroundResource(R.drawable.gradient_second);
                            break;
                        case 3:
                            txtExpertise.setBackgroundResource(R.drawable.gradient_third);
                            break;
                        case 4:
                            txtExpertise.setBackgroundResource(R.drawable.gradient_fourth);
                            break;
                        case 5:
                            txtExpertise.setBackgroundResource(R.drawable.gradient_fifth);
                            break;
                        case 6:
                            txtExpertise.setBackgroundResource(R.drawable.gradient_sixth);
                            break;
                    }
                    mExpertiseArray.get(position).setSelected(true);
                }
            }
        });
        return view;
    }

    @Override
    public void onScaleChange(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;
        if (isFullscreen) {
            ViewGroup.LayoutParams layoutParams = flVideo.getLayoutParams();
            layoutParams.width = mHeight;
            layoutParams.height = mWidth;
            flVideo.setLayoutParams(layoutParams);
            llBottom.setVisibility(View.GONE);
            txtUploadVideo.setVisibility(View.GONE);
        } else {
            ViewGroup.LayoutParams layoutParams = flVideo.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = this.cachedHeight;
            flVideo.setLayoutParams(layoutParams);
            llBottom.setVisibility(View.VISIBLE);
            txtUploadVideo.setVisibility(View.VISIBLE);
        }
        switchTitleBar(!isFullscreen);
    }


    private void switchTitleBar(boolean show) {
        if (show) {
            rlToolBar.setVisibility(View.VISIBLE);
        } else {
            rlToolBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView != null) {
            mSeekPosition = videoView.getCurrentPosition();
            videoView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (videoView != null) {
            videoView.seekTo(mSeekPosition);
        }
    }

    @Override
    public void onBackPressed() {
        if (this.isFullscreen) {
            videoView.setFullscreen(false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(SEEK_POSITION_KEY, mSeekPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mSeekPosition = savedInstanceState.getInt(SEEK_POSITION_KEY);
    }

    @Override
    public void onPause(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onStart(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onBufferingStart(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onBufferingEnd(MediaPlayer mediaPlayer) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case UPLOAD_VIDEO:
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
            }
        }

    }


}
