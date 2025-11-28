package com.example.camaraderie.my_events;

import android.content.Context;
import android.icu.lang.UProperty;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;

import com.example.camaraderie.Event;
import com.example.camaraderie.R;
import com.example.camaraderie.SharedEventViewModel;
import com.example.camaraderie.databinding.FragmentViewMyEventHistoryBinding;
import com.example.camaraderie.databinding.FragmentViewMyEventHistoryItemBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MyAcceptedEventsArrayAdapter extends ArrayAdapter<Event> {

    private SharedEventViewModel svm;
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private NavController nav;
    public MyAcceptedEventsArrayAdapter(@NonNull Context context, @NonNull ArrayList<Event> events, SharedEventViewModel svm, NavController nav) {
        super(context, 0, events);

        this.svm = svm;
        this.nav = nav;
    }

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
