package com.example.camaraderie.my_events;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.camaraderie.Event;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class MyEventsViewModel extends ViewModel {

    public interface MyEventsCallback{
        void onEventsLoaded(ArrayList<Event> events);
    }

    private ArrayList<Event> events = new ArrayList<>();

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

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
