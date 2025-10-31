package com.example.camaraderie;//

import static com.example.camaraderie.Util.*;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.camaraderie.databinding.ActivityMainBinding;
//import com.example.camaraderie.databinding.ActivityMainTestBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    static private CollectionReference eventsRef;
    boolean userExists = false;
    static private CollectionReference usersRef;
    private EventViewModel eventViewModel;
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());  // purely for backend purposes
        setContentView(binding.getRoot());

        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);



        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("Users");
        String id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);


        usersRef.whereEqualTo("UserID", id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (!task.getResult().isEmpty()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        Log.d("Firestore", "Found Document");
                        userExists = true;

                        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                        NavController navController = navHostFragment.getNavController();
                        navController.navigate(R.id.fragment_main);
                    }
                }
                else{
                        Log.d("Firestore", "No Documents");
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        LayoutInflater inflater = getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.user_info_dialog, null);
                        EditText name = dialogView.findViewById(R.id.edit_full_name_text);
                        EditText Email = dialogView.findViewById(R.id.edit_email_text);
                        EditText address = dialogView.findViewById(R.id.edit_text_address_text);
                        EditText phoneNum = dialogView.findViewById(R.id.edit_phone_number_text);

                        builder.setMessage("Please enter Your information to create a profile")
                                .setView(dialogView)
                                .setPositiveButton("Done", (dialog, id1) -> {
                                    String name1 = name.getText().toString();
                                    String email2 = Email.getText().toString();
                                    String address2 = address.getText().toString();
                                    String phoneNum2 = phoneNum.getText().toString();
                                    String id2 = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

                                    Map<String, Object> user = new HashMap<>();
                                    user.put("Full Name", name1);
                                    user.put("Email", email2);
                                    user.put("Address", address2);
                                    user.put("Phone Number", phoneNum2);
                                    user.put("UserID", id2);

                                    Log.d("Firestore", "Got Here");
                                    usersRef.document(id2).set(user);

                                }).setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss()).create().show();
                        userExists = false;
                    }
                }
            else {
                Log.d("Firestore", "Did not get documents");
            }
        });



        // add dummy data
        //clearAndAddDummyEvents();

        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("Events");
        usersRef = db.collection("Users");

        // get database events (this is fine, we don't have that many entries)
        eventsRef.get().addOnSuccessListener(querySnapshot -> {
            ArrayList<Event> events = new ArrayList<>();
            for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                Event event = doc.toObject(Event.class);
                events.add(event);
            }

            eventViewModel.setLocalEvents(events);

        });

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        navController.navigate(R.id.fragment_main);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}