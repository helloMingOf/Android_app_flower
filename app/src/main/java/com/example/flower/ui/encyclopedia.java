package com.example.flower.ui;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.flower.LoginUser;
import com.example.flower.R;
import com.example.flower.User;
import com.example.flower.message.PersonInfo;
import com.example.flower.message.Setting;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;


public class encyclopedia extends Fragment implements View.OnClickListener {
    private Button fan;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_encyclopedia, container, false);
        fan = (Button) view.findViewById(R.id.flowerfan);
        fan.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.flowerfan:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                try {
                    fa();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                    }
                }).start();
                break;
            default:
                break;

        } }

        public void fa()throws IOException{
            String urlstr = "http://172.17.143.35:8008/mqttpublish/";
            URL url = new URL(urlstr);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(http.getInputStream()));//获得输入流
            String line = "";
            StringBuilder sb = new StringBuilder();
            while (null != (line = bufferedReader.readLine())) {
                sb.append(line);
            }
            String result = sb.toString();
            try {
                Gson gson = new Gson();
            } catch (Exception e) {
                Log.e("log_tag", "the Error parsing data " + e.toString());
            }

        }

    }


