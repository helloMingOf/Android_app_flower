package com.example.flower;


import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

/**
LitePal巨坑之一！下面的属性remember本可以设计为boolean值，但是LitePal的boolean值update不了！int尝试也不行！所以只能用Integer代替。
 解决：使用update更新数据
 **/
public class User extends LitePalSupport implements Comparable<User> {
    private String id;
    private String name;
    private String password;
    private String region;
    private String gender;
    private Bitmap portrait;
    private String brithday;
    private String picture;
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", region='" + region + '\'' +
                ", gender='" + gender + '\'' +
                ", brithday='" + brithday + '\'' +
                ", picture='" + picture + '\'' +
                ", portrait='" + portrait + '\'' +
                '}';
    }

    //check是传入未MD5加密的
    public boolean checkPassword(String str){
        if (password.equals(str)) return true;
        else return false;
    }
    public boolean logout(){
        if(id == null) return false;
        id = null;
        name = null;
        region = null;
        gender = null;
        brithday = null;
        return true;
    }
    @Override
    public boolean equals(Object o) {
        if (o != null) {
            User UserInfo = (User) o;
            return (getId()==UserInfo.getId());
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(@NonNull User User) {
        return this.getName().compareTo(User.getName());
    }

    public String getpicture() {
        return picture;
    }

    public void setpicture(String picture) {
        this.picture = picture;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBrithday() {
        return brithday;
    }

    public void setBrithday(String brithday) {
        this.brithday = brithday;
    }

    public Bitmap getPortrait() {
        return portrait;
    }

    public void setPortrait(Bitmap portrait) {
        this.portrait = portrait;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
