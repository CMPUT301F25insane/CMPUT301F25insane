package com.example.camaraderie.my_events;

import static com.example.camaraderie.MainActivity.user;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.camaraderie.Event;
import com.example.camaraderie.R;

import java.util.ArrayList;

public class ViewMyEventsArrayAdapter extends ArrayAdapter<Event> {

    private OnEventClickListener listener;


    public interface OnEventClickListener {
        void onEventClick(Event event);
    }
    public ViewMyEventsArrayAdapter(@NonNull Context context, int resource, ArrayList<Event> myEvents) {
        super(context, resource, myEvents);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {  // convert view is a reused view, to save resources
            // create new view using layout inflater if no recyclable view available

            view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_main_event_dashboard_item, parent, false);
        }
        else {
            // just reuse the garbage view
            view = convertView;
        }

        Event event = getItem(position);
        TextView eventName = view.findViewById(R.id.eventName);
        //TextView eventPrice = view.findViewById(R.id.eventPrice);
        TextView eventDeadline = view.findViewById(R.id.eventDeadline);
        //TextView hostName = view.findViewById(R.id.hostName);

        eventName.setText(event.getEventName());
        //eventPrice.setText(String.valueOf(event.getPrice()));
        eventDeadline.setText(event.getRegistrationDeadline().toString());
        //hostName.setText(event.getHost().getUsername());

        Button deleteButton = view.findViewById(R.id.joinButton);
        Button descButton = view.findViewById(R.id.seeDescButton);

        deleteButton.setText("Delete");
        deleteButton.setBackgroundColor(Color.RED);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.deleteCreatedEvent(event.getEventDocRef());
            }
        });

        // this might be a null ref, perhaps the code should just exist here?
        descButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEventClick(event);
            }
        });


        return view;
    }
}
