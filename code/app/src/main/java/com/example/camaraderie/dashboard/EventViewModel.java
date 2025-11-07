package com.example.camaraderie.dashboard;//

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.camaraderie.Event;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;


/**
 * This class handles the display for the events dashboard
 * */
public class EventViewModel extends ViewModel {

    private FirebaseFirestore db;
    private CollectionReference eventsRef;
    private CollectionReference usersRef;

    private ListenerRegistration listener;
    private MutableLiveData<ArrayList<Event>> localEvents = new MutableLiveData<>();

    public EventViewModel() {
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("Events");
        usersRef = db.collection("Users");

        // get database events (this is fine, we don't have that many entries)
        listener = eventsRef.addSnapshotListener((querySnapshot, error) -> {
            if (error != null) {
                Log.e("Firebase", "Listener error has occurred", error);
                throw new RuntimeException("Listener failed in EventViewModel");
            }
            ArrayList<Event> events = new ArrayList<>();
            assert querySnapshot != null;
            for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                Event event = doc.toObject(Event.class);
                events.add(event);
            }

            localEvents.setValue(events);

        });
    }

    public LiveData<ArrayList<Event>> getLocalEvents() {
        return localEvents;
    }

    @Override
    protected void onCleared() {
        listener.remove();
    }

}