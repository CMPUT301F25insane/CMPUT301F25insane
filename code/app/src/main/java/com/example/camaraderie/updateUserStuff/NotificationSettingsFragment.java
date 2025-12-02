package com.example.camaraderie.updateUserStuff;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.camaraderie.R;
import com.example.camaraderie.databinding.FragmentGuidelinesBinding;
import com.example.camaraderie.databinding.FragmentNotificationSettingsBinding;

/**
 * Fragment that open the notification permissions activity
 */
public class NotificationSettingsFragment extends Fragment {

    private FragmentNotificationSettingsBinding binding;

    private NavController nav;

    /**
     * creates the binding
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return created binding root
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNotificationSettingsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    /**
     * sets the nav and buttons
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nav = NavHostFragment.findNavController(this);

        binding.returnButtonForNotifications.setOnClickListener(v -> nav.popBackStack());

        binding.settingsButton.setOnClickListener(new View.OnClickListener() {
            /**
             * creates notification permissions activity
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, getActivity().getPackageName());
                startActivity(intent);
            }
        });
    }


    /**
     * sets binding to null
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
