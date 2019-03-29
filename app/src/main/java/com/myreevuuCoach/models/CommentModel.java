package com.myreevuuCoach.models;

import java.util.List;

public class CommentModel extends BaseModel {
    private int code;
    private int comments_count;
    private List<ResponseBean> response;

    public int getComments_count() {
        return comments_count;
    }

    public int getCode() {
        return code;
    }

    public List<ResponseBean> getResponse() {
        return response;
    }


    public static class ResponseBean {
        /**
         * "id": 25,
         * "article_id": 1,
         * "user_id": 287,
         * "username": "navi666",
         * "name": "navneet sharmaaaaaa",
         * "profile_pic": "https://s3-ap-southeast-1.amazonaws.com/myreevu-test/myreevuu/profile_pics/c0667801af338ae2282a7f8a3182420f.jpg",
         * "comment": "hi",
         * "created_at": "2019-01-18 05:41"
         */

        private int id;
        private int article_id;
        private int user_id;
        private String username;
        private String name;
        private String profile_pic;
        private String comment;
        private String created_at;
        private boolean isselected;

        public boolean isSelected() {
            return isselected;
        }

        public void setSelected(boolean selected) {
            this.isselected = selected;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getArticle_id() {
            return article_id;
        }

        public void setArticle_id(int article_id) {
            this.article_id = article_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProfile_pic() {
            return profile_pic;
        }

        public void setProfile_pic(String profile_pic) {
            this.profile_pic = profile_pic;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
    }

}
