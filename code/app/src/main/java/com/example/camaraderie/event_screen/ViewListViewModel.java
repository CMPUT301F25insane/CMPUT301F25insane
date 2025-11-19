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
 */
public class ViewListViewModel extends ViewModel {


    /**
     * Waitlist callback interface
     */
    public interface ViewListCallback {
        void onUsersLoaded(ArrayList<User> users);
    }

    /**
     * empty constructor for ViewWaitlistViewModel
     */
    public ViewListViewModel() {

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
                if (result.size() >= refs.size()) {
                    callback.onUsersLoaded(result);
                }

            }).addOnFailureListener(e -> {
                Log.e("Firebase", "Error loading users from arraylist", e);
                e.printStackTrace();
            });
        }
    }

}