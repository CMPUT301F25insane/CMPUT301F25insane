package com.example.camaraderie.event_screen.organizer_view;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import static com.example.camaraderie.main.Camaraderie.getUser;

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

/**
 * This class initializes an interactive interface to allow for the organizer of an event to choose a list and send a notification to the people in that list
 */

public class OrganizerNotificationChooserFragment extends Fragment {

    /**
     * Initalize our needed attributes
     */
    private FragmentOrganizerChooseNotifTestBinding binding;
    private NavController nav;

    /**
     * When we create the fragment we setup the nav
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nav = NavHostFragment.findNavController(this);
    }

    /**
     * onCreateView inflates the fragment to use the required XML with the actual UI elements
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     * The method returns the root of the binding
     */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrganizerChooseNotifTestBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * onViewCreated adds the backend functionality to the interface and sets onClickListener for
     * each button to take the user to the required screen to publish a notification
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.choosenotifBack.setOnClickListener(v -> nav.popBackStack());

        Bundle args = new Bundle();

        binding.acceptedNotifButton.setOnClickListener(v -> {
            args.putString("list", "acceptedUsers");
            nav.navigate(R.id.organizerNotificationFragment, args);
        });

        binding.cancelledButtonNotif.setOnClickListener(v -> {
            args.putString("list", "cancelledUsers");
            nav.navigate(R.id.organizerNotificationFragment, args);
        });

        binding.waitlistNotifButton.setOnClickListener(v -> {
            args.putString("list", "waitlist");
            nav.navigate(R.id.organizerNotificationFragment, args);
        });

        binding.selectedButtonNotifs.setOnClickListener(v -> {
            args.putString("list", "selectedUsers");
            nav.navigate(R.id.organizerNotificationFragment, args);
        });


        binding.orgViewNotifLogs.setEnabled(false);
        binding.orgViewNotifLogs.setVisibility(INVISIBLE);
        binding.orgViewNotifLogs.setOnClickListener(v -> nav.navigate(R.id.AdminNotificationLogsFragment));

        if (getUser().isAdmin()) {
            binding.orgViewNotifLogs.setEnabled(true);
            binding.orgViewNotifLogs.setVisibility(VISIBLE);
        }

    }

    /**
     * Destroy anything that might cause a glitch or memory leak
     */

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
