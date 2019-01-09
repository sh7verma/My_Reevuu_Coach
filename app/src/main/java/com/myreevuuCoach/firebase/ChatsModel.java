package com.myreevuuCoach.firebase;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.HashMap;

/**
 * Created by app on 8/2/2016.
 */
public class ChatsModel implements Parcelable {


    public static final Creator<ChatsModel> CREATOR = new Creator<ChatsModel>() {
        @Override
        public ChatsModel createFromParcel(Parcel source) {
            return new ChatsModel(source);
        }

        @Override
        public ChatsModel[] newArray(int size) {
            return new ChatsModel[size];
        }
    };
    public String chat_dialog_id;
    public String last_message;
    public HashMap<String, Long> last_message_time;
    public String last_message_sender_id;
    public String last_message_id;
    public String participant_ids;
    public HashMap<String, Integer> unread_count;
    public HashMap<String, String> name;
    public HashMap<String, String> profile_pic;
    public HashMap<String, Long> delete_dialog_time;
    public HashMap<String, String> block_status;
    public HashMap<String, String> user_type;
    public HashMap<String, String> rating;
    public HashMap<String, String> delete_outer_dialog;
    public String opponent_user_id;
    public HashMap<String, Integer> message_rating_count;
    public HashMap<String, String> last_message_type;
    public HashMap<String, String> last_message_data;

    public ChatsModel() {
    }

    protected ChatsModel(Parcel in) {
        this.chat_dialog_id = in.readString();
        this.last_message = in.readString();
        this.last_message_time = (HashMap<String, Long>) in.readSerializable();
        this.last_message_sender_id = in.readString();
        this.last_message_id = in.readString();
        this.participant_ids = in.readString();
        this.unread_count = (HashMap<String, Integer>) in.readSerializable();
        this.name = (HashMap<String, String>) in.readSerializable();
        this.profile_pic = (HashMap<String, String>) in.readSerializable();
        this.delete_outer_dialog = (HashMap<String, String>) in.readSerializable();
        this.delete_dialog_time = (HashMap<String, Long>) in.readSerializable();
        this.block_status = (HashMap<String, String>) in.readSerializable();
        this.user_type = (HashMap<String, String>) in.readSerializable();
        this.rating = (HashMap<String, String>) in.readSerializable();
        this.opponent_user_id = in.readString();
        this.message_rating_count = (HashMap<String, Integer>) in.readSerializable();
        this.last_message_type = (HashMap<String, String>) in.readSerializable();
        this.last_message_data = (HashMap<String, String>) in.readSerializable();
    }

    public static ChatsModel parseChat(DataSnapshot dataSnapshot, String userId) {
//        ChatsModel chat = dataSnapshot.getValue(ChatsModel.class);
        ChatsModel chat = null;
        if (!TextUtils.isEmpty(dataSnapshot.child("participant_ids").getValue(String.class))) {
            chat = new ChatsModel();
            chat.chat_dialog_id = dataSnapshot.child("chat_dialog_id").getValue(String.class);
            chat.last_message = dataSnapshot.child("last_message").getValue(String.class);

            GenericTypeIndicator<HashMap<String, Long>> gtTime = new GenericTypeIndicator<HashMap<String, Long>>() {
            };
            chat.last_message_time = dataSnapshot.child("last_message_time").getValue(gtTime);

            chat.last_message_sender_id = dataSnapshot.child("last_message_sender_id").getValue(String.class);
            chat.last_message_id = dataSnapshot.child("last_message_id").getValue(String.class);
            chat.participant_ids = dataSnapshot.child("participant_ids").getValue(String.class);

            GenericTypeIndicator<HashMap<String, Integer>> gtUnread = new GenericTypeIndicator<HashMap<String, Integer>>() {
            };
            chat.unread_count = dataSnapshot.child("unread_count").getValue(gtUnread);

            GenericTypeIndicator<HashMap<String, String>> gtName = new GenericTypeIndicator<HashMap<String, String>>() {
            };
            chat.name = dataSnapshot.child("name").getValue(gtName);


            GenericTypeIndicator<HashMap<String, String>> gtPic = new GenericTypeIndicator<HashMap<String, String>>() {
            };
            chat.profile_pic = dataSnapshot.child("profile_pic").getValue(gtPic);

            GenericTypeIndicator<HashMap<String, Long>> gtDelete = new GenericTypeIndicator<HashMap<String, Long>>() {
            };
            chat.delete_dialog_time = dataSnapshot.child("delete_dialog_time").getValue(gtDelete);

            GenericTypeIndicator<HashMap<String, String>> gtBlock = new GenericTypeIndicator<HashMap<String, String>>() {
            };
            chat.block_status = dataSnapshot.child("block_status").getValue(gtBlock);

            GenericTypeIndicator<HashMap<String, String>> gtType = new GenericTypeIndicator<HashMap<String, String>>() {
            };
            chat.user_type = dataSnapshot.child("user_type").getValue(gtType);

            GenericTypeIndicator<HashMap<String, String>> gtRating = new GenericTypeIndicator<HashMap<String, String>>() {
            };
            chat.rating = dataSnapshot.child("rating").getValue(gtRating);

            GenericTypeIndicator<HashMap<String, String>> gtLastMessageType = new GenericTypeIndicator<HashMap<String, String>>() {
            };
            chat.last_message_type = dataSnapshot.child("last_message_type").getValue(gtLastMessageType);

            GenericTypeIndicator<HashMap<String, String>> gLastMessageData = new GenericTypeIndicator<HashMap<String, String>>() {
            };
            chat.last_message_data = dataSnapshot.child("last_message_data").getValue(gLastMessageData);

            GenericTypeIndicator<HashMap<String, Integer>> gtRatingCount = new GenericTypeIndicator<HashMap<String, Integer>>() {
            };
            chat.message_rating_count = dataSnapshot.child("message_rating_count").getValue(gtRatingCount);

            GenericTypeIndicator<HashMap<String, String>> gtDeleteOuterDialog = new GenericTypeIndicator<HashMap<String, String>>() {
            };
            chat.delete_outer_dialog = dataSnapshot.child("delete_outer_dialog").getValue(gtDeleteOuterDialog);


            if (chat.message_rating_count == null) {
                chat.message_rating_count = new HashMap<>();
                chat.message_rating_count.put(userId, 0);
                chat.message_rating_count.put(chat.opponent_user_id, 0);
            } else {
                if (!chat.message_rating_count.containsKey(userId)) {
                    chat.message_rating_count.put(userId, 0);
                }
            }

            if (chat.rating == null) {
                chat.rating = new HashMap<>();
                chat.rating.put(userId, "0");
                chat.rating.put(chat.opponent_user_id, "0");
            } else {
                if (!chat.rating.containsKey(userId)) {
                    chat.rating.put(userId, "0");
                }
            }

            String otherUserId = "";
            for (String id : chat.user_type.keySet()) {
                if (!id.equals(userId)) {
                    otherUserId = id;
                }
            }
            chat.opponent_user_id = otherUserId;

        }

        return chat;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.chat_dialog_id);
        dest.writeString(this.last_message);
        dest.writeSerializable(this.last_message_time);
        dest.writeString(this.last_message_sender_id);
        dest.writeString(this.last_message_id);
        dest.writeString(this.participant_ids);
        dest.writeSerializable(this.unread_count);
        dest.writeSerializable(this.name);
        dest.writeSerializable(this.profile_pic);
        dest.writeSerializable(this.delete_dialog_time);
        dest.writeSerializable(this.block_status);
        dest.writeSerializable(this.user_type);
        dest.writeSerializable(this.rating);
        dest.writeSerializable(this.delete_outer_dialog);
        dest.writeString(this.opponent_user_id);
        dest.writeSerializable(this.message_rating_count);
        dest.writeSerializable(this.last_message_type);
        dest.writeSerializable(this.last_message_data);
    }
}
