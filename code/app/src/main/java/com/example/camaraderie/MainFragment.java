package com.example.camaraderie;//

import android.os.Bundle;
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

import java.util.ArrayList;

public class MainFragment extends Fragment implements DashboardEventArrayAdapter.OnEventClickListener {

    private FragmentMainBinding binding;
    private DashboardEventArrayAdapter dashboardEventArrayAdapter;
    private EventViewModel eventViewModel;


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

    // i dont think this should live here, it could violate MVC principles
    public void onEventClick(Event event){

    }
}
