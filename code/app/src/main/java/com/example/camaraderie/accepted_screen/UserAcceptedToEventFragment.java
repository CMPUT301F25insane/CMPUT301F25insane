package com.example.camaraderie.accepted_screen;

import static com.example.camaraderie.MainActivity.user;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.camaraderie.Event;
import com.example.camaraderie.R;
import com.example.camaraderie.databinding.FragmentPendingEventsBinding;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class UserAcceptedToEventFragment extends Fragment {

    private FragmentPendingEventsBinding binding;
    private DocumentReference eventDocRef;
    private PendingEventArrayAdapter pendingEventArrayAdapter;
    private UserAcceptedViewModel vm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<Event> selectedEvents = new ArrayList<>();


        vm  = new ViewModelProvider(this).get(UserAcceptedViewModel.class);
        for (DocumentReference eventRef : user.getSelectedEvents()) {
            eventRef.get()
                    .addOnSuccessListener(doc -> {
                        Event event = doc.toObject(Event.class);
                        if (event != null) {
                            selectedEvents.add(event);
                        }
                    })
                    .addOnFailureListener(e ->
                            Log.e("Firestore", "Failed to get event", e));
        }
        pendingEventArrayAdapter = new PendingEventArrayAdapter(requireContext(), 0, selectedEvents, vm);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPendingEventsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

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

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
