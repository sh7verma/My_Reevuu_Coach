package com.myreevuuCoach.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.myreevuuCoach.R;
import com.myreevuuCoach.activities.EditProfileActivity;
import com.myreevuuCoach.activities.SettingsActivity;
import com.myreevuuCoach.adapters.FragmentPagerAdapter;
import com.myreevuuCoach.interfaces.InterConst;
import com.myreevuuCoach.utils.RoundedTransformation;
import com.myreevuuCoach.utils.ViewImageActivity;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

/**
 * Created by dev on 12/11/18.
 */

public class ProfileFragment extends BaseFragment implements View.OnClickListener {

    @SuppressLint("StaticFieldLeak")
    static ProfileFragment fragment;
    @SuppressLint("StaticFieldLeak")
    static Context mContext;

    @BindView(R.id.vp_profile)
    ViewPager vpProfile;
    @BindView(R.id.profile_tabs)
    TabLayout profileTabs;
    @BindView(R.id.img_edit_profile)
    ImageView imgEditProfile;
    @BindView(R.id.img_setting)
    ImageView imgSetting;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvGender)
    TextView tvGender;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.imgUser)
    ImageView imgUser;
    @BindView(R.id.txt_email)
    TextView txtEmail;

    FragmentPagerAdapter adapter;

    public static ProfileFragment newInstance(Context context) {
        fragment = new ProfileFragment();
        mContext = context;
        return fragment;
    }

    public static ProfileFragment getInstance() {
        return fragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_profile;
    }

    @Override
    protected void onCreateStuff() {
        setData();

        adapter = new FragmentPagerAdapter(getChildFragmentManager());
        adapter.addFragment(ProfileInfoFragment.newInstance(mContext), getString(R.string.info));
        adapter.addFragment(ProfileVideoFragment.newInstance(mContext), getString(R.string.videos));
        adapter.addFragment(ProfilePaymentFragment.newInstance(mContext), getString(R.string.payments));
        vpProfile.setAdapter(adapter);
        vpProfile.setOffscreenPageLimit(3);
        profileTabs.setupWithViewPager(vpProfile);

        vpProfile.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("onPageSelected", "ProfileFragment" + String.valueOf(position));
                if (position != InterConst.PROFILE_VIDEO_FRAG) {
                    ((ProfileVideoFragment) adapter.getFragment(InterConst.PROFILE_VIDEO_FRAG)).onCallPause();
                } else {
                    ((ProfileVideoFragment) adapter.getFragment(InterConst.PROFILE_VIDEO_FRAG)).onCallResume();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void initListeners() {
        imgEditProfile.setOnClickListener(this);
        imgUser.setOnClickListener(this);
        imgSetting.setOnClickListener(this);
    }


    public void onResetData() {
        setData();
    }

    private void setData() {
        tvName.setText(utils.getString(InterConst.NAME, ""));
        tvUserName.setText(utils.getString(InterConst.USER_NAME, ""));
        tvGender.setText(utils.getString(InterConst.GENDER, ""));
        txtEmail.setText(utils.getString(InterConst.EMAIL, ""));

        if (!utils.getString(InterConst.PROFILE_PIC, "").equalsIgnoreCase("")) {
            Picasso.get()
                    .load(utils.getString(InterConst.PROFILE_PIC, ""))
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

    public void onCallPause() {
        ((ProfileVideoFragment) adapter.getFragment(InterConst.PROFILE_VIDEO_FRAG)).onCallPause();
    }

    public void setPagerItem(int position) {
        vpProfile.setCurrentItem(position);
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.img_edit_profile:
                intent = new Intent(mContext, EditProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.imgUser:
                if (!utils.getString(InterConst.PROFILE_PIC, "").equalsIgnoreCase("")) {
                    intent = new Intent(mContext, ViewImageActivity.class);
                    intent.putExtra("display", utils.getString(InterConst.PROFILE_PIC, ""));
                    startActivity(intent);
                }
                break;

            case R.id.img_setting:
                intent = new Intent(mContext, SettingsActivity.class);
                startActivity(intent);
                break;
        }
    }
}
