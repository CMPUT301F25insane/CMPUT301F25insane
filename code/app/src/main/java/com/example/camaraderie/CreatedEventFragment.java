package com.example.camaraderie;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import com.example.camaraderie.databinding.CreatedEventBinding;

public class CreatedEventFragment extends Fragment {

    private FirebaseFirestore db;

    private CreatedEventBinding binding;
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
    private String userID;

    // Factory method to create a new instance with Event
    public static CreatedEventFragment newInstance(String eventID, String userID) {
        CreatedEventFragment fragment = new CreatedEventFragment();
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
        binding = CreatedEventBinding.inflate(inflater, container, false);
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
        getWaitlistDocRef(event);
        getUserID(user);

        binding.joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * adding the user to the waitlist document
                 * see if userID is in waitlist
                 * if its not, add it
                 * if it is, delete it
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

    public void getWaitlistDocRef(DocumentReference event) {

        event.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                waitlistDocRef = documentSnapshot.getDocumentReference("waitlist");
            }
        });
    }

    public void getUserID(DocumentReference user){
        user.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userID = documentSnapshot.getId();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
