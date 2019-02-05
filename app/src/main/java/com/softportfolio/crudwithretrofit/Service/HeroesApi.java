package com.softportfolio.crudwithretrofit.Service;

import com.softportfolio.crudwithretrofit.Model.Heroes;
import com.softportfolio.crudwithretrofit.Model.HeroesPOST;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HeroesApi {

    String BASE_URL = "http://192.168.15.18:8000/api/v1/";

    @GET("heroes/")
    Call<List<Heroes>> getEats(@Header("Authorization") String authorization);

    @GET("heroes/{id}")
    Call<Heroes> getIdHero(@Header("Authorization") String authorization, @Path("id") int id);

    @POST("heroesp/")
    Call<Heroes> sendHeroDate(@Header("Authorization") String authorization, @Body Heroes heroes, @Query("format") String format);

}
