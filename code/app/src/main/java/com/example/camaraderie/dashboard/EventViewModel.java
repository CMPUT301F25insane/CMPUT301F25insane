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
 * EventViewModel extends ViewModel and is used for transferring data and making it easier for the fragments and dialogs
 * to communicate when it comes to the database
 * */
public class EventViewModel extends ViewModel {

    private FirebaseFirestore db;
    private CollectionReference eventsRef;
    private CollectionReference usersRef;

    private ListenerRegistration listener;
    private MutableLiveData<ArrayList<Event>> localEvents = new MutableLiveData<>();

    /**
     * We have a standard constructor that initializes all the information we need
     * to communicate with the database
     * In this constructor we first initialize the database and get the instance
     * We then set the collection references
     * We then setup a listener and we initialize the events with a new array list
     * and we add each event to the events array list to be used later
     */

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

    /**
     * We also have a getLocalEvents function which just returns local events
     * @return
     * We just return a LiveData list of our list of events
     */

    public LiveData<ArrayList<Event>> getLocalEvents() {
        return localEvents;
    }

    /**
     * We also have a onCleared method which just removes the listener so we dont blow up the database with thousands of
     * queries
     */

    @Override
    protected void onCleared() {
        listener.remove();
    }

}