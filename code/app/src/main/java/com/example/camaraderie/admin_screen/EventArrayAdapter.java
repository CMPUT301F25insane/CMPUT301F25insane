package com.example.camaraderie.admin_screen;

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
import androidx.navigation.NavController;

import com.example.camaraderie.R;
import com.example.camaraderie.Event;
import com.example.camaraderie.SharedEventViewModel;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * EventArrayAdapter extends ArrayAdapter to be used to list the events that the admin can view or delete
 */
public class EventArrayAdapter extends ArrayAdapter<Event> {

    FirebaseFirestore db;
    private NavController nav;
    SharedEventViewModel svm;
    ArrayList<Event> events;

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
        this.db = FirebaseFirestore.getInstance();
        this.events = events;

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

        event_name.setText(event.getEventName());

        deadline.setText(event.getRegistrationDeadline().toString());

        /**
         * We implement a join button
         */

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                join.setBackgroundColor(Color.GRAY);
                join.setEnabled(false);
                event.addWaitlistUser(user.getDocRef());
                user.addWaitlistedEvent(event.getEventDocRef());

                // update db
                user.updateDB();
                event.updateDB();
            }
        });

        description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //View profile

                svm.setEvent(event);
                nav.navigate(R.id.fragment_view_event_user);
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DocumentReference eventDocRef = event.getEventDocRef();
                db.collection("Users").get()
                        .addOnSuccessListener(snapshot -> {
                            for (DocumentSnapshot userDoc : snapshot.getDocuments()) {
                                DocumentReference uRef = userDoc.getReference();
                                uRef.update("waitlistedEvents", FieldValue.arrayRemove(eventDocRef));
                                uRef.update("selectedEvents", FieldValue.arrayRemove(eventDocRef));
                                uRef.update("acceptedEvents", FieldValue.arrayRemove(eventDocRef));
                            }

                            eventDocRef.delete();
                        });
            }
        });
        return view;
    }
}
