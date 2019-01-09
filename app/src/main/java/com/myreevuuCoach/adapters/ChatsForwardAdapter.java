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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.myreevuuCoach.R;
import com.myreevuuCoach.activities.ForwardChatActivity;
import com.myreevuuCoach.firebase.ChatsModel;
import com.myreevuuCoach.firebase.Database;
import com.myreevuuCoach.firebase.FirebaseChatConstants;
import com.myreevuuCoach.fragments.ChatFragment;
import com.myreevuuCoach.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatsForwardAdapter extends RecyclerView.Adapter<ChatsForwardAdapter.ViewHolder> {


    Context mContext = null;
    Utils mUtils = null;
    Database mDb = null;
    int mScreenWidth = 0;
    LinkedHashMap<String, ChatsModel> mChats = null;
    ArrayList<String> mKeys = new ArrayList<>();
    String mUserID = "";
    SimpleDateFormat date_format = new SimpleDateFormat("dd MMM hh:mm a", Locale.US);
    SimpleDateFormat today_format = new SimpleDateFormat("hh:mm a", Locale.US);
    SimpleDateFormat only_date_format = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    boolean state = false;
    private ForwardChatActivity activity = null;

    public ChatsForwardAdapter(Context mContext, ForwardChatActivity activity,int width, LinkedHashMap<String, ChatsModel> chats,
                               ArrayList<String> keys, String userId, boolean status) {
        this.mContext = mContext;
        mUtils = new Utils(mContext);
        mDb = new Database(mContext);
        this.activity = activity;
        mScreenWidth = width;
        mChats = chats;
        mKeys = keys;
        mUserID = userId;
        state = status;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.item_recent_chats, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatsModel mPrivateChat = null;
        if (state) {
            mPrivateChat = mChats.get(mKeys.get(position));
        } else {
            mPrivateChat = activity.getMChats().get(activity.getMKeys().get(position));
        }
        String otherUserId = mPrivateChat.opponent_user_id;

        RequestOptions options = new RequestOptions().centerCrop().placeholder(R.mipmap.ic_profile_avatar)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE);

        Glide.with(mContext).load(mPrivateChat.profile_pic.get(otherUserId)).apply(options).into(holder.imgProfileAvatar);

        holder.txtName.setText(mPrivateChat.name.get(otherUserId));

        if (mPrivateChat.unread_count.get(mUserID) != 0) {
            holder.txtUnreadCount.setVisibility(View.VISIBLE);
            holder.txtUnreadCount.setText(mPrivateChat.unread_count.get(mUserID).toString());
        } else {
            holder.txtUnreadCount.setVisibility(View.INVISIBLE);
        }

        if (!mPrivateChat.last_message_data.get(mUserID).equals(FirebaseChatConstants.DEFAULT_MESSAGE_REGEX)) {
            holder.llLastMessage.setVisibility(View.VISIBLE);
            holder.imgLastMessageIcon.setImageResource(0);
            holder.txtLastMessage.setText(mPrivateChat.last_message_data.get(mUserID));
            try {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(FirebaseChatConstants.getLocalTime(mPrivateChat.last_message_time.get(mUserID)));
                Calendar today = Calendar.getInstance();
                long time = mDb.getMessageTime(mPrivateChat.chat_dialog_id);
                if (time > 0) {
                    Calendar calLocal = Calendar.getInstance();
//                    val values = mUtils!!.getString("offset", "0.0").split(".\\").toTypedArray()
                    calLocal.setTimeInMillis(time);/*+ values[0].toLong()*/
                    if (only_date_format.format(today.getTime()) == only_date_format.format(cal.getTime())) {
                        // today
                        holder.txtTime.setText(today_format.format(calLocal.getTime()));
                    } else {

                        holder.txtTime.setText(date_format.format(calLocal.getTime()));
                    }
                } else {
                    if (only_date_format.format(today.getTime()) == only_date_format.format(cal.getTime())) {
                        // today
                        holder.txtTime.setText(today_format.format(cal.getTime()));
                    } else {

                        holder.txtTime.setText(date_format.format(cal.getTime()));
                    }
                }

            } catch (Exception e) {
                holder.txtTime.setText("");
            }

        } else {
            holder.llLastMessage.setVisibility(View.GONE);
            holder.txtTime.setText("");
        }

        final ChatsModel finalMPrivateChat = mPrivateChat;
        holder.llChatOuter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.openConversationActivity(finalMPrivateChat);
            }
        });
        holder.llChatOuter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
              //  activity.deleteConversation(finalMPrivateChat);

                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        if (state) {
            return mKeys.size();
        } else {
            return activity.getMKeys().size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.llChatOuter)
        LinearLayout llChatOuter;
        @BindView(R.id.imgProfileAvatar)
        ImageView imgProfileAvatar;
        @BindView(R.id.txtName)
        TextView txtName;
        @BindView(R.id.txtTime)
        TextView txtTime;
        @BindView(R.id.txtUnreadCount)
        TextView txtUnreadCount;
        @BindView(R.id.imgLastMessageIcon)
        ImageView imgLastMessageIcon;
        @BindView(R.id.txtLastMessage)
        TextView txtLastMessage;
        @BindView(R.id.llLastMessage)
        LinearLayout llLastMessage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }



}
