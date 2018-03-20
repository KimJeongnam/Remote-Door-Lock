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

public class ServerConfigActivity extends AppCompatActivity {
    EditText edit_ip;
    EditText edit_port;
    Button btnApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server_config_activity);
        edit_ip = (EditText)findViewById(R.id.edit_ip);
        edit_port = (EditText)findViewById(R.id.edit_port);
        btnApply = (Button)findViewById(R.id.IP_apply_button);
        edit_ip.setText(getRetrofit.Server_url);
        edit_port.setText(getRetrofit.Server_port);

        btnApply.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                getRetrofit.Server_url = edit_ip.getText().toString();
                getRetrofit.Server_port = edit_port.getText().toString();
                finish();
            }
        });
    }
}
