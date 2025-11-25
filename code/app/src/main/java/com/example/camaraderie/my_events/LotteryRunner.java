package com.example.camaraderie.my_events;

import com.example.camaraderie.Event;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;

import java.util.Random;

public class LotteryRunner {

    /**
     * lottery system, runs while waitlist is nonempty and selectedList size is less than capacity.
     * updates database, updates UI
     */
    public static void runLottery(Event event) {
        Random r = new Random();

        while (event.getSelectedUsers().size() + event.getAcceptedUsers().size() < event.getCapacity() &&
                !event.getWaitlist().isEmpty()) {

            int index = r.nextInt(event.getWaitlist().size());
            DocumentReference userRef = event.getWaitlist().get(index);

            event.getWaitlist().remove(userRef);
            event.getSelectedUsers().add(userRef);

            // Update user document lists
            userRef.update("waitlistedEvents", FieldValue.arrayRemove(event.getEventDocRef()));
            userRef.update("selectedEvents", FieldValue.arrayUnion(event.getEventDocRef()));
        }

        event.updateDB();
    }
}
