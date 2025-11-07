package com.example.camaraderie.accepted_screen;

import static com.example.camaraderie.MainActivity.user;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class PendingEventArrayAdapter extends ArrayAdapter<Event> {

    private UserAcceptedViewModel vm;

    public PendingEventArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Event> events, UserAcceptedViewModel vm) {
        super(context, resource, events);

        this.vm = vm;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {  // convert view is a reused view, to save resources
            // create new view using layout inflater if no recyclable view available

            view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_pending_event_item, parent, false);
        }
        else {
            // just reuse the garbage view
            view = convertView;
        }

        Event event = getItem(position);
        ((TextView) view.findViewById(R.id.pendingEventDate)).setText(event.getEventDate().toString());

        ((TextView) view.findViewById(R.id.pendingEventName)).setText(event.getEventName());

        view.findViewById(R.id.acceptEventButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vm.userAcceptInvite(event.getEventDocRef());

                if (vm.allInvitesResolved()) {
                    ((Button)parent.findViewById(R.id.pendingEventsContinueButton)).setEnabled(true);
                }

            }
        });


        view.findViewById(R.id.declineEventButton).setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                vm.userDeclineInvite(event.getEventDocRef());
                if (vm.allInvitesResolved()) {
                    ((Button)parent.findViewById(R.id.pendingEventsContinueButton)).setEnabled(true);
                }
            }
        });

        return view;
    }
}
