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

public class AdminNotificationLogFragment extends Fragment {

    private NavController nav;
    private SharedEventViewModel svm;
    private AdminNotificationLogArrayAdapter adapter;
    private FragmentAdminViewNotificationLogsBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nav = NavHostFragment.findNavController(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdminViewNotificationLogsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}