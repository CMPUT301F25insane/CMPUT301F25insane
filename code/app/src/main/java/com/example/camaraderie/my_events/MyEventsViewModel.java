package com.example.camaraderie.my_events;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.camaraderie.Event;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

/**
 * Viewmodel for the user associated events
 * @author Fecici
 */
public class MyEventsViewModel extends ViewModel {

    /**
     * custom callback interface for MyEventsViewModel
     */
    public interface MyEventsCallback{
        /**
         * user defined lambda signature for when events have been loaded
         * @param events resulting events list
         */
        void onEventsLoaded(ArrayList<Event> events);
    }

    private ArrayList<Event> events = new ArrayList<>();

    public ArrayList<Event> getEvents() {
        return events;
    }

    /**
     * sets the viewmodels events
     * @param events events list to focus on
     */
    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    /**
     * loads users from events list
     * @param refs Document References list to load users from
     * @param onComplete onComplete lambda to handle on completion
     */
    public void getUserEventsFromList(ArrayList<DocumentReference> refs, MyEventsCallback onComplete) {
        events.clear();

        if (refs.isEmpty()) {
            Log.d("MyEventsViewModel", "User has no events in list");
            onComplete.onEventsLoaded(new ArrayList<>());
            return;
        }

        for (DocumentReference ref : refs) {
            ref.get().addOnSuccessListener(doc -> {
                Event e = doc.toObject(Event.class);
                if (e != null) {

                    events.add(e);
                }
                else {
                    Log.e("Firebase", "EVENT " + ref + " COULD NOT BE LOADED FROM USER");
                }

                if (events.size() == refs.size()) {
                    onComplete.onEventsLoaded(events);
                }
            }).addOnFailureListener(err -> {
                Log.e("Firebase", "failed to get events", err);
                err.printStackTrace();
            });
        }
    }

}
