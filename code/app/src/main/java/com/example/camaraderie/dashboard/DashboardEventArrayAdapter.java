package com.example.camaraderie.dashboard;


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
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

/**
 *This is the class that handles the events in a dashboard by creating them and adding functionality to each event item
 * */
public class DashboardEventArrayAdapter extends ArrayAdapter<Event> {

    public OnEventClickListener listener;

    /**
     *
     * @param context
     *  Context of the activity
     * @param events
     *  List of events to be displayed
     */
    public DashboardEventArrayAdapter(@NonNull Context context, ArrayList<Event> events) {
        super(context, 0, events);
    }

    public interface OnEventClickListener {
        void onEventClick(Event event);
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

        Button joinButton = view.findViewById(R.id.joinButton);

        String path = event.getEventDocRef().getPath();

        boolean userInWaitlist = false;
        System.out.println(event.getEventId());
        for (DocumentReference ref : user.getWaitlistedEvents()) {
            if (ref.getPath().equals(path)) {
                System.out.println(ref.getPath() + " | " + user.getDocRef().getPath());  // TODO: make this a log
                userInWaitlist = true;
                break;
            }
        }

        if (!userInWaitlist) {
            for (DocumentReference ref : user.getSelectedEvents()) {
                if (ref.getPath().equals(path)) {
                    userInWaitlist = true;
                    break;
                }
            }
        }

        if (!userInWaitlist) {
            for (DocumentReference ref : user.getAcceptedEvents()) {
                if (ref.getPath().equals(path)) {
                    userInWaitlist = true;
                    break;
                }
            }
        }

        // organizer cannot join their own event because that is stupid
        if (user.getDocRef().equals(event.getHostDocRef())) {
            userInWaitlist = true;  // change the name later, who cares
        }

        if (event.getWaitlistLimit() != -1) {
            if (event.getWaitlist().size() >= event.getWaitlistLimit()) {
                userInWaitlist = true;
            }
        }

        if (userInWaitlist) {
            joinButton.setEnabled(false);
            joinButton.setBackgroundColor(Color.GRAY);
        }
        else {
            joinButton.setEnabled(true);
            joinButton.setBackgroundColor(Color.parseColor("#AAF2C8"));  // original colour
        }

        Button descButton = view.findViewById(R.id.seeDescButton);

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinButton.setBackgroundColor(Color.GRAY);
                joinButton.setEnabled(false);
                event.addWaitlistUser(user.getDocRef());
                user.addWaitlistedEvent(event.getEventDocRef());

                // update db
                user.updateDB();
                event.updateDB();
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
