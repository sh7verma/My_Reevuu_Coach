package com.myreevuuCoach.firebase;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.HashMap;

/**
 * Created by dev on 17/12/18.
 */

public class MessagesModel implements Parcelable {

    public static final Parcelable.Creator<MessagesModel> CREATOR = new Parcelable.Creator<MessagesModel>() {
        @Override
        public MessagesModel createFromParcel(Parcel source) {
            return new MessagesModel(source);
        }

        @Override
        public MessagesModel[] newArray(int size) {
            return new MessagesModel[size];
        }
    };
    public String message_id;
    public String message;
    public String message_type;
    public String message_time;
    public Long firebase_message_time;
    public String chat_dialog_id;
    public String sender_id;
    public int message_status;
    public String attachment_url;
    public HashMap<String, String> message_deleted;
    public HashMap<String, String> favourite_message;
    public boolean is_header;
    public String attachment_progress;
    public String attachment_path;
    public String attachment_status;
    public String show_message_datetime;
    public String show_header_text;
    public String custom_data;
    public String receiver_id;

    public MessagesModel() {
    }

    protected MessagesModel(Parcel in) {
        this.message_id = in.readString();
        this.message = in.readString();
        this.message_type = in.readString();
        this.message_time = in.readString();
        this.firebase_message_time = (Long) in.readValue(Long.class.getClassLoader());
        this.chat_dialog_id = in.readString();
        this.sender_id = in.readString();
        this.message_status = in.readInt();
        this.attachment_url = in.readString();
        this.message_deleted = (HashMap<String, String>) in.readSerializable();
        this.favourite_message = (HashMap<String, String>) in.readSerializable();
        this.is_header = in.readByte() != 0;
        this.attachment_progress = in.readString();
        this.attachment_path = in.readString();
        this.attachment_status = in.readString();
        this.show_message_datetime = in.readString();
        this.show_header_text = in.readString();
        this.custom_data = in.readString();
        this.receiver_id = in.readString();
    }

    public static MessagesModel parseMessage(DataSnapshot dataSnapshot) {
//        MessagesModel msg = dataSnapshot.getValue(MessagesModel.class);
        MessagesModel msg = null;
        if (!TextUtils.isEmpty(dataSnapshot.child(FirebaseChatConstants.chat_dialog_id).getValue(String.class))) {
            msg = new MessagesModel();
            msg.message_id = dataSnapshot.child(FirebaseChatConstants.message_id).getValue(String.class);
            msg.message = dataSnapshot.child(FirebaseChatConstants.message).getValue(String.class);
            msg.message_type = dataSnapshot.child(FirebaseChatConstants.message_type).getValue(String.class);
            long time = FirebaseChatConstants.getLocalTime(Long.parseLong(dataSnapshot.child(FirebaseChatConstants.message_time).getValue(String.class)));
            msg.message_time = "" + time;  //  Comment for show local time
            msg.firebase_message_time = dataSnapshot.child(FirebaseChatConstants.firebase_message_time).getValue(Long.class);
            msg.chat_dialog_id = dataSnapshot.child(FirebaseChatConstants.chat_dialog_id).getValue(String.class);
            msg.sender_id = dataSnapshot.child(FirebaseChatConstants.sender_id).getValue(String.class);
            if (dataSnapshot.child(FirebaseChatConstants.message_status).getValue(Integer.class) == null) {
                msg.message_status = FirebaseChatConstants.STATUS_MESSAGE_SENT;
            } else {
                msg.message_status = dataSnapshot.child(FirebaseChatConstants.message_status).getValue(Integer.class);
            }
            msg.attachment_url = dataSnapshot.child(FirebaseChatConstants.attachment_url).getValue(String.class);

            GenericTypeIndicator<HashMap<String, String>> gtDelete = new GenericTypeIndicator<HashMap<String, String>>() {
            };
            msg.message_deleted = dataSnapshot.child(FirebaseChatConstants.message_deleted).getValue(gtDelete);
            if (msg.message_deleted == null) {
                msg.message_deleted = new HashMap<>();
            }

            GenericTypeIndicator<HashMap<String, String>> gtFavourite = new GenericTypeIndicator<HashMap<String, String>>() {
            };
            msg.favourite_message = dataSnapshot.child(FirebaseChatConstants.favourite_message).getValue(gtFavourite);
            if (msg.favourite_message == null) {
                msg.favourite_message = new HashMap<>();
            }

            msg.receiver_id = dataSnapshot.child(FirebaseChatConstants.receiver_id).getValue(String.class);

            if (TextUtils.isEmpty(msg.receiver_id)) {
                msg.receiver_id = "";
            }

            msg.is_header = false;
            msg.attachment_progress = "0";
            msg.attachment_path = "";
            msg.attachment_status = "";
            msg.show_message_datetime = "";
            msg.show_header_text = "";
            msg.custom_data = "";
        }
        return msg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.message_id);
        dest.writeString(this.message);
        dest.writeString(this.message_type);
        dest.writeString(this.message_time);
        dest.writeValue(this.firebase_message_time);
        dest.writeString(this.chat_dialog_id);
        dest.writeString(this.sender_id);
        dest.writeInt(this.message_status);
        dest.writeString(this.attachment_url);
        dest.writeSerializable(this.message_deleted);
        dest.writeSerializable(this.favourite_message);
        dest.writeByte(this.is_header ? (byte) 1 : (byte) 0);
        dest.writeString(this.attachment_progress);
        dest.writeString(this.attachment_path);
        dest.writeString(this.attachment_status);
        dest.writeString(this.show_message_datetime);
        dest.writeString(this.show_header_text);
        dest.writeString(this.custom_data);
        dest.writeString(this.receiver_id);
    }
}
