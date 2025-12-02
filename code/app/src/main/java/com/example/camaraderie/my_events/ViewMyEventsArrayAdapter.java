package com.example.camaraderie.my_events;

import static android.widget.Toast.LENGTH_SHORT;
import static com.example.camaraderie.main.MainActivity.user;
import static com.example.camaraderie.utilStuff.EventHelper.handleUnjoin;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
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

import com.example.camaraderie.Event;
import com.example.camaraderie.R;
import com.example.camaraderie.dashboard.EventViewModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * Event array adapter for events the user has joined the waitlist for.
 */
public class ViewMyEventsArrayAdapter extends ArrayAdapter<Event> {

    public OnEventClickListener listener;


    /**
     * constructor for array adapter
     * @param context context
     * @param events event list
     */
    public ViewMyEventsArrayAdapter(@NonNull Context context, ArrayList<Event> events) {
        super(context, 0, events);

        setNotifyOnChange(true);
    }


    /**
     * interface for onEventClick listener
     */
    public interface OnEventClickListener {
        /**
         * runs the onEventClick logic
         * @param event event clicked on
         */
        void onEventClick(Event event);
    }

    /**
     * setup view for each item. sets listeners for buttons.
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
        View view;

        if (convertView == null) {  // convert view is a reused view, to save resources
            // create new view using layout inflater if no recyclable view available

            view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_view_my_events_item, parent, false);
        }
        else {
            // just reuse the garbage view
            view = convertView;
        }

        Event event = getItem(position);
        TextView eventName = view.findViewById(R.id.event_Name_For_My_Events);
        //TextView eventPrice = view.findViewById(R.id.eventPrice);
        TextView eventDeadline = view.findViewById(R.id.eventDeadlineForMyEvents);
        //TextView hostName = view.findViewById(R.id.hostName);

        if (event == null) {return view;}
        eventName.setText(event.getEventName());
        //eventPrice.setText(String.valueOf(event.getPrice()));
        eventDeadline.setText(event.getRegistrationDeadline().toString());
        //hostName.setText(event.getHost().getUsername());

        Button leaveButton = view.findViewById(R.id.leaveButton);
        Button descButton = view.findViewById(R.id.seeDescButtonForMyEvents);

        //leaveButton.setText("Delete");
        //leaveButton.setBackgroundColor(Color.RED);

        leaveButton.setOnClickListener(new View.OnClickListener() {
            /**
             * leave te waitlist for this event.
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {

                handleUnjoin(event,
                        () -> {
                            remove(event);

                            leaveButton.setEnabled(false);
                            leaveButton.setBackgroundColor(Color.GRAY);
                            Toast.makeText(getContext(), "You have left the event", LENGTH_SHORT).show();
                        },
                        () -> {
                            Log.e("ViewMyEvents Array Adapter", "Failed to unjoin user");
                            Toast.makeText(getContext(), "Failed to leave event", LENGTH_SHORT).show();
                        });


            }
        });

        descButton.setOnClickListener(v -> listener.onEventClick(event));


        return view;
    }
}
