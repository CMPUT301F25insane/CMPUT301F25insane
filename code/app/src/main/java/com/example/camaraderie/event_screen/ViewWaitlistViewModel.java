package com.example.camaraderie.event_screen;

import androidx.lifecycle.ViewModel;

import com.example.camaraderie.Event;
import com.example.camaraderie.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;

/**
 * viewmodel for the ViewWaitlistFragment class
 */
public class ViewWaitlistViewModel extends ViewModel {


    /**
     * Waitlist callback interface
     */
    public interface WaitlistCallback {
        void onUsersLoaded(ArrayList<User> users);
    }

    /**
     * empty constructor for ViewWaitlistViewModel
     */
    public ViewWaitlistViewModel() {

    }

    /**
     * functionality for kicking user. updates database for user and event.
     *
     * @param u          user to kick
     * @param event      event from which they are kicked
     * @param onComplete runnable listener implement by lambda for on-complete
     */
    public void kickUser(User u, Event event, Runnable onComplete) {
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
     *
     * @param event    event from which to load users
     * @param callback callback for when this finishes running (lambda function)
     */
    public void loadWaitlistedUsers(Event event, WaitlistCallback callback) {
        ArrayList<DocumentReference> refs = event.getWaitlist();
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
                e.printStackTrace();
            });
        }
    }
    /**
     * loads waitlisted users
     *
     * @param event    event from which to load users
     * @param callback callback for when this finishes running (lambda function)
     */

    public void loadCancelledUsers(Event event, OnUsersLoadedListener listener) {

        ArrayList<DocumentReference> cancelledRefs = event.getCancelledUsers();
        ArrayList<User> cancelledUsers = new ArrayList<>();

        if (cancelledRefs == null || cancelledRefs.isEmpty()) {
            listener.onLoaded(cancelledUsers);
            return;
        }

        for (DocumentReference ref : cancelledRefs) {
            ref.get().addOnSuccessListener(snapshot -> {
                User user = snapshot.toObject(User.class);
                if (user != null) {
                    cancelledUsers.add(user);
                }

                // When all users have been fetched, trigger callback
                if (cancelledUsers.size() == cancelledRefs.size()) {
                    listener.onLoaded(cancelledUsers);
                }
            });
        }
    }
}