package com.example.camaraderie;//

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Collection;

public class User {

    private String firstName;
    private String phoneNumber;
    private String email;
    private String address;
    private String userId;
    private DocumentReference docRef;

    private boolean admin = false;

    //private String bankNumber;  // REQUIRED to sign up for events, but not to create account (we probably don't need this)
    private ArrayList<DocumentReference> events = new ArrayList<>();  // USERS created events
    private ArrayList<DocumentReference> selectedEvents = new ArrayList<>();
    private ArrayList<DocumentReference> acceptedEvents = new ArrayList<>();

    public User(String firstName, String phone, String email, String address, String userId, DocumentReference docref) {
        this.firstName = firstName;
        this.phoneNumber = phone;
        this.email = email;
        this.address = address;
        this.userId = userId;
        this.docRef = docref;
    }

    public ArrayList<DocumentReference> getEvents() {
        return events;
    }

    public void addEvent(DocumentReference event) {

        if (!this.events.contains(event)) {
            this.events.add(event);
        }
    }

    public void removeEvent(DocumentReference event) {
        this.events.remove(event);
    }


    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean this_admin) {  // we MANUALLY create admins for the app
        admin = this_admin;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public void setPhoneNumber(String number) {
        this.phoneNumber = number;
    }

    public void setEmail(String email1) {
        this.email = email1;
    }

    public void setAddress(String address1) {
        this.address = address1;
    }

    public String getFirstName() {
        return this.firstName;
    }

    //public String getLastName() {return this.lastName;}
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getEmail() {
        return this.email;
    }

    public String getAddress() {
        return this.address;
    }

    public String getUserId() {
        return this.userId;
    }

    public DocumentReference getDocRef() {
        return this.docRef;
    }

    public void setDocRef(DocumentReference docRef1) {
        this.docRef = docRef1;
    }

    public ArrayList<DocumentReference> getSelectedEvents() {
        return selectedEvents;
    }

    public void addSelectedEvent(DocumentReference selectedEvent) {
        if (!this.selectedEvents.contains(selectedEvent)) {
            this.selectedEvents.add(selectedEvent);
        }
    }

    public ArrayList<DocumentReference> getAcceptedEvents() {
        return acceptedEvents;
    }

    public void addAcceptedEvent(DocumentReference acceptedEvent) {
        if (!this.acceptedEvents.contains(acceptedEvent)) {
            this.acceptedEvents.add(acceptedEvent);
        }
    }

    public void removeAcceptedEvent(DocumentReference acceptedEvent) {
        this.acceptedEvents.remove(acceptedEvent);
    }

    public void deleteEvent(DocumentReference event) {
        this.events.remove(event);
        event.delete();  // from db
    }

    public void removeSelectedEvent(DocumentReference selectedEvent) {
        this.selectedEvents.remove(selectedEvent);
    }

    public void updateDB() {
        // update the DB from the user
        this.docRef.set(this, SetOptions.merge())
                .addOnSuccessListener(aVoid ->
                        Log.d("UserRepository", "User updated"))
                .addOnFailureListener(e ->
                        Log.e("UserRepository", "Error updating user", e));
    }

}

