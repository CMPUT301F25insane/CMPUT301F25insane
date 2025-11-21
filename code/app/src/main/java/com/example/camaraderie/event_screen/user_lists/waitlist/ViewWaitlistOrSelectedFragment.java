package com.example.camaraderie.event_screen.user_lists.waitlist;

import android.os.Bundle;
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
import com.example.camaraderie.SharedEventViewModel;
import com.example.camaraderie.databinding.FragmentViewWaitlistOrSelectedBinding;
import com.example.camaraderie.databinding.FragmentViewWaitlistOrSelectedBinding;
import com.example.camaraderie.event_screen.ViewListViewModel;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Screen to view the waitlist for an event. Also shows number of waitlisted users.
 */

public class ViewWaitlistOrSelectedFragment extends Fragment {

    private FragmentViewWaitlistOrSelectedBinding binding;
    private DocumentReference eventDocRef;
    private ViewWaitlistOrSelectedArrayAdapter viewWaitlistOrSelectedArrayAdapter;
    private ViewListViewModel listViewModel;
    private SharedEventViewModel svm;
    private Event event;
    private FirebaseFirestore db;
    private NavController nav;
    //private ViewCancelledUsersModel cu;

    /**
     * setup database, nav, event view model, and shareeventsviewmodel
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();
        nav = NavHostFragment.findNavController(ViewWaitlistOrSelectedFragment.this);
        listViewModel = new ViewModelProvider(requireActivity()).get(ViewListViewModel.class);
        svm = new ViewModelProvider(requireActivity()).get(SharedEventViewModel.class);
        //cu = new ViewModelProvider(requireActivity()).get(ViewCancelledUsersModel.class);

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
     * @return bidning root
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentViewWaitlistOrSelectedBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    /**
     * setup bindings for buttons, use svm to set event list items
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     *
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        svm.getEvent().observe(getViewLifecycleOwner(), evt -> {
            event = evt;
            listViewModel.loadUsersFromList(event.getWaitlist(), users -> {
                viewWaitlistOrSelectedArrayAdapter = new ViewWaitlistOrSelectedArrayAdapter(requireContext(), 0, users, event, listViewModel);
                viewWaitlistOrSelectedArrayAdapter.setNotifyOnChange(true);  // auto calls notifyDataSetChanged when it changes, but im not gonna remove the old calls unless i have to
                binding.usersInWaitlist.setAdapter(viewWaitlistOrSelectedArrayAdapter);
            });
            fillTextViews(event);
        });
        binding.backButton.setOnClickListener(v -> nav.popBackStack());
        // for cancelled users
        binding.viewCancelledToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (event == null) return;

            if (isChecked) {
                // Load cancelled attendees
                listViewModel.loadUsersFromList(event.getCancelledUsers(), cancelledUsers -> {
                    viewWaitlistOrSelectedArrayAdapter.clear();
                    viewWaitlistOrSelectedArrayAdapter.addAll(cancelledUsers);
                    viewWaitlistOrSelectedArrayAdapter.notifyDataSetChanged();

                    binding.attendeesNum.setText(String.valueOf(cancelledUsers.size()));
                });

            } else {
                // Load normal waitlist
                listViewModel.loadUsersFromList(event.getWaitlist(), waitlist -> {
                    viewWaitlistOrSelectedArrayAdapter.clear();
                    viewWaitlistOrSelectedArrayAdapter.addAll(waitlist);
                    viewWaitlistOrSelectedArrayAdapter.notifyDataSetChanged();

                    binding.attendeesNum.setText(String.valueOf(waitlist.size()));
                });
            }
        });

    }

    /**
     * sets textviews for attendees number
     * @param event event for which to get waitlist
     */
    private void fillTextViews(Event event) {
        binding.attendeesNum.setText(String.valueOf(event.getWaitlist().size()));
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
