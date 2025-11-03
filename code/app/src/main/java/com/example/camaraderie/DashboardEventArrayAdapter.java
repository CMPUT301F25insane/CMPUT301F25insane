package com.example.camaraderie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

public class DashboardEventArrayAdapter extends ArrayAdapter<Event> {

    DocumentReference user;

    private OnEventClickListener listener;

    /**
     *
     * @param context
     * @param events
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
        Button descButton = view.findViewById(R.id.seeDescButton);

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });

        descButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEventClick(event);
            }
        });

        return view;
    }
}
