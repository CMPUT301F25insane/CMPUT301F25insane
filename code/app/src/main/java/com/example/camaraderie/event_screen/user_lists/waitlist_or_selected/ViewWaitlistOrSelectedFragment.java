package com.example.camaraderie.event_screen.user_lists.waitlist_or_selected;

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
import com.example.camaraderie.User;
import com.example.camaraderie.databinding.FragmentViewWaitlistOrSelectedBinding;
import com.example.camaraderie.event_screen.UserListType;
import com.example.camaraderie.event_screen.ViewListViewModel;
import com.example.camaraderie.event_screen.user_lists.waitlist_or_selected.ViewWaitlistOrSelectedArrayAdapter;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * Screen to view the waitlist for an event. Also shows number of waitlisted users.
 */

public class ViewWaitlistOrSelectedFragment extends Fragment {

    private FragmentViewWaitlistOrSelectedBinding binding;
    private DocumentReference eventDocRef;
    private ViewWaitlistOrSelectedArrayAdapter adapter;
    private ViewListViewModel vm;
    private SharedEventViewModel svm;
    private ArrayList<User> displayedList;
    private String capacityText;
    private String headerText;
    private NavController nav;


    /**
     * setup database, nav, event view model, and shareeventsviewmodel
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static ViewWaitlistOrSelectedFragment newInstance(UserListType type) {

        ViewWaitlistOrSelectedFragment fragment = new ViewWaitlistOrSelectedFragment();
        Bundle args = new Bundle();
        args.putString("type", type.name());
        fragment.setArguments(args);

        return fragment;
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

        nav = NavHostFragment.findNavController(ViewWaitlistOrSelectedFragment.this);
        vm = new ViewModelProvider(requireActivity()).get(ViewListViewModel.class);

        Bundle args = getArguments();
        if (args != null) {
            UserListType type = UserListType.valueOf(args.getString("type"));

            if (type.equals(UserListType.WAITLIST)) {
                displayedList = vm.getWaitlist();
                headerText = "Waitlisted Entrants:";


            } else if (type.equals(UserListType.SELECTEDLIST)) {
                displayedList = vm.getSelectedList();
                headerText = "Selected Entrants:";

            }
            else {
                throw new RuntimeException("Invalid arguments for ViewAcceptedOrCancelledFragment");
            }

        }
        else {
            throw new RuntimeException("ViewAcceptedOrCancelledFragment must have arguments");
        }

        capacityText = displayedList.size() + " / " + vm.getEventCapacity();

        adapter = new ViewWaitlistOrSelectedArrayAdapter(requireContext(), 0, displayedList, vm);

        binding.backButton.setOnClickListener(v -> nav.popBackStack());
        binding.attendeesNum.setText(capacityText);
        binding.waitlistOrSelectedTextView.setText(headerText);
        binding.usersInWaitlist.setAdapter(adapter);

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
