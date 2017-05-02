package com.example.androidappbase.model;


public class User {
    public int id;
    public String email;
    public String nickname;
    public String uid;

    public User(String uid, String email){
        this.uid = uid;
        this.email = email;
    }
}

