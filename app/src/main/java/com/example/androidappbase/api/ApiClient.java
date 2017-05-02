package com.example.androidappbase.api;

import android.content.Context;
import android.text.TextUtils;

import com.example.androidappbase.AppSharedPrefs;
import com.example.androidappbase.util.ApiUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

    public final static String API_URL = "http://androidappbase.example.com";

    public final static String HEADER_UID = "uid";
    public final static String HEADER_CLIENT = "client";
    public final static String HEADER_ACCESS_TOKEN = "access-token";

    private OkHttpClient mOkHttpClient;
    private Retrofit mRetrofit;
    private ApiService mApiService;

    public ApiClient(final Context context){
        mOkHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("Uid", AppSharedPrefs.getUid(context))
                        .addHeader("Client", AppSharedPrefs.getClient(context))
                        .addHeader("Access-token", AppSharedPrefs.getAccessToken(context))
                        .build();
                return  chain.proceed(request);
            }
        }).build();

    }

    public Retrofit getRetrofit(){
        if(mRetrofit == null){
            Gson gson = ApiUtils.getGson();
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(mOkHttpClient)
                    .build();
        }
        return mRetrofit;
    }

    public ApiService getApiService(){
        if(mApiService == null){
            mApiService = getRetrofit().create(ApiService.class);
        }
        return mApiService;
    }

    public static void saveCredentials(Context context, Headers headers){
        String uid = headers.get(HEADER_UID);
        String client = headers.get(HEADER_CLIENT);
        String accessToken = headers.get(HEADER_ACCESS_TOKEN);
        if(TextUtils.isEmpty(uid) || TextUtils.isEmpty(client) || TextUtils.isEmpty(accessToken)){
            return;
        }
        AppSharedPrefs.setCredentials(context,uid,client,accessToken);
    }

}
