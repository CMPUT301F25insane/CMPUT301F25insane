package com.example.camaraderie.event_screen;

import androidx.lifecycle.ViewModel;

import com.example.camaraderie.Event;
import com.example.camaraderie.User;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class ViewCancelledUsers extends ViewModel {

    public interface Cancelledlistcallback {
        void onUsersLoaded(ArrayList<User> users);
    }

    /**
     * empty constructor for ViewCancelledUsers
     */
    public ViewCancelledUsers() {

    }

    /**
     * loads Cancelled users
     *
     * @param event    event from which to load users
     * @param callback callback for when this finishes running (lambda function)
     */
    public void loadCancelledUsers(Event event, Cancelledlistcallback callback) {

        ArrayList<DocumentReference> cancelledRefs = event.getCancelledUsers();
        ArrayList<User> cancelledUsers = new ArrayList<>();

        if (cancelledRefs == null || cancelledRefs.isEmpty()) {
            callback.onUsersLoaded(cancelledUsers);
            return;
        }
        final int total = cancelledRefs.size();

        for (DocumentReference ref : cancelledRefs) {
            ref.get().addOnSuccessListener(snapshot -> {
                User user = snapshot.toObject(User.class);
                if (user != null) {
                    cancelledUsers.add(user);
                }
                // Only fire callback when ALL fetches are finished
                if (cancelledUsers.size() == total) {
                    callback.onUsersLoaded(cancelledUsers);
                }

                // When all users have been fetched, trigger callback
                if (cancelledUsers.size() == cancelledRefs.size()) {
                    callback.onUsersLoaded(cancelledUsers);
                }
            }).addOnFailureListener(e -> {
                if (cancelledUsers.size() == total) {
                    callback.onUsersLoaded(cancelledUsers);
                }
            });
            }
        }
}

