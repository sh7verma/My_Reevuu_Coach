package com.myreevuuCoach.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myreevuuCoach.R;
import com.myreevuuCoach.customViews.FlowLayout;
import com.myreevuuCoach.firebase.FirebaseChatConstants;
import com.myreevuuCoach.interfaces.InterConst;
import com.myreevuuCoach.models.AthleteModel;
import com.myreevuuCoach.models.OptionsModel;
import com.myreevuuCoach.network.RetrofitClient;
import com.myreevuuCoach.utils.Constants;
import com.myreevuuCoach.utils.RoundedTransformation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dev on 7/12/18.
 */

public class OthersProfileActivity extends BaseActivity {

    @BindView(R.id.imgBack)
    ImageView imgBack;

    @BindView(R.id.imgUser)
    ImageView imgUser;
    @BindView(R.id.txtName)
    TextView txtName;
    @BindView(R.id.txtGender)
    TextView txtGender;
    @BindView(R.id.txtUserName)
    TextView txtUserName;

    @BindView(R.id.txtSendMessage)
    TextView txtSendMessage;

    @BindView(R.id.txtSport)
    TextView txtSport;
    @BindView(R.id.txtSportExperience)
    TextView txtSportExperience;
    @BindView(R.id.txtSportsLevel)
    TextView txtSportsLevel;
    @BindView(R.id.txtParticipate)
    TextView txtParticipate;

    @BindView(R.id.flArea)
    FlowLayout flArea;

    AthleteModel.ResponseBean mResponse;

    @Override
    protected int getContentView() {
        return R.layout.activity_others_profile;
    }

    @Override
    protected void onCreateStuff() {
        setData();
        hitApi();
    }

    @Override
    protected void initUI() {


    }


    private void setData() {
        txtName.setText(getIntent().getStringExtra(InterConst.NAME));

        if (!TextUtils.isEmpty(getIntent().getStringExtra(InterConst.PROFILE_PIC))) {
            Picasso.get()
                    .load(getIntent().getStringExtra(InterConst.PROFILE_PIC))
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


    View inflateExpertiseView(OptionsModel model, int position) {

        final View view = LayoutInflater.from(mContext).inflate(R.layout.layout_expertise, null, false);
        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout llExpertise = view.findViewById(R.id.llExpertise);
        final TextView txtExpertise = view.findViewById(R.id.txtExpertise);

        llExpertise.setLayoutParams(params);

        txtExpertise.setText(model.getName());
        txtExpertise.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        txtExpertise.setBackgroundResource(Constants.Companion.getGradient().get(position));

        return view;
    }


    @Override
    protected void initListener() {
        txtSendMessage.setOnClickListener(this);
        imgBack.setOnClickListener(this);
    }

    @Override
    protected Context getContext() {
        return this;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtSendMessage:
                Intent intent = new Intent(this, ConversationActivity.class);

                intent.putExtra(InterConst.PROFILE_PIC, mResponse.getProfile_pic());
                intent.putExtra(InterConst.NAME, mResponse.getName());

                ArrayList<String> mParticpantIDSList = new ArrayList<String>();

                mParticpantIDSList.add(mResponse.getId() + "_" + FirebaseChatConstants.TYPE_ATHLETE);
                mParticpantIDSList.add(mUtils.getInt(FirebaseChatConstants.user_id, -1) + "_" + FirebaseChatConstants.TYPE_COACH);
                Collections.sort(mParticpantIDSList);

                String mParticpantIDS = mParticpantIDSList.toString();
                mParticpantIDS = mParticpantIDS.replace(" ", "");
                String participants = mParticpantIDS.substring(1, mParticpantIDS.length() - 1);
                intent.putExtra("participantIDs", participants);
                startActivity(intent);

                break;
            case R.id.imgBack:
                finish();
                break;
        }
    }

    void hitApi() {
        if (connectedToInternet(txtName)) {
            showProgress();
            Call<AthleteModel> call = RetrofitClient.getInstance().athlete_profile(
                    mUtils.getString(InterConst.ACCESS_TOKEN, ""),
                    String.valueOf(getIntent().getIntExtra(InterConst.ID, 0)));

            call.enqueue(new Callback<AthleteModel>() {
                @Override
                public void onResponse(Call<AthleteModel> call, Response<AthleteModel> response) {
                    hideProgress();
                    if (response.body().getResponse() != null) {
                        setApiData(response.body().getResponse());
                    } else {
                        toast(response.body().getError().getMessage());
                        if (response.body().getError().getCode() == InterConst.INVALID_ACCESS) {
                            moveToSplash();
                        }
                    }
                }

                @Override
                public void onFailure(Call<AthleteModel> call, Throwable t) {
                    hideProgress();
                    toast(t.getMessage());
                }
            });
        }
    }

    void setApiData(AthleteModel.ResponseBean response) {
        mResponse = response;
        txtName.setText(response.getName());
        txtUserName.setText(response.getUsername());

        if (response.getGender() == 0) {
            txtGender.setText(InterConst.FEMALE);
        } else if (response.getGender() == 1) {
            txtGender.setText(InterConst.MALE);
        } else if (response.getGender() == 2) {
            txtGender.setText(InterConst.OTHER);
        }

        OptionsModel model = new OptionsModel(0, response.getSport_info().getName(), 0, false);
        flArea.addView(inflateExpertiseView(model, 0));
        txtSport.setText(response.getSport_info().getName());

        if (mSigUpModel.getResponse().getSport_info().getExperience() == 0) {
            txtSportExperience.setText("< 1 Year");
        } else if (mSigUpModel.getResponse().getSport_info().getExperience() == 1) {
            txtSportExperience.setText(response.getSport_info().getExperience() + " Year");
        } else {
            txtSportExperience.setText(response.getSport_info().getExperience() + " Years");
        }

        txtSportsLevel.setText(mSigUpModel.getLevels().get(response.getSport_info().getLevel() - 1).getName());

    }


}
