package com.example.camaraderie;

import java.sql.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class Event {

    private String eventName;
    private String eventLocation;
    private String registrationDeadline;
    private String description;
    private Date eventDate;
    private String eventTime;  // this will probably become a better data type soon
    private float price = 0.0f;
    //private ArrayList<User> userWaitlist = new ArrayList<>();
    //private ArrayList<User>
    private int capacity;  // always > 0
    private final User host;

    private final String EventId;

    public Event(String eventName, String eventLocation, String registrationDeadline, String description, Date eventDate, String eventTime, int capacity, User host, String eventId) {
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.registrationDeadline = registrationDeadline;
        this.description = description;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.capacity = capacity;
        this.host = host;
        this.EventId = eventId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

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

    public String getRegistrationDeadline() {
        return registrationDeadline;
    }

    public void setRegistrationDeadline(String registrationDeadline) {
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

    public User getHost() {
        return host;
    }
}
