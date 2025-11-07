package com.example.camaraderie.event_screen;

import android.content.Context;
import android.util.Log;
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
import com.example.camaraderie.User;
import com.example.camaraderie.accepted_screen.PendingEventArrayAdapter;
import com.google.firebase.firestore.DocumentReference;

import static com.example.camaraderie.MainActivity.user;

import java.lang.annotation.Documented;
import java.util.ArrayList;

public class ViewWaitlistArrayAdapter extends ArrayAdapter<User> {

    private Event event;
    private DocumentReference hostRef;
    private ViewWaitlistViewModel vm;

    public ViewWaitlistArrayAdapter(@NonNull Context context, int resource, ArrayList<User> users, Event event, ViewWaitlistViewModel vm) {
        super(context, resource, users);

        this.event = event;
        this.hostRef = event.getHostDocRef();

        this.vm = vm;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {  // convert view is a reused view, to save resources
            // create new view using layout inflater if no recyclable view available

            view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_view_attendees_item, parent, false);
        }
        else {
            // just reuse the garbage view
            view = convertView;
        }

        User entrant = getItem(position);

        TextView name = view.findViewById(R.id.entrantName);
        name.setText(entrant.getFirstName());

        Button kickButton = view.findViewById(R.id.kickFromEventButton);

        kickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Waitlist kick button", "User attempted to be kicked...");
                vm.kickUser(entrant, event, () -> notifyDataSetChanged());
            }
        });


        // TODO: kept these separate in case we need to differentiate in the future
        if (user.getDocRef().equals(hostRef)) {
            // HOST FEATURES
            kickButton.setEnabled(true);
        }

        if (user.isAdmin()) {
            // admin features

            kickButton.setEnabled(true);
        }

        else {
            // normal shit
            kickButton.setEnabled(false);

        }
        return view;
    }
}
