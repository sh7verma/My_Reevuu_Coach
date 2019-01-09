package com.myreevuuCoach.firebase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by dev on 17/12/18.
 */

public class FirebaseChatConstants {

    public final static int STATUS_MESSAGE_PENDING = 0;
    public final static int STATUS_MESSAGE_SENT = 1;
    public final static int STATUS_MESSAGE_DELIVERED = 2;
    public final static int STATUS_MESSAGE_SEEN = 3;

    ////////////////
    public final static String chat_dialog_id = "chat_dialog_id";
    public final static String message_id = "message_id";
    public final static String message = "message";
    public final static String message_type = "message_type";
    public final static String message_time = "message_time";
    public final static String firebase_message_time = "firebase_message_time";
    public final static String sender_id = "sender_id";
    public final static String message_status = "message_status";
    public final static String attachment_url = "attachment_url";
    public final static String message_deleted = "message_deleted";
    public final static String favourite_message = "favourite_message";
    public final static String receiver_id = "receiver_id";
    //////////////

    public final static String TYPE_TEXT = "1";
    public final static String TYPE_IMAGE = "2";
    public final static String TYPE_VIDEO = "3";
    public final static String TYPE_DOCUMENT = "4";
    public final static String TYPE_NOTES = "5";
    public final static String TYPE_AUDIO = "6";
    ////////////
    public final static String DEFAULT_MESSAGE_REGEX = "<*&^(Reevuu)^&*>";
    public final static String USERS = "Users";
    public final static String CHATS = "Chats";
    public final static String MESSAGES = "Messages";
    public final static String READ_STATUS = "ReadStatus";
    public final static String NOTIFICATIONS = "Notifications";
    public final static Long ONLINE_LONG = 123L;
    public final static String ONLINE = "Online";
    public final static int NOTIFICATION_TEXT_LENGTH = 30;
    public final static int SHOW_TEXT_LENGTH = 1000;
    public final static String FILE_SUCCESS = "2";
    public final static String FILE_UPLOADING = "1";
    public final static String FILE_EREROR = "0";
    public final static String user_id = "id";

    public final static int LOAD_MORE_VALUE = 20;
    public static final int TYPE_COACH = 1;
    public static final int TYPE_ATHLETE= 2;
    public static final int TYPE_ADMIN= 3;


    public static long getLocalTime(long time) {
        long localTime = 0;
        String dateValue = "";
        try {
            SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Calendar calUtc = Calendar.getInstance();

            SimpleDateFormat localFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            localFormat.setTimeZone(TimeZone.getDefault());
            Calendar calLocal = Calendar.getInstance();

            calUtc.setTimeInMillis(time);
            dateValue = utcFormat.format(calUtc.getTime());
            Date value = utcFormat.parse(dateValue);

            dateValue = localFormat.format(value);
            Date localvalue = localFormat.parse(dateValue);
            localTime = localvalue.getTime();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return localTime;
    }

}
