package com.myreevuuCoach.models;

public class CommentModelSingle extends BaseModel{
    private int code;
    private ResponseBean response;

    public int getCode() {
        return code;
    }

    public ResponseBean getResponse() {
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

        public int getId() {
            return id;
        }

        public int getArticle_id() {
            return article_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public String getUsername() {
            return username;
        }

        public String getName() {
            return name;
        }

        public String getProfile_pic() {
            return profile_pic;
        }

        public String getComment() {
            return comment;
        }

        public String getCreated_at() {
            return created_at;
        }
    }
}
