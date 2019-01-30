package com.softportfolio.crudwithretrofit.Service;

import com.softportfolio.crudwithretrofit.Model.Login;
import com.softportfolio.crudwithretrofit.Model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserClient {
    String BASE_URL = "http://192.168.15.18:8000/";

    @POST("api-token-auth/")
    Call<User> login(@Body Login login);


}
