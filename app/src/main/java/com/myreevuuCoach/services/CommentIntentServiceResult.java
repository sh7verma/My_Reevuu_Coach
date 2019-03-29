package com.myreevuuCoach.services;


import com.myreevuuCoach.models.VideoModel;

public class CommentIntentServiceResult {
    int value = -1;
    int status = 1; //1=like unlike , 2 = comment , Item= 3 , 4 DELETED
    VideoModel.ResponseBean response;

    public CommentIntentServiceResult(int status, int value, VideoModel.ResponseBean response) {
        this.status = status;
        this.value = value;
        this.response = response;
    }

    public VideoModel.ResponseBean getItem() {
        return response;
    }

    public int getValue() {
        return value;
    }

    public int getStatus() {
        return status;
    }
}
