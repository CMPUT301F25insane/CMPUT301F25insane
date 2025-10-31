package com.example.camaraderie;//

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

/*ONLY GETS CREATED BY EVENT CLASS*/
public class Waitlist {

    private ArrayList<DocumentReference> waitlist = new ArrayList<>();
    private String eventId;

    public void addUserToWaitlist(DocumentReference user) {
        if (!waitlist.contains(user)) {
            waitlist.add(user);
        }
    }

    public void removeUserFromWaitlist(DocumentReference user) {
        this.waitlist.remove(user);  // does nothing if user not in waitlist
    }

    public DocumentReference randomSelectUser() {
        int rand = new Random().nextInt(getSize());
        return waitlist.get(rand);
    }

    public DocumentReference randomSelectUserAndRemove() {
        DocumentReference user = randomSelectUser();
        waitlist.remove(user);
        return user;
    }

    public int getSize() {
        return this.waitlist.size();
    }

    public String getEventId() {
        return eventId;
    }
    public DocumentReference getEvent() {
        return FirebaseFirestore.getInstance().collection("events").document(eventId);
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public ArrayList<DocumentReference> getWaitlist() {return this.waitlist;}
}
