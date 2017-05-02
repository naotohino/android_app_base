package com.example.androidappbase.api;

import com.example.androidappbase.model.User;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiService {
    @GET("api/v1/users")
    Call<List<User>> getUsers();

    @FormUrlEncoded
    @POST("/api/v1/auth/sign_in")
    Call<User> signIn(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("/api/v1/auth")
    Call<User> signUp(@Query("email") String email,
                                @Field("nickname") String nickname,
                                @Field("password") String password,
                                @Field("password_confirmation") String password_confirmation);


    @Multipart
    @POST("api/v1/user_profile_images")
    Call<User> postUserProfileImage(@Part MultipartBody.Part image);

    @FormUrlEncoded
    @PUT("api/v1/profiles")
    Call<User> updateProfile(@Field("nickname") String nickname);


}
