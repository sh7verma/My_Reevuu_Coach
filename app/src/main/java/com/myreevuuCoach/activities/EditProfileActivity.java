package com.myreevuuCoach.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.myreevuuCoach.R;
import com.myreevuuCoach.adapters.SelectOptionAdapter;
import com.myreevuuCoach.firebase.ChatsModel;
import com.myreevuuCoach.firebase.FirebaseChatConstants;
import com.myreevuuCoach.fragments.ProfileFragment;
import com.myreevuuCoach.fragments.ProfileVideoFragment;
import com.myreevuuCoach.interfaces.AdapterClickInterface;
import com.myreevuuCoach.interfaces.InterConst;
import com.myreevuuCoach.models.OptionsModel;
import com.myreevuuCoach.models.SignUpModel;
import com.myreevuuCoach.network.RetrofitClient;
import com.myreevuuCoach.utils.Constants;
import com.myreevuuCoach.utils.RoundedTransformation;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dev on 27/11/18.
 */

public class EditProfileActivity extends BaseActivity {

    final private int PIC = 2;
    @BindView(R.id.edName)
    EditText edName;
    @BindView(R.id.edUserName)
    EditText edUserName;
    @BindView(R.id.llGender)
    LinearLayout llGender;
    @BindView(R.id.txtGender)
    TextView txtGender;
    //    @BindView(R.id.edEmail)
//    EditText edEmail;
    @BindView(R.id.imgEditPic)
    ImageView imgEditPic;
    @BindView(R.id.txtSave)
    TextView txtSave;
    @BindView(R.id.imgUser)
    ImageView imgUser;
    @BindView(R.id.imgBack)
    ImageView imgBack;

    ArrayList<OptionsModel> mGenderArray = new ArrayList<>();
    BottomSheetDialog optionDialog;

    String mPath = "";
    OptionsModel mSelectedGender;
    LinkedHashMap<String, ChatsModel> mChats = new LinkedHashMap<>();
    ArrayList<String> mKeys = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_edit_profile;
    }

    @Override
    protected void onCreateStuff() {
        setData();
    }

    void setData() {
        mChats = db.getAllChats(String.valueOf(mSigUpModel.getResponse().getId()));
        mKeys.addAll(mChats.keySet());


        edName.setText(mUtils.getString(InterConst.NAME, ""));
        edUserName.setText(mUtils.getString(InterConst.USER_NAME, ""));
        txtGender.setText(mUtils.getString(InterConst.GENDER, ""));
//      edEmail.setText(mUtils.getString(InterConst.EMAIL, ""));
        edName.setCursorVisible(false);

        mGenderArray = Constants.Companion.genderData();
        if (mUtils.getInt(InterConst.GENDER_STATUS, -1) == 0) {
            mGenderArray.get(1).setSelected(true);
            mSelectedGender = mGenderArray.get(1);
        } else if (mUtils.getInt(InterConst.GENDER_STATUS, -1) == 1) {
            mGenderArray.get(0).setSelected(true);
            mSelectedGender = mGenderArray.get(0);
        } else if (mUtils.getInt(InterConst.GENDER_STATUS, -1) == 2) {
            mGenderArray.get(2).setSelected(true);
            mSelectedGender = mGenderArray.get(2);
        }

        if (!mUtils.getString(InterConst.PROFILE_PIC, "").equalsIgnoreCase("")) {
            Picasso.get()
                    .load(mUtils.getString(InterConst.PROFILE_PIC, ""))
                    .transform(new RoundedTransformation((int) (mHeight * 10), 0))
                    .resize((int) (mHeight * 0.125), (int) (mHeight * 0.125))
                    .placeholder(R.mipmap.ic_profile)
                    .centerCrop(Gravity.TOP)
                    .error(R.mipmap.ic_profile).into(imgUser);
        } else {
            Picasso.get()
                    .load(R.mipmap.ic_profile)
                    .transform(new RoundedTransformation((int) (mHeight * 10), 0))
                    .resize((int) (mHeight * 0.125), (int) (mHeight * 0.125))
                    .placeholder(R.mipmap.ic_profile)
                    .centerCrop(Gravity.TOP)
                    .error(R.mipmap.ic_profile).into(imgUser);
        }
    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void initListener() {
        llGender.setOnClickListener(this);
        imgEditPic.setOnClickListener(this);
        txtSave.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        edName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                edName.setCursorVisible(true);
                return false;
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

            case R.id.llGender:
                showOption();
                break;
            case R.id.txtSave:
                if (TextUtils.isEmpty(edName.getText().toString().trim())) {
                    showAlertSnackBar(txtGender, "Please Enter your Name");
                    return;
                }
                hitApi();
                break;
            case R.id.imgEditPic:
                Intent inProfile = new Intent(mContext, OptionPhotoSelection.class);
                if (!TextUtils.isEmpty(mPath)) {
                    inProfile.putExtra("visible", "yes");
                    inProfile.putExtra("path", mPath);
                }
                startActivityForResult(inProfile, PIC);
                overridePendingTransition(0, 0);
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
        txtOptionTitle.setText(R.string.select_gender);

        RecyclerView rvOption = optionDialog.findViewById(R.id.rvOptions);

        rvOption.setLayoutManager(new LinearLayoutManager(mContext));
        rvOption.setAdapter(new SelectOptionAdapter(mContext, mGenderArray, new AdapterClickInterface() {
            @Override
            public void onItemClick(int position) {
                mGenderArray.get(position).setSelected(true);
                mSelectedGender = mGenderArray.get(position);
                txtGender.setText(mGenderArray.get(position).getName());
                optionDialog.dismiss();
            }
        }));
        optionDialog.show();
    }

    void hitApi() {
        showProgress();
        File mFile = null;
        if (!TextUtils.isEmpty(mPath)) {
            mFile = new File(mPath);
        }

        Call<SignUpModel> call = RetrofitClient.getInstance().editProfile(
                createPartFromString(mUtils.getString(InterConst.ACCESS_TOKEN, "")),
                createImageFilePart(mFile, "profile_pic"),
                createPartFromString(edName.getText().toString().trim()),
                createPartFromString(String.valueOf(mSelectedGender.getId())));
        call.enqueue(new Callback<SignUpModel>() {
            @Override
            public void onResponse(Call<SignUpModel> call, Response<SignUpModel> response) {
                hideProgress();
                if (response.body().getError() != null) {
                    if (response.body().getError().getCode() == InterConst.INVALID_ACCESS) {
                        moveToSplash();
                    }
                    toast(response.body().getError().getMessage());
                } else {

                    FirebaseDatabase.getInstance().getReference()
                            .child(FirebaseChatConstants.USERS)
                            .child("id_" + mUtils.getInt(FirebaseChatConstants.user_id, -1))
                            .child("name").setValue(response.body().getResponse().getName());

                    FirebaseDatabase.getInstance().getReference()
                            .child(FirebaseChatConstants.USERS)
                            .child("id_" + mUtils.getInt(FirebaseChatConstants.user_id, -1))
                            .child("profile_pic").setValue(response.body().getResponse().getProfile_pic());

                    for (int i = 0; i < mKeys.size(); i++) {
                        FirebaseDatabase.getInstance().getReference()
                                .child(FirebaseChatConstants.CHATS)
                                .child(mKeys.get(i))
                                .child("profile_pic")
                                .child(String.valueOf(mUtils.getInt(FirebaseChatConstants.user_id, -1)))
                                .setValue(response.body().getResponse().getProfile_pic());

                        FirebaseDatabase.getInstance().getReference()
                                .child(FirebaseChatConstants.CHATS)
                                .child(mKeys.get(i))
                                .child("name")
                                .child(String.valueOf(mUtils.getInt(FirebaseChatConstants.user_id, -1)))
                                .setValue(response.body().getResponse().getName());
                    }

                    mUtils.setString(InterConst.NAME, response.body().getResponse().getName());
                    mUtils.setInt(InterConst.GENDER_STATUS, response.body().getResponse().getGender());
                    mUtils.setString(InterConst.PROFILE_PIC, response.body().getResponse().getProfile_pic());

                    if (mUtils.getInt(InterConst.GENDER_STATUS, -1) == 0) {
                        mUtils.setString(InterConst.GENDER, InterConst.FEMALE);
                    } else if (mUtils.getInt(InterConst.GENDER_STATUS, -1) == 1) {
                        mUtils.setString(InterConst.GENDER, InterConst.MALE);
                    } else if (mUtils.getInt(InterConst.GENDER_STATUS, -1) == 2) {
                        mUtils.setString(InterConst.GENDER, InterConst.OTHER);
                    }

                    ProfileFragment.getInstance().onResetData();
                    ProfileVideoFragment.getInstance().onVideoAdded();

                    finish();
                }

            }

            @Override
            public void onFailure(Call<SignUpModel> call, Throwable t) {
                hideProgress();
                showAlertSnackBar(txtSave, t.getMessage());
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PIC:
                    mPath = data.getStringExtra("filePath");
                    File file = new File(mPath);

                    if (!mPath.equalsIgnoreCase("")) {
                        Picasso.get()
                                .load(file)
                                .transform(new RoundedTransformation((int) (mHeight * 10), 0))
                                .resize((int) (mHeight * 0.125), (int) (mHeight * 0.125))
                                .placeholder(R.mipmap.ic_profile)
                                .centerCrop(Gravity.TOP)
                                .error(R.mipmap.ic_profile).into(imgUser);
                    } else {
                        Picasso.get()
                                .load(R.mipmap.ic_profile)
                                .transform(new RoundedTransformation((int) (mHeight * 10), 0))
                                .resize((int) (mHeight * 0.125), (int) (mHeight * 0.125))
                                .placeholder(R.mipmap.ic_profile)
                                .centerCrop(Gravity.TOP)
                                .error(R.mipmap.ic_profile).into(imgUser);
                    }

                    break;
            }
        }
    }

}
