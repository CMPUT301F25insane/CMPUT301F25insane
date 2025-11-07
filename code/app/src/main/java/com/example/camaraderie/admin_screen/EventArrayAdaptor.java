package com.example.camaraderie.admin_screen;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

        TextView event_name = view.findViewById(R.id.eventName);
        TextView host_name = view.findViewById(R.id.RegistrationDeadline);

        Button join = view.findViewById(R.id.joinButton);
        Button description = view.findViewById(R.id.seeDescButton);
        Button remove = view.findViewById(R.id.RemoveButton);

        event_name.setText(event.getEventName());

        host_name.setText(event.getRegistrationDeadline().toString());

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //join function
                Toast.makeText(getContext(), "Join feature not implemented yet", Toast.LENGTH_SHORT).show();
            }
        });

        description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //View profile
                Bundle bundle = new Bundle();
                bundle.putString("eventId", event.getEventId());

                NavController navController = Navigation.findNavController(v);
                //navController.navigate(R.id.list_to_detail_view, bundle);
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Events")
                        .document(event.getEventId())
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(getContext(), "Event deleted", Toast.LENGTH_SHORT).show();
                            events.remove(position);
                            notifyDataSetChanged();
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(getContext(), "Failed to delete: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                        );
            }
        });
        return view;
    }
}
