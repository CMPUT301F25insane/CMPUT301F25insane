package com.example.camaraderie.admin_screen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.camaraderie.Event;
import com.example.camaraderie.R;
import com.example.camaraderie.SharedEventViewModel;
import com.example.camaraderie.databinding.FragmentAdminEventsViewBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

/**
 *  This class extends Fragment in order to allow the users to view the current running events so that they can view them and possibly
 *  remove them as necessary
 */

public class AdminEventsViewFragment extends Fragment {

    private FragmentAdminEventsViewBinding binding;
    FirebaseFirestore db;
    private CollectionReference eventsRef;
    private NavController nav;
    private ArrayList<Event> eventsArrayList;
    private EventArrayAdapter eventsArrayAdapter;
    private ListenerRegistration eventListener;
    SharedEventViewModel svm;

    /**
     * This is an empty that is required
     */

    public AdminEventsViewFragment() {
        // Required empty public constructor
    }

    /**
     * onCreateView is first initialized when we open the fragment
     * It initializes the binding, and it also inflates the xml file to be used to set the contents
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     * @return
     * We return a view which is the layout
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdminEventsViewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * onViewCreated initializes all the parts of the layout that we need to use
     * It initializes the database and the collection reference for the events
     * It then initializes the new eventArrayList to an empty array list
     * We setup the nav for navigation and svm for our view model
     * We then set the eventsArrayAdapter to be new EventArrayAdapters to store the list of events
     * We set the list in our xml to the events array adapter
     * We load the data
     * We then setup the back button to navigate back
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("Events");

        eventsArrayList = new ArrayList<Event>();
        nav = NavHostFragment.findNavController(AdminEventsViewFragment.this);
        svm = new ViewModelProvider(requireActivity()).get(SharedEventViewModel.class);

        eventsArrayAdapter = new EventArrayAdapter(requireContext(), eventsArrayList, nav, svm);

        binding.list.setAdapter(eventsArrayAdapter);

        //for loading data
        loadList();

        binding.backButton.setOnClickListener( v ->
                nav.navigate(R.id.action_admin_event_data_screen_view_to_admin_main_screen)
        );
    }

    /**
     * The loadlist function uses a snapshot listener to add each event to our events Array List
     * We then refresh the list
     */
    private void loadList(){
        eventListener = eventsRef.addSnapshotListener((value, error) -> {
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

    /**
     * onDestroyView, just removes the listener so that our database does not receive thousands of read requests
     */

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (eventListener != null) {eventListener.remove();}
        binding = null;
    }

    /**
     * onDestroy sets the binding to be null so we can avoid memory leaks
     */

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}