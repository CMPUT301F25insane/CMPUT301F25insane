package com.example.camaraderie.event_screen;

import androidx.lifecycle.ViewModel;

import com.example.camaraderie.Event;
import com.example.camaraderie.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;

public class ViewWaitlistViewModel extends ViewModel {

    public interface WaitlistCallback {
        void onUsersLoaded(ArrayList<User> users);
    }

    public ViewWaitlistViewModel() {

    }

    public void kickUser(User u, Event event, Runnable onComplete) {
        DocumentReference userRef = u.getDocRef();
        DocumentReference eventRef = event.getEventDocRef();

        eventRef.update("waitlist", FieldValue.arrayRemove(userRef))
                .addOnSuccessListener(aVoid -> {

                    // Remove event from the User waitlistedEvents list
                    userRef.update("waitlistedEvents", FieldValue.arrayRemove(eventRef))
                            .addOnSuccessListener(aVoid2 -> {

                                // Update the local event object too, so UI stays consistent
                                event.getWaitlist().remove(userRef);

                                // Call UI callback to refresh adapter
                                onComplete.run();
                            });
                });
    }

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
}
