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
    private Event event;
    private FirebaseFirestore db;
    private NavController nav;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();
        nav = NavHostFragment.findNavController(ViewWaitlistFragment.this);
        vm = new ViewModelProvider(requireActivity()).get(ViewWaitlistViewModel.class);
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

        String path = getArguments().getString("eventDocRefPath");
        eventDocRef = db.document(path);
        eventDocRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    event = documentSnapshot.toObject(Event.class);

                    vm.loadWaitlistedUsers(event, users -> {
                        // 'users' is now an ArrayList<User>
                        viewWaitlistArrayAdapter = new ViewWaitlistArrayAdapter(requireContext(), 0, users, event, vm);
                        binding.usersInWaitlist.setAdapter(viewWaitlistArrayAdapter);
                    });

                    fillTextViews(event);

                    binding.backButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Bundle arg = new Bundle();

                            arg.putString("eventDocRefPath", eventDocRef.getPath());
                            if (event.getHostDocRef().equals(user.getDocRef())) {
                                nav.navigate(R.id.action_fragment_view_waitlist_to__fragment_organizer_view_event, arg);
                            } else if (user.isAdmin()) {
                                nav.navigate(R.id.action_fragment_view_waitlist_to_fragment_view_event_user, arg);
                            } else {
                                // is normal user
                                nav.navigate(R.id.action_fragment_view_waitlist_to_fragment_view_event_user, arg);
                            }
                        }
                    });
                });
    }

    private void fillTextViews(Event event) {
        binding.attendeesNum.setText(String.valueOf(event.getWaitlist().size()));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
