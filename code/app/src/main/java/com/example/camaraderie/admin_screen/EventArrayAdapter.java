package com.example.camaraderie.admin_screen;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.example.camaraderie.main.MainActivity.user;
import static com.example.camaraderie.utilStuff.EventDeleter.deleteEvent;
import static com.example.camaraderie.utilStuff.EventHelper.handleJoin;

import android.content.Context;
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
import androidx.navigation.NavController;

import com.example.camaraderie.R;
import com.example.camaraderie.Event;
import com.example.camaraderie.SharedEventViewModel;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Date;

/**
 * EventArrayAdapter extends ArrayAdapter to be used to list the events that the admin can view or delete
 */
public class EventArrayAdapter extends ArrayAdapter<Event> {


    private final NavController nav;
    private final SharedEventViewModel svm;
    private final Date date = new Date();

    /**
     * This is a constructor that initializes all the required attributes for the array adapter to to function how we
     * want it to
     * @param context
     * @param events
     * @param nav
     * @param svm
     */
    public EventArrayAdapter(@NonNull Context context, ArrayList<Event> events, NavController nav, SharedEventViewModel svm){
        super(context, 0, events);

        this.nav = nav;
        this.svm = svm;
    }

    /**
     * getView is used for initializing the view of events for our custom display of events
     * We initialize the view using the layout inflater
     * We set all the textviews to the values of the events like their name, registration deadline etc
     * @param position
     * @param convertView
     * @param parent
     * @return
     * We return a view for each list item
     */

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_admin_events_view_item, parent, false);
        }

        Event event = getItem(position);
        if (event == null) {
            return view;
        }

        TextView event_name = view.findViewById(R.id.eventName);
        TextView deadline = view.findViewById(R.id.RegistrationDeadline);

        Button join = view.findViewById(R.id.joinButton);
        Button description = view.findViewById(R.id.seeDescButton);
        Button remove = view.findViewById(R.id.RemoveButton);

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
                Log.d("Admin Event Array Adapter", ref.getPath() + " | " + user.getDocRef().getPath());
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
            join.setEnabled(false);
            join.setVisibility(INVISIBLE);
        }

        else if (userCannotJoinWaitlist) {
            join.setVisibility(VISIBLE);
            join.setEnabled(false);
            join.setBackgroundColor(Color.GRAY);
        }
        else {
            join.setVisibility(VISIBLE);
            join.setEnabled(true);
            join.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.custom_join_button_color));  // original colour
        }

        event_name.setText(event.getEventName());

        deadline.setText(event.getRegistrationDeadline().toString());

        join.setOnClickListener(v ->

            handleJoin(
                event,
                () -> {
                    join.setBackgroundColor(Color.GRAY);
                    join.setEnabled(false);
                    join.setClickable(false);},
                () -> {
                    Log.e("AdminEventArrayAdapter", "Failed to join admin to event");
                }
            )

        );

        description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //View profile

                svm.setEvent(event);
                nav.navigate(R.id.fragment_view_event_user);
            }
        });

        remove.setOnClickListener(v -> {
                deleteEvent(event);
        });

        return view;
    }
}
