package com.example.xiner.entity;

import java.io.Serializable;

/**
 * Created by xiner on 14-12-22.
 */
public class DetailShareEntity implements Serializable{
   private String subject;
   private String grade;
    private String time;
    private String detail;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public int getCollectionnum() {
        return collectionnum;
    }

    public void setCollectionnum(int collectionnum) {
        this.collectionnum = collectionnum;
    }

    public String getZannum() {
        return zannum;
    }

    public void setZannum(String zannum) {
        this.zannum = zannum;
    }

    public String getCommentnum() {
        return commentnum;
    }

    public void setCommentnum(String commentnum) {
        this.commentnum = commentnum;
    }

    private String picture;
    private String filepath;
    private int collectionnum;
    private String zannum;
    private String commentnum;
}
