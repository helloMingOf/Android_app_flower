
package com.example.flower;

 import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
 import android.view.View;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.TextView;

        import androidx.appcompat.app.AppCompatActivity;

        import org.json.JSONObject;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.io.OutputStream;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.util.Calendar;

public class AddArticle extends AppCompatActivity implements View.OnClickListener{
    private ImageView iv_backward;
    private TextView forward;
    private EditText addtext;
    private LoginUser loginUser = LoginUser.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_article);

        iv_backward = (ImageView)findViewById(R.id.backward);
        forward = findViewById(R.id.forward);
        addtext = findViewById(R.id.add_text);

        forward.setOnClickListener(this);
        addtext.setOnClickListener(this);
        iv_backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forward:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            add_article();
                            finish();
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }}).start();
                break;
            case R.id.backward:
                finish();
                break;
            default:
                break;
        }

    }
    private void add_article()throws IOException{
        String text=addtext.getText().toString();
        String author=loginUser.getId();
        String picture=loginUser.getpicture();
        String time=gettime();
        String url2 = "http://172.17.143.35:8008/add_article/";
        URL url = new URL(url2);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        String params = "author=" + author+ '&' + "text=" + text+ '&' + "picture=" + picture+ '&' + "time=" +time;
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
    private String gettime()
    {
        Calendar calendar = Calendar.getInstance();
//获取系统的日期
//年
        int year = calendar.get(Calendar.YEAR);
//月
        int month = calendar.get(Calendar.MONTH)+1;
//日
        int day = calendar.get(Calendar.DAY_OF_MONTH);
//获取系统时间
//小时
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//分钟
        int minute = calendar.get(Calendar.MINUTE);
        return (year+"年"+month+"月"+day+"日"+hour+":"+minute);
    }
}