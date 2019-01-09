package com.myreevuuCoach.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.myreevuuCoach.R;
import com.myreevuuCoach.activities.ConversationActivity;
import com.myreevuuCoach.activities.FullViewMessageActivity;
import com.myreevuuCoach.firebase.ChatsModel;
import com.myreevuuCoach.firebase.Database;
import com.myreevuuCoach.firebase.FirebaseChatConstants;
import com.myreevuuCoach.firebase.MessagesModel;
import com.myreevuuCoach.holder.ChatHolderHeader;
import com.myreevuuCoach.holder.ChatHolderReceiverText;
import com.myreevuuCoach.holder.ChatHolderSenderText;
import com.myreevuuCoach.interfaces.InterConst;
import com.myreevuuCoach.utils.Utils;


/**
 * Created by dev on 30/7/18.
 */

public class ConversationAdapter extends BaseAdapter {

    Context mContext;
    int mScreenwidth;
    Database mDb;
    Utils mUtils;
    ConversationActivity mConversationActivity;
    ChatsModel mPrivateChat = null;

    String mUserID, mOpponentUserId, mParticipantIds;
    boolean flag = false;

    public ConversationAdapter(Context con, ConversationActivity mActivity, int width, String userId,
                               String otherUserId, String participantIds, ChatsModel mChat) {
        mContext = con;
        mScreenwidth = width;
        mUtils = new Utils(mContext);
        mDb = new Database(mContext);
        mConversationActivity = mActivity;
        mUserID = userId;
        mOpponentUserId = otherUserId;
        mParticipantIds = participantIds;
        mPrivateChat = mChat;
    }

    public void remove_selection() {
        notifyDataSetChanged();
    }

    public void animationStatus(boolean status) {
        flag = status;
    }

    @Override
    public int getCount() {
        return mConversationActivity.getMMessagesMap().size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final MessagesModel mMessage = mConversationActivity.getMMessagesMap().get(mConversationActivity.getMMessageIds().get(position));

        if (mMessage.is_header) {
            ChatHolderHeader header = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_header, parent, false);
                header = new ChatHolderHeader(mContext, convertView, mScreenwidth);
                convertView.setTag(header);
            } else {
                if (convertView.getTag() instanceof ChatHolderHeader) {
                    header = (ChatHolderHeader) convertView.getTag();
                } else {
                    convertView = LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.item_header, parent, false);
                    header = new ChatHolderHeader(mContext, convertView, mScreenwidth);
                    convertView.setTag(header);
                }
            }
            header.bindHolder(mMessage.show_header_text);
        } else {
            switch (mMessage.message_type) {
                case FirebaseChatConstants.TYPE_TEXT:
                    if (mMessage.sender_id.equalsIgnoreCase(mUserID)) {
                        ChatHolderSenderText mSentTextHolder = null;
                        if (convertView == null) {
                            convertView = LayoutInflater.from(parent.getContext()).inflate(
                                    R.layout.item_text_sent, parent, false);
                            mSentTextHolder = new ChatHolderSenderText(mContext, convertView, mScreenwidth);
                            convertView.setTag(mSentTextHolder);
                        } else {
                            if (convertView.getTag() instanceof ChatHolderSenderText) {
                                mSentTextHolder = (ChatHolderSenderText) convertView.getTag();
                            } else {
                                convertView = LayoutInflater.from(parent.getContext()).inflate(
                                        R.layout.item_text_sent, parent, false);
                                mSentTextHolder = new ChatHolderSenderText(mContext, convertView, mScreenwidth);
                                convertView.setTag(mSentTextHolder);
                            }
                        }
                        mSentTextHolder.bindHolder(mContext, mMessage, mUserID);

                        mSentTextHolder.txtReadMore.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent in = new Intent(mContext, FullViewMessageActivity.class);
                                in.putExtra("message", "" + mMessage.message);
                                in.putExtra("pic", "" + mPrivateChat.profile_pic.get(mOpponentUserId));
                                in.putExtra("name", "" + mPrivateChat.name.get(mOpponentUserId));
                                mContext.startActivity(in);
                            }
                        });

                        if (mConversationActivity.getSelectedPosition() == position) {
                            mSentTextHolder.llSentText.setBackgroundColor(mContext.getResources().getColor(R.color.app_color));
                        } else {
                            mSentTextHolder.llSentText.setBackgroundColor(Color.TRANSPARENT);
                        }

                        mSentTextHolder.llSentMessage.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                mConversationActivity.isOptionsVisible();
                                mConversationActivity.makeOptionsVisible(position, mMessage.message_id, 1);
                                notifyDataSetChanged();
                                return true;
                            }
                        });

                        mSentTextHolder.txtMessage.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                mConversationActivity.isOptionsVisible();
                                mConversationActivity.makeOptionsVisible(position, mMessage.message_id, 1);
                                notifyDataSetChanged();
                                return true;
                            }
                        });
//                        if (flag)
//                            setAnimation(mSentTextHolder.llSentMessage, position);
                    } else {
                        ChatHolderReceiverText mReceiveTextHolder;
                        if (convertView == null) {
                            convertView = LayoutInflater.from(parent.getContext()).inflate(
                                    R.layout.item_text_received, parent, false);
                            mReceiveTextHolder = new ChatHolderReceiverText(mContext, convertView, mScreenwidth);
                            convertView.setTag(mReceiveTextHolder);
                        } else {
                            if (convertView.getTag() instanceof ChatHolderReceiverText) {
                                mReceiveTextHolder = (ChatHolderReceiverText) convertView.getTag();
                            } else {
                                convertView = LayoutInflater.from(parent.getContext()).inflate(
                                        R.layout.item_text_received, parent, false);
                                mReceiveTextHolder = new ChatHolderReceiverText(mContext, convertView, mScreenwidth);
                                convertView.setTag(mReceiveTextHolder);
                            }
                        }
                        mReceiveTextHolder.bindHolder(mContext, mMessage, mUserID);

                        mReceiveTextHolder.txtReadMore.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent in = new Intent(mContext, FullViewMessageActivity.class);
                                in.putExtra("message", "" + mMessage.message);
                                in.putExtra("pic", "" + mPrivateChat.profile_pic.get(mOpponentUserId));
                                in.putExtra("name", "" + mPrivateChat.name.get(mOpponentUserId));
                                mContext.startActivity(in);
                            }
                        });

                        if (mConversationActivity.getSelectedPosition() == position) {
                            mReceiveTextHolder.llReceiveText.setBackgroundColor(mContext.getResources().getColor(R.color.app_color));
                        } else {
                            mReceiveTextHolder.llReceiveText.setBackgroundColor(Color.TRANSPARENT);
                        }

                        if (mConversationActivity.getMOtherUserId().equals(InterConst.APP_ADMIN_ID)) {
                            if (position != 1) {
                                mReceiveTextHolder.llReceiveMessage.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View v) {
                                        mConversationActivity.isOptionsVisible();
                                        mConversationActivity.makeOptionsVisible(position, mMessage.message_id, 1);
                                        notifyDataSetChanged();
                                        return true;
                                    }
                                });

                                mReceiveTextHolder.txtMessage.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View v) {
                                        mConversationActivity.isOptionsVisible();
                                        mConversationActivity.makeOptionsVisible(position, mMessage.message_id, 1);
                                        notifyDataSetChanged();
                                        return true;
                                    }
                                });
                            }
                        } else {
                            mReceiveTextHolder.llReceiveMessage.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    mConversationActivity.isOptionsVisible();
                                    mConversationActivity.makeOptionsVisible(position, mMessage.message_id, 1);
                                    notifyDataSetChanged();
                                    return true;
                                }
                            });

                            mReceiveTextHolder.txtMessage.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    mConversationActivity.isOptionsVisible();
                                    mConversationActivity.makeOptionsVisible(position, mMessage.message_id, 1);
                                    notifyDataSetChanged();
                                    return true;
                                }
                            });
                        }

                        /*mReceiveTextHolder.llReceiveMessage.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                mConversationActivity.isOptionsVisible();
                                mConversationActivity.makeOptionsVisible(position, mMessage.message_id, 1);
                                notifyDataSetChanged();
                                return true;
                            }
                        });

                        mReceiveTextHolder.txtMessage.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                mConversationActivity.isOptionsVisible();
                                mConversationActivity.makeOptionsVisible(position, mMessage.message_id, 1);
                                notifyDataSetChanged();
                                return true;
                            }
                        });*/
                    }
                    break;
            }
        }
        return convertView;
    }

}
