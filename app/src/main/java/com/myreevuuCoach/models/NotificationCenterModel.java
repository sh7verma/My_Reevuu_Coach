package com.myreevuuCoach.models;

import java.util.ArrayList;

/**
 * Created by dev on 3/1/19.
 */

public class NotificationCenterModel extends ErrorModelJava {

    ArrayList<NotificationResponse> response;
    int code;

    public ArrayList<NotificationResponse> getResponse() {
        return response;
    }


    public class NotificationResponse {
        public int id;
        public String user_id;
        public String name;
        public String profile_pic;
        public int video_id;
        public int read_status; //0 unread
        public int review_request_id;
        public String message;
        public String push_type;
        public String created_at;
        public String updated_at;
        public void setRead_status(int read_status) {
            this.read_status = read_status;
        }

        public int getNotificationId() {
            return id;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getName() {
            return name;
        }

        public String getProfile_pic() {
            return profile_pic;
        }

        public int getVideo_id() {
            return video_id;
        }

        public int getRead_status() {
            return read_status;
        }

        public int getReview_request_id() {
            return review_request_id;
        }

        public String getMessage() {
            return message;
        }

        public String getPush_type() {
            return push_type;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }
    }
}
