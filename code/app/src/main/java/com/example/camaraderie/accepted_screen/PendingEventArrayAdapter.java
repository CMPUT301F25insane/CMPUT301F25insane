package com.example.camaraderie.accepted_screen;

import static com.example.camaraderie.accepted_screen.UserAcceptedHandler.userAcceptInvite;
import static com.example.camaraderie.accepted_screen.UserAcceptedHandler.userDeclineInvite;

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

import java.util.ArrayList;
/**
 * PendingEventArrayAdapter extends array adapter in order to customize how we list the events
 * that are waiting for the users to accept them
 */

/**
 * Array adapter to display events the user has yet to accept.
 */
public class PendingEventArrayAdapter extends ArrayAdapter<Event> {


    private UserAcceptedToEventFragment listener;

    // user accepts invitation

    /**
     * sets listener for array adapter
     * @param listener listener to use
     */

    public void setListener(UserAcceptedToEventFragment listener) {
        this.listener = listener;
    }

    /**
     * gets listener used by the array adapter
     * @return listener used
     */
    public UserAcceptedToEventFragment getListener() {return listener;}


    /**
     * PendingEventArrayAdapter has a constructor that initializes the view model
     * @param context app context
     * @param resource resource to load
     * @param events events loaded from db
     *
     */
    public PendingEventArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Event> events) {
        super(context, resource, events);

    }

    /**
     * getView sets up the view of each item in the array so that we can
     *      * setup all the buttons and text for each item and what they are meant to do
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return created item view
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;

        /**
         * To make sure that we inflate the correct view, we make sure that convertView is not null
         * We then inflate the view with our custom fragment
         */

        if (convertView == null) {  // convert view is a reused view, to save resources
            // create new view using layout inflater if no recyclable view available
            view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_pending_event_item, parent, false);
        }
        else {
            // just reuse the garbage view
            view = convertView;
        }

        /**
         * We grab the event we are currently at and we set the fragments date and name fields to the
         * same values as the event's date and name
         */

        Event event = getItem(position);
        ((TextView) view.findViewById(R.id.pendingEventDate)).setText(event.getEventDate().toString());

        ((TextView) view.findViewById(R.id.pendingEventName)).setText(event.getEventName());

        /**
         * We use an onlick listener to listen when the accept event button is clicked
         * Once it is clicked we use our custom function inside the viewModel to accept the event
         * If all the spots have been taken then we disable the button
         */

        view.findViewById(R.id.acceptEventButton).setOnClickListener(new View.OnClickListener() {
            /**
             * handles accept button click
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                userAcceptInvite(event);
                remove(event);
                listener.enableConfirmButton();

            }

        });

        /**
         * We use a setOnClickListener to listen to the decline button
         * When it is clicked we use our decline invite function in the our custom view model in order to decline the invite to the event
         * We also check to make sure that if we are fully resolved we disable the button to decline
         */


        view.findViewById(R.id.declineEventButton).setOnClickListener(new View.OnClickListener() {
            /**
             * handles decline invite button click
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                userDeclineInvite(event);
                remove(event);
                listener.enableConfirmButton();
            }
        });

        return view;
    }
}
