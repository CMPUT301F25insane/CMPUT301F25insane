package com.example.camaraderie.event_screen;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.camaraderie.Event;
import com.example.camaraderie.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;

/**
 * general viewmodel for handling data for waitlist, selected, accepted, and cancelled lists
 * @author Fecici
 */
public class ViewListViewModel extends ViewModel {

    private ArrayList<User> waitlist = new ArrayList<>();
    private ArrayList<User> acceptedEvents = new ArrayList<>();
    private ArrayList<User> selectedEvents = new ArrayList<>();
    private ArrayList<User> cancelledEvents = new ArrayList<>();
    private Event event;

    /**
     * Waitlist callback interface
     */
    public interface ViewListCallback {
        /**
         * custom on loaded callback for user lists
         * @param users users loaded from list
         */
        void onUsersLoaded(ArrayList<User> users);
    }

    /**
     * sets the currently focues event
     * @param event event to focus
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * gets the current event
     * @return current event focues on
     */
    public Event getEvent() {
        return event;
    }

    /**
     * gets event capacity
     * @return event capacity of focused event
     */
    public int getEventCapacity() {
        return event.getCapacity();
    }

    /**
     * empty constructor for ViewWaitlistViewModel
     * probably not needed but its not grey so idk lol
     */
    public ViewListViewModel() {

    }

    /**
     * gets the user list required by the enum type
     * @param type enum type of list
     * @return specified user list
     */
    public ArrayList<User> getList(UserListType type) {

        switch (type) {
            case WAITLIST:
                return waitlist;

            case ACCEPTEDLIST:
                return acceptedEvents;

            case SELECTEDLIST:
                return selectedEvents;

            case CANCELLEDLIST:
                return cancelledEvents;

            default:
                return new ArrayList<User>();
        }

    }

    /**
     * functionality for kicking user. updates database for user and event.
     *
     * @param u          user to kick
     * @param onComplete runnable listener implement by lambda for on-complete
     */
    public void kickUser(User u, Runnable onComplete) {
        DocumentReference userRef = u.getDocRef();
        DocumentReference eventRef = event.getEventDocRef();

        eventRef.update("waitlist", FieldValue.arrayRemove(userRef))
            .addOnSuccessListener(aVoid -> {

                userRef.update("waitlistedEvents", FieldValue.arrayRemove(eventRef))
                    .addOnSuccessListener(aVoid2 -> {

                        // Update the local event object too, so UI stays consistent
                        event.getWaitlist().remove(userRef);

                        // Call UI callback to refresh adapter
                        onComplete.run();
                    });
            });
    }

    /**
     * loads waitlisted users
     * @param refs     references from which to load the users
     * @param callback callback for when this finishes running (lambda function)
     */
    public void loadUsersFromList(ArrayList<DocumentReference> refs, ViewListCallback callback) {
        //ArrayList<DocumentReference> refs = event.getWaitlist();
        ArrayList<User> result = new ArrayList<>();

        if (refs.isEmpty()) {
            callback.onUsersLoaded(result);
            return;
        }

        for (DocumentReference ref : refs) {
            ref.get().addOnSuccessListener(documentSnapshot -> {
                User user = documentSnapshot.toObject(User.class);
                result.add(user);

                // Check if all users are loaded
                if (result.size() == refs.size()) {
                    callback.onUsersLoaded(result);
                }

            }).addOnFailureListener(e -> {
                Log.e("Firebase", "Error loading users from arraylist", e);
                e.printStackTrace();
            });
        }
    }

    /**
     * gets event waitlist
     * @return waitlist
     */
    public ArrayList<User> getWaitlist() {return this.waitlist;}

    /**
     * gets event selected users list
     * @return event selected users list
     */
    public ArrayList<User> getSelectedList() {return this.selectedEvents;}

    /**
     * gets event accepted list
     * @return event accepted list
     */
    public ArrayList<User> getAcceptedList() {return this.acceptedEvents;}

    /**
     * gets event cancelled list
     * @return event cancelled list
     */
    public ArrayList<User> getCancelledList() {return this.cancelledEvents;}

    /**
     * generates all user lists of associated event
     * @param onComplete oncomplete lambda callback
     */
    public void generateAllLists(Runnable onComplete) {

        loadUsersFromList(event.getWaitlist(), waitlistResult -> {

            this.waitlist = waitlistResult;

            loadUsersFromList(event.getSelectedUsers(), selectedResult -> {

                this.selectedEvents = selectedResult;

                loadUsersFromList(event.getAcceptedUsers(), acceptedResult -> {

                    this.acceptedEvents = acceptedResult;

                    loadUsersFromList(event.getCancelledUsers(), cancelledResult -> {

                        this.cancelledEvents = cancelledResult;

                        onComplete.run();

                    });
                });
            });
        });
    }
}