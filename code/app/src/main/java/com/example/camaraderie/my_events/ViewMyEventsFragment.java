package com.example.camaraderie.my_events;

import android.os.Bundle;
import android.util.Log;
import static com.example.camaraderie.MainActivity.user;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.camaraderie.Event;
import com.example.camaraderie.R;
import com.example.camaraderie.dashboard.EventViewModel;
import com.example.camaraderie.databinding.FragmentViewMyEventsBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ViewMyEventsFragment extends Fragment implements ViewMyEventsArrayAdapter.OnEventClickListener{

    private FragmentViewMyEventsBinding binding;
    private FirebaseFirestore db;

    private ViewMyEventsArrayAdapter myEvents;
    NavController nav;
    DocumentReference eventDocRef;
    private EventViewModel eventViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        myEvents = new ViewMyEventsArrayAdapter(getContext(), new ArrayList<>());

        nav = NavHostFragment.findNavController(ViewMyEventsFragment.this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentViewMyEventsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        db = FirebaseFirestore.getInstance();
        binding.myEventsForViewMyEvents.setAdapter(myEvents);
        //binding.nameForMyEvents.setText(user.getFirstName());
        eventViewModel.getLocalEvents().observe(getViewLifecycleOwner(), events -> {
            myEvents.clear();
            for(Event event : events) {
                if (event.getWaitlist() != null && event.getWaitlist().contains(user.getDocRef())) {
                    myEvents.add(event);
                }
            }
            myEvents.notifyDataSetChanged();
        });



        binding.dashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nav.navigate(R.id.action_fragment_view_my_events_to_fragment_main);
            }
        });

        binding.hostEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nav.navigate(R.id.action_fragment_view_my_events_to_fragment_create_event_testing);
            }
        });

        binding.myEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: do nothing here, we're already in this fragment. maybe later, the button just reloads this?
            }
        });



    }

    public void onEventClick(Event event){

        Log.d("clicked event description", event.getEventName());

        Bundle args = new Bundle();

        args.putString("eventDocRefPath", event.getEventDocRef().getPath());

        if (args.getString("eventDocRefPath") == null){
            Log.e("Firestore", "Event path is null");
            throw new RuntimeException("Firestore event path is null in onEventClick()");
        }

        NavHostFragment.findNavController(this).navigate(R.id.action_fragment_view_my_events_to__fragment_organizer_view_event, args);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
