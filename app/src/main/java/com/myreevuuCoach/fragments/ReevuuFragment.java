package com.myreevuuCoach.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.myreevuuCoach.R;
import com.myreevuuCoach.activities.LandingActivity;
import com.myreevuuCoach.activities.ReevuuSearchActivity;
import com.myreevuuCoach.adapters.FragmentPagerAdapter;
import com.myreevuuCoach.interfaces.InterConst;

import butterknife.BindView;

/**
 * Created by dev on 12/11/18.
 */

public class ReevuuFragment extends BaseFragment implements View.OnClickListener {

    @SuppressLint("StaticFieldLeak")
    static ReevuuFragment fragment;
    @SuppressLint("StaticFieldLeak")
    static Context mContext;

    @BindView(R.id.reevuu_tabs)
    TabLayout tabLayout;
    @BindView(R.id.imgSearch)
    ImageView imgSearch;
    @BindView(R.id.vp_reevuu)
    ViewPager vpReevuu;
    @BindView(R.id.img_unread_dot)
    ImageView img_unread_dot;
    FragmentPagerAdapter adapter;
    @BindView(R.id.imgNoti)
    ImageView imgNoti;

    public static ReevuuFragment newInstance(Context context) {
        fragment = new ReevuuFragment();
        mContext = context;
        return fragment;
    }

    public static ReevuuFragment getInstance() {
        return fragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_reevuu;
    }

    @Override
    protected void onCreateStuff() {
        adapter = new FragmentPagerAdapter(getChildFragmentManager());
        adapter.addFragment(ReevuuRequestsFragment.newInstance(mContext), getString(R.string.requests));
        adapter.addFragment(ReevuuAcceptedFragment.newInstance(mContext), getString(R.string.accepted));
        adapter.addFragment(ReevuuReviewedFragment.newInstance(mContext), getString(R.string.reviewed));

        vpReevuu.setAdapter(adapter);
        vpReevuu.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(vpReevuu);

        vpReevuu.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == InterConst.FRAG_REEVUU_REQUEST) {
                    ((ReevuuRequestsFragment) adapter.getFragment(position)).onCallResume();
                } else if (position == InterConst.FRAG_REEVUU_ACCEPTED) {
                    ((ReevuuAcceptedFragment) adapter.getFragment(position)).onCallResume();
                } else if (position == InterConst.FRAG_REEVUU_REVIEWED) {
                    ((ReevuuReviewedFragment) adapter.getFragment(position)).onCallResume();
                }

                if (position != InterConst.FRAG_REEVUU_ACCEPTED) {
                    ReevuuAcceptedFragment.getInstance().onCallPause();
                }

                if (position != InterConst.FRAG_REEVUU_REVIEWED) {
                    ReevuuReviewedFragment.getInstance().onCallPause();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void onCallPause() {
        ReevuuAcceptedFragment.getInstance().onCallPause();
        ReevuuReviewedFragment.getInstance().onCallPause();
    }

    public void onCallResume() {
        ReevuuAcceptedFragment.getInstance().onCallResume();
        ReevuuReviewedFragment.getInstance().onCallResume();
    }

    public void setPagerFirstItem() {
        vpReevuu.setCurrentItem(InterConst.FRAG_REEVUU_REQUEST);
        ReevuuRequestsFragment.getInstance().onCallResume();
    }

    void hideUnreadNotificationDot() {
        utils.setInt(InterConst.UNREAD_NOTIFICATION_DOT, 0);
        img_unread_dot.setVisibility(View.GONE);
    }

    void showUnreadNotificationDot() {
        img_unread_dot.setVisibility(View.VISIBLE);

    }

    public void checkUnreadNotification() {
        if (utils.getInt(InterConst.UNREAD_NOTIFICATION_DOT, 0) == 0) {
            hideUnreadNotificationDot();
        } else {
            showUnreadNotificationDot();
        }
    }

    public void setPagerItem(int position) {
        vpReevuu.setCurrentItem(position);
    }

    @Override
    protected void initListeners() {
        imgSearch.setOnClickListener(this);
        imgNoti.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgSearch:
                Intent intent = new Intent(mContext, ReevuuSearchActivity.class);
                intent.putExtra(InterConst.INTEND_EXTRA, String.valueOf(vpReevuu.getCurrentItem()));
                startActivity(intent);
                break;
            case R.id.imgNoti:
                hideUnreadNotificationDot();
                assert ((LandingActivity) getActivity()) != null;
                ((LandingActivity) getActivity()).openNotificationCenterPage();
                break;
        }
    }
}