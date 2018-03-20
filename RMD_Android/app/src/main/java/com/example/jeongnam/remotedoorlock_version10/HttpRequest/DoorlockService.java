package com.example.jeongnam.remotedoorlock_version10.HttpRequest;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.POST;

/**
 * Created by jeongnam on 17. 11. 4.
 */

public interface DoorlockService {
    @POST("api/v1.0/tasks/login")
    Call<user_info> postRepos(
            @Body user_info user
    );

    @POST("api/v1.0/tasks/App/Service/DoorSwitch")
    Call<user_info> DoorSwitch(
            @Body user_info user
    );
}
