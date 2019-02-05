package com.softportfolio.crudwithretrofit;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.softportfolio.crudwithretrofit.Activity.AddActivity;
import com.softportfolio.crudwithretrofit.Model.Heroes;
import com.softportfolio.crudwithretrofit.Model.Login;
import com.softportfolio.crudwithretrofit.Model.User;
import com.softportfolio.crudwithretrofit.Service.HeroesApi;
import com.softportfolio.crudwithretrofit.Service.UserClient;
import com.softportfolio.crudwithretrofit.Utils.SharedPref;

import java.util.List;

import okhttp3.internal.http.HttpHeaders;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    Context context;
    Button add;
    SharedPref sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        context = this;
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listViewHeroes);
        add =findViewById(R.id.add);

//        login();
        sharedPref = new SharedPref();
        getHeroes();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
//                Toast.makeText(getApplicationContext(), "" + arg1, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("id", position+1);
                startActivity(intent);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddActivity.class);
                startActivity(intent);
            }
        });
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

    private void getHeroes(){
//        HttpHeaders headers = new HttpHeaders();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HeroesApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HeroesApi heroesApi = retrofit.create(HeroesApi.class);
        Call<List<Heroes>> call = heroesApi.getEats(sharedPref.getValue(getApplicationContext(),"token"));
        call.enqueue(new Callback<List<Heroes>>() {
            @Override
            public void onResponse(Call<List<Heroes>> call, Response<List<Heroes>> response) {
                List<Heroes> eats = response.body();

                String[] heroes = new String[eats.size()];
                for (int i=0;i < eats.size(); i++){
                    heroes[i]= eats.get(i).getName();
                }


                listView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, heroes));
            }

            @Override
            public void onFailure(Call<List<Heroes>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
