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


    public ViewAcceptedOrCancelledListArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<User> users) {
        super(context, resource, users);

    }

    /**
     * The getView method inflates the current item with the required XML file and sets up
     * the fields with the required ones
     * @param position
     * @param convertView
     * @param parent
     * @return
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
