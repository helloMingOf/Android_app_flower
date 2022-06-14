package com.example.flower;

import android.app.Application;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class LoginUser extends Application {
    private static LoginUser login_user = new LoginUser();
    private static User _user;
    private String id;
    private String picture;
    private String name;
    private String region;
    private String gender;
    private byte[] portrait;
    private String brithday;


    public static LoginUser getInstance(){
        return login_user;
    }

    public User getUser(){
        return _user;
    }

    //保存至数据库
    public void update() throws IOException {
        if(_user.getId()==this.id){
            _user.setName(this.name);
            _user.setGender(this.gender);
            _user.setpicture(this.picture);
            _user.setRegion(this.region);
            _user.setBrithday(this.brithday);
        }
        String urlstr = "http://172.17.143.35:8008/updateperson/";
        URL url = new URL(urlstr);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        String params = "uid=" + this.id + '&' + "name=" + this.name+ '&' + "region=" + this.region+ '&' + "gender=" + this.gender+ '&' + "birthday=" + this.brithday + '&' + this.picture;
        http.setDoOutput(true);
        http.setRequestMethod("POST");
        OutputStream out = http.getOutputStream();
        out.write(params.getBytes());
        out.flush();
        out.close();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(http.getInputStream()));//获得输入流
        String line = "";
        StringBuilder sb = new StringBuilder();
        while (null != (line = bufferedReader.readLine())) {
            sb.append(line);
        }
        String result = sb.toString();
        try {
            JSONObject jsonObject = new JSONObject(result);
        } catch (Exception e) {
            Log.e("log_tag", "the Error parsing data " + e.toString());
        }
    }

    //重新init
    public void reinit(){
        login_user.id = _user.getId();
        login_user.name = _user.getName();
        login_user.region = _user.getRegion();
        login_user.picture = _user.getpicture();
        login_user.gender = _user.getGender();
        login_user.brithday = _user.getBrithday();
    }

    public boolean login(User user) {
        _user = user;
        login_user.id = user.getId();
        login_user.name = user.getName();
        login_user.region = user.getRegion();
        login_user.gender = user.getGender();
        login_user.brithday = user.getBrithday();
        login_user.picture = user.getpicture();
        return true;
    }

    public static boolean logout(){
        if(login_user.id == null) return false;
        login_user.id = null;
        login_user.name = null;
        login_user.region = null;
        login_user.gender = null;
        login_user.brithday = null;
        return true;
    }


    @Override
    public String toString() {
        return "LoginUser{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", picture ='" + picture + '\'' +
                ", region='" + region + '\'' +
                ", gender='" + gender + '\'' +
                ", brithday='" + brithday + '\'' +
                ", portrait ='" + portrait + '\'' +
                '}';
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
    public byte[] getPortrait() {
        return portrait;
    }

    public void setPortrait(byte[] portrait) {
        this.portrait = portrait;
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
}
