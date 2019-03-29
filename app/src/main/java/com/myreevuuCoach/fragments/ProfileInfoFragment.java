package com.myreevuuCoach.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myreevuuCoach.R;
import com.myreevuuCoach.activities.RegisterCoachActivity;
import com.myreevuuCoach.adapters.CertificateAdapter;
import com.myreevuuCoach.customViews.FlowLayout;
import com.myreevuuCoach.interfaces.InterConst;
import com.myreevuuCoach.models.DefaultArrayModel;
import com.myreevuuCoach.models.OptionsModel;
import com.myreevuuCoach.models.SignUpModel;
import com.myreevuuCoach.utils.Constants;

import butterknife.BindView;


@SuppressLint("ValidFragment")
public class ProfileInfoFragment extends BaseFragment implements View.OnClickListener {

    @SuppressLint("StaticFieldLeak")
    static ProfileInfoFragment fragment;
    @SuppressLint("StaticFieldLeak")
    static Context mContext;

    @BindView(R.id.txtAbout)
    TextView txtAbout;
    @BindView(R.id.flArea)
    FlowLayout flArea;

    @BindView(R.id.txtSport)
    TextView txtSport;
    @BindView(R.id.txtSportExperience)
    TextView txtSportExperience;
    @BindView(R.id.txtSportsLevel)
    TextView txtSportsLevel;
    @BindView(R.id.txtSportPlayedCollege)
    TextView txtSportPlayedCollege;

    @BindView(R.id.txtApproval)
    TextView txtApproval;

    @BindView(R.id.txtCoachSports)
    TextView txtCoachSports;
    @BindView(R.id.txtCoachExperience)
    TextView txtCoachExperience;
    @BindView(R.id.txtCoachLevel)
    TextView txtCoachLevel;

    @BindView(R.id.rvCertificates)
    RecyclerView rvCertificates;
    @BindView(R.id.imgEditInfo)
    ImageView imgEditInfo;

    CertificateAdapter mAdapter;


    public static ProfileInfoFragment newInstance(Context context) {
        fragment = new ProfileInfoFragment();
        mContext = context;
        return fragment;
    }

    public static ProfileInfoFragment getInstance() {
        return fragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_info;
    }

    @Override
    protected void onCreateStuff() {
        setData();
    }

    public void setData() {
        mSigUpModel = mGson.fromJson(utils.getString(InterConst.RESPONSE, ""), SignUpModel.class);
        mArrayModel = mGson.fromJson(utils.getString(InterConst.SPORTS_RESPONSE, ""), DefaultArrayModel.class);
        if (mArrayModel != null) {
            txtAbout.setText(mSigUpModel.getResponse().getCoach_info().getAbout());

            loadExpertiseData();

            txtSport.setText(mSigUpModel.getResponse().getSport_info().getSport().getName());

            if (mSigUpModel.getResponse().getSport_info().getExperience() == 0) {
                txtSportExperience.setText("< 1 Year");
            } else if (mSigUpModel.getResponse().getSport_info().getExperience() == 1) {
                txtSportExperience.setText(mSigUpModel.getResponse().getSport_info().getExperience() + " Year");
            } else {
                txtSportExperience.setText(mSigUpModel.getResponse().getSport_info().getExperience() + " Years");
            }

            txtSportsLevel.setText(String.valueOf(mSigUpModel.getResponse().getSport_info().getSport_level_name()));

            if (mSigUpModel.getResponse().getSport_info().getFrom_college() == 1)
                txtSportPlayedCollege.setText(R.string.yes);
            else
                txtSportPlayedCollege.setText(R.string.no);

            if (utils.getInt(InterConst.PROFILE_APPROVED, 0) == 1)
                txtApproval.setVisibility(View.GONE);
            else
                txtApproval.setVisibility(View.VISIBLE);

            txtCoachSports.setText(mSigUpModel.getResponse().getSport_info().getSport().getName());

            if (mSigUpModel.getResponse().getCoach_info().getCoach_experience() == 0) {
                txtCoachExperience.setText("< 1 Year");
            } else if (mSigUpModel.getResponse().getCoach_info().getCoach_experience() == 1) {
                txtCoachExperience.setText(mSigUpModel.getResponse().getCoach_info().getCoach_experience() + " Year");
            } else {
                txtCoachExperience.setText(mSigUpModel.getResponse().getCoach_info().getCoach_experience() + " Years");
            }
            if (TextUtils.isEmpty(mSigUpModel.getResponse().getCoach_info().getCollege_name())) {
                txtCoachLevel.setText(String.valueOf(mSigUpModel.getResponse().getCoach_info().getCoach_level_name()));
            } else {
                txtCoachLevel.setText(String.valueOf(mSigUpModel.getResponse().getCoach_info().getCoach_level_name()) + "-" + mSigUpModel.getResponse().getCoach_info().getCollege_name());
            }
            setCertificatesAdapter();
        }

    }

    void setCertificatesAdapter() {
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        rvCertificates.setLayoutManager(mLayoutManager);
        mAdapter = new CertificateAdapter(mContext, mSigUpModel.getResponse().getCoach_info().getCertificates());
        rvCertificates.setAdapter(mAdapter);
    }

    private void loadExpertiseData() {
        flArea.removeAllViews();

        for (int i = 0; i < mSigUpModel.getResponse().getCoach_info().getExperties().size(); i++)
            flArea.addView(inflateExpertiseView(mSigUpModel.getResponse().getCoach_info().getExperties().get(i), i));
    }

    @Override
    protected void initListeners() {
        imgEditInfo.setOnClickListener(this);
    }

    View inflateExpertiseView(OptionsModel model, int position) {

        final View view = LayoutInflater.from(mContext).inflate(R.layout.layout_expertise, null, false);
        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout llExpertise = view.findViewById(R.id.llExpertise);
        final TextView txtExpertise = view.findViewById(R.id.txtExpertise);

        llExpertise.setLayoutParams(params);

        txtExpertise.setText(model.getName());
        txtExpertise.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        txtExpertise.setBackgroundResource(Constants.Companion.getGradient().get(model.getColor()));

        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgEditInfo:
                Intent intent = new Intent(mContext, RegisterCoachActivity.class);
                intent.putExtra(InterConst.EDIT_INFO, InterConst.EDIT_INFO);
                startActivity(intent);
                break;
        }
    }
}
