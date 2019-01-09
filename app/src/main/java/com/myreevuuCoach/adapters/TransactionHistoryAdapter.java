package com.myreevuuCoach.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myreevuuCoach.R;
import com.myreevuuCoach.interfaces.InterConst;
import com.myreevuuCoach.models.TransactionModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dev on 28/11/18.
 */

public class TransactionHistoryAdapter extends RecyclerView.Adapter<TransactionHistoryAdapter.ViewHolder> {

    Context mContext;
    ArrayList<TransactionModel.ResponseBean> mData;
    String mAccount;

    public TransactionHistoryAdapter(Context mContext, ArrayList<TransactionModel.ResponseBean> list, String account) {
        this.mContext = mContext;
        this.mData = list;
        mAccount = account;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.item_transaction_history, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.txtDateTime.setText(mData.get(position).getCreated_at());
        holder.txtId.setText("ID: " + String.valueOf(mData.get(position).getId()));
        holder.txtAmount.setText(InterConst.DOLLAR + mData.get(position).getPayment());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtDateTime)
        TextView txtDateTime;
        @BindView(R.id.txtId)
        TextView txtId;
        @BindView(R.id.txtAmount)
        TextView txtAmount;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
