package com.example.camaraderie;

import android.app.Dialog;
import android.content.Intent;
import static com.example.camaraderie.Util.*;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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
    boolean userExists = false;
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

        String id = Settings.Secure.ANDROID_ID;

        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("Users");

        usersRef.whereEqualTo("UserID", id ).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                if(!task.getResult().isEmpty()){
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        Log.d("Firestore", "Found Document");
                        userExists = true;
                    }
                }
                else{
                    Log.d("Firestore", "No Documents");
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.user_info_dialog, null);
                    EditText name = findViewById(R.id.name_edit_text);
                    EditText Email = findViewById(R.id.editTextTextEmailAddress);
                    EditText address = findViewById(R.id.editTextTextPostalAddress);
                    EditText phoneNum = findViewById(R.id.editTextPhone);

                    builder.setMessage("Please enter Your information to create a profile")
                            .setPositiveButton("Done", (dialog, id1)->{
                                String name1 = name.getText().toString();
                                String email2 = Email.getText().toString();
                                String address2 = address.getText().toString();
                                String phoneNum2 = phoneNum.getText().toString();
                                User user = new User(name1, email2, address2, phoneNum2);
                            })
                    userExists = false;
                }
            }
            else{
                Log.d("Firestore", "Did not get documents");
            }
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