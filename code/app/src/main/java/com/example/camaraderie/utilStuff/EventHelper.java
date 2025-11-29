package com.example.camaraderie.utilStuff;


import static com.example.camaraderie.main.Camaraderie.getUser;
import static com.example.camaraderie.main.MainActivity.user;

import android.util.Log;

import com.example.camaraderie.Event;
import com.example.camaraderie.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;
import java.util.Date;

public class EventHelper {

    public static boolean isOneDayBefore(Date deadline, Date now) {
        long diff = deadline.getTime() - now.getTime();
        long oneDayMillis = 24L * 60L * 60L * 1000L;
        return diff > 0 && diff <= oneDayMillis;
    }


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
                })
                .addOnFailureListener(e -> {
                    Log.e("Event Handler", "Failed to unjoin user", e);
                    onFailure.run();
                });

    }

}
