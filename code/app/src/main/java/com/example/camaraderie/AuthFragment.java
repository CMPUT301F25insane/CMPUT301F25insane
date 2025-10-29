package com.example.camaraderie;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AuthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AuthFragment extends Fragment {

    private FirebaseFirestore db;
    static private CollectionReference usersRef;
    boolean userExists = false;


    public static AuthFragment newInstance(String param1, String param2) {
        AuthFragment fragment = new AuthFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

                    userExists = false;
                }
            }
            else{
                Log.d("Firestore", "Did not get documents");
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_auth, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}