package com.myreevuuCoach.models;

import java.util.ArrayList;

/**
 * Created by dev on 5/12/18.
 */

public class RequestListModel extends ErrorModelJava {

    private int code;

    private ArrayList<RequestsModel.ResponseBean> response;

    public ArrayList<RequestsModel.ResponseBean> getResponse() {
        return response;
    }

    public void setResponse(ArrayList<RequestsModel.ResponseBean> response) {
        this.response = response;
    }

    public int getCode() {

        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
