package com.myreevuuCoach.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by dev on 21/11/18.
 */

public class VideoModel extends ErrorModelJava {

    /**
     * response : {"id":50,"user_type":1,"profile_pic":"https://s3.ap-south-1.amazonaws.com/kittydev/myreevuu/profile_pics/42ab941ea0b3b9e42d3c045d59387516.jpg","sport_id":1,"sport":"Tennis","privacy":0,"improvement":[],"url":"https://s3.ap-south-1.amazonaws.com/kittydev/myreevuu/videos/f3885d46919457f1f89d62060695d32a.mp4","thumbnail":"https://s3.ap-south-1.amazonaws.com/kittydev/myreevuu/thumbnails/d5e0381ecd9df2764f611abff54960fb.jpg","fullname":"Shuhham","title":"title","description":"description","views":1,"created_at":"2018-11-21 09:19"}
     * code : 111
     */

    private ResponseBean response;
    private int code;

    public ResponseBean getResponse() {
        return response;
    }

    public void setResponse(ResponseBean response) {
        this.response = response;
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
         * id : 50
         * user_type : 1
         * profile_pic : https://s3.ap-south-1.amazonaws.com/kittydev/myreevuu/profile_pics/42ab941ea0b3b9e42d3c045d59387516.jpg
         * sport_id : 1
         * sport : Tennis
         * privacy : 0
         * improvement : []
         * url : https://s3.ap-south-1.amazonaws.com/kittydev/myreevuu/videos/f3885d46919457f1f89d62060695d32a.mp4
         * thumbnail : https://s3.ap-south-1.amazonaws.com/kittydev/myreevuu/thumbnails/d5e0381ecd9df2764f611abff54960fb.jpg
         * fullname : Shuhham
         * title : title
         * description : description
         * views : 1
         * created_at : 2018-11-21 09:19
         */

        private int id;
        private int user_id;
        private int user_type;
        private String profile_pic;
        private int sport_id;
        private String sport;
        private int privacy;
        private String url;
        private String thumbnail;
        private String fullname;
        private String title;
        private String description;
        private int views;
        private String created_at;
        private int post_type; //Video = 2,Image = 1
        private int likes_count;
        private int liked;
        private int comments_count;
        private List<OptionsModel> improvement;

        public ResponseBean() {
        }

        protected ResponseBean(Parcel in) {
            this.id = in.readInt();
            this.user_id = in.readInt();
            this.user_type = in.readInt();
            this.profile_pic = in.readString();
            this.sport_id = in.readInt();
            this.sport = in.readString();
            this.privacy = in.readInt();
            this.url = in.readString();
            this.thumbnail = in.readString();
            this.fullname = in.readString();
            this.title = in.readString();
            this.description = in.readString();
            this.views = in.readInt();
            this.created_at = in.readString();
            this.post_type = in.readInt();
            this.likes_count = in.readInt();
            this.liked = in.readInt();
            this.comments_count = in.readInt();
            this.improvement = in.createTypedArrayList(OptionsModel.CREATOR);
        }

        public int getPost_type() {
            return post_type;
        }

        public void setPost_type(int post_type) {
            this.post_type = post_type;
        }

        public int getLikes_count() {
            return likes_count;
        }

        public void setLikes_count(int likes_count) {
            this.likes_count = likes_count;
        }

        public int getLiked() {
            return liked;
        }

        public void setLiked(int liked) {
            this.liked = liked;
        }

        public int getComments_count() {
            return comments_count;
        }

        public void setComments_count(int comments_count) {
            this.comments_count = comments_count;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUser_type() {
            return user_type;
        }

        public void setUser_type(int user_type) {
            this.user_type = user_type;
        }

        public String getProfile_pic() {
            return profile_pic;
        }

        public void setProfile_pic(String profile_pic) {
            this.profile_pic = profile_pic;
        }

        public int getSport_id() {
            return sport_id;
        }

        public void setSport_id(int sport_id) {
            this.sport_id = sport_id;
        }

        public String getSport() {
            return sport;
        }

        public void setSport(String sport) {
            this.sport = sport;
        }

        public int getPrivacy() {
            return privacy;
        }

        public void setPrivacy(int privacy) {
            this.privacy = privacy;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getViews() {
            return views;
        }

        public void setViews(int views) {
            this.views = views;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public List<OptionsModel> getImprovement() {
            return improvement;
        }

        public void setImprovement(List<OptionsModel> improvement) {
            this.improvement = improvement;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeInt(this.user_id);
            dest.writeInt(this.user_type);
            dest.writeString(this.profile_pic);
            dest.writeInt(this.sport_id);
            dest.writeString(this.sport);
            dest.writeInt(this.privacy);
            dest.writeString(this.url);
            dest.writeString(this.thumbnail);
            dest.writeString(this.fullname);
            dest.writeString(this.title);
            dest.writeString(this.description);
            dest.writeInt(this.views);
            dest.writeString(this.created_at);
            dest.writeInt(this.post_type);
            dest.writeInt(this.likes_count);
            dest.writeInt(this.liked);
            dest.writeInt(this.comments_count);
            dest.writeTypedList(this.improvement);
        }
    }
}
