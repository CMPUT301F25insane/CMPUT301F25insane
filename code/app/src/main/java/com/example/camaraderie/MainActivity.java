package com.example.camaraderie;

import static com.example.camaraderie.Util.*;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.camaraderie.databinding.ActivityAuthBinding;
import com.example.camaraderie.databinding.ActivityMainBinding;
import com.example.camaraderie.databinding.ActivityMainTestBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    static private CollectionReference eventsRef;
    static private CollectionReference usersRef;
    private ArrayList<Event> localEvents = new ArrayList<>();
    private ActivityMainTestBinding binding;
    private DashboardEventArrayAdapter dashboardEventArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //setContentView(R.layout.activity_main);
        binding = ActivityMainTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setContentView(R.layout.activity_main_test);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_test), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // add dummy data
        clearAndAddDummyEvents();

        dashboardEventArrayAdapter = new DashboardEventArrayAdapter(this, localEvents);
        binding.eventsList.setAdapter(dashboardEventArrayAdapter);

        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("Events");
        usersRef = db.collection("Users");

        // get database events (this is fine, we don't have that many entries)
        eventsRef.get().addOnSuccessListener(querySnapshot -> {
            localEvents.clear();
            for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                Event event = doc.toObject(Event.class);
                localEvents.add(event);
            }

            dashboardEventArrayAdapter.notifyDataSetChanged();
        });


    }

    public void displayEvents() {
        //
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}