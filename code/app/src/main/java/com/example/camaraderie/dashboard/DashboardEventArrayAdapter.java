package com.example.camaraderie.dashboard;


import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.example.camaraderie.geolocation.AddUserLocation.addLocation;
import static com.example.camaraderie.main.MainActivity.user;
import static com.example.camaraderie.utilStuff.EventHelper.handleJoin;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.camaraderie.Event;
import com.example.camaraderie.R;
import com.example.camaraderie.geolocation.AddUserLocation;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.Date;

/**
 * DashboardEventArrayAdapter extends ArrayAdapter and is used to display events on the home screen
 * that the user can join, it formats events to be able to be used in the dashboard
 * */
public class DashboardEventArrayAdapter extends ArrayAdapter<Event> {

    public OnEventClickListener listener;
    private final Date date = new Date();

    /**
     * A default constructor
     * @param context
     *  Context of the activity
     * @param events
     *  List of events to be displayed
     */
    public DashboardEventArrayAdapter(@NonNull Context context, ArrayList<Event> events) {
        super(context, 0, events);
        setNotifyOnChange(true);
    }

    /**
     * A interface to setup onEventClick to be used later
     */
    public interface OnEventClickListener {
        /**
         * custom lambda callback with resulting event parameter
         * @param event loaded event
         */
        void onEventClick(Event event);
    }

    /**
     * getView to initialize the view of the events for this custom array adapter
     *      * It allows for use to implement the backend for our various buttons and text
     *      * We first set the view to the custom xml we have for each item, and so we inflate it
     *      * We grab the information from the xml like the eventName and eventDeadline fields and store
     *      * them in textview objects
     *      * We then set them to be the correct information
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return created item view
     */
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
        joinButton.setClickable(true);

        DocumentReference eventRef = event.getEventDocRef();

        /*
         * We go through each document reference in the waitlist for each user and
         * If their path is equal to the path of the current document, we say that the user
         * is in that waitlist and set the boolean to true
         */

        ArrayList<DocumentReference> userListUnion = new ArrayList<>();
        userListUnion.addAll(user.getWaitlistedEvents());
        userListUnion.addAll(user.getSelectedEvents());
        userListUnion.addAll(user.getAcceptedEvents());

        boolean userCannotJoinWaitlist = false;

        for (DocumentReference ref : userListUnion) {
            if (ref.getPath().equals(eventRef.getPath())) {
                Log.d("Dashboard Array Adapter", ref.getPath() + " | " + user.getDocRef().getPath());
                userCannotJoinWaitlist = true;
                break;
            }
        }

        if (event.getWaitlistLimit() != -1) {
            if (event.getWaitlist().size() + event.getSelectedUsers().size() >= event.getWaitlistLimit()) {
                userCannotJoinWaitlist = true;
            }
        }

        // reg date passed (filter this out?)
        if (event.getRegistrationDeadline().before(date)) {
            userCannotJoinWaitlist = true;
        }

        /*
         * If at any point the the boolean is true then we gray out the button because they
         * are not eligible to join that event
         */

        // organizer cannot join their own event because that is stupid
        if (user.getDocRef().equals(event.getHostDocRef())) {
            joinButton.setEnabled(false);
            joinButton.setVisibility(INVISIBLE);
        }

        else if (userCannotJoinWaitlist) {
            joinButton.setVisibility(VISIBLE);
            joinButton.setEnabled(false);
            joinButton.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }
        else {
            joinButton.setVisibility(VISIBLE);
            joinButton.setEnabled(true);
            joinButton.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.custom_join_button_color));  // original colour
        }

        /*
         * If the user we are looking at is the organizer of an event we set it to true
         */

        /*
         * We have a join button so that the user can join right then and there and not have to view the
         * description of the event
         */

        Button descButton = view.findViewById(R.id.seeDescButton);

        /*
         * When they click the join button, we first gray out the button and disable it so that
         * they cant join multiple times
         * We then add the user to the events waitlist and the local objects waitlist attribute as well
         * add the event to the user's registration history
         */

        joinButton.setOnClickListener(v ->
            addLocation(event, () -> {

                handleJoin(
                    event,

                    () -> {
                        joinButton.setClickable(false);
                        joinButton.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                        joinButton.setEnabled(false);
                    },

                    () -> {
                        Log.e("DashboardEventArrayAdapter", "failed to join event");
                        joinButton.setEnabled(true);
                        joinButton.setBackgroundTintList(
                                ContextCompat.getColorStateList(getContext(), R.color.custom_join_button_color)
                        );
                    });})

        );

        /*
         * We also have a see description button so that the user can see
         * the details about the event
         */

        // this might be a null ref, perhaps the code should just exist here?
        descButton.setOnClickListener(new View.OnClickListener() {
            /**
             * handles description button click
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                listener.onEventClick(event);
            }
        });

        return view;
    }
}
