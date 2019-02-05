package com.softportfolio.crudwithretrofit.Model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class HeroesPOST {

    private String name;

    private String realname;

    private String team;

    private String firstappearance;

    private String createdby;

    private String publisher;

    private String bio;

    public HeroesPOST(String name, String realname, String team, String firstappearance, String createdby, String publisher,  String bio) {
        this.name = name;
        this.realname = realname;
        this.team = team;
        this.firstappearance = firstappearance;
        this.createdby = createdby;
        this.publisher = publisher;
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
                ", bio='" + bio + '\'' +
                '}';
    }
}