package com.example.camaraderie;//

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.SetOptions;

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
    private String eventDateTime;  // this will probably become a better data type soon
    //private float price = 0.0f;

    private String eventDeadlineTime;
    private ArrayList<DocumentReference> waitlist = new ArrayList<>();
    private ArrayList<DocumentReference> selectedUsers = new ArrayList<>();
    private ArrayList<DocumentReference> acceptedUsers = new ArrayList<>();
    private ArrayList<DocumentReference> cancelledUsers = new ArrayList<>();
    private ArrayList<DocumentReference> notificationLogs = new ArrayList<>();

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
     * @param eventDateTime
     *  Time (in hours) that the event takes place
     * @param capacity
     *  Maximum number of people that can be accepted to the event
     * @param host
     *  User reference for who is organizing the event
     * @param eventDocRef
     * event docRef that points to the event in the database
     * @param eventId
     *  Id that uniquely identifies the event
     * @param geoEnabled
     *  enable or disable the geolocation requirement for the event
     *
     */
    public Event(String eventName, String eventLocation, Date registrationDeadline, String description, Date eventDate, String eventDateTime, String eventDeadlineTime, int capacity, int waitlistLimit, DocumentReference host, DocumentReference eventDocRef, String eventId, boolean geoEnabled) {
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.registrationDeadline = registrationDeadline;
        this.description = description;
        this.eventDate = eventDate;
        this.eventDateTime = eventDateTime;
        this.eventDeadlineTime = eventDeadlineTime;
        this.capacity = capacity;
        this.waitlistLimit = waitlistLimit;
        this.hostDocRef = host;
        this.eventDocRef = eventDocRef;
        this.eventId = eventId;
        this.geoEnabled = geoEnabled;
    }

    //location getters and setters

    /**
     * gets the geolocation requirement
     * @return boolean true if geolocation is required
     */
    public boolean isGeoEnabled() {
        return geoEnabled;
    }

    /**
     * sets the geolocation requirement for the event
     * @param geoEnabled the new geolocation requirement state
     */
    public void setGeoEnabled(boolean geoEnabled) {
        this.geoEnabled = geoEnabled;
    }

    /**
     * gets the coordinates of users in the waitlist
     * @return arraylist of the hashmap of user locations, indexed by user id
     */
    public ArrayList<HashMap<String, Object>> getUserLocationArrayList() {
        return userLocationArrayList;
    }

    /**
     * sets the user location array for firebase
     * @param userLocationArrayList the new userlocation list
     */
    public void setUserLocationArrayList(ArrayList<HashMap<String, Object>> userLocationArrayList) {
        if (userLocationArrayList == null) {
            userLocationArrayList = new ArrayList<>();
        }
        this.userLocationArrayList = userLocationArrayList;
    }

    /**
     * adds a new location to the location list
     * @param location new location being added
     */
    public void addUserLocationArrayList(HashMap<String, Object> location) {
        if (userLocationArrayList == null) {
            userLocationArrayList = new ArrayList<>();
        }
        this.userLocationArrayList.add(location);
    }

    /**
     * gets the image url in firebase storage
     * @return the image url for the event poster
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * sets the event image url for the firebase storage
     * @param url new url to set
     */
    public void setImageUrl(String url) {
        this.imageUrl = url;
    }

    /**
     * Get maximum capacity of the event
     * @return capacity of the event
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
    public String getEventDateTime() {
        return eventDateTime;
    }

    /**
     * Set time of the event
     * @param eventDateTime
     *  New time of the event
     */
    public void setEventDateTime(String eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    /**
     * gets event deadline time
     * @return deadline time
     */
    public String getEventDeadlineTime() { return this.eventDeadlineTime;}

    /**
     * sets event deadline time
     * @param eventDeadlineTime new event deadline time
     */
    public void setEventDeadlineTime(String eventDeadlineTime) {this.eventDeadlineTime = eventDeadlineTime;}
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
     * sets the host reference for the event fore firebase integration
     * @param ref new reference of host
     */
    public void setHostDocRef(DocumentReference ref) {this.hostDocRef = ref;}

    /**
     * sets the new accepted users list
     * @param acceptedUsers new accepted users list to set
     */
    public void setAcceptedUsers(ArrayList<DocumentReference> acceptedUsers) {
        this.acceptedUsers = acceptedUsers;
    }

    /**
     * sets new cancelled users for the event
     * @param cancelledUsers new cancelled users to set
     */
    public void setCancelledUsers(ArrayList<DocumentReference> cancelledUsers) {
        this.cancelledUsers = cancelledUsers;
    }

    /**
     * sets the event id
     * @param eventId new event id to set
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     * sets the new selected users list
     * @param selectedUsers new selected users list
     */
    public void setSelectedUsers(ArrayList<DocumentReference> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

    /**
     * sets the new event waitlist
     * @param waitlist new waitlist to set
     */
    public void setWaitlist(ArrayList<DocumentReference> waitlist) {
        this.waitlist = waitlist;
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
     * gets list of accepted users for this event
     * @return returns list of accepted users
     */
    public ArrayList<DocumentReference> getAcceptedUsers() {
        return acceptedUsers;
    }

    /**
     * gets list of cancelled users for this event
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
     * gets the events doc ref for firebase
     * @return returns event doc ref
     */
    public DocumentReference getEventDocRef() {
        return this.eventDocRef;
    }

    /**
     * updates event in database
     * @param onComplete on complete runnable after database has updated
     */
    public void updateDB(Runnable onComplete) {
        //TODO: this bitch is broken lol idk why

        HashMap<String, Object> data = new HashMap<>();
        data.put("eventName", eventName);
        data.put("eventLocation", eventLocation);
        data.put("registrationDeadline", registrationDeadline);
        data.put("description", description);
        data.put("eventDate", eventDate);
        data.put("eventDateTime", eventDateTime);
        data.put("eventDeadlineTime", eventDeadlineTime);

        data.put("imageUrl", imageUrl);
        data.put("waitlist", waitlist);
        data.put("selectedUsers", selectedUsers);
        data.put("acceptedUsers", acceptedUsers);
        data.put("cancelledUsers", cancelledUsers);

        data.put("geoEnabled", geoEnabled);
        data.put("userLocationArrayList", userLocationArrayList);

        data.put("capacity", capacity);
        data.put("waitlistLimit", waitlistLimit);

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
     * sets event description
     * @param description new description to set
     */
    public void setEventDescription(String description) {
        this.description = description;
    }

    /**
     * gets the optional waitlist limit
     * @return optional waitlist limit
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

    /**
     * clears the selected users list
     */
    public void clearSelectedUsers() {
        this.selectedUsers.clear();
    }

    /**
     * clears the waitlist users list
     */
    public void clearWaitlistedUsers() {
        this.waitlist.clear();
    }

    /**
     * returns the logs of all notifications associated with this event
     * @return list of event notification document references for firebase collection
     */
    public ArrayList<DocumentReference> getNotificationLogs() {
        return notificationLogs;
    }

    /**
     * sets the notification logs arraylist
     * @param notificationLogs new arraylist to be set
     */
    public void setNotificationLogs(ArrayList<DocumentReference> notificationLogs) {
        this.notificationLogs = notificationLogs;
    }
}