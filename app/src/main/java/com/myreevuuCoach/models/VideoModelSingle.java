package com.myreevuuCoach.models;

import java.util.List;

public class VideoModelSingle extends ErrorModelJava {

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

    public static class ResponseBean {
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
        private List<OptionsModel> improvement;

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
    }
}
