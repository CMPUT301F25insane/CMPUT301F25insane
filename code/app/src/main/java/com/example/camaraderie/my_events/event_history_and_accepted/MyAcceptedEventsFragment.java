package com.example.camaraderie.my_events.event_history_and_accepted;

import static com.example.camaraderie.main.Camaraderie.getUser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.camaraderie.SharedEventViewModel;
import com.example.camaraderie.databinding.FragmentViewMyAcceptedEventsBinding;
import com.example.camaraderie.databinding.FragmentViewMyEventHistoryBinding;
import com.example.camaraderie.my_events.MyEventsViewModel;

import java.util.ArrayList;

/**
 * View accepted events fragment for user to see their accepted events
 */
public class MyAcceptedEventsFragment extends Fragment {

    private FragmentViewMyAcceptedEventsBinding binding;
    private SharedEventViewModel svm;
    private MyEventsViewModel vm;
    private MyAcceptedEventsArrayAdapter adapter;
    private NavController nav;

    /**
     * onCreate initializes the viewModel provider and nav
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nav = NavHostFragment.findNavController(this);
        svm = new ViewModelProvider(requireActivity()).get(SharedEventViewModel.class);
        vm = new ViewModelProvider(requireActivity()).get(MyEventsViewModel.class);
    }

    /**
     * Setup the view to be inflated by the XML
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     * Returns the root of the binding as a view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentViewMyAcceptedEventsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * onViewCreated sets up the adapter with the required events and sets the UI elements to match the list we are
     * providing
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new MyAcceptedEventsArrayAdapter(requireContext(), new ArrayList<>(), svm, nav);
        adapter.setNotifyOnChange(true);
        binding.historyListView.setAdapter(adapter);
        binding.headerText.setText("Accepted Events");

        vm.getUserEventsFromList(getUser().getAcceptedEvents(),
                events -> {
                    adapter.addAll(events);
                });

        binding.historyBackButton.setOnClickListener(v -> nav.popBackStack());
    }

    /**
     * sets binding to null
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
