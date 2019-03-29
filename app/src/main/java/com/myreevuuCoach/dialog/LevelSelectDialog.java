package com.myreevuuCoach.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myreevuuCoach.R;
import com.myreevuuCoach.adapters.LevelSelectAdapter;
import com.myreevuuCoach.interfaces.AdapterClickInterface;
import com.myreevuuCoach.interfaces.InterConst;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dev on 5/2/19.
 */

public class LevelSelectDialog implements View.OnClickListener, AdapterClickInterface {

    static private Dialog mDialog;

    @BindView(R.id.txtOptionTitle)
    TextView txtOptionTitle;

    @BindView(R.id.rvOptions)
    RecyclerView rvOptions;

    LevelSelectAdapter mAdapter;
    ArrayList<String> mData = new ArrayList<>();
    String stringArray;
    private Activity mContext;
    private DialogCallBack mCallBack;
    View view;

    public LevelSelectDialog(Activity context, DialogCallBack dialogRecordVideoCallBack, String s) {
        mContext = context;
        this.mCallBack = dialogRecordVideoCallBack;
        stringArray = s;
        showDialog();
    }

    public void showDialog() {
        if (mDialog == null) {
            mDialog = new Dialog(mContext);
            mDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            view = View.inflate(mContext, R.layout.dialog_options, null);
            mDialog.setContentView(view);
            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            mDialog.getWindow().setGravity(Gravity.BOTTOM);
            mDialog.getWindow().getAttributes().windowAnimations = R.style.DialogBottomAnimation;
            mDialog.show();

            ButterKnife.bind(this, view);
            initListeners();
            txtOptionTitle.setText(R.string.select_level);

            String array[] = stringArray.split(InterConst.REGEX_CERTIFICATED);
            for (int i = 0; i < array.length; i++) {
                mData.add(array[i]);
            }

            rvOptions.setLayoutManager(new LinearLayoutManager(mContext));
            mAdapter = new LevelSelectAdapter(mData);
            mAdapter.onAdapterItemClick(this);
            rvOptions.setAdapter(mAdapter);
            mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    mCallBack.dismiss();
                    mDialog = null;
                }
            });
        }
    }


    private void initListeners() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    void dismiss() {
        mDialog.dismiss();
        mDialog = null;
    }

    @Override
    public void onItemClick(int position) {
        mCallBack.done(mData.get(position));
        InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        mCallBack.dismiss();
        dismiss();
    }


    public interface DialogCallBack {

        void done(String s);

        void dismiss();

    }

}
