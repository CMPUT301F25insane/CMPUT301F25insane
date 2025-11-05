package com.example.camaraderie;//

import static com.example.camaraderie.utilStuff.Util.*;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.camaraderie.data.AppDataRepository;
import com.example.camaraderie.dashboard.EventViewModel;
import com.example.camaraderie.databinding.ActivityMainBinding;
//import com.example.camaraderie.databinding.ActivityMainTestBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * The main activity of the app
 */
@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    @Inject
    AppDataRepository appDataRepository;
    private FirebaseFirestore db;
    public static User user;

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

        Log.d("Firestore", "Searching for user in database...");
        usersRef.document(id).get()
                .addOnSuccessListener(documentSnapshot -> {
                    user = documentSnapshot.toObject(User.class);
                    Log.d("Firestore", "User found");
                })
                .addOnFailureListener(e -> {
                        Log.e("Firestore", "User does not exist! Creating new user...");
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        LayoutInflater inflater = getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.fragment_user_info_dialog, null);
                        EditText name = dialogView.findViewById(R.id.edit_full_name_text);
                        EditText Email = dialogView.findViewById(R.id.edit_email_text);
                        EditText address = dialogView.findViewById(R.id.edit_text_address_text);
                        EditText phoneNum = dialogView.findViewById(R.id.edit_phone_number_text);

                        //TODO: deal with empty fields

                        builder.setMessage("Please enter Your information to create a profile")
                                .setView(dialogView)
                                .setPositiveButton("Done", (dialog, id1) -> {
                                    String name1 = name.getText().toString();
                                    String email2 = Email.getText().toString();
                                    String address2 = address.getText().toString();
                                    String phoneNum2 = phoneNum.getText().toString();
                                    String id2 = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);


                                    Log.d("Firestore", "wenis");
                                    // TALK TO RAMIZ ABT THIS!!!!!!
                                    // firebase should automatically serialize the object, and user should be org so that it has an empty arr of events
                                    DocumentReference userDocRef = usersRef.document();

                                    user = new User(name1, email2, address2, phoneNum2, id2, userDocRef);
                                    userDocRef.set(user);

                                    //appDataRepository.setSharedData(usersRef.document(id).getPath());
                                    //Log.d("set data", usersRef.document(id).getPath());

                                    Log.d("Firestore", "User has been created!");
                                });
                });

        // add dummy data
        clearAndAddDummyEvents();


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

//        appDataRepository.setSharedData(usersRef.document(id).getPath());


        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        if (user.isAdmin()) {
            // navigate to admin fragment
        }

        if (!user.getSelectedEvents().isEmpty()) {
            navController.navigate(R.id.fragment_pending_events);
        }

        navController.navigate(R.id.fragment_main);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}