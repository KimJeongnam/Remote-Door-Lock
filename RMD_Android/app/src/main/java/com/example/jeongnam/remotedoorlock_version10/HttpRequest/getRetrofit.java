package com.example.jeongnam.remotedoorlock_version10.HttpRequest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jeongnam on 17. 11. 5.
 */

public class getRetrofit {

    public static String Server_url = "";
    public static String Server_port = "";

    public static Retrofit get_Retrofit(String url, String port){
        if(!url.equals("")){
            if(port.equals("") || port.equals("80")){

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://"+url+"/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                return retrofit;
            }else{
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://"+url+":"+port+"/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                return retrofit;
            }
        }
        return null;
    }
}
