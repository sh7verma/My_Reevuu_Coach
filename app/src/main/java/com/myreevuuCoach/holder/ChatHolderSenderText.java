package com.myreevuuCoach.holder;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myreevuuCoach.R;
import com.myreevuuCoach.firebase.FirebaseChatConstants;
import com.myreevuuCoach.firebase.MessagesModel;

import org.apache.commons.lang3.StringEscapeUtils;


public class ChatHolderSenderText {
    public LinearLayout llSentText, llSentMessage;
    public ImageView imgFavouriteTextSent, imgRead;
    public TextView txtMessage, txtReadMore, txtTime;
    int mWidth;

    public ChatHolderSenderText(Context con, View view, int width) {
        // TODO Auto-generated constructor stub
        mWidth = width;

        llSentText = (LinearLayout) view.findViewById(R.id.llSentText);

        imgFavouriteTextSent = (ImageView) view.findViewById(R.id.imgFavouriteTextSent);

        llSentMessage = (LinearLayout) view.findViewById(R.id.llSentMessage);

        txtMessage = (TextView) view.findViewById(R.id.txtMessage);

        txtReadMore = (TextView) view.findViewById(R.id.txtReadMore);

        txtTime = (TextView) view.findViewById(R.id.txtTime);

        imgRead = (ImageView) view.findViewById(R.id.imgRead);

    }

    public void bindHolder(Context mContext, MessagesModel mMessage, String userId) {

        boolean containsOtherText = false;
        int emojiCounter = 0;
        String tempChatTrimmed = null;

        for (int i = 0; i < mMessage.message.trim().length(); ) {
            if (mMessage.message.trim().length() >= i + 2) {
                tempChatTrimmed = mMessage.message.trim().substring(i, i + 2);
                String toServerUnicodeEncoded1 = StringEscapeUtils.escapeJava(tempChatTrimmed);
                if (toServerUnicodeEncoded1.startsWith("\\u")) {
                    emojiCounter++;
                    i = i + 2;
                } else if (!toServerUnicodeEncoded1.startsWith("\\u")) {
                    containsOtherText = true;
                }
            } else {
                tempChatTrimmed = mMessage.message.trim().substring(i, i + 1);
                String toServerUnicodeEncoded1 = StringEscapeUtils.escapeJava(tempChatTrimmed);
                if (toServerUnicodeEncoded1.startsWith("\\u")) {
                    emojiCounter++;
                    i = i + 1;
                } else if (!toServerUnicodeEncoded1.startsWith("\\u")) {
                    containsOtherText = true;
                }
            }
            if (emojiCounter > 3 || containsOtherText) {
                break;
            }
        }

        if (emojiCounter == 1 && !containsOtherText) {
            txtMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (mWidth * 0.08));
        } else if (emojiCounter == 2 && !containsOtherText) {
            txtMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (mWidth * 0.07));
        } else if (emojiCounter == 3 && !containsOtherText) {
            txtMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (mWidth * 0.06));
        } else {
            txtMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (mWidth * 0.05));
        }

        String show = "";
        if (mMessage.message.length() > FirebaseChatConstants.SHOW_TEXT_LENGTH) {
            show = mMessage.message.substring(0, FirebaseChatConstants.SHOW_TEXT_LENGTH) + "...";
            txtReadMore.setVisibility(View.VISIBLE);
        } else {
            show = mMessage.message;
            txtReadMore.setVisibility(View.GONE);
        }
        txtMessage.setText(show);

        txtTime.setText(mMessage.show_message_datetime);

//        if (mMessage.favourite_message.get(userId).equals("0")) {
//            imgFavouriteTextSent.setImageResource(R.mipmap.ic_fav);
//        } else {
//            imgFavouriteTextSent.setImageResource(R.mipmap.ic_fav_fill);
//        }

        if (mMessage.message_status == FirebaseChatConstants.STATUS_MESSAGE_SENT) {
            imgRead.setImageResource(R.mipmap.ic_sent);
        } else if (mMessage.message_status == FirebaseChatConstants.STATUS_MESSAGE_DELIVERED) {
            imgRead.setImageResource(R.mipmap.ic_delivered);
        } else if (mMessage.message_status == FirebaseChatConstants.STATUS_MESSAGE_SEEN) {
            imgRead.setImageResource(R.mipmap.ic_read);
        }


    }
}
