package com.myreevuuCoach.interfaces;

/**
 * Created by dev on 12/11/18.
 */

public interface InterConst {


    int FRAG_NULL = -1;
    int FRAG_HOME = 0;
    int FRAG_CHAT = 1;
    int FRAG_REEVUU = 2;
    int FRAG_PROFILE = 3;
    String DISPLAY_TIP = "displayTip";
    String DISPLAY_ANIMATION = "displayAnimation";
    String EMAIL = "email";
    String ACCESS_TOKEN = "accessToken";
    String EMAIL_VERIFIED = "emailVerified";
    String PROFILE_STATUS = "profileStatus";
    String ID = "id";
    String PHONE_NUMBER = "phone_number";
    String GENDER = "gender";
    String GENDER_STATUS = "gender_status";
    String NAME = "name";
    String PROFILE_PIC = "profile_pric";
    String REVIEW_REQUEST_ID = "reviewRequestId";
    String VIDEO_URL = "VIDEO_URL";
    String RESPONSE = "response";
    String USER_NAME = "user_name";
    String USER_TYPE = "user_type";
    String MALE = "Male";
    String FEMALE = "Female";
    String OTHER = "Other";
    String PRIVACY_PUBLIC = "0";
    String PRIVACY_PRIVATE = "1";
    int PROFILE_INFO_FRAG = 0;
    int PROFILE_VIDEO_FRAG = 1;
    int PROFILE_PAYMENT_FRAG = 2;
    String PROFILE_VIDEO_POSITION = "profile_video_position";
    String REGEX_CERTIFICATED = "#split#";
    String DATA_REMOVED = "#split#_removed";

    String BROADCAST_VIDEO_FRAG_DELETE = "android.profilevideoFragment.reciver_delete";
    String BROADCAST_HOME_FRAG_DELETE = "android.homevideoFragment.reciver_delete";
    String BROADCAST_SEARCH_VIDEO_DELETE = "android.searchvideoActivity.reciver_delete";

    String BROADCAST_MY_FEED_VIDEO_DELETE = "android.myFeedDelete.reciver_delete";

    String HOME_VIDEO_POSITION = "home_video_positon";
    String EDIT_INFO = "edit_info";
    String INTEND_EXTRA = "intedn_extra";
    String FORWARD_MESSAGE = "intent_forward_message";
    String DOLLAR = "$";
    String INTEND_AMOUNT = "amount";
    String INTEND_ACCOUNT = "account";
    String BROADCAST_VIDEO_STOP_RECIVER = "BROADCAST_VIDEO_STOP_RECIVER";
    String FINISH_CONVERSATION_ACTIVITY = "FINISH_CONVERSATION_ACTIVITY";
    String CAMERA_PERMISSION = "camera.permission.";
    int INVALID_ACCESS = 301;
    int CAMERA_DURATION_MAX_TIME = 60 * 15;
    Object APP_NAME = "MyReevuu";
    String NOTIFICATION_CHANNEL = "1";
    String EMAIL_PUSH_STATUS = "email_push_status";
    String SPORTS_RESPONSE = "sports_response";

    String REEVUU_REQUESTS_REQUESTED = "0";
    String REEVUU_REQUESTS_ACCEPTED = "1";
    String REEVUU_REQUESTS_DECLINE = "3";
    String REEVUU_REQUESTS_REVIEWED = "2";

    int FRAG_REEVUU_REQUEST = 0;
    int FRAG_REEVUU_ACCEPTED = 1;
    int FRAG_REEVUU_REVIEWED = 2;
    String NEW_EMAIL = "new_email";
    String PROFILE_APPROVED = "profile_approved";
    String BACKGROUND = "Background";
    int APP_ONLINE = 0;
    int APP_OFFLINE = 1;


    String WEBVIEW_TYPE = "WEBVIEW_TYPE";
    String WEBVIEW_URL = "WEBVIEW_URL";
    String FEED_SEARCH_VIDEO_POSITION = "feed_search_video_position";
    int REQUEST_NOT_EXIST = 506;
    String NEW_REQUEST_COUNT = "new_request_count";
    String SERVICE_RUNNING = "service_running";
    String APP_ICON_BADGE_COUNT = "app_icon_batch_count";
    String UNREAD_NOTIFICATION_DOT = "UNREAD_NOTIFICATION_DOT";
    String APP_ADMIN_UNREAD_COUNT = "APP_ADMIN_UNREAD_COUNT";
    String APP_ADMIN_NAME = "CONTACT US";
    String APP_ADMIN_DEFAULT_MESSAGE = "Ask us anything or share your feedback. We'll get back to you shortly!";
    String APP_ADMIN_ID = "a0";
    String CLEAR_CHAT = "1";
    String DELETE_DIALOG = "2";
    String NotificationID = "NotificationID";
    String IN_PROCESSING = "0";
    String BROADCAST_VIDEO_PROCESSED = "video_processed";
    String BROADCAST_VIDEO_ADDED_RECIVER = "BROADCAST_VIDEO_ADDED_RECIVER";
    String FCM_TOKEN = "deviceToken";
    String REFERRAL_CODE = "referralCode";
    String REFERRED_BY = "REFERRED_BY";
    String FIREBASE_TOPIC_1 = "android_coach";
    String FIREBASE_TOPIC_2 = "android_both";
    String FIREBASE_TOPIC_3 = "android_admin_articles";
    int POST_TYPE_IMAGE = 1;
    int POST_TYPE_VIDEO = 2;
    int REQ_BROADCAST = 1009;
    String BROADCAST_PROFILE_APPROVED = "BROADCAST_PROFILE_APPROVED";
    String VIDEO_CONTAINER_HEIGHT = "VIDEO_CONTAINER_HEIGHT";
    int VIDEO_ORIENTATION_VERTICLE = 1;
    int VIDEO_ORIENTATION_LANDSCAPE = 2;

    /*WebView*/
    enum Webview {
        ABOUT, TERM_CONDITION, FAQs, PRIVACY
    }

    class EMAIL_PUSH_STATUS {
    }

    class PUSH_TYPE_EMAIL_VERIFY {
    }


    /*WebView*/

}
