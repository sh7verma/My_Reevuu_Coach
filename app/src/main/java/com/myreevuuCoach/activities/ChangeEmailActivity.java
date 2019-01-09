package com.myreevuuCoach.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.myreevuuCoach.R;
import com.myreevuuCoach.interfaces.InterConst;
import com.myreevuuCoach.models.BaseSuccessModel;
import com.myreevuuCoach.network.RetrofitClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dev on 4/12/18.
 */

public class ChangeEmailActivity extends BaseActivity {

    @BindView(R.id.imgBack)
    ImageView imgBack;

    @BindView(R.id.edEmail)
    EditText edEmail;

    @BindView(R.id.txtDone)
    TextView txtDone;

    @BindView(R.id.tvEmailExisting)
    TextView tvEmailExisting;

    @Override
    protected int getContentView() {
        return R.layout.activity_change_email;
    }

    @Override
    protected void onCreateStuff() {
        if (mUtils.getString(InterConst.NEW_EMAIL, "").equalsIgnoreCase("")) {
            tvEmailExisting.setText(mUtils.getString(InterConst.EMAIL, ""));
        } else {
            tvEmailExisting.setText(mUtils.getString(InterConst.EMAIL, ""));
            edEmail.setText(mUtils.getString(InterConst.NEW_EMAIL, ""));
        }
    }

    @Override
    protected void initUI() {
        imgBack.setOnClickListener(this);
        txtDone.setOnClickListener(this);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected Context getContext() {
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.txtDone:
                checkValidation();
                break;
        }
    }

    //
    private void checkValidation() {
        if (TextUtils.isEmpty(edEmail.getText().toString())) {
            showAlertSnackBar(txtDone, getString(R.string.enter_email));
            return;
        } else if (!validateEmail(edEmail.getText().toString().trim())) {
            showAlertSnackBar(txtDone, getString(R.string.enter_valid_email));
            return;
        }

        changeEmail();

    }

    private boolean validateEmail(CharSequence text) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }


    private void changeEmail() {
        if (connectedToInternet(txtDone)) {
            showProgress();
            Call<BaseSuccessModel> call = RetrofitClient.getInstance().change_email(
                    mUtils.getString(InterConst.ACCESS_TOKEN, ""),
                    edEmail.getText().toString().trim());
            call.enqueue(new retrofit2.Callback<BaseSuccessModel>() {
                @Override
                public void onResponse(@NonNull Call<BaseSuccessModel> call, @NonNull Response<BaseSuccessModel> response) {
                    hideProgress();
                    if (response.body().getResponse() != null) {
                        mUtils.setString(InterConst.NEW_EMAIL, edEmail.getText().toString().trim());
                        moveToVerifyEmail();
                    } else {
                        toast(response.body().getError().getMessage());
                        if (response.body().getError().getCode() == InterConst.INVALID_ACCESS) {
                            moveToSplash();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<BaseSuccessModel> call, @NonNull Throwable t) {
                    hideProgress();
                    t.printStackTrace();
                }
            });
        }
    }

    private void moveToVerifyEmail() {
        Intent intent = new Intent(mContext, VerifyEmailActivity.class);
        intent.putExtra(InterConst.INTEND_EXTRA, "Setting");
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();

    }
}
