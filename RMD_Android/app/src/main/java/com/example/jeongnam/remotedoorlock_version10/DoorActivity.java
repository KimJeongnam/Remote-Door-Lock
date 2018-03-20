package com.example.jeongnam.remotedoorlock_version10;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jeongnam.remotedoorlock_version10.HttpRequest.DoorlockService;
import com.example.jeongnam.remotedoorlock_version10.HttpRequest.getRetrofit;
import com.example.jeongnam.remotedoorlock_version10.HttpRequest.user_info;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DoorActivity extends AppCompatActivity {
    private TextView tv_userid;
    private TextView tv_username;
    private Button doorswitch;
    String id;
    String name;
    String Send_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.door_activity);
        Intent doorintent = getIntent();

        tv_userid = (TextView)findViewById(R.id.textView_userid);
        tv_username = (TextView)findViewById(R.id.textView_username);
        id = tv_userid.getText().toString();
        name = tv_username.getText().toString();
        Send_id = doorintent.getExtras().getString("id");
        tv_userid.setText(id+Send_id);
        tv_username.setText(name+doorintent.getExtras().getString("name"));
        doorswitch = (Button)findViewById(R.id.btn_DoorSwitch);

        WebView webView = (WebView)findViewById(R.id.webView);
        webView.setPadding(0,0,0,0);
        //webView.setInitialScale(100);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        //webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);

        String url ="http://"+getRetrofit.Server_url+":8080/javascript_simple.html";
        webView.loadUrl(url);

        doorswitch.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                DoorSwitch(Send_id);
            }
        });
    }
    public void DoorSwitch(String id) {
        Retrofit retrofit = getRetrofit.get_Retrofit(getRetrofit.Server_url,getRetrofit.Server_port);

        if(retrofit == null){
            Toast.makeText(getApplicationContext(), "insert Server IP IP Address is Null!!", Toast.LENGTH_SHORT).show();
            return;
        }
        DoorlockService retrofitService = retrofit.create(DoorlockService.class);
        user_info buf = new user_info();
        buf.setId(id);
        buf.setPassword("");
        buf.setName("");
        buf.setDone(false);
        Call<user_info> call = retrofitService.DoorSwitch(buf);
        try {
            call.enqueue(new Callback<user_info>() {
                @Override
                public void onResponse(Call<user_info> call, Response<user_info> response) {
                    user_info repo = response.body();
                    if(repo.getDone()) {
                        Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Fail!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<user_info> call, Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error receive Data Type Check!", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Server not Connected Check ip Address!!", Toast.LENGTH_SHORT).show();
        }
    }

}
