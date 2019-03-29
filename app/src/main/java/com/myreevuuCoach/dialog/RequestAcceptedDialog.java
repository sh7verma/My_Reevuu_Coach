package com.myreevuuCoach.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myreevuuCoach.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RequestAcceptedDialog implements View.OnClickListener {

    static private Dialog mDialog;

    @BindView(R.id.txtOk)
    TextView txtOk;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    DialogInterface mDialogInterface;
    String title;
    private Context mContext;

    public RequestAcceptedDialog(Context context, String s, DialogInterface dialogInterface) {
        mContext = context;
        mDialogInterface = dialogInterface;
        title = s;
    }

    public void showDialog() {
        View view;
        if (mDialog == null) {
            mDialog = new Dialog(mContext);
            mDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            view = View.inflate(mContext, R.layout.dialog_accepted, null);
            mDialog.setContentView(view);
            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            mDialog.getWindow().getAttributes().windowAnimations = R.style.DialogPopAnimation;
            mDialog.show();

            ButterKnife.bind(this, view);
            initUI();
            initListeners();
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
        txtTitle.setText(title + "");
    }

    private void initListeners() {
        txtOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtOk:
                dismiss();
                break;
        }
    }

    public void dismiss() {
        mDialogInterface.dismiss();
        mDialog.dismiss();
        mDialog = null;

    }
}
