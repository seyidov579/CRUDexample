package com.softportfolio.crudwithretrofit.Model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class Heroes {

    @SerializedName("name")
    private String name;

    @SerializedName("realname")
    private String realname;

    @SerializedName("team")
    private String team;

    @SerializedName("firstappearance")
    private String firstappearance;

    @SerializedName("createdby")
    private String createdby;

    @SerializedName("publisher")
    private String publisher;

    @SerializedName("image")
    @Nullable
    private byte[] imageurl;

    @SerializedName("bio")
    private String bio;

    public Heroes(String name, String realname, String team, String firstappearance, String createdby, String publisher, byte[] imageurl, String bio) {
        this.name = name;
        this.realname = realname;
        this.team = team;
        this.firstappearance = firstappearance;
        this.createdby = createdby;
        this.publisher = publisher;
        this.imageurl = imageurl;
        this.bio = bio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getFirstappearance() {
        return firstappearance;
    }

    public void setFirstappearance(String firstappearance) {
        this.firstappearance = firstappearance;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public byte[] getImageurl() {
        return imageurl;
    }

    public void setImageurl(byte[] imageurl) {
        this.imageurl = imageurl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public String toString() {
        return "Heroes{" +
                "name='" + name + '\'' +
                ", realname='" + realname + '\'' +
                ", team='" + team + '\'' +
                ", firstappearance='" + firstappearance + '\'' +
                ", createdby='" + createdby + '\'' +
                ", publisher='" + publisher + '\'' +
                ", imageurl='" + imageurl + '\'' +
                ", bio='" + bio + '\'' +
                '}';
    }
}