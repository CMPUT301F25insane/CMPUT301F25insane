package com.example.camaraderie.admin_screen.admin_notification_logs;

import static com.example.camaraderie.admin_screen.admin_notification_logs.AdminNotifLogHelper.loadNotificationsFromLogs;

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

import com.example.camaraderie.SharedEventViewModel;
import com.example.camaraderie.databinding.FragmentAdminViewNotificationLogsBinding;

/**
 * The class displays the actual fragment to the user with the list and headers
 */

public class AdminNotificationLogFragment extends Fragment {

    private NavController nav;
    private SharedEventViewModel svm;
    private AdminNotificationLogArrayAdapter adapter;
    private FragmentAdminViewNotificationLogsBinding binding;

    /**
     * Initialize the nav controller for the fragment
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nav = NavHostFragment.findNavController(this);
    }

    /**
     * We initalize the fragment with the xml file
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     * binding.getroot
     */

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdminViewNotificationLogsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * onViewCreated sets up the back button and sets up the the adapater and fills it with events
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        svm = new ViewModelProvider(requireActivity()).get(SharedEventViewModel.class);

        // Back button
        binding.backButton.setOnClickListener(v -> nav.popBackStack());

        // event should be set, we're assuming this is coming from the admin view of an event
        svm.getEvent().observe(getViewLifecycleOwner(), event -> {
            if (event == null) return;

            loadNotificationsFromLogs(event.getNotificationLogs(), logs -> {
                adapter = new AdminNotificationLogArrayAdapter(requireContext(), logs);
                binding.logListView.setAdapter(adapter);
            });


        });
    }

    /**
     * Destroy View and sets binding to null to avoid memory leaks
     */

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}