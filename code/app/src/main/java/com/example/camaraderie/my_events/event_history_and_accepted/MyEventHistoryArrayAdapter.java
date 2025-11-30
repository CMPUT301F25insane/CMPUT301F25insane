package com.example.camaraderie.my_events.event_history_and_accepted;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.camaraderie.Event;
import com.example.camaraderie.R;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;

import java.util.ArrayList;

public class MyEventHistoryArrayAdapter extends ArrayAdapter<Event> {

    private FirebaseFirestore db;
    private NavController nav;

    public MyEventHistoryArrayAdapter(@NonNull Context context, ArrayList<Event> events) {
        super(context, 0, events);

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view;
        db = FirebaseFirestore.getInstance();

        Event event = getItem(position);

        if (convertView == null) {  // convert view is a reused view, to save resources
            // create new view using layout inflater if no recyclable view available
            view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_view_my_event_history_item, parent, false);

        } else {
            // just reuse the garbage view
            view = convertView;
        }

        return view;
    }
}
