package com.example.flower;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Looper;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.flower.Login.Register;
import com.example.flower.Login.UI;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText et_name, et_password;
    Button login, exit, register;
    ImageView ic_eye, ic_more_account;

    private boolean passwordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //login button
        et_name = findViewById(R.id.et_account_name);
        et_password = findViewById(R.id.et_password);
        login = findViewById(R.id.login);
        exit = findViewById(R.id.exit);
        register = findViewById(R.id.register);
        ic_eye = findViewById(R.id.iv_eye);
        ic_more_account = findViewById(R.id.iv_more_accout);

        login.setOnClickListener(this);
        register.setOnClickListener(this);
        exit.setOnClickListener(this);
        ic_eye.setOnClickListener(this);
        ic_more_account.setOnClickListener(this);

    }

    public void onClick(View v) {
        String name = et_name.getText().toString();
        String password = et_password.getText().toString();
        switch (v.getId()) {
            //注册按钮的逻辑
            case R.id.register:
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
                break;
            //登录按钮的逻辑
            case R.id.login:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int result = login();
                            //login()为向php服务器提交请求的函数，返回数据类型为int
                            if (result == 1) {
                                Log.e("log_tag", "登陆成功！");
                                //Toast toast=null;
                                Looper.prepare();
                                Toast.makeText(MainActivity.this, "登陆成功！", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(MainActivity.this, UI.class);
                                startActivity(intent);
                                Looper.loop();
                            } else if (result == -2) {
                                Log.e("log_tag", "密码错误！");
                                //Toast toast=null;
                                Looper.prepare();
                                Toast.makeText(MainActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            } else if (result == -1) {
                                Log.e("log_tag", "不存在该用户！");
                                //Toast toast=null;
                                Looper.prepare();
                                Toast.makeText(MainActivity.this, "不存在该用户！", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }).start();
                break;
            //退出功能
            case R.id.exit:
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this);
                builder.setTitle("你确定退出该APP吗？");
                builder.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                MainActivity.this.finish();
                            }
                        });
                builder.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
                break;
            //隐藏密码功能
            case R.id.iv_eye:
                if (passwordVisible) {  //如果可见，则转为不可见
                    ic_eye.setSelected(false);
                    et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    passwordVisible = false;
                } else {  //如果不可见，则转为可见
                    ic_eye.setSelected(true);
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    passwordVisible = true;
                }
                break;
            //显示本地所有登录信息
        }
    }

    private int login() throws IOException {
        int returnResult = 0;
        String user_id = et_name.getText().toString();
        String input_pwd = et_password.getText().toString();
        if (user_id == null || user_id.length() <= 0) {
            Looper.prepare();
            Toast.makeText(MainActivity.this, "请输入账号", Toast.LENGTH_LONG).show();
            Looper.loop();
            return 0;
        }
        if (input_pwd == null || input_pwd.length() <= 0) {
            Looper.prepare();
            Toast.makeText(MainActivity.this, "请输入密码", Toast.LENGTH_LONG).show();
            Looper.loop();
            return 0;
        }
        String urlstr = "http://172.17.143.35:8008/login/";
        URL url = new URL(urlstr);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        String params = "uid=" + user_id + '&' + "password=" + input_pwd;
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
            returnResult = jsonObject.getInt("status");
            if(returnResult==1)
            {
                User user = new User();
                user.setId(jsonObject.getString("ID"));
                user.setPassword(jsonObject.getString("password"));
                user.setName(jsonObject.getString("name"));
                user.setRegion(jsonObject.getString("region"));
                user.setGender(jsonObject.getString("gender"));
                user.setBrithday(jsonObject.getString("birthday"));
                user.setpicture(jsonObject.getString("picture"));
                final String yourUrl = "http://172.17.143.35:8008/media/"+user.getpicture();
                Bitmap bitmap= Glide.with(getApplicationContext())
                        .load(yourUrl)
                        .asBitmap() //必须
                        .centerCrop()
                        .into(500, 500)
                        .get();
                user.setPortrait(bitmap);
                LoginUser.getInstance().login(user);

            }
        } catch (Exception e) {
            Log.e("log_tag", "the Error parsing data " + e.toString());
        }
        return returnResult;
    }

}


