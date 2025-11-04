package com.example.camaraderie;//

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class Event {

    private String eventName;
    private String eventLocation;
    private Date registrationDeadline;
    private String description;
    private Date eventDate;
    private String eventTime;  // this will probably become a better data type soon
    //private float price = 0.0f;
    private Waitlist waitlist = new Waitlist();
    private ArrayList<DocumentReference> selectedUsers = new ArrayList<>();
    private ArrayList<DocumentReference> acceptedUsers = new ArrayList<>();
    private int capacity;  // always > 0
    private DocumentReference hostDocRef;
    private DocumentReference eventDocRef;

    private String eventId;

    public Event() {}  // required for FIREBASE

    public Event(String eventName, String eventLocation, Date registrationDeadline, String description, Date eventDate, String eventTime, int capacity, DocumentReference host, String eventId) {
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.registrationDeadline = registrationDeadline;
        this.description = description;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.capacity = capacity;
        this.hostDocRef = host;
        this.eventId = eventId;

        //this.waitlist.setEventDocRef(this.EventId);  // bind the waitlist to this event
    }

    public Event(String eventName, Date registrationDeadline, String description, Date eventDate, String eventTime, int capacity, DocumentReference host, String eventId) {
        this.eventName = eventName;
        this.registrationDeadline = registrationDeadline;
        this.description = description;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.capacity = capacity;
        this.hostDocRef = host;
        this.eventId = eventId;

        //this.waitlist.setEventDocRef(this.EventId);  // bind the waitlist to this event
    }

//    //public float getPrice() {
//        return price;
//    }

//    //public void setPrice(float price) {
//        this.price = price;
//    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getRegistrationDeadline() {
        return registrationDeadline;
    }

    public void setRegistrationDeadline(Date registrationDeadline) {
        this.registrationDeadline = registrationDeadline;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventId() {
        return eventId;
    }
    public void setEventId(String eventId1) {
        this.eventId = eventId1;
    }

    public DocumentReference getHostDocRef() {
        return hostDocRef;
    }

    public Waitlist getWaitlist() {
        return waitlist;
    }

    public ArrayList<DocumentReference> getSelectedUsers() {
        return selectedUsers;
    }

    public ArrayList<DocumentReference> getAcceptedUsers() {
        return acceptedUsers;
    }

    public void addAcceptedUser(DocumentReference acceptedUser) {
        if (!this.acceptedUsers.contains(acceptedUser)) {
            this.acceptedUsers.add(acceptedUser);
        }
    }

    public void setEventDocRef(DocumentReference eventDocRef1) {
        this.eventDocRef = eventDocRef1;
    }

    public DocumentReference getEventDocRef() {
        return this.eventDocRef;
    }

    public void updateDB() {
        // update DB from event
    }
}
