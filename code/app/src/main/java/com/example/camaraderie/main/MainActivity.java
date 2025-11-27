package com.example.camaraderie.main;//


import static android.app.ProgressDialog.show;
import static com.example.camaraderie.utilStuff.Util.*;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;


import com.example.camaraderie.Event;
import com.example.camaraderie.R;
import com.example.camaraderie.SharedEventViewModel;
import com.example.camaraderie.User;
import com.example.camaraderie.dashboard.EventViewModel;
import com.example.camaraderie.databinding.ActivityMainBinding;
//import com.example.camaraderie.databinding.ActivityMainTestBinding;
import com.example.camaraderie.notifications.NotificationController;
import com.example.camaraderie.notifications.NotificationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * The main activity of the app. Deals with QR code scans,login, and initial navigation.
 */

public class MainActivity extends AppCompatActivity {


    private FirebaseFirestore db;
    public static User user;

    private NavController navController;

    static private CollectionReference eventsRef;
    static private CollectionReference usersRef;
    static private CollectionReference notifsRef;
    private EventViewModel eventViewModel;
    private ActivityMainBinding binding;
    private SharedEventViewModel svm;

    private Uri pendingDeeplink = null;

    private boolean __DEBUG_DATABASE_CLEAR = false;
    private NotificationView notificationView;
    private NotificationController notificationController;
    private String token;

    /**
     * sets up the SharedEventViewModel {@link #svm}, sets up the events view model for general event listings,
     * sets up the notification controller and asks the user for notification permissions
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

        svm = new ViewModelProvider(this).get(SharedEventViewModel.class);

        binding = ActivityMainBinding.inflate(getLayoutInflater());  // purely for backend purposes
        setContentView(binding.getRoot());

        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        notificationController = new NotificationController(this, notificationView);
        notificationController.setChannelId("basic_notification_channel");
        notificationController.createNotificationChannel("Basic Notifications", "General Notifications");

        requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 0);

        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("Events");
        usersRef = db.collection("Users");
        notifsRef = db.collection("Notifications");
        String id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        notificationController.setChannelId(id);
        notificationController.createNotificationChannel("Personal Notifications", "Personalized Notifications");

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FCM", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        token = task.getResult();

                        //Toast toast = Toast.makeText(getApplicationContext(), token, Toast.LENGTH_LONG);
                        //toast.show();
                    }
                });


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
                            DocumentReference userRef = documentSnapshot.getReference();
                            user.setNotificationToken(token);
                            userRef.update("notificationToken", token);
                            user.setDocRef(userRef);
                            user.updateDB( () -> {
                                // no need to do anything here because it seemed to be working before
                                new LoadUser(user.getDocRef()).loadAllData(
                                        () -> {
                                            // do nothing for now, but probably do something later
                                            return;
                                        }
                                );
                            });

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
        AlertDialog.Builder builder = new MaterialAlertDialogBuilder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fragment_user_info_dialog, null);
        EditText name = dialogView.findViewById(R.id.Name_field_for_dialog);
        EditText Email = dialogView.findViewById(R.id.email_field_for_dialog);
        EditText address = dialogView.findViewById(R.id.Address_field_for_dialog);
        EditText phoneNum = dialogView.findViewById(R.id.PhoneNo_field_for_dialog);

        //TODO: deal with empty fields

        builder
                .setView(dialogView)
                .setCancelable(false);

        AlertDialog alertDialog = builder.create();

        alertDialog.show();

        Button confirm_button = dialogView.findViewById(R.id.confirm_button_for_creating_profile);

        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name1 = name.getText().toString();
                String email2 = Email.getText().toString();
                String phoneNum2 = phoneNum.getText().toString();
                String address2 = address.getText().toString();
//                    String id2 = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);


                Log.d("Firestore", "wenis (builder built)");
                // TALK TO RAMIZ ABT THIS!!!!!!
                // firebase should automatically serialize the object, and user should be org so that it has an empty arr of events
                DocumentReference userDocRef = usersRef.document(id);
                FirebaseMessaging.getInstance().subscribeToTopic(id);


                User newUser = new User(name1, phoneNum2, email2, address2, id, token, userDocRef);
                userDocRef.set(newUser)
                        .addOnSuccessListener(
                                aVoid -> {
                                    Log.d("Firestore", "User has been created!");
                                    MainActivity.user = newUser;
                                    FirebaseMessaging.getInstance().getToken()
                                            .addOnCompleteListener(new OnCompleteListener<String>() {
                                                @Override
                                                public void onComplete(@NonNull Task<String> task) {
                                                    if (!task.isSuccessful()) {
                                                        Log.w("FCM", "Fetching FCM registration token failed", task.getException());
                                                        return;
                                                    }

                                                    // Get new FCM registration token
                                                    token = task.getResult();

                                                    //Toast toast = Toast.makeText(getApplicationContext(), token, Toast.LENGTH_LONG);
                                                    //toast.show();
                                                }
                                            });
                                    user.setNotificationToken(token);
                                    userDocRef.update("notificationToken", token);
                                    user.setDocRef(userDocRef);
                                    user.updateDB(() -> {
                                        return;
                                    });

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

                alertDialog.dismiss();
            }
        });

        Button cancel_button = dialogView.findViewById(R.id.Cancel_button_for_creating_profile);

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
            }
        });
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