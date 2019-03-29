package com.myreevuuCoach.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.myreevuuCoach.R;
import com.myreevuuCoach.interfaces.DialogRecordVideoCallBack;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dev on 10/10/18.
 */

public class RecordVideoDialog implements View.OnClickListener {

    static private Dialog mDialog;

    @BindView(R.id.layRoot)
    CardView layRoot;

    @BindView(R.id.llCamera)
    FrameLayout llCamera;

    @BindView(R.id.llBrowse)
    FrameLayout llBrowse;

    private Activity mContext;
    private DialogRecordVideoCallBack dialogRecordVideoCallBack;

    public RecordVideoDialog(Activity context, DialogRecordVideoCallBack dialogRecordVideoCallBack) {
        mContext = context;
        this.dialogRecordVideoCallBack = dialogRecordVideoCallBack;
        showDialog();
    }

    public void showDialog() {
        View view;
        if (mDialog == null) {
            mDialog = new Dialog(mContext);
            mDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            view = View.inflate(mContext, R.layout.dialog_record_video, null);
            mDialog.setContentView(view);
            mDialog.setCancelable(true);
            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            mDialog.getWindow().setGravity(Gravity.BOTTOM);
            mDialog.getWindow().getAttributes().windowAnimations = R.style.DialogUpTheme;
            WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
            params.y = 10; // Here is the param to set your dialog position. Same with params.x
            params.x = 10; // Here is the param to set your dialog position. Same with params.x
            mDialog.getWindow().setAttributes(params);
            FrameLayout.LayoutParams paramsRoot = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
            );
            paramsRoot.setMargins(10, 0, 10, 0);
            mDialog.show();

            ButterKnife.bind(this, view);
            layRoot.setLayoutParams(paramsRoot);
            initListeners();
            mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    mDialog = null;
                }
            });
        }
    }


    private void initListeners() {
        llBrowse.setOnClickListener(this);
        llCamera.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llCamera:
                dialogRecordVideoCallBack.openCamera();
                break;
            case R.id.llBrowse:
                dialogRecordVideoCallBack.openGallery();
                break;
        }

        dismiss();

    }

    void dismiss() {
        mDialog.dismiss();
        mDialog = null;
    }
}

