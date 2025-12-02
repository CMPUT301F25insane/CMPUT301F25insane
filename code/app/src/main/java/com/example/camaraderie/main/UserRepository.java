package com.example.camaraderie.main;

import com.example.camaraderie.User;

import javax.inject.Singleton;

/**
 * UserRepository stores a user information for the duration of the app
 * @author Fecici
 */
@Singleton
public class UserRepository {

    private static UserRepository instance;

    private static User user;

    /**
     * defines a singleton pattern for the repository
     * @return gets the repository instance
     */
    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }

        return instance;
    }

    /**
     * current user of application
     * @return User object of current user
     */
    public User getUser() {
        return user;
    }

    /**
     * sets the current user
     * @param user1 new user to set
     */
    public void setUser(User user1) {user = user1;}

    /**
     * constructor for UserRepository, sets the instance
     * to itself
     */
    public UserRepository() {
        instance = this;
    }
}
