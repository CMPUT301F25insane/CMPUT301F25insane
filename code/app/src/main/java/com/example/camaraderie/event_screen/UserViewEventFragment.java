package com.example.camaraderie.event_screen;

import static android.widget.Toast.LENGTH_SHORT;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;


import static com.example.camaraderie.MainActivity.user;

import com.example.camaraderie.Event;
import com.example.camaraderie.R;
import com.example.camaraderie.dashboard.MainFragment;
import com.example.camaraderie.databinding.FragmentViewEventUserBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserViewEventFragment extends Fragment {

    private FirebaseFirestore db;

    private FragmentViewEventUserBinding binding;
    private DocumentReference eventDocRef;
    private Event event;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assert getArguments() != null;
        String eventPath = getArguments().getString("eventDocRefPath");

        db = FirebaseFirestore.getInstance();
        eventDocRef = db.collection("Events").document(eventPath);
        eventDocRef.get().addOnSuccessListener(documentSnapshot -> {
            event = documentSnapshot.toObject(Event.class);
            Log.d("Firestore", "Event class loaded form db");
        })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Event not loaded from db!");
                    Toast.makeText(requireContext(), "Event Not Found", LENGTH_SHORT).show();
                    NavHostFragment.findNavController(UserViewEventFragment.this)
                            .navigate(R.id.fragment_main);
                    //TODO: HANDLE THIS EXCEPTION
                });

    }

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        binding = FragmentViewEventUserBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        fillTextViews();

        binding.joinButtonUserView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("here", "somehow");
                eventDocRef.update("waitlist.waitlist", FieldValue.arrayUnion(user.getDocRef())).addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "You have joined the event", LENGTH_SHORT).show();
                });
            }

        });

        //TODO: fix this to be greyed out when the user is not in vs in the event
        binding.unjoinButtonUserView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventDocRef.update("waitlist.waitlist", FieldValue.arrayRemove(user.getDocRef())).addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "You have left the event", LENGTH_SHORT).show();
                });
            }
        });

        binding.dashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });
    }

    private void fillTextViews() {

        binding.eventNameForUserView.setText(event.getEventName());
        binding.eventDescriptionUserView.setText(event.getDescription());
        binding.registrationDeadlineTextUserView.setText((CharSequence) event.getRegistrationDeadline());  //TODO: deal with date stuff
        binding.userEventViewEventDate.setText((CharSequence) event.getEventDate());
        binding.locationOfUserView.setText(event.getEventLocation()); //NEED TO CHANGE THIS WHEN GEOLOCATION STUFF IS IMPLEMENTED
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
