package com.softportfolio.crudwithretrofit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.softportfolio.crudwithretrofit.Model.Heroes;
import com.softportfolio.crudwithretrofit.Service.HeroesApi;
import com.softportfolio.crudwithretrofit.Utils.SharedPref;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity{

    EditText textView;
    SimpleDraweeView imageView;
    Button back;
    Context context = this;
    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_hero);

        textView = findViewById(R.id.detailHero);
        imageView = findViewById(R.id.imageView);
        back = findViewById(R.id.back);
        sharedPref = new SharedPref();
        Intent intent = getIntent();
        getIdHero(intent.getIntExtra("id", 1));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });
    }


    private void getIdHero(int id){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HeroesApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HeroesApi heroesApi = retrofit.create(HeroesApi.class);
        Call<Heroes> call = heroesApi.getIdHero(sharedPref.getValue(getApplicationContext(),"token"), id);
        call.enqueue(new Callback<Heroes>() {
            @Override
            public void onResponse(Call<Heroes> call, Response<Heroes> response)  {
                Bitmap val = null;
                try {
                    Heroes heroes = response.body();
                    textView.setText(heroes.getBio());
                    Uri uri = Uri.parse(heroes.getImageurl().toString());
                    imageView.setImageURI(uri);

                } catch (NullPointerException e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<Heroes> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
//        call.enqueue(new Callback<List<Heroes>>() {
//            @Override
//            public void onResponse(Call<List<Heroes>> call, Response<List<Heroes>> response) {
//                List<Heroes> eats = response.body();
//
//                String[] heroes = new String[eats.size()];
//                for (int i=0;i < eats.size(); i++){
//                    heroes[i]= eats.get(i).getBio();
//                }
//                listView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, heroes));
//            }
//
//            @Override
//            public void onFailure(Call<List<Heroes>> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

//    @Override
//    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.back:
//                Intent intent = new Intent(context, MainActivity.class);
//                startActivity(intent);
//                break;
//        }
//
//    }
}
