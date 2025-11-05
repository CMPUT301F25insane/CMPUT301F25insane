package com.example.camaraderie;//

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class handles the waitlist for an event
 * ONLY GETS CREATED BY EVENT CLASS
 * */
/*ONLY GETS CREATED BY EVENT CLASS*/

/**
 * This is a class that defines a waitlist for an event
 * @author fecici
 */
public class Waitlist {

    private ArrayList<DocumentReference> waitlist = new ArrayList<>();
    private String eventId;

    /**
     * Adds a user to the waitList
     * @param user
     *  User to be added to the waitlist
     */
    public void addUserToWaitlist(DocumentReference user) {
        if (!waitlist.contains(user)) {
            waitlist.add(user);
        }
    }

    /**
     * Removes a user from the waitlist
     * @param user
     *  User to be removed from the waitlist
     */
    public void removeUserFromWaitlist(DocumentReference user) {
        this.waitlist.remove(user);  // does nothing if user not in waitlist
    }

    /**
     * Select a random user from the waitlist
     * @return
     *  Randomly selected user from the waitlist
     */
    public DocumentReference randomSelectUser() {
        int rand = new Random().nextInt(getSize());
        return waitlist.get(rand);
    }

    /**
     * Calls on randomSelectUser and removes the user from the waitlist
     * @return
     *  Return user to be removed from the waitlist
     */
    public DocumentReference randomSelectUserAndRemove() {
        DocumentReference user = randomSelectUser();
        waitlist.remove(user);
        return user;
    }

    /**
     * Get the number of users signed up to the waitlist
     * @return
     *  Return the number of users signed up to the waitlist
     */
    public int getSize() {
        return this.waitlist.size();
    }

    /**
     * Get the id of the event the waitlist belongs to
     * @return
     *  Return the event id
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * Get the event the waitlist belongs to
     * @return
     *  Return the event
     */
    public DocumentReference getEvent() {
        return FirebaseFirestore.getInstance().collection("events").document(eventId);
    }

    /**
     * Set the id of the event the waitlist belongs to
     * @param eventId
     *  New event id
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     * Get the waitlist
     * @return
     *  Return the waitlist
     */
    public ArrayList<DocumentReference> getWaitlist() {return this.waitlist;}
}
