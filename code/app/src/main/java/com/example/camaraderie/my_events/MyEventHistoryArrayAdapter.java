package com.example.camaraderie.my_events;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.camaraderie.Event;
import com.example.camaraderie.R;

import static com.example.camaraderie.main.MainActivity.user;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class MyEventHistoryArrayAdapter extends ArrayAdapter<Event> {
    public MyEventHistoryArrayAdapter(@NonNull Context context, ArrayList<Event> events) {
        super(context, 0, events);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {  // convert view is a reused view, to save resources
            // create new view using layout inflater if no recyclable view available
            view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_view_my_events_item, parent, false);

        } else {
            // just reuse the garbage view
            view = convertView;
        }

        return view;
    }
}
