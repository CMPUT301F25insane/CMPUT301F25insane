package com.example.camaraderie;//

import android.net.Uri;
import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.SetOptions;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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
    private ArrayList<DocumentReference> cancelledUsers = new ArrayList<>();

    private boolean geoEnabled = false;
    private ArrayList<HashMap<String, Object>> userLocationArrayList = new ArrayList<>();

    private int capacity;  // always > 0
    private int waitlistLimit = -1;
    private DocumentReference hostDocRef;
    private DocumentReference eventDocRef;

    private String imageUrl;

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
     *  User reference for who is organizing the event
     * @param eventDocRef
     * event docref that points to the event in the database
     * @param eventId
     *  Id that uniquely identifies the event
     * @param geoEnabled
     *  enable or disable the geolocation requirement for the event
     *
     */
    public Event(String eventName, String eventLocation, Date registrationDeadline, String description, Date eventDate, String eventTime, int capacity, int waitlistLimit, DocumentReference host, DocumentReference eventDocRef, String eventId, boolean geoEnabled) {
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.registrationDeadline = registrationDeadline;
        this.description = description;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.capacity = capacity;
        this.waitlistLimit = waitlistLimit;
        this.hostDocRef = host;
        this.eventDocRef = eventDocRef;
        this.eventId = eventId;
        this.geoEnabled = geoEnabled;
    }

    //location getters and setters
    public boolean isGeoEnabled() {
        return geoEnabled;
    }
    public void setGeoEnabled(boolean geoEnabled) {
        this.geoEnabled = geoEnabled;
    }

    public ArrayList<HashMap<String, Object>> getLocationArrayList() {
        return userLocationArrayList;
    }

    public void setLocationArrayList(ArrayList<HashMap<String, Object>> userLocationArrayList){
        this.userLocationArrayList = userLocationArrayList;
    }

    public void addLocationArrayList(HashMap<String, Object> location) {
        if (userLocationArrayList == null) {
            userLocationArrayList = new ArrayList<>();
        }
        this.userLocationArrayList.add(location);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String url) {
        this.imageUrl = url;
    }
    //logic needed for map

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

    /**
     *
     * @return returns list of accepted users
     */
    public ArrayList<DocumentReference> getAcceptedUsers() {
        return acceptedUsers;
    }

    /**
     *
     * @return returns list of cancelled users
     */
    public ArrayList<DocumentReference> getCancelledUsers() {
        return cancelledUsers;
    }

    /**
     * adds user to accepted list
     * @param acceptedUser user to add to accepted list
     */
    public void addAcceptedUser(DocumentReference acceptedUser) {
        if (!this.acceptedUsers.contains(acceptedUser)) {
            this.acceptedUsers.add(acceptedUser);
        }
    }
    /**
     * adds user to cancelled list
     * @param user to add to cancelled list
     */
    public void addCancelledUser(DocumentReference user) {
        if (!cancelledUsers.contains(user)) {
            cancelledUsers.add(user);
        }
    }

    /**
     * set the event docref
     * @param eventDocRef1 new docref to set
     */
    public void setEventDocRef(DocumentReference eventDocRef1) {
        this.eventDocRef = eventDocRef1;
    }

    /**
     *
     * @return returns event doc ref
     */
    public DocumentReference getEventDocRef() {
        return this.eventDocRef;
    }

    /**
     * updates event in database
     */
    public void updateDB(Runnable onComplete) {
        //TODO: this bitch is broken lol idk why

        HashMap<String, Object> data = new HashMap<>();
        data.put("eventName", eventName);
        data.put("eventLocation", eventLocation);
        data.put("registrationDeadline", registrationDeadline);
        data.put("description", description);
        data.put("eventDate", eventDate);
        data.put("eventTime", eventTime);

        data.put("imageUrl", imageUrl);
        data.put("waitlist", waitlist);
        data.put("selectedUsers", selectedUsers);
        data.put("acceptedUsers", acceptedUsers);
        data.put("cancelledUsers", cancelledUsers);

        data.put("geoEnabled", geoEnabled);
        data.put("userLocationArrayList", userLocationArrayList);

        data.put("capacity", capacity);
        data.put("waitlistLimit", waitlistLimit);
        data.put("imageUrl", imageUrl);

        eventDocRef.set(data, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "Successfully update event");
                    onComplete.run();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Failed to update event");
                    onComplete.run();
                });
    }

    /**
     * adds user to waitlist
     * @param user user to add to waitlist
     */
    public void addWaitlistUser(DocumentReference user) {
        if (!waitlist.contains(user)) {
            waitlist.add(user);
        }
    }
    /**
     * removes user from cancelled list
     * @param user user that is removed from cancelled list
     */
    public void removeCancelledUser(DocumentReference user) {
        cancelledUsers.remove(user);
    }

    /**
     * removes user from waitlsit
     * @param user user that is removed
     */
    public void removeWaitlistUser(DocumentReference user) {
        waitlist.remove(user);
    }

    /**
     * sets event descrition
     * @param description new description to set
     */
    public void setEventDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return returns optional waitlist limit
     */
    public int getWaitlistLimit() {
        return waitlistLimit;
    }

    /**
     * sets waitlist limit
     * @param waitlistLimit new waitlist limit
     */
    public void setWaitlistLimit(int waitlistLimit) {
        if (waitlistLimit < 1) {
            this.waitlistLimit = -1;  // set to default
            return;
        }
        this.waitlistLimit = waitlistLimit;
    }

    public void clearSelectedUsers() {
        this.selectedUsers.clear();
    }

    public void clearWaitlistedUsers() {
        this.waitlist.clear();
    }

}
