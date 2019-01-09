package com.myreevuuCoach.activities;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.myreevuuCoach.R;
import com.myreevuuCoach.adapters.TransactionHistoryAdapter;
import com.myreevuuCoach.interfaces.InterConst;
import com.myreevuuCoach.models.TransactionModel;
import com.myreevuuCoach.network.RetrofitClient;

import java.util.ArrayList;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dev on 28/11/18.
 */

public class TransactionsHistoryActivity extends BaseActivity {

    @BindView(R.id.txtEarningAmount)
    TextView txtEarningAmount;
    @BindView(R.id.imgBack)
    ImageView imgBack;
    @BindView(R.id.rvTransaction)
    RecyclerView rvTransaction;

    RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected int getContentView() {
        return R.layout.activity_transaction_history;
    }

    @Override
    protected void onCreateStuff() {
        mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rvTransaction.setLayoutManager(mLayoutManager);
        txtEarningAmount.setText(getIntent().getStringExtra(InterConst.INTEND_AMOUNT));
        hitApi();
    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void initListener() {
        imgBack.setOnClickListener(this);
    }

    @Override
    protected Context getContext() {
        return this;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
        }
    }

    void hitApi() {
        showProgress();
        Call<TransactionModel> call = RetrofitClient.getInstance().transactions(mUtils.getString(InterConst.ACCESS_TOKEN, ""));
        call.enqueue(new Callback<TransactionModel>() {
            @Override
            public void onResponse(Call<TransactionModel> call, Response<TransactionModel> response) {
                hideProgress();
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
            public void onFailure(Call<TransactionModel> call, Throwable t) {
                hideProgress();
                toast(t.getMessage());

            }
        });
    }

    private void setData(ArrayList<TransactionModel.ResponseBean> response) {
        rvTransaction.setAdapter(new TransactionHistoryAdapter(mContext, response, getIntent().getStringExtra(InterConst.INTEND_ACCOUNT)));
    }

}
