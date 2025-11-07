package com.example.camaraderie.admin_screen;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.camaraderie.R;
import com.example.camaraderie.Event;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class EventArrayAdaptor extends ArrayAdapter<Event> {

    FirebaseFirestore db;
    private NavController nav;
    ArrayList<Event> events;
    public EventArrayAdaptor(@NonNull Context context, ArrayList<Event> events, NavController nav){
        super(context, 0, events);
        this.db = FirebaseFirestore.getInstance();
        this.events = events;

        this.nav = nav;
    }

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

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //join function
            }
        });

        description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //View profile
                Bundle bundle = new Bundle();
                bundle.putString("eventDocRefPath", event.getEventDocRef().getPath());


                nav.navigate(R.id.action_admin_event_data_screen_to_fragment_view_event_user, bundle);
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
