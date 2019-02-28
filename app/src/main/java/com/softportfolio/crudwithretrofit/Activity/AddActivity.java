package com.softportfolio.crudwithretrofit.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.softportfolio.crudwithretrofit.Model.Heroes;
import com.softportfolio.crudwithretrofit.Model.HeroesPOST;
import com.softportfolio.crudwithretrofit.Model.Login;
import com.softportfolio.crudwithretrofit.Model.User;
import com.softportfolio.crudwithretrofit.R;
import com.softportfolio.crudwithretrofit.Service.HeroesApi;
import com.softportfolio.crudwithretrofit.Service.UserClient;
import com.softportfolio.crudwithretrofit.Utils.GlideApp;
import com.softportfolio.crudwithretrofit.Utils.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AddActivity extends AppCompatActivity{

    EditText name, realname, team, firstappearance, createdby, publisher, bio;
    Button add;
    SharedPref sharedPref;
    ImageView image;
    String encodeImage;
    Uri imageUri;

    private static final int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        login();
        setContentView(R.layout.add_hero);
        name = findViewById(R.id.heroName);
        realname = findViewById(R.id.heroRealname);
        team = findViewById(R.id.heroTeam);
        firstappearance = findViewById(R.id.firstappearance);
        createdby = findViewById(R.id.createdby);
        publisher = findViewById(R.id.publisher);
        bio = findViewById(R.id.bio);

        image = findViewById(R.id.imageView2);


        add = findViewById(R.id.add);


        sharedPref = new SharedPref();


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imageintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(imageintent, RESULT_LOAD_IMAGE );
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                clientBuilder.addInterceptor(loggingInterceptor);
                Bitmap bitmap = null;

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(AddActivity.this.getContentResolver(),imageUri);

//                    InputStream inputStream = new FileInputStream(imageUri.getPath());//You can get an inputStream using any IO API
//                    byte[] bytes;
//                    byte[] buffer = new byte[8192];
//                    int bytesRead;
//                    ByteArrayOutputStream output = new ByteArrayOutputStream();
//                    try {
//                        while ((bytesRead = inputStream.read(buffer)) != -1) {
//                            output.write(buffer, 0, bytesRead);
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    bytes = output.toByteArray();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    encodeImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(HeroesApi.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                HeroesApi heroes = retrofit.create(HeroesApi.class);
                Heroes heroesData = new Heroes(name.getText().toString(), realname.getText().toString(), team.getText().toString(), firstappearance.getText().toString(), createdby.getText().toString(), publisher.getText().toString(),encodeImage,
                        bio.getText().toString()
                );
                Call<Heroes> heroesCall = heroes.sendHeroDate(sharedPref.getValue(getApplicationContext(),"token") ,heroesData,"json");
                heroesCall.enqueue(new Callback<Heroes>() {
                    @Override
                    public void onResponse(Call<Heroes> call, Response<Heroes> response) {
                        if(response.isSuccessful())
                            Toast.makeText(getApplicationContext(), "Girdi", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), sharedPref.getValue(getApplicationContext(),"token"), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Heroes> call, Throwable t) {
                        System.out.println("Error :" + t.getMessage());
                    }
                });

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




    private byte[] getByteArrayFromImage(String filePath) throws FileNotFoundException, IOException {

        File file = new File(filePath);

        FileInputStream fis = new FileInputStream(file);
        //create FileInputStream which obtains input bytes from a file in a file system
        //FileInputStream is meant for reading streams of raw bytes such as image data. For reading streams of characters, consider using FileReader.

        //InputStream in = resource.openStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum);
                //no doubt here is 0
                /*Writes len bytes from the specified byte array starting at offset
                off to this byte array output stream.*/
            }
        } catch (IOException ex) {
            Log.d("error","error");
        }
        byte[] bytes = bos.toByteArray();

        return bytes;

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            GlideApp
                    .with(getApplicationContext())
                    .load(imageUri)
                    .apply(new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE))
                    .into(image);

        }
    }
    }
