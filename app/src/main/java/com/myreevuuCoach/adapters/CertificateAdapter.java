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
import com.myreevuuCoach.models.OptionsModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dev on 22/11/18.
 */

public class CertificateAdapter extends RecyclerView.Adapter<CertificateAdapter.ViewHolder> {
    Context mContext;
    ArrayList<OptionsModel> mData;

    public CertificateAdapter(Context context, ArrayList<OptionsModel> arrayList) {
        mContext = context;
        mData = arrayList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.item_certificates, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mData.get(position).getName().contains(InterConst.REGEX_CERTIFICATED)) {
            holder.txtCertificateName.setText(mData.get(position).getName().replace(InterConst.REGEX_CERTIFICATED, "-"));
        } else {
            holder.txtCertificateName.setText(mData.get(position).getName());
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtCertificateName)
        TextView txtCertificateName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
