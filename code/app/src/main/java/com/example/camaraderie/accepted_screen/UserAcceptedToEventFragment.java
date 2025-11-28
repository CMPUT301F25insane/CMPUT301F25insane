package com.example.camaraderie.accepted_screen;

//import static com.example.camaraderie.main.MainActivity.user;

import static com.example.camaraderie.main.Camaraderie.getUser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.camaraderie.Event;
import com.example.camaraderie.R;
import com.example.camaraderie.databinding.FragmentPendingEventsBinding;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

/**
 * This fragment is used to allow the user to accept events they are drawn for
 */

public class UserAcceptedToEventFragment extends Fragment {

    /**
     * We set up our attributes for this class
     * We need binding, a eventDoc reference, a pendingEventArrayAdapter, and our View Model
     */

    private FragmentPendingEventsBinding binding;
    private PendingEventArrayAdapter pendingEventArrayAdapter;
    private UserAcceptedViewModel vm;

    /**
     * When we override the onCreate method we set up our ViewModelProvider
     * The array list for the selected events
     * We also setup a pendingEventArrayAdapater to initalize the list of events
     * We also iterate through the users selected events and we have a success listener that lists all the events where user is selected
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     * Does not return anything but is run when the fragment is created
     */

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vm  = new ViewModelProvider(this).get(UserAcceptedViewModel.class);
        ArrayList<Event> selectedEvents = new ArrayList<>();
        pendingEventArrayAdapter = new PendingEventArrayAdapter(requireContext(), 0, selectedEvents, vm);
        pendingEventArrayAdapter.setListener(this);
        pendingEventArrayAdapter.setNotifyOnChange(true);

        for (DocumentReference eventRef : getUser().getSelectedEvents()) {
            eventRef.get().addOnSuccessListener(doc -> {
                Event event = doc.toObject(Event.class);
                if (event != null) {
                    selectedEvents.add(event);
                    pendingEventArrayAdapter.notifyDataSetChanged();
                }
            });
        }

    }

    /**
     * onCreateView initalizes the binding to inflate the layout
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return the root of the binding
     */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPendingEventsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    /**
     * omViewCreated ensures that the fragment displays what it needs to and that when a user clicks
     * the continue buttone ensures that once they do what they need to do they can continue nad it would
     * go back to the main screen
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.pendingEventListView.setAdapter(pendingEventArrayAdapter);
        binding.pendingEventsContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(UserAcceptedToEventFragment.this)
                        .navigate(R.id.action_fragment_pending_events_to_fragment_main);
            }
        });

        enableConfirmButton();

    }

    public void enableConfirmButton() {
        if (vm.allInvitesResolved()) {
            binding.pendingEventsContinueButton.setEnabled(true);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
