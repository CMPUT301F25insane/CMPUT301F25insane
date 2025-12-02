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

import com.example.camaraderie.R;
import com.example.camaraderie.SharedEventViewModel;
import com.example.camaraderie.databinding.FragmentOrganizerNotificationDialogTestBinding;
import com.google.firebase.firestore.DocumentReference;

/**
 * The class setups the UI and backend to allow the user to actually send a notification to any required users
 */

public class OrganizerNotificationFragment extends Fragment {

    private FragmentOrganizerNotificationDialogTestBinding binding;
    private SharedEventViewModel svm;
    private NavController nav;

    /**
     * onCreate sets up the nav and the ViewModelProvider
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nav = NavHostFragment.findNavController(this);
        svm = new ViewModelProvider(requireActivity()).get(SharedEventViewModel.class);
    }

    /**
     * onCreateView sets the required XML file to inflate the current view, we also use binding to make it easier to
     * access UI elements
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     * We return the root of the binding as a view
     */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrganizerNotificationDialogTestBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * onViewCreated sets up the Bundle with arguments and also sets up the back button and allows for us to initialize
     * the notification information to send
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */

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

    /**
     * This method confirms the notification for us and actually sends it to FireBase
     * then it pops the backstack and returns
     * @param msg
     * @param title
     * @param eventRef
     * @param list
     */

    private void confirmNotification(String msg, String title, DocumentReference eventRef, String list) {
        OrganizerNotificationHandler handler = new OrganizerNotificationHandler(title, msg, eventRef);

        handler.sendNotificationToFirebase(list,
                // on success
                () -> {
                    Log.d("Organizer Notifications", "God loves us");
                    nav.popBackStack(R.id._fragment_organizer_view_event, false);
                },

                // on screw up
                () -> {
                    nav.popBackStack();
                    Log.e("Organizer Notifications", "Notification failed to send.");
                }
        );
    }
}
