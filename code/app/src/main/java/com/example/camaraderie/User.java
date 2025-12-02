package com.example.camaraderie;//

import static com.example.camaraderie.utilStuff.EventDeleter.deleteEvent;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * This is a class that defines a user. Admin privileges granted by setting admin to true.
 * @author fecici, RamizHHH, UmranRahman
 */
public class User implements Serializable {

    private String firstName;
    private String phoneNumber;
    private String email;
    private String address;
    private String userId;
    private String notificationToken;
    private DocumentReference docRef;

    private boolean admin = false;

    private ArrayList<DocumentReference> userCreatedEvents = new ArrayList<>();
    private ArrayList<DocumentReference> waitlistedEvents = new ArrayList<>();
    private ArrayList<DocumentReference> selectedEvents = new ArrayList<>();
    private ArrayList<DocumentReference> acceptedEvents = new ArrayList<>();
    private ArrayList<DocumentReference> cancelledEvents = new ArrayList<>();
    //geolocation
    private boolean geoEnabled = false;

    boolean useFirestore = true;
    private ArrayList<DocumentReference> userEventHistory = new ArrayList<>();

    private ArrayList<DocumentReference> pendingNotifications = new ArrayList<>();

    /**
     * Constructor for User
     * @param firstName
     *  First name of the user
     * @param phone
     *  Phone number of the user
     * @param email
     *  Email of the user
     * @param address
     *  Address of the user
     * @param userId
     *  Id that uniquely identifies the user
     * @param docref
     *  Pointer to an equivalent user document in Firebase
     */
    public User(@NonNull String firstName, @NonNull String phone, @NonNull String email, @NonNull String address, @NonNull String userId, String notificationToken, @NonNull DocumentReference docref) {
        this.firstName = firstName;
        this.phoneNumber = phone;
        this.email = email;
        this.address = address;
        this.userId = userId;
        this.notificationToken = notificationToken;
        this.docRef = docref;
    }

    /**
     * empty constructor for firebase to use
     */
    public User(){} // for firebase

    /**
     * Get the events the user has created
     * @return
     *  Return the list of events the user has created
     */
    public ArrayList<DocumentReference> getUserCreatedEvents() {
        return userCreatedEvents;
    }

    /**
     * Check if the user is an admin
     * @return
     *  Return true if user is an admin, false otherwise
     */
    public boolean isAdmin() {return admin;}


    /**
     * Set admin status of user
     * @param this_admin
     *  True if user should be admin. False otherwise
     */
    public void setAdmin(boolean this_admin) {  // we MANUALLY create admins for the app
        admin = this_admin;
        if (docRef != null) docRef.update("admin", this_admin);  // we need this because firebase might run this setter before it sets docRef
    }

    /**
     * Set the first name of the user
     * @param name
     *  New first name of the user
     */
    public void setFirstName(String name) {this.firstName = name;}

    /**
     * Set the phone number of the user
     * @param number
     *  New phone number of the user
     */
    public void setPhoneNumber(String number) {this.phoneNumber = number;}

    /**
     * Set email of the user
     * @param email1
     *  New email of the user
     */
    public void setEmail(String email1) {this.email = email1;}

    /**
     * Set address of the user
     * @param address1
     *  New address of the user
     */
    public void setAddress(String address1) {this.address = address1;}

    public void setNotificationToken(String token) {this.notificationToken = token;}

    /**
     * Get first name of the user
     * @return
     *  Return firstname of the user
     */

    public String getFirstName() {return this.firstName;}
    //public String getLastName() {return this.lastName;}

    /**
     * Get phone number of the user
     * @return
     *  Return phone number of the user
     */
    public String getPhoneNumber() {return this.phoneNumber;}

    /**
     * Get email of the user
     * @return
     *  Return email of the user
     */
    public String getEmail() {return this.email;}

    /**
     * Get address of the user
     * @return
     *  Return address of the user
     */
    public String getAddress() {return this.address;}

    /**
     * Get user id of the user
     * @return
     *  Return user id of the user
     */
    public String getUserId() {return this.userId;}

    /**
     * Get the docRef of the user
     * @return
     *  Return the docRef of the user
     */
    public DocumentReference getDocRef() { return this.docRef;}

    public String getNotificationToken() {return this.notificationToken;}
    /**
     * Set the docRef of the user
     * @param docRef1
     *  The docRef of the user
     */
    public void setDocRef(DocumentReference docRef1) {this.docRef = docRef1;}

    /**
     * delete a created event and update database
     * @param event
     *  The docRef of the event being deleted
     */
    public void deleteCreatedEvent(DocumentReference event) {
        if (!useFirestore) {
            // Skip Firestore and just remove from local list (for unit tests)
            userCreatedEvents.remove(event);
            return;
        }

        event.get().addOnSuccessListener(doc -> {
            Event e = doc.toObject(Event.class);
            if (e != null) {
                deleteEvent(e);
                Log.d("User", "Deleted user event");
            }
        }).addOnFailureListener(ee -> {
            Log.e("User", "deleteCreatedEvent: could not get event", ee);
        });
    }

    /**
     * NOT TO BE CONFUSED WITH `deleteCreatedEvent` - removes event from local memory list
     * @param event event to be removed
     */
    public void removeCreatedEvent(DocumentReference event) {
        this.userCreatedEvents.remove(event);
    }

    /**
     * remove an event from the selectedEvents list
     * @param selectedEvent DocRef of the event to remove from the selected events list
     */
    public void removeSelectedEvent(DocumentReference selectedEvent) {
        this.selectedEvents.remove(selectedEvent);
    }

    /**
     * update user in database
     */
    public void updateDB(Runnable onComplete) {
        // update the DB from the user

        HashMap<String, Object> data = new HashMap<>();

        data.put("firstName", firstName);
        data.put("phoneNumber", phoneNumber);
        data.put("email", email);
        data.put("address", address);
        data.put("notificationToken", notificationToken);

        data.put("admin", admin);

        data.put("userCreatedEvents", userCreatedEvents);
        data.put("waitlistedEvents", waitlistedEvents);
        data.put("selectedEvents", selectedEvents);
        data.put("acceptedEvents", acceptedEvents);
        data.put("cancelledEvents", cancelledEvents);
        data.put("pendingNotifications", pendingNotifications);
        data.put("geoEnabled", geoEnabled);
        data.put("userEventHistory", userEventHistory);

        this.docRef.set(data, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    Log.d("UserRepository", "User updated");
                    onComplete.run();
                })

                .addOnFailureListener(e ->
                {
                    Log.e("UserRepository", "Error updating user", e);
                    onComplete.run();
                });
    }

    /**
     * add event to accepted events list
     * @param eventDocRef event being added to list
     */
    public void addAcceptedEvent(DocumentReference eventDocRef) {
        if (!acceptedEvents.contains(eventDocRef)) {
            acceptedEvents.add(eventDocRef);
        }
    }

    public void addEventToHistory(DocumentReference eventDocRef) {
        if (!userEventHistory.contains(eventDocRef)) {
            userEventHistory.add(eventDocRef);
        }
    }

    public void removeEventFromHistory(DocumentReference eventDocRef) {
        userEventHistory.remove(eventDocRef);
    }

    public ArrayList<DocumentReference> getEventHistory() {
        return userEventHistory;
    }

    /**
     *
     * @return returns accepted events list
     */
    public ArrayList<DocumentReference> getAcceptedEvents() {
        return acceptedEvents;
    }

    /**
     *
     * @return selected events list
     */
    public ArrayList<DocumentReference> getSelectedEvents() {
        return selectedEvents;
    }

    /**
     * add event to selected events list
     * @param event event being added to selected events
     */
    public void addSelectedEvent(DocumentReference event) {
        if (!selectedEvents.contains(event)) {
            selectedEvents.add(event);
        }
    }


    /**
     *
     * @return returns waitlisted events list
     */
    public ArrayList<DocumentReference> getWaitlistedEvents() {
        return waitlistedEvents;
    }

    /**
     * add event to waitlist list
     * @param waitlistedEvent event being added to waitlist list
     */
    public void addWaitlistedEvent(DocumentReference waitlistedEvent) {
        if(!waitlistedEvents.contains(waitlistedEvent)){
            this.waitlistedEvents.add(waitlistedEvent);
        }
    }

    /**
     * remove event from waitlist
     * @param eventDocRef event docref being removed from waitlist list
     */
    public void removeWaitlistedEvent(DocumentReference eventDocRef) {
        waitlistedEvents.remove(eventDocRef);
    }

    /**
     * add event ref to created events list
     * @param eventRef ref of event being added to list
     */
    public void addCreatedEvent(DocumentReference eventRef) {
        if (!userCreatedEvents.contains(eventRef)) {
            userCreatedEvents.add(eventRef);
            if (useFirestore) {
                updateDB(() -> {
                });
            }
        }
    }

    //geolocation
    public boolean isGeoEnabled() {
        return geoEnabled;
    }

    public void setGeoEnabled(boolean geoEnabled) {
        this.geoEnabled = geoEnabled;
    }

    public ArrayList<DocumentReference> getCancelledEvents() {
        return cancelledEvents;
    }

    public void addCancelledEvent(DocumentReference cancelledEvent) {
        if (!cancelledEvents.contains(cancelledEvent)) {
            this.cancelledEvents.add(cancelledEvent);
        }
    }

    public void removeCancelledEvent(DocumentReference cancelledEvent) {
        cancelledEvents.remove(cancelledEvent);
    }

    public void setAcceptedEvents(ArrayList<DocumentReference> acceptedEvents) {
        this.acceptedEvents = acceptedEvents;
    }

    public void setCancelledEvents(ArrayList<DocumentReference> cancelledEvents) {
        this.cancelledEvents = cancelledEvents;
    }

    public void setSelectedEvents(ArrayList<DocumentReference> selectedEvents) {
        this.selectedEvents = selectedEvents;
    }

    public void setUserCreatedEvents(ArrayList<DocumentReference> userCreatedEvents) {
        this.userCreatedEvents = userCreatedEvents;
    }

    public void setWaitlistedEvents(ArrayList<DocumentReference> waitlistedEvents) {
        this.waitlistedEvents = waitlistedEvents;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<DocumentReference> getPendingNotifications() {
        return pendingNotifications;
    }

    public void setPendingNotifications(ArrayList<DocumentReference> pendingNotifications) {
        this.pendingNotifications = pendingNotifications;
    }

    public void setUserEventHistory(ArrayList<DocumentReference> userEventHistory) {
        this.userEventHistory = userEventHistory;
    }

    public ArrayList<DocumentReference> getUserEventHistory() {
        return userEventHistory;
    }

}


