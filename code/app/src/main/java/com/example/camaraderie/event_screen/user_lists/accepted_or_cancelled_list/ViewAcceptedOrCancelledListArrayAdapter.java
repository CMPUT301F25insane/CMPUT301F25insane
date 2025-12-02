package com.example.camaraderie.event_screen.user_lists.accepted_or_cancelled_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.camaraderie.R;
import com.example.camaraderie.User;

import java.util.ArrayList;

/**
 * This class setups a custom array adapter for the accepted or cancelled list
 */
public class ViewAcceptedOrCancelledListArrayAdapter extends ArrayAdapter<User> {

    /**
     * initializes the super with the event
     * @param context application context
     * @param resource resource used
     * @param users user list
     */
    public ViewAcceptedOrCancelledListArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<User> users) {
        super(context, resource, users);

    }


    /**
     * The getView method inflates the current item with the required XML file and sets up
     *      * the fields with the required ones
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return new view
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {  // convert view is a reused view, to save resources
            // create new view using layout inflater if no recyclable view available

            view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_view_accepted_or_cancelled_item, parent, false);
        }
        else {
            // just reuse the garbage view
            view = convertView;
        }

        User user = getItem(position);
        TextView nameView = view.findViewById(R.id.acceptedName);
        nameView.setText(user.getFirstName());

        return view;
    }
}
