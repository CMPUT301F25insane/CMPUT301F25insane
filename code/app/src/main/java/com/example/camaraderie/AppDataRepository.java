package com.example.camaraderie;

import com.google.firebase.firestore.DocumentReference;

import javax.inject.Singleton;

@Singleton
public class AppDataRepository {
    private String sharedData;
    private DocumentReference sharedDocRef;

    public String getSharedData() {
        return sharedData;
    }

    public void setSharedData(String data) {
        this.sharedData = data;
    }

    public DocumentReference getSharedDocRef() {
        return sharedDocRef;
    }

    public void setSharedDocRef(DocumentReference docRef) {
        this.sharedDocRef = docRef;
    }


}
