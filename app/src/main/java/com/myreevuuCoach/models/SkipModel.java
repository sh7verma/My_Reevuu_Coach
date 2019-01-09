package com.myreevuuCoach.models;

/**
 * Created by dev on 30/11/18.
 */

public class SkipModel extends ErrorModelJava {

    /**
     * response : {"message":"successfully skip the tips","code":111}
     */

    private ErrorBean response;

    public ErrorBean getResponse() {
        return response;
    }

    public void setResponse(ErrorBean response) {
        this.response = response;
    }
}
