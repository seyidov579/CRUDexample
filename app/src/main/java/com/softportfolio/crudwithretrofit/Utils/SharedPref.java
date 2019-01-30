package com.softportfolio.crudwithretrofit.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import retrofit2.http.Body;

public class SharedPref {
    static final String PREF_NAME = "sharedpref";
    public void save(Context context, String key, String value){
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public String getValue(Context context, String key){
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return settings.getString(key, null);
    }

    public void remove(Context context, String key){
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(key);
        editor.commit();
    }
    public void clear(Context context){
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();
    }
}
