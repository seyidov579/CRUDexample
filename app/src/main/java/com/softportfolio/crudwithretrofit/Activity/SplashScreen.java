package com.softportfolio.crudwithretrofit.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.softportfolio.crudwithretrofit.MainActivity;
import com.softportfolio.crudwithretrofit.Model.Login;
import com.softportfolio.crudwithretrofit.Model.User;
import com.softportfolio.crudwithretrofit.R;
import com.softportfolio.crudwithretrofit.Service.UserClient;
import com.softportfolio.crudwithretrofit.Utils.SharedPref;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashScreen extends Activity {
    SharedPref sharedPref;
//    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        startHeavyProcessing();
        sharedPref = new SharedPref();
        login();
//        context = this;

    }

    private void startHeavyProcessing(){
        new LongOperation().execute("");
    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            //some heavy processing resulting in a Data String
//            for (int i = 0; i < 5; i++) {
            do {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }while (sharedPref.getValue(getApplicationContext(), "token").equals(""));

//            }
            return "whatever result you have";
        }

        @Override
        protected void onPostExecute(String result) {
            Intent i = new Intent(SplashScreen.this, MainActivity.class);
            i.putExtra("data", result);
            startActivity(i);
            finish();
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    private void login(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Login login = new Login("kenan","test12345");
        UserClient userService =
                retrofit.create(UserClient.class);
        Call<User> call = userService.login(login);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    try {
                        sharedPref.save(getApplicationContext(), "token", "JWT " + response.body().getToken());

                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(),sharedPref.getValue(getApplicationContext(),"token"), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"username and password error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}