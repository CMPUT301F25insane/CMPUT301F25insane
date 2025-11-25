package com.example.camaraderie.my_events;

import static com.example.camaraderie.main.MainActivity.user;
import static com.example.camaraderie.my_events.LotteryRunner.runLottery;

import android.util.Log;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.camaraderie.Event;
import com.example.camaraderie.SharedEventViewModel;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.Date;

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
            Log.d("Firebase", "User has no created events");
            return;
        }

        for (DocumentReference ref : refs) {
            ref.get().addOnSuccessListener(doc -> {
                Event e = doc.toObject(Event.class);
                if (e != null) {
                    if (filterPassedEvents(e)) {
                        runRegistrationDeadline(e);
                        events.add(e);
                    }
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

    private void runRegistrationDeadline(Event event) {

        // run the lottery automatically when the deadline passes
        Date date = new Date();
        if (event.getRegistrationDeadline().before(date)) {
            return;
        }

        runLottery(event);

    }

    private Boolean filterPassedEvents(Event event) {
        Date date = new Date();
        if (event.getEventDate().after(date)) {
            return false;
        }

        return true;
    }
}
