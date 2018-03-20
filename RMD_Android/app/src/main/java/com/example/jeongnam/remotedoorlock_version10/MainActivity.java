package com.example.jeongnam.remotedoorlock_version10;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class MainActivity extends AppCompatActivity {

    EditText ET_id = null;
    EditText ET_pw = null;
    TextView textview = null;
    private Retrofit retrofit;
    Button btn_serverconfig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ET_id = (EditText)findViewById(R.id.editText);
        ET_pw = (EditText)findViewById(R.id.editText2);
        textview = (TextView)findViewById(R.id.textView);
        Button button = (Button) findViewById(R.id.button);
        btn_serverconfig = (Button)findViewById(R.id.Button_ServerConfig);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id =ET_id.getText().toString();
                String pw =  ET_pw.getText().toString();
                post(id,pw);

            }
        });

        btn_serverconfig.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), ServerConfigActivity.class);
                startActivity(intent);
            }
        });



    }

    public void post(String id, String password) {
        Retrofit retrofit = getRetrofit.get_Retrofit(getRetrofit.Server_url,getRetrofit.Server_port);

        if(retrofit == null){
            Toast.makeText(getApplicationContext(), "insert Server IP IP Address is Null!!", Toast.LENGTH_SHORT).show();
            return;
        }
        DoorlockService retrofitService = retrofit.create(DoorlockService.class);
        user_info buf = new user_info();
        buf.setId(id);
        buf.setPassword(password);
        buf.setName("");
        buf.setDone(false);
        Call<user_info> call = retrofitService.postRepos(buf);
        try {
            call.enqueue(new Callback<user_info>() {
                @Override
                public void onResponse(Call<user_info> call, Response<user_info> response) {
                    user_info repo = response.body();
                    if(repo.getDone()) {
                        Intent intent = new Intent(getApplicationContext(), DoorActivity.class);
                        intent.putExtra("id",repo.getId());
                        intent.putExtra("name",repo.getName());
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "No Search User!", Toast.LENGTH_SHORT).show();
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
