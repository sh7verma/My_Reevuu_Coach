package com.myreevuuCoach.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myreevuuCoach.R;
import com.myreevuuCoach.activities.RequestDetailActivity;
import com.myreevuuCoach.interfaces.InterConst;
import com.myreevuuCoach.interfaces.ItemClickInterface;
import com.myreevuuCoach.models.RequestsModel;
import com.myreevuuCoach.utils.Constants;
import com.myreevuuCoach.utils.RoundedTransformation;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dev on 4/12/18.
 */

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.ViewHolder> {

    Context mContext;
    ArrayList<RequestsModel.ResponseBean> mData;
    int mHeight;
    ItemClickInterface mItemClick;

    public RequestsAdapter(Context context, ArrayList<RequestsModel.ResponseBean> mData, int mHeight, ItemClickInterface clickInterface) {
        mContext = context;
        this.mData = mData;
        this.mHeight = mHeight;
        mItemClick = clickInterface;
    }

    public void notifyAdapter(ArrayList<RequestsModel.ResponseBean> list) {
        mData = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.item_requests, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        try {
            holder.txtTime.setText(Constants.Companion.displayDateTime(mData.get(position).getCreated_at()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.txtUserName.setText(mData.get(position).getName());

        if (!mData.get(position).getProfile_pic().equalsIgnoreCase("")) {
            Picasso.get()
                    .load(mData.get(position).getProfile_pic())
                    .transform(new RoundedTransformation((int) (mHeight * 10), 0))
                    .resize((int) (mHeight * 0.06), (int) (mHeight * 0.06))
                    .placeholder(R.mipmap.ic_profile)
                    .centerCrop(Gravity.TOP)
                    .error(R.mipmap.ic_profile).into(holder.imgUser);
        } else {
            Picasso.get()
                    .load(R.mipmap.ic_profile)
                    .transform(new RoundedTransformation((int) (mHeight * 10), 0))
                    .resize((int) (mHeight * 0.06), (int) (mHeight * 0.06))
                    .placeholder(R.mipmap.ic_profile)
                    .centerCrop(Gravity.TOP)
                    .error(R.mipmap.ic_profile).into(holder.imgUser);
        }

        holder.txtAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClick.onItemClick(position, InterConst.REEVUU_REQUESTS_ACCEPTED);
            }
        });
        holder.txtDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClick.onItemClick(position, InterConst.REEVUU_REQUESTS_DECLINE);
            }
        });

        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, RequestDetailActivity.class);
                intent.putExtra(InterConst.INTEND_EXTRA, mData.get(position));
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imgUser)
        ImageView imgUser;
        @BindView(R.id.txtUserName)
        TextView txtUserName;
        @BindView(R.id.txtTime)
        TextView txtTime;
        @BindView(R.id.llMain)
        LinearLayout llMain;
        @BindView(R.id.txtAccept)
        TextView txtAccept;
        @BindView(R.id.txtDecline)
        TextView txtDecline;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
