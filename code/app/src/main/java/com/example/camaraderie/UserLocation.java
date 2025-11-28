package com.example.camaraderie;

import com.google.firebase.firestore.auth.User;

public class UserLocation {
    private String userID;
    private double latitude;
    private double longitude;

    public UserLocation(String userID, double latitude, double longitude){
        this.userID = userID;
        this.latitude = latitude;
        this.longitude = longitude;
    }

<<<<<<< HEAD
    public UserLocation(){}

=======
    public UserLocation() {} // firebase
    
>>>>>>> d4d8b837ecc3ed7c23ac97d906ef2993cce96fe3
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
