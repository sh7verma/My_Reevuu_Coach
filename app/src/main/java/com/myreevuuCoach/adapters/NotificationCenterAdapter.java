package com.myreevuuCoach.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.myreevuuCoach.R;
import com.myreevuuCoach.interfaces.AdapterClickInterface;
import com.myreevuuCoach.models.NotificationCenterModel;
import com.myreevuuCoach.utils.RoundedTransformation;
import com.myreevuuCoach.utils.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class NotificationCenterAdapter extends RecyclerView.Adapter<NotificationCenterAdapter.DessertVh> {


    protected int mWidth, mHeight;
    Utils utils;
    AdapterClickInterface adaptorClick;
    private ArrayList<NotificationCenterModel.NotificationResponse> mdata = new ArrayList<>();
    private Context context;
    private SimpleDateFormat chat_date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
    private SimpleDateFormat today_date_format = new SimpleDateFormat("hh:mm aa", Locale.US);
    private SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private SimpleDateFormat show_dateheader_format = new SimpleDateFormat("dd MMM yyyy", Locale.US);
    private SimpleDateFormat mTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    public NotificationCenterAdapter(Context context, ArrayList<NotificationCenterModel.NotificationResponse> mdata, AdapterClickInterface adaptorClick) {
        this.context = context;
        this.adaptorClick = adaptorClick;
        this.mdata = mdata;
        utils = new Utils(context);
        mWidth = new Utils(context).getInt("width", mWidth);
        mHeight = new Utils(context).getInt("height", mHeight);
    }

    @Override
    public DessertVh onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_notification_center, parent, false);
        return new DessertVh(view);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(DessertVh holder, final int position) {
        final NotificationCenterModel.NotificationResponse notificationResponse = mdata.get(position);

        holder.txt_msg.setText(notificationResponse.getMessage());
        holder.txt_date.setText(convertTime(notificationResponse.getUpdated_at()));

        holder.root_View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adaptorClick.onItemClick(position);
            }
        });

        if (!notificationResponse.getProfile_pic().equalsIgnoreCase("")) {
            Picasso.get()
                    .load(notificationResponse.getProfile_pic())
                    .transform(new RoundedTransformation((int) (mHeight * 10), 0))
                    .resize((int) (mHeight * 0.185), (int) (mHeight * 0.185))
                    .placeholder(R.mipmap.ic_profile)
                    .centerCrop(Gravity.TOP)
                    .error(R.mipmap.ic_profile).into(holder.img_pic, new Callback() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onError(Exception e) {
                }
            });

        } else {
            Picasso.get()
                    .load(R.mipmap.ic_profile)
                    .transform(new RoundedTransformation((int) (mHeight * 10), 0))
                    .resize((int) (mHeight * 0.185), (int) (mHeight * 0.185))
                    .centerCrop(Gravity.TOP)
                    .error(R.mipmap.ic_profile).into(holder.img_pic);
        }

        if (notificationResponse.getRead_status() == 0) {
            holder.img_notification_dot.setVisibility(View.VISIBLE);
            holder.root_View.setBackgroundResource(R.drawable.notification_blue_border);
        } else {
            holder.img_notification_dot.setVisibility(View.INVISIBLE);
            holder.root_View.setBackgroundResource(R.drawable.notification_grey_border);
        }

    }

    @Override
    public int getItemCount() {
        return mdata == null ? 0 : mdata.size();
    }

    String convertTime(String newMessageDate) {
        // newMessageDate = "2019-1-2 12:38";
        String date = "";
        Date calNew = null;
        String dateValue = "";

        try {
            chat_date_format.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date utcDate = chat_date_format.parse(newMessageDate);
            chat_date_format.setTimeZone(TimeZone.getDefault());
            date_format.setTimeZone(TimeZone.getDefault());
            today_date_format.setTimeZone(TimeZone.getDefault());
            today_date_format.setTimeZone(TimeZone.getDefault());
            String formattedDate = chat_date_format.format(utcDate);
            calNew = chat_date_format.parse(formattedDate);

        } catch (ParseException ex) {
            Log.v("Exception", ex.getLocalizedMessage());
        }
        String notificationDate = date_format.format(calNew.getTime());
        Calendar cal = Calendar.getInstance();
        String today = date_format.format(cal.getTime());

        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.DATE, -1);
        String yesterday = date_format.format(cal1.getTime());

        if (notificationDate.equalsIgnoreCase(today)) {
            date = "Today, " + today_date_format.format(calNew.getTime());
        } else if (notificationDate.equalsIgnoreCase(yesterday)) {
            date = "Yesterday, " + today_date_format.format(calNew.getTime());
        } else {
            date = show_dateheader_format.format(calNew.getTime()) + " " + today_date_format.format(calNew.getTime());
            /*mMessage.show_header_text = show_dateheader_format.format(calNew.time)
            mMessage.show_message_datetime = today_date_format.format(calNew.time)*/
        }

        return date;
    }

    public static class DessertVh extends RecyclerView.ViewHolder {

        View root_View;
        ImageView img_pic, img_notification_dot;
        private TextView txt_date;
        private TextView txt_msg;

        public DessertVh(View itemView) {
            super(itemView);
            root_View = itemView.findViewById(R.id.root_View);
            txt_msg = (TextView) itemView.findViewById(R.id.txt_msg);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            img_pic = (ImageView) itemView.findViewById(R.id.img_pic);
            img_notification_dot = (ImageView) itemView.findViewById(R.id.img_notification_dot);

        }
    }


}