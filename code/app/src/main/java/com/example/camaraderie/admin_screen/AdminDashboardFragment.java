package com.example.camaraderie.admin_screen;

import static com.example.camaraderie.MainActivity.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.activity.OnBackPressedDispatcher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.camaraderie.R;
import com.example.camaraderie.databinding.FragmentAdminDashboardBinding;

/**
 * The AdminDashboardFragment class extends Fragment and hosts the UI for the admin dashboard
 * It displays the buttons that the admin can use to execute their respective tests
 */

public class AdminDashboardFragment extends Fragment {
    private FragmentAdminDashboardBinding binding;

    /**
     * onCreateView is used to inflate the layout for the fragment and return the root of the binding
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     * @return
     * We return the root
     */

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdminDashboardBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }

    /**
     * onViewCreated defines all the functionality for the buttons and other UI components for the screen
     * We setup all the buttons using binding in order to navigate to the different screens for the admin to use
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */

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
                        .navigate(R.id.action_admin_main_screen_to_admin_user_data_screen_view)
        );

        binding.adminSeeEvents.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_admin_main_screen_to_admin_event_data_screen_view)
        );

        binding.adminSeePics.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_admin_main_screen_to_admin_event_images_screen_view));

        binding.back.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_admin_main_screen_to_update_user));

    }

    /**
     * onDestoryView ensures no memory leaks by setting binding to null
     */

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // avoid memory leaks
    }
}