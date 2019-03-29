package com.myreevuuCoach.models;

import java.util.List;

/**
 * Created by dev on 26/2/19.
 */

public class SportsArrayModel extends BaseModel{


    /**
     * response : [{"id":1,"name":"Baseball","description":"Lorem ipsum","image":"https://s3-ap-southeast-1.amazonaws.com/myreevu-test/myreevuu/sports/ic_baseball.png"},{"id":2,"name":"wrestling","description":"Lorem ipsum","image":"https://s3-ap-southeast-1.amazonaws.com/myreevu-test/myreevuu/sports/ic_wrestling.png"},{"id":3,"name":"Tennis Test","description":"tennis sport","image":"https://s3-ap-southeast-1.amazonaws.com/myreevu-test/myreevuu/sports/769747cf4824d89df21961f215c5f59a.png"},{"id":7,"name":"Cricket Test","description":"cricket","image":"https://s3-ap-southeast-1.amazonaws.com/myreevu-test/myreevuu/sports/608dfd55276e908f0ac9b1a4a9ed5fba.png"}]
     * code : 111
     */

    private int code;
    private List<ResponseBean> response;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<ResponseBean> getResponse() {
        return response;
    }

    public void setResponse(List<ResponseBean> response) {
        this.response = response;
    }

    public static class ResponseBean {
        /**
         * id : 1
         * name : Baseball
         * description : Lorem ipsum
         * image : https://s3-ap-southeast-1.amazonaws.com/myreevu-test/myreevuu/sports/ic_baseball.png
         */

        private int id;
        private String name;
        private String description;
        private String image;

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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
