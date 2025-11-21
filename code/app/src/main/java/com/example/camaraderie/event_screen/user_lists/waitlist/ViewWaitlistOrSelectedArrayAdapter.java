package com.example.camaraderie.event_screen.user_lists.waitlist;

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
import com.example.camaraderie.event_screen.ViewListViewModel;
import com.google.firebase.firestore.DocumentReference;

import static com.example.camaraderie.MainActivity.user;

import java.util.ArrayList;

/**
 * Array adapter for user objects for fragments involving waitlists
 */
public class ViewWaitlistOrSelectedArrayAdapter extends ArrayAdapter<User> {

    private Event event;
    private DocumentReference hostRef;
    private ViewListViewModel vm;

    /**
     * setup event, hostRef, view model for ViewWaitlistViewmodel
     * @param context context
     * @param resource resource, usually set to 0
     * @param users users list
     * @param event event to get details from
     * @param vm viewmodel for waitlist
     */
    public ViewWaitlistOrSelectedArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<User> users, @NonNull Event event, @NonNull ViewListViewModel vm) {
        super(context, resource, users);

        this.event = event;
        this.hostRef = event.getHostDocRef();

        this.vm = vm;
    }

    /**
     * get view, set buttons and textviews for each item
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return construtced view
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {  // convert view is a reused view, to save resources
            // create new view using layout inflater if no recyclable view available

            view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_view_waitlist_or_selected_item, parent, false);
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
            /**
             * calls kickUser function in vm and updates UI
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                Log.d("Waitlist kick button", "User attempted to be kicked...");
                vm.kickUser(entrant, event, () -> {
                    remove(entrant);
                    notifyDataSetChanged();
                });
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
