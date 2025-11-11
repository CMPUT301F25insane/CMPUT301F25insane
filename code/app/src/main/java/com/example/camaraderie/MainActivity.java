package com.example.camaraderie;//


import static com.example.camaraderie.utilStuff.Util.*;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.activity.OnBackPressedDispatcher;


import com.example.camaraderie.dashboard.EventViewModel;
import com.example.camaraderie.databinding.ActivityMainBinding;
//import com.example.camaraderie.databinding.ActivityMainTestBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

/**
 * The main activity of the app. Deals with QR code scans,login, and initial navigation.
 */

public class MainActivity extends AppCompatActivity {


    private FirebaseFirestore db;
    public static User user;

    private NavController navController;

    static private CollectionReference eventsRef;
    static private CollectionReference usersRef;
    private EventViewModel eventViewModel;
    private ActivityMainBinding binding;
    private SharedEventViewModel svm;

    private Uri pendingDeeplink = null;

    private boolean __DEBUG_DATABASE_CLEAR = false;


    /**
     * sets up the SharedEventViewModel {@link #svm}, sets up the events view model for general event listings,
     * initializes navcontroller and deeplink functionalities.
     * clears the database for testing purposes at the moment.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        //FirebaseFirestore.getInstance().clearPersistence();  // TODO: DO NOT UNCOMMENT THIS CODE

        svm = new ViewModelProvider(this).get(SharedEventViewModel.class);

        binding = ActivityMainBinding.inflate(getLayoutInflater());  // purely for backend purposes
        setContentView(binding.getRoot());

        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("Users");
        String id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        if (getIntent() != null && getIntent().getData() != null){
            pendingDeeplink = getIntent().getData();
        }
        //TODO: this can be refactored into clean functions. it should be. i do not care to do this right now.
        if (!__DEBUG_DATABASE_CLEAR) {
            Log.d("Firestore", "Searching for user in database...");
            usersRef.document(id).get()
                    .addOnSuccessListener(documentSnapshot -> {

                        if (documentSnapshot.exists()) {
                            user = documentSnapshot.toObject(User.class);
                            Log.d("Firestore", "User found");

                            if (user.isAdmin()) {
                                if (pendingDeeplink != null) {
                                    handleDeepLink();

                                } else {

                                    navController.navigate(R.id.admin_main_screen);
                                }
                            }

                            if (!user.getSelectedEvents().isEmpty()) {
                                if (pendingDeeplink != null) {
                                    handleDeepLink();
                                } else {
                                    navController.navigate(R.id.fragment_pending_events);

                                }
                            }

                            // else, nav to the main fragment
                            if (pendingDeeplink != null) {
                                handleDeepLink();

                            } else {
                                navController.navigate(R.id.fragment_main);
                            }
                        } else {
                            newUserBuilder(id, navController);  // build user

                        }

                    })
                    .addOnFailureListener(e -> {
                        throw new RuntimeException("listen, we fucked up.");
                    });
        } else {
            // add dummy data
            clearDBAndSeed(
                    () -> {
                        Log.d("Firestore", "Searching for user in database...");
                        usersRef.document(id).get()
                                .addOnSuccessListener(documentSnapshot -> {

                                    if (documentSnapshot.exists()) {
                                        user = documentSnapshot.toObject(User.class);
                                        Log.d("Firestore", "User found");

                                        if (user.isAdmin()) {
                                            if (pendingDeeplink != null) {
                                                handleDeepLink();

                                            } else {

                                                navController.navigate(R.id.admin_main_screen);
                                            }
                                        }

                                        if (!user.getSelectedEvents().isEmpty()) {
                                            if (pendingDeeplink != null) {
                                                handleDeepLink();
                                            } else {
                                                navController.navigate(R.id.fragment_pending_events);

                                            }
                                        }

                                        // else, nav to the main fragment
                                        if (pendingDeeplink != null) {
                                            handleDeepLink();

                                        } else {
                                            navController.navigate(R.id.fragment_main);
                                        }
                                    } else {
                                        newUserBuilder(id, navController);  // build user

                                    }

                                })
                                .addOnFailureListener(e -> {
                                    throw new RuntimeException("listen, we fucked up.");
                                });
                    });
        }



    }

    /**
     * handles deeplink on new intent
     * @param intent The new intent that was used to start the activity
     *
     */
    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);

        if (intent.getData() != null){
            pendingDeeplink = intent.getData();

        } if (user != null){
            handleDeepLink();

        }
    }

    /**
     * sets up the pending deeplinks for qr codes
     */
    private void handleDeepLink(){
        if (pendingDeeplink == null){
            return;
        }

        String eventId = pendingDeeplink.getQueryParameter("id");
        String eventDocPath = "Events/" + eventId;

        db.document(eventDocPath).get()
                .addOnSuccessListener(
                        doc -> {
                            Event event = doc.toObject(Event.class);
                            svm.setEvent(event);
                            pendingDeeplink = null;
                            navController.navigate(R.id.fragment_view_event_user);
                        }
                );


    }

    /**
     * creates a dialogfragment to get user information and sets up static user variable
     * @param id id of teh android device
     * @param navController navController for the navigation
     */
    public void newUserBuilder(String id, NavController navController) {
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
//                    String id2 = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);


                    Log.d("Firestore", "wenis (builder built)");
                    // TALK TO RAMIZ ABT THIS!!!!!!
                    // firebase should automatically serialize the object, and user should be org so that it has an empty arr of events
                    DocumentReference userDocRef = usersRef.document(id);

                    User newUser = new User(name1, email2, address2, phoneNum2, id, userDocRef);
                    userDocRef.set(newUser)
                            .addOnSuccessListener(
                                    aVoid -> {
                                        Log.d("Firestore", "User has been created!");
                                        MainActivity.user = newUser;

                                        if (pendingDeeplink != null){
                                            handleDeepLink();
                                        } else{
                                            navController.navigate(R.id.fragment_main);
                                        }

                                    }

                            )
                            .addOnFailureListener(e -> {
                                Log.e("Firestore", "Could not create user", e);
                                throw new RuntimeException(e);
                            });

                    //appDataRepository.setSharedData(usersRef.document(id).getPath());
                    //Log.d("set data", usersRef.document(id).getPath());


                })
                .setCancelable(false)
                .show();
        //return newUser;
    }

    /**
     * sets binding to null
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}