package com.example.camaraderie.event_screen.user_lists.accepted_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.camaraderie.R;
import com.example.camaraderie.User;
import com.example.camaraderie.event_screen.ViewListViewModel;

import java.util.ArrayList;

/**
 *
 *
 * uses its own item xml
 */
public class ViewAcceptedListArrayAdapter extends ArrayAdapter<User> {

    private ViewListViewModel vm;


    public ViewAcceptedListArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<User> users, @NonNull ViewListViewModel vm) {
        super(context, resource, users);

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



        return view;
    }
}
