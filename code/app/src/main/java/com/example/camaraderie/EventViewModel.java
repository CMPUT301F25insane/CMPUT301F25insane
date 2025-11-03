package com.example.camaraderie;//

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;


/**
 * This class handles the display for the events dashboard
 * */
public class EventViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Event>> localEvents = new MutableLiveData<>();

    public LiveData<ArrayList<Event>> getLocalEvents() {
        return localEvents;
    }

    public void setLocalEvents(ArrayList<Event> events) {
        localEvents.setValue(events);
    }
}
