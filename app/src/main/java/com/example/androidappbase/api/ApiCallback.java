package com.example.androidappbase.api;

import android.content.Context;
import android.widget.Toast;


import com.example.androidappbase.model.Error;
import com.example.androidappbase.util.LogUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public abstract class ApiCallback<T> implements Callback<T> {

    Context mContext;

    public ApiCallback(Context context){
        mContext = context;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if(response.isSuccessful()){
            ApiClient.saveCredentials(mContext,response.headers());
            onSuccess(response);
        }else{
            onError(response);
        }
    }

    abstract public void onSuccess(Response<T> response);

    public void onError(Response<T> response){
        LogUtils.d("Full json res wrapped in gson => "+(new Gson().toJson(response)));

        if (response.errorBody() != null) {

            ApiClient client = new ApiClient(mContext);
            Converter<ResponseBody, Error> errorConverter = client.getRetrofit().responseBodyConverter(Error.class,new Annotation[0]);

            try {
                Error error = errorConverter.convert(response.errorBody());
                Toast.makeText(mContext,error.message,Toast.LENGTH_SHORT).show();
                LogUtils.d(error.message);
            } catch (IOException e) {
                LogUtils.e(e);
                e.printStackTrace();
            }

            return;
        }

    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        LogUtils.e(t);
    }
}
