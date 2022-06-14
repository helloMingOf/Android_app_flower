package com.example.flower;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    private Bitmap portrait;
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
            _user.setPortrait(this.portrait);
        }
        if (this.getpicture()!=null)
        uploadBitmap1("http://172.17.143.35:8008/picture/",Bitmap2Bytes(this.portrait),this.id);
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
        login_user.portrait=_user.getPortrait();
    }

    public boolean login(User user) {
        _user = user;
        login_user.id = user.getId();
        login_user.name = user.getName();
        login_user.region = user.getRegion();
        login_user.gender = user.getGender();
        login_user.brithday = user.getBrithday();
        login_user.picture =user.getpicture();
        login_user.portrait=user.getPortrait();
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
    public Bitmap getPortrait() {
        return portrait;
    }

    public void setPortrait(Bitmap portrait) {
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

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public String uploadBitmap1(String urlHost, byte[] imageBytes, String key) {
        String endString = "\r\n";
        String twoHyphen = "--";
        String boundary = "*****";
        try {
            URL url = new URL(urlHost);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            //con.setRequestProperty("content-type", "text/html");
            //允许input、Output,不使用Cache
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            //设置传送的method=POST
            con.setRequestMethod("POST");
            //setRequestProperty
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "utf-8");
            con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            OutputStream outputStream = new DataOutputStream(con.getOutputStream());
            String header =twoHyphen + boundary + endString;
            header +="Content-Disposition: form-data;name=\"img\";" + "filename=\"" + "zjm.jpg" + "\"" + endString + endString;
            outputStream.write(header.getBytes());
            //取得文件的FileInputStream
            outputStream.write(imageBytes, 0, imageBytes.length);
            outputStream.write(endString.getBytes());
            String footer=endString+twoHyphen + boundary + twoHyphen + endString;
            outputStream.write(footer.getBytes());
            String params=twoHyphen + boundary + endString+"Content-Disposition: form-data; name=\"uid\"" +endString+endString+ key + endString+twoHyphen + boundary;
            outputStream.write(params.getBytes());
            outputStream.flush();

            int cah = con.getResponseCode();
            if (cah == 200) {
                InputStream isInputStream = con.getInputStream();
                int ch;
                StringBuffer buffer = new StringBuffer();
                while ((ch = isInputStream.read()) != -1) {
                    buffer.append((char) ch);
                }
                return buffer.toString();
            } else {
                return "false";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "false";
    }

}
