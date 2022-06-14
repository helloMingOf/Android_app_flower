package com.example.flower.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.flower.LoginUser;
import com.example.flower.R;
import com.example.flower.message.PersonInfo;
import com.example.flower.message.Setting;
import com.example.flower.message.wiget.RoundImageView;

import java.math.RoundingMode;


public class message extends Fragment implements View.OnClickListener {
    private ImageView setting;
    private LinearLayout info;
    private RoundImageView ri_portrati;
    private TextView info_name,info_account;
    private LoginUser loginUser = LoginUser.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_person, container, false);
        ri_portrati = (RoundImageView) view.findViewById(R.id.portrait);
        setting = (ImageView)view.findViewById(R.id.setting);
        info = (LinearLayout)view.findViewById(R.id.info);
        info_name = (TextView)view.findViewById(R.id.info_name);
        info_account = (TextView)view.findViewById(R.id.info_account);
        info.setOnClickListener(this);
        setting.setOnClickListener(this);
        //登录则初始化用户的信息
        initInfo();

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        //在onStart中init，修改信息后返回不会出现没有修改的情况
        initInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //点击设置按钮的逻辑
            case R.id.setting:
                Intent intent = new Intent(getActivity(), Setting.class);
                getActivity().startActivity(intent);
                break;
            case R.id.info:
                Intent intent1 = new Intent(getActivity(), PersonInfo.class);
                getActivity().startActivity(intent1);
                break;
            default:
                break;
        }
    }

    //
    private void initInfo(){
        info_name.setText(loginUser.getName());
        info_account.setText(loginUser.getId());
        ri_portrati.setImageBitmap(loginUser.getPortrait());
    }
}
