package com.example.camaraderie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.camaraderie.databinding.CreatedEventBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class CreatedEventFragment extends Fragment {

    private FirebaseFirestore db;

    private CreatedEventBinding binding;
    private DocumentReference event;
    private DocumentReference user;

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

    public static CreatedEventFragment newInstance(DocumentReference event, DocumentReference user) {
        CreatedEventFragment fragment = new CreatedEventFragment();
        Bundle args = new Bundle();

        args.putString(ARG_EVENT, event.getPath());
        args.putString(ARG_USER, user.getPath());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        binding = CreatedEventBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String eventPath;
        String userPath;
        eventPath = (String) getArguments().getSerializable(ARG_EVENT);
        userPath = (String)  getArguments().getSerializable(ARG_USER);

        db = FirebaseFirestore.getInstance();
        event = db.document(eventPath);
        user = db.document(userPath);

        fillTextViews(event);

        binding.joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                waitlistDocRef.get().addOnCompleteListener( task -> {
                    if (task.isSuccessful()){
                        DocumentSnapshot userToAdd = task.getResult();
                        if (userToAdd.exists()){
                            DocumentReference existingUser = user;
                            existingUser.delete();
                        }
                        else{
                            waitlistDocRef.set(userToAdd);
                        }
                    }
                });


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
