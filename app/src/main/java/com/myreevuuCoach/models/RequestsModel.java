package com.myreevuuCoach.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dev on 4/12/18.
 */

public class RequestsModel extends ErrorModelJava {

    /**
     * response : [{"id":202,"video_id":270,"user_id":287,"name":"navneet sharma","review_status":0,"profile_pic":"https://s3.ap-south-1.amazonaws.com/kittydev/myreevuu/profile_pics/c0667801af338ae2282a7f8a3182420f.jpg","created_at":"2018-12-04 12:23","video":{"id":270,"user_id":287,"user_type":2,"profile_pic":"https://s3.ap-south-1.amazonaws.com/kittydev/myreevuu/profile_pics/c0667801af338ae2282a7f8a3182420f.jpg","sport_id":1,"sport":"Tennis","privacy":0,"improvement":[{"id":4,"name":"Ground Strokes","color":4},{"id":6,"name":"Game Strategy","color":6}],"url":"https://s3.ap-south-1.amazonaws.com/kittydev/myreevuu/videos/ef60b482898095ff262d8db57b3ae9c4.mp4","thumbnail":"https://s3.ap-south-1.amazonaws.com/kittydev/myreevuu/thumbnails/2beb8885487440e30402856f2cf3c053.jpg","fullname":"navneet sharma","title":"Wetttt","description":"fdffff","views":1,"is_favourite":0,"created_at":"2018-12-04 12:23"},"remaining_time":"23:07:30","reviewed_at":"2018-12-04 12:23"},{"id":203,"video_id":271,"user_id":287,"name":"navneet sharma","review_status":0,"profile_pic":"https://s3.ap-south-1.amazonaws.com/kittydev/myreevuu/profile_pics/c0667801af338ae2282a7f8a3182420f.jpg","created_at":"2018-12-04 12:25","video":{"id":271,"user_id":287,"user_type":2,"profile_pic":"https://s3.ap-south-1.amazonaws.com/kittydev/myreevuu/profile_pics/c0667801af338ae2282a7f8a3182420f.jpg","sport_id":1,"sport":"Tennis","privacy":0,"improvement":[{"id":4,"name":"Ground Strokes","color":4},{"id":6,"name":"Game Strategy","color":6}],"url":"https://s3.ap-south-1.amazonaws.com/kittydev/myreevuu/videos/663cc1c278fa6f0e201df2e3821bf72c.mp4","thumbnail":"https://s3.ap-south-1.amazonaws.com/kittydev/myreevuu/thumbnails/705247cac7194161b5db87d8cbd82fff.jpg","fullname":"navneet sharma","title":"Hcychhc","description":"vgvggh","views":1,"is_favourite":0,"created_at":"2018-12-04 12:25"},"remaining_time":"23:09:40","reviewed_at":"2018-12-04 12:25"}]
     * code : 111
     */

    private int code;

    @SerializedName("response")
    private ResponseBean response;

    public ResponseBean getResponse() {
        return response;
    }

    public void setResponse(ResponseBean responseX) {
        this.response = responseX;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class ResponseBean implements Parcelable {
        public static final Creator<ResponseBean> CREATOR = new Creator<ResponseBean>() {
            @Override
            public ResponseBean createFromParcel(Parcel source) {
                return new ResponseBean(source);
            }

            @Override
            public ResponseBean[] newArray(int size) {
                return new ResponseBean[size];
            }
        };
        /**
         * id : 202
         * video_id : 270
         * user_id : 287
         * name : navneet sharma
         * review_status : 0
         * profile_pic : https://s3.ap-south-1.amazonaws.com/kittydev/myreevuu/profile_pics/c0667801af338ae2282a7f8a3182420f.jpg
         * created_at : 2018-12-04 12:23
         * video : {"id":270,"user_id":287,"user_type":2,"profile_pic":"https://s3.ap-south-1.amazonaws.com/kittydev/myreevuu/profile_pics/c0667801af338ae2282a7f8a3182420f.jpg","sport_id":1,"sport":"Tennis","privacy":0,"improvement":[{"id":4,"name":"Ground Strokes","color":4},{"id":6,"name":"Game Strategy","color":6}],"url":"https://s3.ap-south-1.amazonaws.com/kittydev/myreevuu/videos/ef60b482898095ff262d8db57b3ae9c4.mp4","thumbnail":"https://s3.ap-south-1.amazonaws.com/kittydev/myreevuu/thumbnails/2beb8885487440e30402856f2cf3c053.jpg","fullname":"navneet sharma","title":"Wetttt","description":"fdffff","views":1,"is_favourite":0,"created_at":"2018-12-04 12:23"}
         * remaining_time : 23:07:30
         * reviewed_at : 2018-12-04 12:23
         */

        private int id;
        private int video_id;
        private int user_id;
        private String name;
        private int review_status;
        private String profile_pic;
        private String created_at;
        private FeedModel.Response video;
        private String remaining_time;
        private String reviewed_at;
        private int is_accepted;
        private int is_last_request;

        public ResponseBean() {
        }

        protected ResponseBean(Parcel in) {
            this.id = in.readInt();
            this.video_id = in.readInt();
            this.user_id = in.readInt();
            this.name = in.readString();
            this.review_status = in.readInt();
            this.profile_pic = in.readString();
            this.created_at = in.readString();
            this.video = in.readParcelable(FeedModel.Response.class.getClassLoader());
            this.remaining_time = in.readString();
            this.reviewed_at = in.readString();
            this.is_accepted = in.readInt();
            this.is_last_request = in.readInt();
        }

        public int getIs_accepted() {
            return is_accepted;
        }

        public void setIs_accepted(int is_accepted) {
            this.is_accepted = is_accepted;
        }

        public int getIs_last_request() {
            return is_last_request;
        }

        public void setIs_last_request(int is_last_request) {
            this.is_last_request = is_last_request;
        }

        public FeedModel.Response getVideo() {
            return video;
        }

        public void setVideo(FeedModel.Response video) {
            this.video = video;
        }

        public int getId() {

            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getVideo_id() {
            return video_id;
        }

        public void setVideo_id(int video_id) {
            this.video_id = video_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getReview_status() {
            return review_status;
        }

        public void setReview_status(int review_status) {
            this.review_status = review_status;
        }

        public String getProfile_pic() {
            return profile_pic;
        }

        public void setProfile_pic(String profile_pic) {
            this.profile_pic = profile_pic;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getRemaining_time() {
            return remaining_time;
        }

        public void setRemaining_time(String remaining_time) {
            this.remaining_time = remaining_time;
        }

        public String getReviewed_at() {
            return reviewed_at;
        }

        public void setReviewed_at(String reviewed_at) {
            this.reviewed_at = reviewed_at;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeInt(this.video_id);
            dest.writeInt(this.user_id);
            dest.writeString(this.name);
            dest.writeInt(this.review_status);
            dest.writeString(this.profile_pic);
            dest.writeString(this.created_at);
            dest.writeParcelable(this.video, flags);
            dest.writeString(this.remaining_time);
            dest.writeString(this.reviewed_at);
            dest.writeInt(this.is_accepted);
            dest.writeInt(this.is_last_request);
        }
    }
}
