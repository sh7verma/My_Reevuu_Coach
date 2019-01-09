package com.myreevuuCoach.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.myreevuuCoach.R;

import butterknife.ButterKnife;

/**
 * Created by dev on 6/12/18.
 */

public class PasswordChangeDialog {

    static private Dialog mDialog;

    DialogInterface mDialogInterface;

    private Context mContext;

    public PasswordChangeDialog(Context context, DialogInterface dialogInterface) {
        mContext = context;
        mDialogInterface = dialogInterface;
    }

    public void showDialog() {
        View view;
        if (mDialog == null) {
            mDialog = new Dialog(mContext);
            mDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            view = View.inflate(mContext, R.layout.dialog_password_changed, null);
            mDialog.setContentView(view);
            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            mDialog.getWindow().getAttributes().windowAnimations = R.style.DialogPopAnimation;
            mDialog.show();

            ButterKnife.bind(this, view);
            initUI();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDialogInterface.dismiss();
                    mDialog.dismiss();
                }
            }, 2000);

            mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    mDialogInterface.dismiss();
                    mDialog = null;
                }
            });
        }
    }

    private void initUI() {

    }

    public void dismiss() {
        mDialogInterface.dismiss();
        mDialog.dismiss();
        mDialog = null;

    }
}
