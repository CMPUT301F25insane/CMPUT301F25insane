package com.example.camaraderie.data;

import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppDataRepository {
    private String userDocPath;

    public String getSharedData() {
        Log.d("tries","to get data");
        return userDocPath;
    }

    public void setSharedData(String data) {
        this.userDocPath = data;
        Log.d("sets","at least");
    }

    @Inject
    public AppDataRepository(){
    };


}