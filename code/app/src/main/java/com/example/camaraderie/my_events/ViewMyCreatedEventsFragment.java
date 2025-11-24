package com.example.camaraderie.my_events;

import static com.example.camaraderie.MainActivity.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.fragment.NavHostFragment;

import com.example.camaraderie.Event;
import com.example.camaraderie.SharedEventViewModel;
import com.example.camaraderie.databinding.FragmentMyCreatedEventsBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * Screen for events that the user has created.
 */

public class ViewMyCreatedEventsFragment extends Fragment {

    private FragmentMyCreatedEventsBinding binding;
    private SharedEventViewModel svm;
    private Event event;
    private FirebaseFirestore db;
    private NavController nav;
    private MyCreatedEventsArrayAdapter adapter;

    /**
     * setup nav, svm, adapter, adds user created events to event array list
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nav = NavHostFragment.findNavController(this);
        ArrayList<Event> events = new ArrayList<>();
        ArrayList<DocumentReference> refs = user.getUserCreatedEvents();
        svm = new ViewModelProvider(requireActivity()).get(SharedEventViewModel.class);
        adapter = new MyCreatedEventsArrayAdapter(requireContext(), 0, events, nav, svm);

        if (refs.isEmpty()) {
            return;
        }

        for (DocumentReference ref : refs) {
            ref.get().addOnSuccessListener(doc -> {
                Event e = doc.toObject(Event.class);
                if (e != null) {
                    events.add(e);
                }


                if (events.size() == refs.size()) {
                    adapter.notifyDataSetChanged();
                }
            }).addOnFailureListener(err -> {
                err.printStackTrace();
            });
        }


    }

    /**
     * setup binding
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
        binding = FragmentMyCreatedEventsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * set bindings for back button, set adapter
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.listView.setAdapter(adapter);
        binding.backButton.setOnClickListener(v -> nav.popBackStack());
        binding.textView5.setText("My created events");
    }

    /**
     * set binding to null
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
