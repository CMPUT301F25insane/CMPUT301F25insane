package com.example.camaraderie.my_events;

import static android.view.View.INVISIBLE;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;

import com.example.camaraderie.Event;
import com.example.camaraderie.R;
import com.example.camaraderie.SharedEventViewModel;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * Event array adapter for events created by the user. They can delete from here.
 */

public class MyCreatedEventsArrayAdapter extends ArrayAdapter<Event> {

    private NavController nav;
    private FirebaseFirestore db;
    private SharedEventViewModel svm;

    /**
     * constructor for MyCreatedEventArrayAdapter
     * @param context context
     * @param resource usually set to 0
     * @param events events list
     * @param nav passed in nav controller
     * @param svm passed sharedeventviewmodel
     */
    public MyCreatedEventsArrayAdapter(@NonNull Context context, int resource, ArrayList<Event> events, NavController nav, SharedEventViewModel svm) {
        super(context, resource, events);
        this.nav = nav;
        db = FirebaseFirestore.getInstance();
        this.svm = svm;
    }

    /**
     * set bindings for join, desc, and remove
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return constructed view
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

       join.setEnabled(false);
       join.setVisibility(INVISIBLE);

//        join.setBackgroundColor(Color.RED);
//        join.setText("Remove");

        event_name.setText(event.getEventName());

        deadline.setText(event.getRegistrationDeadline().toString());

        description.setOnClickListener(new View.OnClickListener() {
            /**
             * set svm to this event and navigate to fragment_organizer_view_event
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                //View profile
                svm.setEvent(event);


                nav.navigate(R.id._fragment_organizer_view_event);
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            /**
             * remove logic for deleting event
             * @param v The view that was clicked.
             */
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
                            remove(event);
                            notifyDataSetChanged();
                        });
            }
        });

        return view;
    }
}
