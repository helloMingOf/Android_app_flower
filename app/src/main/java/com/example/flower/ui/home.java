package com.example.flower.ui;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flower.R;
import com.example.flower.message.wiget.RoundImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class home extends Fragment {
    private View view;
    RecyclerView recyclerView;
    private MyRecycleViewAdapter adapter;
    private List<HashMap<String,Object>> list=new ArrayList<HashMap<String,Object>>();
    private List<HashMap<String,Object>> mData=new ArrayList<HashMap<String,Object>>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView=view.findViewById(R.id.recycler_view);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getData();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }).start();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//垂直线性布局
        adapter=new MyRecycleViewAdapter();
        recyclerView.setAdapter(adapter);
        return view;
    }
    private void getData()throws IOException {
        String urlstr = "http://172.17.143.35:8008/flower_rfid/";;
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
            list = gson.fromJson(result ,new TypeToken<List<HashMap<String, Object>>>() {}.getType());
            mData.clear();
            mData.addAll(list);
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e("log_tag", "the Error parsing data " + e.toString());
        }
    }
    class  MyRecycleViewAdapter extends RecyclerView.Adapter<MyRecycleViewAdapter.ViewHolder>
    {


        public  class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView picture;
            public TextView name;
            public TextView rfid;
            public TextView time;

            public ViewHolder(View convertView) {
                super(convertView);
                picture=convertView.findViewById(R.id.flowerpicture);
                name = (TextView)convertView.findViewById(R.id.flowername);
                rfid = (TextView)convertView.findViewById(R.id.flowerid);
                time = (TextView)convertView.findViewById(R.id.flowertime);

            }
        }
        @NonNull
        @Override
        public MyRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(getActivity()).inflate(R.layout.item,parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MyRecycleViewAdapter.ViewHolder holder, final int position) {
            //picture
            holder.picture.setImageResource(R.drawable.i1);
            holder.name.setText((String)mData.get(position).get("flower_name"));
            holder.rfid.setText((String)mData.get(position).get("rfid_id"));
            holder.time.setText((String)mData.get(position).get("time"));
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }
}
