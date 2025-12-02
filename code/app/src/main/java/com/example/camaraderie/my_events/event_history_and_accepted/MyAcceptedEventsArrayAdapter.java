package com.example.camaraderie.my_events.event_history_and_accepted;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;

import com.example.camaraderie.Event;
import com.example.camaraderie.R;
import com.example.camaraderie.SharedEventViewModel;
import com.example.camaraderie.databinding.FragmentViewMyEventHistoryItemBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * This class sets up a custom array adapter for our list of accepted events
 */

public class MyAcceptedEventsArrayAdapter extends ArrayAdapter<Event> {

    private SharedEventViewModel svm;
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private NavController nav;

    /**
     * A constructor to initialize the attributes
     * @param context
     * @param events
     * @param svm
     * @param nav
     */
    public MyAcceptedEventsArrayAdapter(@NonNull Context context, @NonNull ArrayList<Event> events, SharedEventViewModel svm, NavController nav) {
        super(context, 0, events);

        this.svm = svm;
        this.nav = nav;
    }

    /**
     * getView inflates the view with the XML and sets up any needed fields using the event information
     * @param position
     * @param convertView
     * @param parent
     * @return
     */

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        FragmentViewMyEventHistoryItemBinding binding;
        if (convertView == null) {
            binding = FragmentViewMyEventHistoryItemBinding.inflate(
                    LayoutInflater.from(getContext()),
                    parent,
                    false
            );
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            binding = (FragmentViewMyEventHistoryItemBinding) convertView.getTag();
        }

        Event event = getItem(position);
        if (event == null) return convertView;  // hopefully this never happens

        binding.historyEventName.setText(event.getEventName());



        binding.historyEventDate.setText(formatter.format(event.getEventDate()));

        binding.descriptionButton.setOnClickListener(v -> {
            svm.setEvent(event);
            nav.navigate(R.id.fragment_view_event_user);
        });

        return convertView;
    }
}
