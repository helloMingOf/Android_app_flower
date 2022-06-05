package com.example.flower.Login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.flower.MainActivity;
import com.example.flower.R;
import com.example.flower.User;
import com.example.flower.message.util.ProvinceBean;
import com.example.flower.message.wiget.CityBean;
import com.example.flower.message.wiget.ItemGroup;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Register extends AppCompatActivity implements View.OnClickListener {
    private ItemGroup ig_gender_1, ig_region_1, ig_brithday_1;
    private TextView ig_id_1,ig_name_1,ig_password_1,ig_password_2;
    private Button register;
    private OptionsPickerView pvOptions;
    private ArrayList<String> optionsItems_gender = new ArrayList<>();
    private ArrayList<ProvinceBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    User user=new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initOptionData();
        ig_id_1 = (TextView) findViewById(R.id.new_ig_ID);
        ig_name_1 = (TextView) findViewById(R.id.new_ig_name);
        ig_gender_1 = (ItemGroup) findViewById(R.id.new_ig_gender);
        ig_region_1 = (ItemGroup) findViewById(R.id.new_ig_region);
        ig_brithday_1 = (ItemGroup) findViewById(R.id.new_ig_brithday);
        ig_password_1 = (TextView) findViewById(R.id.new_ig_password);
        ig_password_2 = (TextView) findViewById(R.id.new_ig_password_1);
        register = (Button) findViewById(R.id.register);
        ig_id_1.setOnClickListener(this);
        ig_name_1.setOnClickListener(this);
        ig_gender_1.setOnClickListener(this);
        ig_region_1.setOnClickListener(this);
        ig_brithday_1.setOnClickListener(this);
        ig_password_1.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            //点击修改地区逻辑
            case R.id.new_ig_region:
                pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //选择了则显示并暂存LoginUser，退出时在保存至数据库
                        String tx = options1Items.get(options1).getPickerViewText()
                                + options2Items.get(options1).get(options2);
                        ig_region_1.getContentEdt().setText(tx);
                        user.setRegion(tx);
                    }
                }).setCancelColor(Color.GRAY).build();
                pvOptions.setPicker(options1Items, options2Items);//二级选择器
                pvOptions.show();
                break;

            //点击修改性别逻辑
            case R.id.new_ig_gender:
                //性别选择器
                pvOptions = new OptionsPickerBuilder(Register.this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3, View v) {
                        //选择了则显示并暂存LoginUser，退出时在保存至数据库
                        String tx = optionsItems_gender.get(options1);
                        ig_gender_1.getContentEdt().setText(tx);
                        user.setGender(tx);
                    }
                }).setCancelColor(Color.GRAY).build();
                pvOptions.setPicker(optionsItems_gender);
                pvOptions.show();
                break;

            //点击修改生日逻辑
            case R.id.new_ig_brithday:
                //时间选择器
                //修改打开的默认时间，如果选择过则是选择过的时间，否则是系统默认时间
                Calendar selectedDate = Calendar.getInstance();

                TimePickerView pvTime = new TimePickerBuilder(Register.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        //选择了则显示并暂存LoginUser，退出时在保存至数据库
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
                        ig_brithday_1.getContentEdt().setText(sdf.format(date));
                        user.setBrithday(sdf.format(date));
                    }
                }).setDate(selectedDate).setCancelColor(Color.GRAY).build();
                pvTime.show();
                break;
            case R.id.register:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int re = register();
                            if (re == 1) {
                                Log.e("log_tag", "注册成功！");
                                //Toast toast=null;
                                Looper.prepare();
                                Toast.makeText(Register.this, "注册成功！", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Register.this, MainActivity.class);
                                startActivity(intent);
                                Looper.loop();
                            } else if (re == -1) {
                                Log.e("log_tag", "用户ID已经存在！");
                                //Toast toast=null;
                                Looper.prepare();
                                Toast.makeText(Register.this, "用户ID已经存在！", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }}).start();
                break;
            default:
                break;
        }
    }
    private int register() throws IOException {
        int re =0;
        String id = ig_id_1.getText().toString();
        String name = ig_name_1.getText().toString();
        String password = ig_password_1.getText().toString();
        String password2=ig_password_2.getText().toString();
        if(id == null || id.length() <= 0){
            Looper.prepare();
            Toast.makeText(Register.this, "用户ID不能为空", Toast.LENGTH_LONG).show();
            Looper.loop();
            return 0;}
        if((password == null || password.length() <= 0) || (password2 == null || password2.length() <= 0)){
            Looper.prepare();
            Toast.makeText(Register.this, "密码为空", Toast.LENGTH_LONG).show();
            Looper.loop();
            return 0;}
        if(name == null || name.length() <= 0){
            Looper.prepare();
            Toast.makeText(Register.this, "用户姓名不能为空", Toast.LENGTH_LONG).show();
            Looper.loop();
            return 0;}
        if(!password.equals(password2)){
            Looper.prepare();
            Toast.makeText(Register.this, "用输入两次密码不同", Toast.LENGTH_LONG).show();
            Looper.loop();
            return 0;}
        if(user.getBrithday()==null){
            Looper.prepare();
            Toast.makeText(Register.this, "生日不能为空", Toast.LENGTH_LONG).show();
            Looper.loop();
            return 0;}
        if(user.getGender()==null){
            Looper.prepare();
            Toast.makeText(Register.this, "性别没有选择", Toast.LENGTH_LONG).show();
            Looper.loop();
            return 0;}
        if(user.getRegion()==null){
            Looper.prepare();
            Toast.makeText(Register.this, "地区没有选择", Toast.LENGTH_LONG).show();
            Looper.loop();
            return 0;}
        user.setId(id);
        user.setName(name);
        user.setPassword(password);
        String url2 = "http://172.17.143.35:88/json/Register.php";
        URL url = new URL(url2);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        String params = "uid=" + user.getId() + '&' + "name=" + user.getName()+ '&' + "region=" + user.getRegion()+ '&' + "gender=" + user.getGender()+ '&' + "birthday=" + user.getBrithday()+ '&' + "password=" + user.getPassword();
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
            re = jsonObject.getInt("status");
        } catch (Exception e) {
            Log.e("log_tag", "the Error parsing data " + e.toString());
        }
    return re;

    }

    //初始化性别、地址和生日的数据
    private void initOptionData() {
        //性别选择器数据
        optionsItems_gender.add(new String("保密"));
        optionsItems_gender.add(new String("男"));
        optionsItems_gender.add(new String("女"));
        optionsItems_gender.add(new String("不男不女"));

        //地址选择器数据
        String province_data = readJsonFile("province.json");
        String city_data = readJsonFile("city.json");

        Gson gson = new Gson();

        options1Items = gson.fromJson(province_data, new TypeToken<ArrayList<ProvinceBean>>() {
        }.getType());
        ArrayList<CityBean> cityBean_data = gson.fromJson(city_data, new TypeToken<ArrayList<CityBean>>() {
        }.getType());
        for (ProvinceBean provinceBean : options1Items) {
            ArrayList<String> temp = new ArrayList<>();
            for (CityBean cityBean : cityBean_data) {
                if (provinceBean.getProvince().equals(cityBean.getProvince())) {
                    temp.add(cityBean.getName());
                }
            }
            options2Items.add(temp);
        }

    }
    //传入：asset文件夹中json文件名
    //返回：读取的String
    private String readJsonFile(String file){
        StringBuilder newstringBuilder = new StringBuilder();
        try {
            InputStream inputStream = getResources().getAssets().open(file);

            InputStreamReader isr = new InputStreamReader(inputStream);

            BufferedReader reader = new BufferedReader(isr);

            String jsonLine;
            while ((jsonLine = reader.readLine()) != null) {
                newstringBuilder.append(jsonLine);
            }
            reader.close();
            isr.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String data =  newstringBuilder.toString();
        return data;
    }
}

