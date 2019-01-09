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


public class ChatHolderReceiverText {
    public LinearLayout llReceiveText, llReceiveMessage;
    public ImageView imgFavouriteTextReceive;
    public TextView txtMessage, txtReadMore, txtTime;
    int mWidth;

    public ChatHolderReceiverText(Context con, View view, int width) {
        // TODO Auto-generated constructor stub
        mWidth = width;

        llReceiveText = (LinearLayout) view.findViewById(R.id.llReceiveText);

        imgFavouriteTextReceive = (ImageView) view.findViewById(R.id.imgFavouriteTextReceive);

        llReceiveMessage = (LinearLayout) view.findViewById(R.id.llReceiveMessage);

        txtMessage = (TextView) view.findViewById(R.id.txtMessage);

        txtReadMore = (TextView) view.findViewById(R.id.txtReadMore);

        txtTime = (TextView) view.findViewById(R.id.txtTime);

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
    }
}
