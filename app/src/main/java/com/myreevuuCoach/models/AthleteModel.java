package com.myreevuuCoach.models;

/**
 * Created by dev on 7/12/18.
 */

public class AthleteModel extends ErrorModelJava {

    /**
     * response : {"id":287,"name":"navneet sharma","username":"navi666","profile_pic":"https://s3.ap-south-1.amazonaws.com/kittydev/myreevuu/profile_pics/c0667801af338ae2282a7f8a3182420f.jpg","gender":0,"user_type":2,"sport_info":{"id":329,"sport_id":1,"experience":15,"level":3,"sport_level_name":"International","state_id":null,"zip_code":"1515","name":"Tennis","description":"Select Tennis if you want ReeVuu on Tennis videos"},"coach_info":{},"rating":4,"dob":"27-11-2013","height":"7.5","weight":"60","dexterity":"Right","is_favourite":0}
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
         * id : 287
         * name : navneet sharma
         * username : navi666
         * profile_pic : https://s3.ap-south-1.amazonaws.com/kittydev/myreevuu/profile_pics/c0667801af338ae2282a7f8a3182420f.jpg
         * gender : 0
         * user_type : 2
         * sport_info : {"id":329,"sport_id":1,"experience":15,"level":3,"sport_level_name":"International","state_id":null,"zip_code":"1515","name":"Tennis","description":"Select Tennis if you want ReeVuu on Tennis videos"}
         * coach_info : {}
         * rating : 4
         * dob : 27-11-2013
         * height : 7.5
         * weight : 60
         * dexterity : Right
         * is_favourite : 0
         */

        private int id;
        private String name;
        private String username;
        private String profile_pic;
        private int gender;
        private int user_type;
        private SportInfoBean sport_info;
        private CoachInfoBean coach_info;
        private int rating;
        private String dob;
        private String height;
        private String weight;
        private String dexterity;
        private int is_favourite;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getProfile_pic() {
            return profile_pic;
        }

        public void setProfile_pic(String profile_pic) {
            this.profile_pic = profile_pic;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public int getUser_type() {
            return user_type;
        }

        public void setUser_type(int user_type) {
            this.user_type = user_type;
        }

        public SportInfoBean getSport_info() {
            return sport_info;
        }

        public void setSport_info(SportInfoBean sport_info) {
            this.sport_info = sport_info;
        }

        public CoachInfoBean getCoach_info() {
            return coach_info;
        }

        public void setCoach_info(CoachInfoBean coach_info) {
            this.coach_info = coach_info;
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getDexterity() {
            return dexterity;
        }

        public void setDexterity(String dexterity) {
            this.dexterity = dexterity;
        }

        public int getIs_favourite() {
            return is_favourite;
        }

        public void setIs_favourite(int is_favourite) {
            this.is_favourite = is_favourite;
        }

        public static class SportInfoBean {

            private int id;
            private int sport_id;
            private int experience;
            private int level;
            private String sport_level_name;
            private Object state_id;
            private String zip_code;
            private String name;
            private String description;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getSport_id() {
                return sport_id;
            }

            public void setSport_id(int sport_id) {
                this.sport_id = sport_id;
            }

            public int getExperience() {
                return experience;
            }

            public void setExperience(int experience) {
                this.experience = experience;
            }

            public int getLevel() {
                return level;
            }

            public void setLevel(int level) {
                this.level = level;
            }

            public String getSport_level_name() {
                return sport_level_name;
            }

            public void setSport_level_name(String sport_level_name) {
                this.sport_level_name = sport_level_name;
            }

            public Object getState_id() {
                return state_id;
            }

            public void setState_id(Object state_id) {
                this.state_id = state_id;
            }

            public String getZip_code() {
                return zip_code;
            }

            public void setZip_code(String zip_code) {
                this.zip_code = zip_code;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }
        }


        public static class CoachInfoBean {
        }
    }
}
