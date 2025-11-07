package com.example.camaraderie;//

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.sql.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

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
    private ArrayList<DocumentReference> waitlist = new ArrayList<>();
    private ArrayList<DocumentReference> selectedUsers = new ArrayList<>();
    private ArrayList<DocumentReference> acceptedUsers = new ArrayList<>();
    private int capacity;  // always > 0
    private DocumentReference hostDocRef;
    private DocumentReference eventDocRef;

    private String eventId;

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
    public Event(String eventName, String eventLocation, Date registrationDeadline, String description, Date eventDate, String eventTime, int capacity, DocumentReference host, DocumentReference eventDocRef, String eventId) {
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.registrationDeadline = registrationDeadline;
        this.description = description;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.capacity = capacity;
        this.hostDocRef = host;
        this.eventDocRef = eventDocRef;
        this.eventId = eventId;

        //this.waitlist.setEventDocRef(this.EventId);  // bind the waitlist to this event
    }




//    //public float getPrice() {
//        return price;
//    }

//    //public void setPrice(float price) {
//        this.price = price;
//    }

    /**
     * Get maximum capacity of the event
     * @return
     *  return capacity of the event
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Set maximum capacity of the event
     * @param capacity
     *  New capacity of the event
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Get time of the event
     * @return
     *  Return time of the event
     */
    public String getEventTime() {
        return eventTime;
    }

    /**
     * Set time of the event
     * @param eventTime
     *  New time of the event
     */
    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    /**
     * Get date of the event
     * @return
     *  Return date of the event
     */
    public Date getEventDate() {
        return eventDate;
    }

    /**
     * Set date of the event
     * @param eventDate
     *  New date of the event
     */
    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    /**
     * Get description of the event
     * @return
     *  Return description of the event
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set description of the event
     * @param description
     *  New description of the event
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get registration deadline of the event
     * @return
     *  Return registration deadline of the event
     */
    public Date getRegistrationDeadline() {
        return registrationDeadline;
    }

    /**
     * Set registration deadline of the event
     * @param registrationDeadline
     *  New registration deadline of the event
     */
    public void setRegistrationDeadline(Date registrationDeadline) {
        this.registrationDeadline = registrationDeadline;
    }

    /**
     * Get location of the event
     * @return
     *  Return location of the event
     */
    public String getEventLocation() {
        return eventLocation;
    }

    /**
     * Set location of the event
     * @param eventLocation
     *  New location of the event
     */
    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    /**
     * Get name of the event
     * @return
     *  Return name of the event
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Set name of the event
     * @param eventName
     *  New name of the event
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * Get id of the event
     * @return
     *  Return id of the event
     */
    public String getEventId() {
        return eventId;
    }
    public void setEventId(String eventId1) {
        this.eventId = eventId1;
    }

    /**
     * Get organizer of the event
     * @return
     *  Return organizer of the event
     */
    public DocumentReference getHostDocRef() {
        return hostDocRef;
    }

    /**
     * Get the event waitlist
     * @return
     *  Return the event waitlist
     */
    public ArrayList<DocumentReference> getWaitlist() {
        return waitlist;
    }

    /**
     * Get the selected users of the event
     * @return
     *  Return the selected users of the event
     */
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
        eventDocRef.set(this, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Successfully update event"))
                .addOnFailureListener(e -> Log.e("Firestore", "Failed to update event"));
    }

    public void addWaitlistUser(DocumentReference user) {
        if (!waitlist.contains(user)) {
            waitlist.add(user);
        }
    }

    public void runLottery() {

        for (int i = 0; i < capacity; i++) {
            if (!waitlist.isEmpty()) {
                System.out.println(capacity);
                int rand = new Random().nextInt(waitlist.size());
                System.out.println(rand);
                DocumentReference randUser = waitlist.get(rand);
                selectedUsers.add(randUser);
                waitlist.remove(randUser);

                // update user fields
                randUser.update("waitlistedEvents", FieldValue.arrayRemove(eventDocRef));
                randUser.update("selectedEvents", FieldValue.arrayUnion(eventDocRef));
            }
        }

    }

    public void removeWaitlistUser(DocumentReference user) {
        waitlist.remove(user);
    }

    public void setEventDescription(String description) {
        this.description = description;
    }
}
