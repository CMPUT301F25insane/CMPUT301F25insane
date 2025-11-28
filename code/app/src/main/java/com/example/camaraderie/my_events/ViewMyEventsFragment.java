package com.example.camaraderie.my_events;

import android.os.Bundle;
import android.util.Log;
import static com.example.camaraderie.main.MainActivity.user;
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
import com.example.camaraderie.SharedEventViewModel;
import com.example.camaraderie.databinding.FragmentViewMyEventsBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * Screen for events the user has joined the waitlist for.
 */
public class ViewMyEventsFragment extends Fragment implements ViewMyEventsArrayAdapter.OnEventClickListener{

    private FragmentViewMyEventsBinding binding;
    private FirebaseFirestore db;

    private ViewMyEventsArrayAdapter myEvents;
    private NavController nav;


    /**
     * sets nav, myEvents list, and eventViewModel
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myEvents = new ViewMyEventsArrayAdapter(getContext(), new ArrayList<>());
        myEvents.listener = this;

        nav = NavHostFragment.findNavController(ViewMyEventsFragment.this);
    }

    /**
     * sets binding
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return binding root
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentViewMyEventsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    /**
     * set bindings and listeners
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.nameForMyEvents.setText(user.getFirstName());


        db = FirebaseFirestore.getInstance();
        myEvents.clear();
        binding.myEventsForViewMyEvents.setAdapter(myEvents);
        binding.userProfileImageButton.setOnClickListener(v -> nav.navigate(R.id.update_user));
        //binding.nameForMyEvents.setText(user.getFirstName());

        for (DocumentReference eventRef : user.getWaitlistedEvents()) {
            eventRef.get().addOnSuccessListener(snapshot -> {
                Event event = snapshot.toObject(Event.class);
                myEvents.add(event);
                myEvents.notifyDataSetChanged();
            });
        }



        binding.dashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nav.navigate(R.id.fragment_main);
            }
        });

        binding.hostEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nav.navigate(R.id.fragment_create_event);
            }
        });

        binding.myEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: do nothing here, we're already in this fragment. maybe later, the button just reloads this?
            }
        });

        binding.MyEventsButton.setOnClickListener(v -> nav.navigate(R.id.fragment_my_created_events));

        binding.pendingEventButtons.setOnClickListener(v -> {
            nav.navigate(R.id.fragment_pending_events);
        });

        binding.acceptedEventButton.setOnClickListener(v -> nav.navigate(R.id.MyAcceptedEventsFragment));


    }

    /**
     * listener to navigate to the organizer view of event
     * @param event event to set organizer view to
     */
    public void onEventClick(Event event){

        Log.d("clicked event description", event.getEventName());

//        Bundle args = new Bundle();
//
//        args.putString("eventDocRefPath", event.getEventDocRef().getPath());
//
//        if (args.getString("eventDocRefPath") == null){
//            Log.e("Firestore", "Event path is null");
//            throw new RuntimeException("Firestore event path is null in onEventClick()");
//        }

        SharedEventViewModel vm = new ViewModelProvider(requireActivity()).get(SharedEventViewModel.class);
        vm.setEvent(event);  // this is a better way to pass args instead of the bundle. ask me about it (abdul) if you need more info on why it works

        NavHostFragment.findNavController(this).navigate(R.id.fragment_view_event_user);
    }

    /**
     * set binding to null
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
