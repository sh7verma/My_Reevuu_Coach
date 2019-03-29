package com.myreevuuCoach.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myreevuuCoach.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dev on 4/2/19.
 */

public class EditTextDialog implements View.OnClickListener {

    static private Dialog mDialog;

    @BindView(R.id.txtDone)
    TextView txtDone;

    @BindView(R.id.edEnter)
    EditText edEnter;
    View view;
    private Activity mContext;
    private DialogCallBack mCallBack;

    public EditTextDialog(Activity context, DialogCallBack dialogRecordVideoCallBack) {
        mContext = context;
        this.mCallBack = dialogRecordVideoCallBack;
        showDialog();
    }

    public void showDialog() {
        if (mDialog == null) {
            mDialog = new Dialog(mContext);
            mDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            view = View.inflate(mContext, R.layout.dialog_enter_text, null);
            mDialog.setContentView(view);
            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            mDialog.getWindow().getAttributes().windowAnimations = R.style.DialogPopAnimation;
            mDialog.show();

            ButterKnife.bind(this, view);
            initListeners();
            mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    mCallBack.dismiss();
                    mDialog = null;
                }
            });
        }
    }


    private void initListeners() {
        txtDone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtDone:
                if (!TextUtils.isEmpty(edEnter.getText().toString())) {
                    mCallBack.done(edEnter.getText().toString());
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    dismiss();
                } else {
                    edEnter.setError(mContext.getString(R.string.please_enter));
                }
                break;

        }
    }

    void dismiss() {
        mDialog.dismiss();
        mDialog = null;
    }


    public interface DialogCallBack {

        void done(String s);

        void dismiss();

    }

}
