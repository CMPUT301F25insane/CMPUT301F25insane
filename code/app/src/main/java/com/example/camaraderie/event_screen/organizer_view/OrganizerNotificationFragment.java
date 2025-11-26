package com.example.camaraderie.event_screen.organizer_view;

import android.os.Bundle;
import android.util.Log;
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
import com.example.camaraderie.databinding.FragmentOrganizerNotificationDialogTestBinding;
import com.google.firebase.firestore.DocumentReference;

public class OrganizerNotificationFragment extends Fragment {

    private FragmentOrganizerNotificationDialogTestBinding binding;
    private SharedEventViewModel svm;
    private NavController nav;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nav = NavHostFragment.findNavController(this);
        svm = new ViewModelProvider(requireActivity()).get(SharedEventViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrganizerNotificationDialogTestBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args == null) {
            throw new RuntimeException("organizer notification fragmant args cannot be null");
        }

        String list = args.getString("list");
        if (list == null) {
            throw new RuntimeException("list cannot be null");
        }


        binding.orgNotifBack.setOnClickListener(v -> nav.popBackStack());

        svm.getEvent().observe(getViewLifecycleOwner(), event -> {
            binding.orgNotifConfButton.setOnClickListener(v -> {
                String msg = binding.orgNotifBody.getText().toString();
                String title = binding.orgNotifTitle.getText().toString();
                confirmNotification(msg, title, event.getEventDocRef(), list);
            });
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void confirmNotification(String msg, String title, DocumentReference eventRef, String list) {
        OrganizerNotificationHandler handler = new OrganizerNotificationHandler(title, msg, eventRef);

        handler.sendNotificationToFirebase(list,
                // on success
                () -> {
                    Log.d("Organizer Notifications", "God loves us");
                    nav.popBackStack();
                },

                // on screw up
                () -> {
                    nav.popBackStack();
                    Log.e("Organizer Notifications", "Notification failed to send.");
                }
        );
    }
}
