package com.myreevuuCoach.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myreevuuCoach.R;
import com.myreevuuCoach.activities.TransactionsHistoryActivity;
import com.myreevuuCoach.interfaces.InterConst;
import com.myreevuuCoach.models.PaymentModel;
import com.myreevuuCoach.network.RetrofitClient;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("ValidFragment")
public class ProfilePaymentFragment extends BaseFragment implements View.OnClickListener {

    @SuppressLint("StaticFieldLeak")
    static ProfilePaymentFragment fragment;
    @SuppressLint("StaticFieldLeak")
    static Context mContext;

    @BindView(R.id.llEarning)
    LinearLayout llEarning;
    @BindView(R.id.txtEarningAmount)
    TextView txtEarningAmount;
    @BindView(R.id.txtAccountName)
    TextView txtAccountName;
    @BindView(R.id.txtAccountNumber)
    TextView txtAccountNumber;

    public static ProfilePaymentFragment newInstance(Context context) {
        fragment = new ProfilePaymentFragment();
        mContext = context;
        return fragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_payment;
    }

    @Override
    protected void onCreateStuff() {
        hitApi();
    }

    @Override
    protected void initListeners() {
        llEarning.setOnClickListener(this);
    }

    void hitApi() {
        Call<PaymentModel> call = RetrofitClient.getInstance().payments(utils.getString(InterConst.ACCESS_TOKEN, ""));
        call.enqueue(new Callback<PaymentModel>() {
            @Override
            public void onResponse(Call<PaymentModel> call, Response<PaymentModel> response) {
                if (response.body().getResponse() != null) {
                    setData(response.body().getResponse());
                } else {
                    toast(response.body().getError().getMessage());
                    if (response.body().getError().getCode() == InterConst.INVALID_ACCESS) {
                        moveToSplash();
                    }
                }
            }

            @Override
            public void onFailure(Call<PaymentModel> call, Throwable t) {

            }
        });

    }

    void setData(PaymentModel.ResponseBean response) {
        txtEarningAmount.setText(InterConst.DOLLAR + response.getEarning());
        txtAccountName.setText(response.getBank_accounts().get(0).getFirst_name());
        txtAccountNumber.setText(response.getBank_accounts().get(0).getAccount_number());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llEarning:
                Intent intent = new Intent(mContext, TransactionsHistoryActivity.class);
                intent.putExtra(InterConst.INTEND_AMOUNT, txtEarningAmount.getText());
                intent.putExtra(InterConst.INTEND_ACCOUNT, txtAccountNumber.getText());
                startActivity(intent);
                break;
        }
    }
}
