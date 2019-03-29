package com.myreevuuCoach.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.myreevuuCoach.R;
import com.myreevuuCoach.dialog.PasswordChangeDialog;
import com.myreevuuCoach.interfaces.InterConst;
import com.myreevuuCoach.models.BaseSuccessModel;
import com.myreevuuCoach.network.RetrofitClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dev on 4/12/18.
 */

public class ChangePasswordActivity extends BaseActivity {

    @BindView(R.id.edOldPassword)
    EditText edOldPassword;
    @BindView(R.id.edNewPassword)
    EditText edNewPassword;
    @BindView(R.id.edConfirmPassword)
    EditText edConfirmPassword;
    @BindView(R.id.txtDone)
    TextView txtDone;
    @BindView(R.id.imgBack)
    ImageView imgBack;

    @Override
    protected int getContentView() {
        return R.layout.activity_change_password;
    }

    @Override
    protected void onCreateStuff() {

    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void initListener() {
        txtDone.setOnClickListener(this);
        imgBack.setOnClickListener(this);

    }

    @Override
    protected Context getContext() {
        return this;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtDone:
                checkValidation();
                break;
            case R.id.imgBack:
                finish();
                break;
        }
    }

    void checkValidation() {

        if (TextUtils.isEmpty(edOldPassword.getText().toString())) {
            showAlertSnackBar(txtDone, getString(R.string.enter_password));
            return;
        } else if (edOldPassword.getText().toString().length() < 8) {
            showAlertSnackBar(txtDone, getString(R.string.error_password));
            return;
        }
        if (TextUtils.isEmpty(edNewPassword.getText().toString())) {
            showAlertSnackBar(txtDone, getString(R.string.enter_password));
            return;
        } else if (edNewPassword.getText().toString().length() < 8) {
            showAlertSnackBar(txtDone, getString(R.string.error_password));
            return;
        }
        if (TextUtils.isEmpty(edConfirmPassword.getText().toString())) {
            showAlertSnackBar(txtDone, getString(R.string.enter_password));
            return;
        } else if (edConfirmPassword.getText().toString().length() < 8) {
            showAlertSnackBar(txtDone, getString(R.string.error_password));
            return;
        }
        if (!edNewPassword.getText().toString().equals(edConfirmPassword.getText().toString())) {
            showAlertSnackBar(txtDone, getString(R.string.confirm_new_password_did_not_match));
            return;
        }
        hitApi();
    }

    void hitApi() {
        showProgress();
        Call<BaseSuccessModel> call = RetrofitClient.getInstance().change_password(
                mUtils.getString(InterConst.ACCESS_TOKEN, ""),
                edOldPassword.getText().toString(),
                edNewPassword.getText().toString());
        call.enqueue(new Callback<BaseSuccessModel>() {
            @Override
            public void onResponse(Call<BaseSuccessModel> call, Response<BaseSuccessModel> response) {
                hideProgress();
                if (response.body().getResponse() != null) {
                    new PasswordChangeDialog(mContext, new DialogInterface() {
                        @Override
                        public void cancel() {

                        }

                        @Override
                        public void dismiss() {
                            finish();
                        }
                    }).showDialog();
                } else {
                    toast(response.body().getError().getMessage());
                    if (response.body().getError().getCode() == InterConst.INVALID_ACCESS) {
                        moveToSplash();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseSuccessModel> call, Throwable t) {
                hideProgress();
            }
        });

    }
}
