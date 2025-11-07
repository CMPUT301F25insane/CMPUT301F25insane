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

import com.example.camaraderie.R;
import com.example.camaraderie.Event;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class EventArrayAdaptor extends ArrayAdapter<Event> {

    FirebaseFirestore db;
    ArrayList<Event> events;
    public EventArrayAdaptor(@NonNull Context context, ArrayList<Event> events){
        super(context, 0, events);
        this.db = FirebaseFirestore.getInstance();
        this.events = events;
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

        TextView event_name = convertView.findViewById(R.id.eventName);
        TextView host_name = convertView.findViewById(R.id.RegistrationDeadline);

        Button join = convertView.findViewById(R.id.joinButton);
        Button description = convertView.findViewById(R.id.seeDescButton);
        Button remove = convertView.findViewById(R.id.RemoveButton);

        event_name.setText(event.getEventName());

        host_name.setText(event.getRegistrationDeadline().toString());

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
                bundle.putString("eventDocRefPath", event.getEventId());

                NavController navController = Navigation.findNavController(v);
                //navController.navigate(R.id.list_to_detail_view, bundle);
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Events")
                        .document(event.getEventId())
                        .delete();
            }
        });
        return view;
    }
}
