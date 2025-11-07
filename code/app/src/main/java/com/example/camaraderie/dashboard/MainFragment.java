package com.example.camaraderie.dashboard;//

import static com.example.camaraderie.MainActivity.user;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.fragment.NavHostFragment;

import com.example.camaraderie.Event;
import com.example.camaraderie.R;
import com.example.camaraderie.SharedEventViewModel;
import com.example.camaraderie.databinding.FragmentMainBinding;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Dashboard/Main screen for users
 */
public class MainFragment extends Fragment implements DashboardEventArrayAdapter.OnEventClickListener {

    private FragmentMainBinding binding;
    private DashboardEventArrayAdapter dashboardEventArrayAdapter;
    private NavController nav;
    private EventViewModel eventViewModel;
    private FirebaseFirestore db;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        dashboardEventArrayAdapter = new DashboardEventArrayAdapter(getContext(), new ArrayList<>());

        nav = NavHostFragment.findNavController(MainFragment.this);
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
        db = FirebaseFirestore.getInstance();
        super.onViewCreated(view, savedInstanceState);

        binding.eventsList.setAdapter(dashboardEventArrayAdapter);
        binding.nameForMainDashboard.setText(user.getFirstName());

        // Observe LiveData from Activity
        eventViewModel.getLocalEvents().observe(getViewLifecycleOwner(), events -> {
            dashboardEventArrayAdapter.clear();
            dashboardEventArrayAdapter.addAll(events);
            dashboardEventArrayAdapter.listener = this;
            dashboardEventArrayAdapter.notifyDataSetChanged();
        });

        // when USER views EVENT, compare user id to host id, and set the corresponding fragment accordingly

        binding.fromDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFromDialog();
            }
        });

        binding.toDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openToDialog();
            }
        });

        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date fromDate = new Date();
                Date toDate = new Date();
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    fromDate =  formatter.parse(binding.fromDateText.getText().toString());
                    toDate = formatter.parse(binding.toDateText.getText().toString());
                }
                catch (Exception e){
                    Log.e("Firestore", "Error parsing date", e);
                }

                Date finalFromDate = fromDate;
                Date finalToDate = toDate;
                eventViewModel.getLocalEvents().observe(getViewLifecycleOwner(), events -> {
                    dashboardEventArrayAdapter.clear();
                    for(int i = 0; i < events.size(); i++){
                        String eventDayMonth = "" + events.get(i).getEventDate().getDay() + events.get(i).getEventDate().getMonth();
                        String toDateMonth = "" + finalToDate.getDay() + finalToDate.getMonth();
                        if(events.get(i).getEventName().toLowerCase().contains(binding.searchBar.getText().toString().toLowerCase())){
                            if((events.get(i).getEventDate().after(finalFromDate) && events.get(i).getEventDate().before(finalToDate)) || eventDayMonth.equals(toDateMonth)){
                                dashboardEventArrayAdapter.add(events.get(i));
                            }
                        }
                    };
                    dashboardEventArrayAdapter.notifyDataSetChanged();
                });
            }
        });
        binding.hostEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nav.navigate(R.id.action_fragment_main_to_fragment_create_event_testing);
            }
        });

        binding.myEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nav.navigate(R.id.action_fragment_main_to_fragment_view_my_events);
            }
        });

        binding.accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO implement the view account fragment
                //BY UMRAN
                nav.navigate(R.id.action_fragment_main_to_update_user);
            }
        });

    }

    public void onEventClick(Event event){

        Log.d("Firebase", "User clicked description for" + event.getEventName());

        SharedEventViewModel vm = new ViewModelProvider(requireActivity()).get(SharedEventViewModel.class);
        vm.setEvent(event);


        if (event.getHostDocRef().equals(user.getDocRef())){
            NavHostFragment.findNavController(this).navigate(R.id.action_fragment_main_to__fragment_organizer_view_event);
        }

        else {
            NavHostFragment.findNavController(this).navigate(R.id.action_fragment_main_to_fragment_view_event_user);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void openFromDialog() {
        DatePickerDialog dateDialog;
        dateDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                binding.fromDateText.setText(year + "-" + (month+1) + "-" + day);
            }

        }, 2025, 10, 6);

        dateDialog.show();

    }

    private void openToDialog() {
        DatePickerDialog dateDialog;
        dateDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                binding.toDateText.setText(year + "-" + (month+1) + "-" + day);
            }

        }, 2025, 10, 6);

        dateDialog.show();

    }

}
