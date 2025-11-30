package com.example.camaraderie.admin_screen;

import static com.example.camaraderie.main.MainActivity.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.camaraderie.R;
import com.example.camaraderie.databinding.FragmentAdminDashboardBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * The AdminDashboardFragment class extends Fragment and hosts the UI for the admin dashboard
 * It displays the buttons that the admin can use to execute their respective tests
 */

public class AdminDashboardFragment extends Fragment {
    private FragmentAdminDashboardBinding binding;
    private NavController nav;
    private NavController navController;
    public static boolean TEST_MODE = false;

    public  AdminDashboardFragment() {
        super(R.layout.fragment_admin_dashboard);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nav = NavHostFragment.findNavController(this);



    }

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

        if (user != null) {
            binding.nameForMainDashboard.setText(user.getFirstName());
        } else {
            binding.nameForMainDashboard.setText("[Name]");
        }

        binding.adminSeeUsers.setOnClickListener(v -> nav.navigate(R.id.admin_user_data_screen_view));
        binding.adminSeeEvents.setOnClickListener(v -> nav.navigate(R.id.admin_event_data_screen_view));
        binding.adminSeePics.setOnClickListener(v -> nav.navigate(R.id.admin_event_images_screen_view));
        binding.back.setOnClickListener(v -> nav.popBackStack());

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