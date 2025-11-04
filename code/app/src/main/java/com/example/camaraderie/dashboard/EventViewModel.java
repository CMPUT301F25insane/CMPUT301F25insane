package com.example.camaraderie.dashboard;//

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.camaraderie.Event;

import java.util.ArrayList;

public class EventViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Event>> localEvents = new MutableLiveData<>();

    public LiveData<ArrayList<Event>> getLocalEvents() {
        return localEvents;
    }

    public void setLocalEvents(ArrayList<Event> events) {
        localEvents.setValue(events);
    }
}