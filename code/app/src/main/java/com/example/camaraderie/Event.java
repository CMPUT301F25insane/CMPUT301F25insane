package com.example.camaraderie;//

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

/**
 * This is a class that defines an event
 * @author tahmid-parvez, swausbeard, fecici
 */
public class Event {

    private String eventName;
    private String eventLocation;
    private Date registrationDeadline;
    private String description;
    private Date eventDate;
    private String eventTime;  // this will probably become a better data type soon
    //private float price = 0.0f;
    private Waitlist waitlist = new Waitlist();
    private ArrayList<User> selectedUsers = new ArrayList<>();
    private int capacity;  // always > 0
    private DocumentReference hostDocRef;

    private String EventId;

    /**
     * Empty constructor for event, necessary for Firebase integration
     */
    public Event() {}  // required for FIREBASE

    /**
     * Constructor for event
     * @param eventName
     *  Name of the event
     * @param eventLocation
     *  Location of the event
     * @param registrationDeadline
     *  Deadline date for the event
     * @param description
     *  Description of the event
     * @param eventDate
     *  Date the event takes place
     * @param eventTime
     *  Time (in hours) that the event takes place
     * @param capacity
     *  Maximum number of people that can be accepted to the event
     * @param host
     *  User who is organizing the event
     * @param eventId
     *  Id that uniquely identifies the event
     */
    public Event(String eventName, String eventLocation, Date registrationDeadline, String description, Date eventDate, String eventTime, int capacity, DocumentReference host, String eventId) {
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.registrationDeadline = registrationDeadline;
        this.description = description;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.capacity = capacity;
        this.hostDocRef = host;
        this.EventId = eventId;

        this.waitlist.setEventId(this.EventId);  // bind the waitlist to this event
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
        return EventId;
    }

    public DocumentReference getHost() {
        return hostDocRef;
    }

    public Waitlist getWaitlist() {
        return waitlist;
    }

    public ArrayList<User> getSelectedUsers() {
        return selectedUsers;
    }
}
