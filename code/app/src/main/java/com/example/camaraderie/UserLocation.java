package com.example.camaraderie;

import com.google.firebase.firestore.DocumentReference;

public class UserLocation {
    private DocumentReference userRef;
    private double latitude;
    private double longitude;

    public UserLocation(DocumentReference userRef, double latitude, double longitude){
        this.userRef = userRef;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public UserLocation() {} // firebase
    
    public DocumentReference getUserDocRef() {
        return userRef;
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
