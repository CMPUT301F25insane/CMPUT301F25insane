package com.example.camaraderie.accepted_screen;

import static com.example.camaraderie.main.MainActivity.user;
import static com.example.camaraderie.my_events.LotteryRunner.runLottery;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.camaraderie.Event;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.Objects;

/** This is the view model meant to ease the transferring of data between the fragment and array adapter
 * It extends the ViewModel class and we use it to implement custom methods to make our code easier and cleaner to
 * Use data
 */

public class UserAcceptedViewModel extends ViewModel {

    // user accepts invitation

    /**
     * userAcceptInvite takes in one parameter which is a Firestore Document Reference
     * This method is meant to run the backend code that is needed to allow the user to accept an invite and
     * store them in the proper spot in the database
     * @param eventDocRef
     * The method first updates the acceptedList of the event document reference and puts the user in there and it
     * also removes the user from the selectedList
     * It also does the same thing locally with the objects
     * It does not return
     */
    public void userAcceptInvite(DocumentReference eventDocRef) {

        WriteBatch batch = FirebaseFirestore.getInstance().batch();
        user.addAcceptedEvent(eventDocRef);  // add to accepted event
        user.removeSelectedEvent(eventDocRef);  // remove from selectedEvents list (no longer needed)

        batch.update(eventDocRef, "acceptedList", FieldValue.arrayUnion(user.getDocRef()));
        batch.update(eventDocRef, "selectedList", FieldValue.arrayRemove(user.getDocRef()));

        user.updateDB(() -> {
            batch.commit()
                    .addOnSuccessListener(aVoid -> {
                        Log.d("Firestore", "User added to acceptedList!");
                        Log.d("Firestore", "User removed from selectedList! (accepted invitation)");
                    })
                    .addOnFailureListener(e -> Log.e("Firestore", "Error updating user lists", e));
        });
    }

    /**
     * This method allows for the user to decline events that they have been drawn for
     * It works by updating the events selected list and removing them
     * This function uses firebase to do so and we also remove the local user object's
     * selected event list
     * @param eventDocRef
     * We dont return anything
     */

    // user rejects invitation
    public void userDeclineInvite(DocumentReference eventDocRef) {
        user.removeSelectedEvent(eventDocRef);

        WriteBatch batch = FirebaseFirestore.getInstance().batch();
        batch.update(eventDocRef, "selectedList", FieldValue.arrayRemove(user.getDocRef()));
        batch.update(eventDocRef, "cancelledList", FieldValue.arrayUnion(user.getDocRef()));

        user.updateDB(() -> {
            batch.commit()
                    .addOnSuccessListener(aVoid -> {

                        Log.d("Firestore", "User removed from selectedList! (declined invitation)");

                        // rerun lottery
                        eventDocRef.get()
                                .addOnSuccessListener(doc -> {
                                    runLottery(Objects.requireNonNull(doc.toObject(Event.class)));
                                });

                    })
                    .addOnFailureListener(e ->
                            Log.e("Firestore", "Error removing user", e));
        });
    }

    /**
     * This function checks to ensure if the selected events arraylist is empty or not
     * @return bool on whether or not the selected events is empty
     */

    public boolean allInvitesResolved() {
        return user.getSelectedEvents().isEmpty();
    }

}
