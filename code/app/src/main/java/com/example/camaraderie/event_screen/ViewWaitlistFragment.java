package com.example.camaraderie.event_screen;

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
import androidx.navigation.fragment.NavHostFragment;

import com.example.camaraderie.Event;
import com.example.camaraderie.R;
import com.example.camaraderie.SharedEventViewModel;
import com.example.camaraderie.User;
import com.example.camaraderie.dashboard.EventViewModel;
import com.example.camaraderie.databinding.FragmentViewAttendeesBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ViewWaitlistFragment extends Fragment {

    private FragmentViewAttendeesBinding binding;
    private DocumentReference eventDocRef;
    private ViewWaitlistArrayAdapter viewWaitlistArrayAdapter;
    private ViewWaitlistViewModel vm;
    private SharedEventViewModel svm;
    private Event event;
    private FirebaseFirestore db;
    private NavController nav;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();
        nav = NavHostFragment.findNavController(ViewWaitlistFragment.this);
        vm = new ViewModelProvider(requireActivity()).get(ViewWaitlistViewModel.class);
        svm = new ViewModelProvider(requireActivity()).get(SharedEventViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentViewAttendeesBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        svm.getEvent().observe(getViewLifecycleOwner(), evt -> {
            event = evt;
            vm.loadWaitlistedUsers(event, users -> {
                viewWaitlistArrayAdapter = new ViewWaitlistArrayAdapter(requireContext(), 0, users, event, vm);
                binding.usersInWaitlist.setAdapter(viewWaitlistArrayAdapter);
            });
            fillTextViews(event);
        });

        binding.backButton.setOnClickListener(v -> nav.popBackStack());

    }

    private void fillTextViews(Event event) {
        binding.attendeesNum.setText(String.valueOf(event.getWaitlist().size()));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
