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
 * The EventHelper class to handle some tasks the original event class doesn't have
 * @author Fecici
 */
public class EventHelper {

    /**
     * Setup the isOneDayBefore method to check if we are one day before or not
     * @param deadline event deadline
     * @param now current date
     * @return returns boolean true if the current date is a day before the deadline
     */
    public static boolean isOneDayBefore(Date deadline, Date now) {
        long diff = deadline.getTime() - now.getTime();
        long oneDayMillis = 24L * 60L * 60L * 1000L;
        return diff > 0 && diff <= oneDayMillis;
    }

    /**
     * The method finalizes the lists and removes any users from the selected list and also removes users from the waitlist
     * @param event event for which to finalize lists
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
     * handles the join function for the event
     * @param event event for user to join
     * @param onComplete on complete lambda if all goes well
     * @param onFailure onfailure lambda to handle an error
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
     * This method handles users unjoining the event and updates the database accordingly
     * @param event event to unjoin the user from
     * @param onComplete on complete lambda if all goes well
     * @param onFailure onfailure lambda to handle an error
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
