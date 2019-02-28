package com.softportfolio.crudwithretrofit;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.softportfolio.crudwithretrofit.Activity.AddActivity;
import com.softportfolio.crudwithretrofit.Adapter.ListViewAdapter;
import com.softportfolio.crudwithretrofit.Model.Heroes;
import com.softportfolio.crudwithretrofit.Model.Login;
import com.softportfolio.crudwithretrofit.Model.User;
import com.softportfolio.crudwithretrofit.Service.HeroesApi;
import com.softportfolio.crudwithretrofit.Service.UserClient;
import com.softportfolio.crudwithretrofit.Utils.SharedPref;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    Context context;
    SharedPref sharedPref;
    List<Heroes> eats;
    FloatingActionButton fab_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listViewHeroes);
        fab_add = findViewById(R.id.fab_add);

//        login();
        sharedPref = new SharedPref();
        getHeroes();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                Intent intent = new Intent(MainActivity.this, ScrollingActivity.class);
                intent.putExtra("id",  eats.get(position).getId());
                intent.putExtra("name",  eats.get(position).getName());
                startActivity(intent);
            }
        });
        fab_add.setOnClickListener(new View.OnClickListener() {
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
                eats = response.body();

                try {
                    String[] heroes = new String[eats.size()];
                    for (int i=0;i < eats.size(); i++){
                        heroes[i]= eats.get(i).getName();
                    }
                    ListViewAdapter customAdapter = new ListViewAdapter(context, R.layout.itemlistrow, eats);

                    listView.setAdapter(customAdapter);
                }catch (NullPointerException e){
                    e.printStackTrace();
                }

//
//                List<Heroes> heroesList = new ArrayList<>();
//                for(int i=0;i < eats.size(); i++){
//                    heroesList.add(eats.get(i));
//                }



//                listView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, heroes));
            }

            @Override
            public void onFailure(Call<List<Heroes>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_add:
                Intent intent = new Intent(context, AddActivity.class);
                startActivity(intent);
                break;
            case R.id.action_settings:
                break;


        }
        return true;
    }

}
