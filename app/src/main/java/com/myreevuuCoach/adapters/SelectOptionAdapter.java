package com.myreevuuCoach.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myreevuuCoach.R;
import com.myreevuuCoach.interfaces.AdapterClickInterface;
import com.myreevuuCoach.models.OptionsModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dev on 27/11/18.
 */

public class SelectOptionAdapter extends RecyclerView.Adapter<SelectOptionAdapter.ViewHolder> {

    Context mContext;
    ArrayList<OptionsModel> mData;

    AdapterClickInterface itemClickInterface;

    public SelectOptionAdapter(Context context, ArrayList<OptionsModel> arrayList, AdapterClickInterface adapterClickInterface) {
        mContext = context;
        mData = arrayList;
        itemClickInterface = adapterClickInterface;
    }

    @NonNull
    @Override
    public SelectOptionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.item_options, parent, false);
        return new SelectOptionAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final SelectOptionAdapter.ViewHolder holder, final int position) {

        holder.txtOption.setText(mData.get(position).getName());

        if (mData.get(position).isSelected()) {
            holder.imgOption.setVisibility(View.VISIBLE);
        } else {
            holder.imgOption.setVisibility(View.GONE);
        }

        holder.llOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int i = 0; i < mData.size(); i++) {
                    if (position == i) {
                        mData.get(position).setSelected(true);
                        holder.imgOption.setVisibility(View.VISIBLE);
                    } else {
                        mData.get(i).setSelected(false);
                        holder.imgOption.setVisibility(View.INVISIBLE);
                    }
                }
                itemClickInterface.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.llOption)
        LinearLayout llOption;
        @BindView(R.id.txtOption)
        TextView txtOption;
        @BindView(R.id.imgOption)
        ImageView imgOption;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
