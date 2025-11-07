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
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_admin_events_view_item, parent, false);
        }
        else {
            // just reuse the garbage view
            view = convertView;
        }

        Event event = getItem(position);
        TextView event_name = convertView.findViewById(R.id.);
        Button profile = convertView.findViewById(R.id.UserProfileButton);
        Button remove = convertView.findViewById(R.id.RemoveButton);

        event_name.setText(event.getEventName());

        profile.setOnClickListener(new View.OnClickListener() {
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
                db.collection("Users")
                        .document(user.getUserId())
                        .delete();
            }
        });

        return convertView;
    }
}
