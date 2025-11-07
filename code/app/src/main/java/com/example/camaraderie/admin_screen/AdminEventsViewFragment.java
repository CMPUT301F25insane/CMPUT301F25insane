package com.example.camaraderie.admin_screen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.camaraderie.R;
import com.example.camaraderie.Event;
import com.example.camaraderie.databinding.FragmentAdminEventsViewBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class AdminEventsViewFragment extends Fragment {

    private FragmentAdminEventsViewBinding binding;
    FirebaseFirestore db;
    private CollectionReference usersRef;
    private ArrayList<Event> eventsArrayList;
    private UserArrayAdaptor eventsArrayAdapter;

    public AdminEventsViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdminEventsViewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("Users");

        eventsArrayList = new ArrayList<Event>();

        eventsArrayAdapter = new UserArrayAdaptor(requireContext(), eventsArrayList);

        binding.list.setAdapter(eventsArrayAdapter);

        //for loading data
        loadList();

        binding.backButton.setOnClickListener( v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_admin_user_data_screen_to_admin_main_screen)
        );
    }
    private void loadList(){
        usersRef.addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.e("Firestore", error.toString());
            }
            if (value != null && !value.isEmpty()) {
                eventsArrayList.clear();
                for (QueryDocumentSnapshot snapshot: value){
                    Event event = snapshot.toObject(Event.class);
                    eventsArrayList.add(event);
                }

                eventsArrayAdapter.notifyDataSetChanged();
            }
        });
    }
}