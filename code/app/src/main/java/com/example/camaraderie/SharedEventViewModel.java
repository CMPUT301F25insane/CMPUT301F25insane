package com.example.camaraderie;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * view model to store the current event being examined by the application.
 * fragments can use getter to get and inflate based on event state,
 * and can use setters to set the current event state to be worked on.
 * this is easier and more efficient than passing docrefs through bundles,
 * and it avoids repeated calls to the database.
 */
public class SharedEventViewModel extends ViewModel {

    private final MutableLiveData<Event> selectedEvent = new MutableLiveData<>();

    /**
     * set current event
     * @param e event being set
     */
    public void setEvent(Event e) {
        selectedEvent.setValue(e);
    }

    /**
     * gets the current event
     * @return returns the currently-focused event
     */
    public LiveData<Event> getEvent() {
        return selectedEvent;
    }
}
