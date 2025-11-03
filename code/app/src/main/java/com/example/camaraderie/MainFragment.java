package com.example.camaraderie;//

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.camaraderie.databinding.FragmentMainBinding;
import com.example.camaraderie.databinding.FragmentMainTestBinding;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainFragment extends Fragment implements DashboardEventArrayAdapter.OnEventClickListener {

    private FragmentMainBinding binding;
    private DashboardEventArrayAdapter dashboardEventArrayAdapter;
    private EventViewModel eventViewModel;

    @Inject
    AppDataRepository appDataRepository;

    private FirebaseFirestore db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentMainBinding.inflate(getLayoutInflater());
//        return super.onCreateView(inflater, container, savedInstanceState);

//        return inflater.inflate(R.layout.fragment_main, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);

        dashboardEventArrayAdapter = new DashboardEventArrayAdapter(getContext(), new ArrayList<>());
        binding.eventsList.setAdapter(dashboardEventArrayAdapter);

        // Observe LiveData from Activity
        eventViewModel.getLocalEvents().observe(getViewLifecycleOwner(), events -> {
            dashboardEventArrayAdapter.clear();
            dashboardEventArrayAdapter.addAll(events);
            dashboardEventArrayAdapter.notifyDataSetChanged();
        });

        // when USER views EVENT, compare user id to host id, and set the corresponding fragment accordingly


        binding.hostEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 NavHostFragment.findNavController(MainFragment.this)
                                          .navigate(R.id.action_fragment_main_to_fragment_create_event_testing);
            }
        });

    }

    public void onEventClick(Event event){
        Bundle bundle = new Bundle();

        db = FirebaseFirestore.getInstance();

        DocumentReference eventRef = db.collection("events").document(event.getEventId());

        Log.d("description button", "reached this point");
        String eventDocPath = eventRef.getPath();
        bundle.putString("event", eventDocPath);
        bundle.putString("user", appDataRepository.getSharedData());

        NavHostFragment.findNavController(MainFragment.this).navigate(
                R.id.action_fragment_main_to_fragment_view_event_user,
                bundle
        );

    }
}
