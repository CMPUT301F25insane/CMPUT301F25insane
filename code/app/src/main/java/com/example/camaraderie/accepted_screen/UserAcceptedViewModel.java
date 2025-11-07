package com.example.camaraderie.accepted_screen;

import static com.example.camaraderie.MainActivity.user;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.camaraderie.MainActivity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;

public class UserAcceptedViewModel extends ViewModel {

    // user accepts invitation
    public void userAcceptInvite(DocumentReference eventDocRef) {

        eventDocRef.update("acceptedList", FieldValue.arrayUnion(user.getDocRef()))
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "User added to acceptedList!"))
                .addOnFailureListener(e -> Log.e("Firestore", "Error adding user", e));

        eventDocRef.update("selectedList", FieldValue.arrayRemove(user.getDocRef()))
                .addOnSuccessListener(aVoid ->
                        Log.d("Firestore", "User removed from selectedList! (accepted invitation)"))
                .addOnFailureListener(e ->
                        Log.e("Firestore", "Error removing user", e));

        user.addAcceptedEvent(eventDocRef);  // add to accepted event
        user.removeSelectedEvent(eventDocRef);  // remove from selectedEvents list (no longer needed)
        user.updateDB();
    }

    // user rejects invitation
    public void userDeclineInvite(DocumentReference eventDocRef) {
        eventDocRef.update("selectedList", FieldValue.arrayRemove(user.getDocRef()))
                .addOnSuccessListener(aVoid ->
                        Log.d("Firestore", "User removed from selectedList! (declined invitation)"))
                .addOnFailureListener(e ->
                        Log.e("Firestore", "Error removing user", e));

        user.removeSelectedEvent(eventDocRef);
        user.updateDB();
    }

}
