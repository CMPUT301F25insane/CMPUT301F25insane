package com.example.camaraderie.my_events;

import static com.example.camaraderie.MainActivity.user;

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

    public void getUserCreatedEvents(MyEventsCallback onComplete) {

        ArrayList<DocumentReference> refs = user.getUserCreatedEvents();
        if (refs.isEmpty()) {
            return;
        }

        for (DocumentReference ref : refs) {
            ref.get().addOnSuccessListener(doc -> {
                Event e = doc.toObject(Event.class);
                if (e != null) {
                    events.add(e);
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
