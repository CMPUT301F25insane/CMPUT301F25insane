package com.example.camaraderie.my_events;

import static android.widget.Toast.LENGTH_SHORT;
import static com.example.camaraderie.MainActivity.user;

import android.content.Context;
import android.graphics.Color;
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

import org.w3c.dom.Document;

import java.util.ArrayList;

/**
 * Event array adapter for events the user has joined the waitlist for.
 */
public class ViewMyEventsArrayAdapter extends ArrayAdapter<Event> {

    private OnEventClickListener listener;
    private DocumentReference ref;
    private FirebaseFirestore db;

    private NavController nav;
    private DocumentReference eventDocref;
    private CollectionReference events;

    private EventViewModel eventViewModel;

    public  ViewMyEventsArrayAdapter(@NonNull Context context, ArrayList<Event> events) {
        super(context, 0, events);
        //this.ref = ref;
    }


    public interface OnEventClickListener {
        void onEventClick(Event event);
    }
    public ViewMyEventsArrayAdapter(@NonNull Context context, int resource, ArrayList<Event> myEvents, DocumentReference ref) {
        super(context, resource, myEvents);
        this.ref = ref;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        db = FirebaseFirestore.getInstance();

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

        assert event != null;
        eventName.setText(event.getEventName());
        eventDocref = db.document(event.getEventDocRef().getPath());
        //eventPrice.setText(String.valueOf(event.getPrice()));
        eventDeadline.setText(event.getRegistrationDeadline().toString());
        //hostName.setText(event.getHost().getUsername());

        Button leaveButton = view.findViewById(R.id.leaveButton);
        Button descButton = view.findViewById(R.id.seeDescButtonForMyEvents);

        leaveButton.setText("Delete");
        //leaveButton.setBackgroundColor(Color.RED);

        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //user.deleteCreatedEvent(event.getEventDocRef());
                eventDocref.update("waitlist", FieldValue.arrayRemove(user.getDocRef())).addOnSuccessListener(aVoid ->{
                    Toast.makeText(getContext(), "You have left the event", LENGTH_SHORT).show();
                });
                user.removeWaitlistedEvent(eventDocref);

                leaveButton.setEnabled(false);
                leaveButton.setBackgroundColor(Color.GRAY);
            }
        });

        // this might be a null ref, perhaps the code should just exist here?
        descButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEventClick(event);
            }
        });


        return view;
    }
}
