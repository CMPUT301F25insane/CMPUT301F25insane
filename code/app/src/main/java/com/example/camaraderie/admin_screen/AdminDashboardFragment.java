package com.example.camaraderie.admin_screen;

import static com.example.camaraderie.MainActivity.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.camaraderie.R;
import com.example.camaraderie.databinding.FragmentAdminDashboardBinding;

public class AdminDashboardFragment extends Fragment {
    private FragmentAdminDashboardBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdminDashboardBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(user != null){
            binding.nameForMainDashboard.setText(user.getFirstName());
        } else {
            binding.nameForMainDashboard.setText("[Name]");
        }

        binding.adminSeeUsers.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_admin_main_screen_to_admin_user_data_screen)
        );

        binding.adminSeeEvents.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_admin_main_screen_to_admin_event_data_screen)
        );

        binding.adminSeePics.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_admin_main_screen_to_admin_event_images_screen));

        binding.back.setOnClickListener(v ->
                NavHostFragment.findNavController(this).popBackStack());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // avoid memory leaks
    }
}