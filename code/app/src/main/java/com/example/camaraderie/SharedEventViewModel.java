package com.example.camaraderie;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedEventViewModel extends ViewModel {

    private final MutableLiveData<Event> selectedEvent = new MutableLiveData<>();

    public void setEvent(Event e) {
        selectedEvent.setValue(e);
    }

    public LiveData<Event> getEvent() {
        return selectedEvent;
    }
}
