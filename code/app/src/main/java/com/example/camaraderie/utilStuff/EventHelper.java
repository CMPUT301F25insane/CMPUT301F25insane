package com.example.camaraderie.utilStuff;


import static com.example.camaraderie.main.Camaraderie.getUser;
import static com.example.camaraderie.main.MainActivity.user;
import static com.example.camaraderie.my_events.LotteryRunner.sendNotificationsToEntrant;

import android.util.Log;

import com.example.camaraderie.Event;
import com.example.camaraderie.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;
import java.util.Date;

/**
 * Helper class with utility methods for handling Event operations.
 * <p>
 * Includes deadline checks, finalizing participant lists, and handling user join/unjoin operations.
 */
public class EventHelper {

    /**
     * Checks if the current time is within one day before a deadline.
     *
     * @param deadline the event deadline
     * @param now      current time
     * @return true if within one day before the deadline
     */
    public static boolean isOneDayBefore(Date deadline, Date now) {
        long diff = deadline.getTime() - now.getTime();
        long oneDayMillis = 24L * 60L * 60L * 1000L;
        return diff > 0 && diff <= oneDayMillis;
    }


    /**
     * Finalizes the participant lists of an event.
     * <p>
     * Moves selected users to event history, clears waitlists and selected lists, and updates Firestore.
     *
     * @param event the event to finalize
     */
    public static void finalizeLists(Event event) {

        ArrayList<DocumentReference> selected   = event.getSelectedUsers();
        ArrayList<DocumentReference> waitlist   = event.getWaitlist();

        // unselected users leave the selected list
        for (DocumentReference user : selected) {
            user.update("selectedEvents", FieldValue.arrayRemove(event.getEventDocRef()));
            user.update("eventHistory", FieldValue.arrayUnion(event.getEventDocRef()));  // add the event to te history
        }

        event.clearSelectedUsers();

        // remove from waitlist
        for (DocumentReference user : waitlist) {
            user.update("waitlistedEvents", FieldValue.arrayRemove(event.getEventDocRef()));
        }

        event.clearWaitlistedUsers();

        // update
        event.updateDB(() -> Log.d("EventHelper", "Finalized lists for " + event.getEventName()));
    }

    /**
     * handles join, updates database
     */
    public static void handleJoin(Event event, Runnable onComplete, Runnable onFailure) {

        event.getEventDocRef().update("waitlist", FieldValue.arrayUnion(user.getDocRef()))
                .addOnSuccessListener(v -> {
                    Log.d("Handle Join", "Waitlist updated for event");

                    event.addWaitlistUser(getUser().getDocRef());
                    user.addWaitlistedEvent(event.getEventDocRef());
                    user.addEventToHistory(event.getEventDocRef());

                    user.updateDB(onComplete);

                    sendNotificationsToEntrant(
                            event.getEventDocRef(),
                            user.getDocRef(),
                            "Event: " + event.getEventName(),
                            "You have joined the waiting list for this event."
                    );

                })
                .addOnFailureListener(e -> {
                    Log.e("Handle Join", "Waitlist failed to update", e);
                    onFailure.run();

                });

    }

    /**
     * handles unjoin, updates database
     */
    public static void handleUnjoin(Event event, Runnable onComplete, Runnable onFailure) {



        event.getEventDocRef().update("waitlist", FieldValue.arrayRemove(user.getDocRef()))
                .addOnSuccessListener(v -> {

                    event.removeWaitlistUser(user.getDocRef());
                    user.removeWaitlistedEvent(event.getEventDocRef());
                    user.removeEventFromHistory(event.getEventDocRef());


                    user.updateDB(onComplete);
                    sendNotificationsToEntrant(
                            event.getEventDocRef(),
                            user.getDocRef(),
                            "Event: " + event.getEventName(),
                            "You have left the waiting list for this event."
                    );
                })
                .addOnFailureListener(e -> {
                    Log.e("Event Handler", "Failed to unjoin user", e);
                    onFailure.run();
                });

    }

}
