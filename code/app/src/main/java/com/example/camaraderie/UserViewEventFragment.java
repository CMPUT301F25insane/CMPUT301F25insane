package com.example.camaraderie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.camaraderie.databinding.FragmentViewEventUserBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserViewEventFragment extends Fragment {

    private FirebaseFirestore db;

    private FragmentViewEventUserBinding binding;
    private DocumentReference event;
    private DocumentReference user;

    private CollectionReference eventsRef;
    private CollectionReference usersRef;
    private static final String ARG_EVENT = "event";
    private static final String ARG_USER = "user";

    private String eventName;
    private String description;
    private String deadline;
    private String dateAndTime;
    private String location;
    private DocumentReference hostDocRef;
    private DocumentReference waitlistDocRef;
    private String hostName;

    // Factory method to create a new instance with Event
    public static UserViewEventFragment newInstance(String eventID, String userID) {
        UserViewEventFragment fragment = new UserViewEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EVENT, eventID);
        args.putString(ARG_USER, userID);
        fragment.setArguments(args);
        return fragment;
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

        String eventString;
        String userString;
        eventString = (String) getArguments().getSerializable(ARG_EVENT);
        userString =(String)  getArguments().getSerializable(ARG_USER);

        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("Events");
        usersRef = db.collection("Users");

        user = usersRef.document(userString);
        event = eventsRef.document(eventString);

        fillTextViews(event);

        binding.joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * adding the user to the waitlist document
                 */
            }
        });
    }

    // NEEDS TO BE CHANGED WHEN THE EVENT DATABASE OBJECTS ARE CREATED
    private void fillTextViews(DocumentReference event) {

        event.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    eventName = documentSnapshot.getString("eventName");
                    description = documentSnapshot.getString("description");
                    deadline = documentSnapshot.getString("registrationDeadline");
                    dateAndTime = documentSnapshot.getString("dateAndTime");
                    location = documentSnapshot.getString("eventLocation");
                    hostDocRef = documentSnapshot.getDocumentReference("host");
                    waitlistDocRef = documentSnapshot.getDocumentReference("waitlist");
                }
            }
        });

        hostDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    hostName = documentSnapshot.getString("name");
                }
            }
        });

        binding.eventName.setText(eventName);
        binding.description.setText(description);
        binding.registrationDeadline.setText(deadline);
        binding.appName.setText("Comaraderie");
        binding.dateAndTime.setText(dateAndTime);
        binding.location.setText(location); //NEED TO CHANGE THIS WHEN GEOLOCATION STUFF IS IMPLEMENTED
        binding.organizerName.setText(hostName);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
