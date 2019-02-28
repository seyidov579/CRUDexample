package com.softportfolio.crudwithretrofit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.softportfolio.crudwithretrofit.Model.Heroes;
import com.softportfolio.crudwithretrofit.Service.HeroesApi;
import com.softportfolio.crudwithretrofit.Utils.GlideApp;
import com.softportfolio.crudwithretrofit.Utils.SharedPref;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScrollingActivity extends AppCompatActivity {


    TextView detail_bio, detail_realname, detail_firstappearance, detail_createdby, detail_publisher, detail_title, detail_team;
    ImageView detail_image;
    SharedPref sharedPref;
    String hero_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        detail_bio = findViewById(R.id.detail_bio);
        detail_team = findViewById(R.id.detail_team);
        detail_createdby = findViewById(R.id.detail_createdby);
        detail_firstappearance = findViewById(R.id.detail_firstappearance);
        detail_publisher = findViewById(R.id.detail_publisher);
        detail_realname = findViewById(R.id.detail_realname);
        detail_image = findViewById(R.id.detail_image);

//        back = findViewById(R.id.back);
        sharedPref = new SharedPref();
        Intent intent = getIntent();

        getIdHero(intent.getIntExtra("id", 1));
        setTitle(intent.getStringExtra("name"));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getIdHero(int id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HeroesApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HeroesApi heroesApi = retrofit.create(HeroesApi.class);
        Call<Heroes> call = heroesApi.getIdHero(sharedPref.getValue(getApplicationContext(), "token"), id);
        call.enqueue(new Callback<Heroes>() {
            @Override
            public void onResponse(Call<Heroes> call, Response<Heroes> response) {
                Bitmap val = null;
                try {
                    Heroes heroes = response.body();
                    hero_name = heroes.getName();
                    detail_bio.setText(heroes.getBio());
                    detail_team.setText(heroes.getTeam());
                    detail_createdby.setText(heroes.getCreatedby());
                    detail_firstappearance.setText(heroes.getFirstappearance());
                    detail_publisher.setText(heroes.getPublisher());
                    detail_realname.setText(heroes.getRealname());
                    if (heroes.getImageurl() != null) {
                        Uri uri = Uri.parse(heroes.getImageurl());
                        GlideApp
                                .with(getApplicationContext())
                                .load(uri)
                                .override(1500,1500)
                                .apply(new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE))
                                .into(detail_image);
                    }

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<Heroes> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}