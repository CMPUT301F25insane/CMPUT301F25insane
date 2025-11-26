package com.example.camaraderie.event_screen.organizer_view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.camaraderie.R;
import com.example.camaraderie.databinding.FragmentOrganizerChooseNotifTestBinding;

public class OrganizerNotificationChooserFragment extends Fragment {

    private FragmentOrganizerChooseNotifTestBinding binding;
    private NavController nav;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nav = NavHostFragment.findNavController(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrganizerChooseNotifTestBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.choosenotifBack.setOnClickListener(v -> nav.popBackStack());

        Bundle args = new Bundle();

        binding.acceptedNotifButton.setOnClickListener(v -> {
            args.putString("list", "acceptedList");
            nav.navigate(R.id.organizerNotificationFragment);
        });

        binding.cancelledButtonNotif.setOnClickListener(v -> {
            args.putString("list", "cancelledList");
            nav.navigate(R.id.organizerNotificationFragment);
        });

        binding.waitlistNotifButton.setOnClickListener(v -> {
            args.putString("list", "waitlist");
            nav.navigate(R.id.organizerNotificationFragment);
        });

        binding.selectedButtonNotifs.setOnClickListener(v -> {
            args.putString("list", "selectedList");
            nav.navigate(R.id.organizerNotificationFragment);
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
