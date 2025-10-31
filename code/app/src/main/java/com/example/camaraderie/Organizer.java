package com.example.camaraderie;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Organizer extends User {

    private ArrayList<Event> events = new ArrayList<>();
    public Organizer(String firstName, String lastName, String phone, String email, String username, String address, String userId) {
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
