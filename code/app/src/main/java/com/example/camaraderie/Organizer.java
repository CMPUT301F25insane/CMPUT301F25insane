package com.example.camaraderie;//

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * This class extends user and handles the organizers for an event
 * */
public class Organizer extends User {

    private ArrayList<Event> events = new ArrayList<>();
    public Organizer(String firstName, String phone, String email, String address, String userId) {
        super(firstName,
                phone,
                email,
                address,
                userId);
    }


    public ArrayList<Event> getEvents() {
        return events;
    }

    public void addEvent(Event event) {
        this.events.add(event);
    }

    public void removeEvent(Event event) {
        this.events.remove(event);
    }

    
}
