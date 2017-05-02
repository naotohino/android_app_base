package com.example.androidappbase.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class ApiUtils {
    public static Gson getGson(){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson;
    }
}
