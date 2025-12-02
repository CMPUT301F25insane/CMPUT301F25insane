package com.example.camaraderie.main;

import com.example.camaraderie.User;

import javax.inject.Singleton;

/**
 * UserRepository stores a user information for the duration of the app
 */

@Singleton
public class UserRepository {

    private static UserRepository instance;

    private static User user;

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }

        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user1) {user = user1;}

    public UserRepository() {
        instance = this;
    }
}
