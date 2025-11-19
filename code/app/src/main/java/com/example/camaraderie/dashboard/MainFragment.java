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
 * This Fragment is the home screen of the app, it initializes the view for the user when they open the app
 */
public class MainFragment extends Fragment implements DashboardEventArrayAdapter.OnEventClickListener {

    private FragmentMainBinding binding;
    private DashboardEventArrayAdapter dashboardEventArrayAdapter;
    private NavController nav;
    private EventViewModel eventViewModel;
    private FirebaseFirestore db;

    /**
     * onCreate is run as soon as the app opens and initializes our eventViewModel and our
     * custom array adapter to be used later in the fragment
     * We also setup our navigation to be used later
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        dashboardEventArrayAdapter = new DashboardEventArrayAdapter(getContext(), new ArrayList<>());

        nav = NavHostFragment.findNavController(MainFragment.this);
    }

    /**
     * We have onCreateView which creates the view we need for later and initializes the xml to be associated with this
     * fragment, we set it up using binding and return the view
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     * Return the view
     */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentMainBinding.inflate(getLayoutInflater());
//        return super.onCreateView(inflater, container, savedInstanceState);

//        return inflater.inflate(R.layout.fragment_main, container, false);
        return binding.getRoot();
    }

    /**
     * onViewCreated handles the logic and backend of the UI
     * We first initalize the database and grab the latest instance
     * We use binding to setup the array adapter
     * We also grab the name for the main dashboard
     * We observe the live data from eventViewModel and we are able to add all the events and set the listener for our adapter
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */

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

        /**
         * We have the search button which allows for us to search through the event or more specifically the user is able
         * to search for events
         */

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
                        if(events.get(i).getEventName().toLowerCase().contains(binding.searchBar.getText().toString().toLowerCase()) ||
                            events.get(i).getDescription().toLowerCase().contains(binding.searchBar.getText().toString().toLowerCase())){
                            if((events.get(i).getEventDate().after(finalFromDate) && events.get(i).getEventDate().before(finalToDate)) || eventDayMonth.equals(toDateMonth)){
                                dashboardEventArrayAdapter.add(events.get(i));
                            }
                        }
                    };
                    dashboardEventArrayAdapter.notifyDataSetChanged();
                });
            }
        });

        /**
         * We have our menu bar at the bottom of the screen for naviagtion
         */

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

    /**
     * OnEventClick allows for us to navigate to the event they clicked on
     * @param event
     */

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

    /**
     * onDestroy makes sure that there are no memory leaks
     */

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    /**
     * openFromDialog gives us a dialog to set the text to the date inputted for an initial date to search
     */
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

    /**
     * We also have a toDate to set and end date
     */

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
