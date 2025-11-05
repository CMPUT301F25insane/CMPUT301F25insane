package com.example.camaraderie.data;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppDataRepositorySecond {
    private String userDocPath;

    public String getSharedData() {
        return userDocPath;
    }

    public void setSharedData(String data) {
        this.userDocPath = data;
    }

    @Inject
    public AppDataRepositorySecond(){};


}
