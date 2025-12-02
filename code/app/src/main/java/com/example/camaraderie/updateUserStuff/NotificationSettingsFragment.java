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
 * Fragment that allows the user to access and manage app notification settings.
 * <p>
 * Features:
 * <ul>
 *     <li>Button to navigate back to the previous screen.</li>
 *     <li>Button to open the system's notification settings for the app.</li>
 * </ul>
 */
public class NotificationSettingsFragment extends Fragment {

    private FragmentNotificationSettingsBinding binding;

    private NavController nav;

    /**
     * set the nav host
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //nav = NavHostFragment.findNavController(this);
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate views in the fragment
     * @param container          If non-null, this is the parent view the fragment's UI should be attached to
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state
     * @return The root view of the fragment's layout
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNotificationSettingsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    /**
     * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * <p>
     * Sets up click listeners for buttons:
     * <ul>
     *     <li>Return button navigates back to the previous screen.</li>
     *     <li>Settings button opens the system notification settings for this app.</li>
     * </ul>
     *
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state
     */

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nav = NavHostFragment.findNavController(this);

        binding.returnButtonForNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nav.popBackStack();
            }
        });

        binding.settingsButton.setOnClickListener(new View.OnClickListener() {
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
