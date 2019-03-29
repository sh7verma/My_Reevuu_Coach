package com.myreevuuCoach.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dev on 28/11/18.
 */

public class TransactionModel extends ErrorModelJava {


    /**
     * response : [{"id":3,"reviewRequestId":118,"payment":"10.245","created_at":"2018-11-27 07:25"}]
     * code : 111
     */

    private int code;
    private ArrayList<ResponseBean> response;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ArrayList<ResponseBean> getResponse() {
        return response;
    }

    public void setResponse(ArrayList<ResponseBean> response) {
        this.response = response;
    }

    public static class ResponseBean {

        /**
         * id : 3
         * reviewRequestId : 118
         * payment : 10.245
         * created_at : 2018-11-27 07:25
         */

        private int id;
        private int review_request_id;
        private String payment;
        private String created_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getReview_request_id() {
            return review_request_id;
        }

        public void setReview_request_id(int review_request_id) {
            this.review_request_id = review_request_id;
        }

        public String getPayment() {
            return payment;
        }

        public void setPayment(String payment) {
            this.payment = payment;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
    }
}
